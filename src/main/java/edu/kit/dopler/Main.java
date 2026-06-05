/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Copyright 2026 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *******************************************************************************/
package edu.kit.dopler;

import static edu.kit.dopler.common.DoplerUtils.readDOPLERModelFromFile;

import com.google.ortools.Loader;
import edu.kit.dopler.common.CpEncodingResult;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPConstants.ILPModels;
import edu.kit.dopler.solvers.ilp.utils.ILPAnomalityChecker;
import edu.kit.dopler.solvers.ilp.utils.ILPSolverUtils;
import edu.kit.dopler.solvers.shared.AnomalyReport;
import edu.kit.dopler.solvers.smt.NativeLibLoader;
import edu.kit.dopler.solvers.smt.SMTConstants;
import edu.kit.dopler.solvers.smt.SMTContext;
import edu.kit.dopler.solvers.smt.encoders.SMTGlobalConstraintEncoder;
import edu.kit.dopler.solvers.smt.utils.SMTAllSatSolver;
import edu.kit.dopler.solvers.smt.utils.SMTAnomalityChecker;
import edu.kit.dopler.solvers.smt.utils.SMTSolver;
import edu.kit.dopler.solvers.smt.utils.SMTUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        name = "dopler-analysis",
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        description = "Analyzes DOPLER models using SMT, ILP, or CP analyzers.")
public class Main implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public enum AnalyzerType {
        SMT,
        ILP,
        CP
    }

    @Parameters(
            paramLabel = "FILES",
            description = "One or more DOPLER model files (CSV or JSON) to process",
            arity = "1..*")
    private List<Path> modelFiles;

    @Option(
            names = {"-a", "--analyzer"},
            description = "Analyzer to use: ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}",
            defaultValue = "SMT")
    private AnalyzerType analyzerType;

    @Option(
            names = {"--smt-solver"},
            description =
                    "SMT Solver to use (Requires SMT analyzer and libraries to be available in ./dependencies): ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}",
            defaultValue = "CVC5")
    private Solvers smtSolver;

    @Option(
            names = {"--ilp-model"},
            description =
                    "ILP Model to use (Requires ILP analyzer): ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}",
            defaultValue = "CBC")
    private ILPModels ilpModel;

    public static void main(final String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        // Apply dynamically selected solvers/models based on CLI input
        if (analyzerType == AnalyzerType.SMT) {
            SMTConstants.setDefaultModel(smtSolver);
            LOGGER.info("Configured SMT Analyzer with solver: {}", smtSolver);
        } else if (analyzerType == AnalyzerType.ILP) {
            ILPConstants.setDefaultModel(ilpModel);
            LOGGER.info("Configured ILP Analyzer with model: {}", ilpModel);
        }

        LOGGER.info("Starting analysis using {}...", analyzerType);

        for (Path modelFile : modelFiles) {
            if (!Files.exists(modelFile) || !Files.isRegularFile(modelFile)) {
                LOGGER.error("File not found or is not a regular file: {}", modelFile);
                continue;
            }

            LOGGER.info("Processing file: {}", modelFile);
            try {
                Dopler dopler = readDOPLERModelFromFile(modelFile);

                executeAnalysis(dopler);
                LOGGER.info("-".repeat(50));
            } catch (IOException e) {
                LOGGER.error("Error reading or processing file {}: {}", modelFile, e.getMessage(), e);
            } catch (Exception e) {
                LOGGER.error("Unexpected error during execution for {}: {}", modelFile, e.getMessage(), e);
            }
        }

        return 0;
    }

    private void executeAnalysis(Dopler dopler) {
        AnalysisRunner runner;
        switch (analyzerType) {
            case SMT:
                runner = new AnalysisRunner() {
                    @Override
                    public void init() {
                        NativeLibLoader.setBasePath(Paths.get("./dependencies"));
                    }

                    @Override
                    public AnomalyReport detectAnomalies() {
                        return SMTAnomalityChecker.detectAnomalies(dopler);
                    }

                    @Override
                    public boolean isSatisfiable() {
                        return SMTSolver.isSatisfiable(dopler);
                    }

                    @Override
                    public Object countConfigurations() {
                        return SMTAllSatSolver.countConfigurations(dopler, true);
                    }

                    @Override
                    public void postAnalysis() {
                        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext())) {
                            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);
                            LOGGER.info("CODE\n\n{}", context);
                        } catch (Exception e) {
                            LOGGER.error("Failed to dump SMT code.", e);
                        }
                    }
                };
                break;
            case ILP:
                runner = new AnalysisRunner() {
                    @Override
                    public void init() {
                        Loader.loadNativeLibraries();
                    }

                    @Override
                    public AnomalyReport detectAnomalies() {
                        return ILPAnomalityChecker.detectAnomalies(dopler);
                    }

                    @Override
                    public boolean isSatisfiable() {
                        return ILPSolverUtils.isSatisfiable(dopler);
                    }

                    @Override
                    public Object countConfigurations() {
                        return ILPSolverUtils.countConfigurations(dopler);
                    }
                };
                break;
            case CP:
                runner = new AnalysisRunner() {
                    private CpEncodingResult result;

                    @Override
                    public void init() {
                        Loader.loadNativeLibraries();
                        result = dopler.toCpModel();
                    }

                    @Override
                    public AnomalyReport detectAnomalies() {
                        return result.detectAnomalies();
                    }

                    @Override
                    public boolean isSatisfiable() {
                        return result.checkSat();
                    }

                    @Override
                    public Object countConfigurations() {
                        return result.getAmountOfConfigs();
                    }
                };
                break;
            default:
                throw new IllegalStateException("Unknown analyzer type");
        }

        runner.init();

        long startTime = System.nanoTime();
        AnomalyReport anomalies = runner.detectAnomalies();
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        LOGGER.info("{}: Took {}ms", anomalies.hasAnomalies() ? "Has found anomalies" : "Has no anomalies", duration);

        startTime = System.nanoTime();
        boolean isSolvable = runner.isSatisfiable();
        duration = (System.nanoTime() - startTime) / 1_000_000;
        LOGGER.info("{} DOPLER is {}: Took {}ms", analyzerType, isSolvable ? "solvable" : "unsolvable", duration);

        if (isSolvable) {
            startTime = System.nanoTime();
            Object configCount = runner.countConfigurations();
            duration = (System.nanoTime() - startTime) / 1_000_000;
            LOGGER.info("Found {} valid configurations: Took {}ms", configCount, duration);
        }

        runner.postAnalysis();
    }

    /**
     * Abstract the solver logic while keeping a common flow.
     */
    private interface AnalysisRunner {
        void init();

        AnomalyReport detectAnomalies();

        boolean isSatisfiable();

        Object countConfigurations();

        default void postAnalysis() {}
    }
}
