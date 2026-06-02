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

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.solvers.shared.AnomalyReport;
import edu.kit.dopler.solvers.smt.NativeLibLoader;
import edu.kit.dopler.solvers.smt.SMTContext;
import edu.kit.dopler.solvers.smt.encoders.SMTGlobalConstraintEncoder;
import edu.kit.dopler.solvers.smt.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        name = "dopler-analysis",
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        description = "Analyzes DOPLER models using Z3.")
public class Main implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Option(
            names = {"-a", "--analyze"},
            defaultValue = "true",
            negatable = true,
            description =
                    "Run anomaly analysis on the provided models (default: ${DEFAULT-VALUE}). Use --no-analyze to disable.")
    private boolean runAnalysis;

    @Parameters(
            paramLabel = "FILES",
            description = "One or more DOPLER model files (CSV or JSON) to process",
            arity = "1..*")
    private List<Path> modelFiles;

    public static void main(final String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        NativeLibLoader.setBasePath(Paths.get("./dependencies"));
        for (Path modelFile : modelFiles) {
            if (!Files.exists(modelFile) || !Files.isRegularFile(modelFile)) {
                LOGGER.error("File not found or is not a regular file: {}", modelFile);
                continue;
            }

            LOGGER.info("Processing file: {}", modelFile);
            try {
                Dopler dopler = readDOPLERModelFromFile(modelFile);

                if (runAnalysis) {
                    performAnomalyAnalysis(dopler);
                    LOGGER.info("-".repeat(50));
                }

            } catch (IOException e) {
                LOGGER.error("Error reading or processing file {}: {}", modelFile, e.getMessage(), e);
            } catch (Exception e) {
                LOGGER.error("Unexpected error during execution for {}: {}", modelFile, e.getMessage(), e);
            }
        }

        return 0;
    }

    private void performAnomalyAnalysis(Dopler dopler) {
        LOGGER.info("Starting anomaly analysis...");

        long startTime = System.nanoTime();
        AnomalyReport anomalies = SMTAnomalityChecker.detectAnomalies(dopler);
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        LOGGER.info("{}: Took {}ms", anomalies.hasAnomalies() ? "Has found anomalies" : "Has no anomalies", duration);

        startTime = System.nanoTime();
        boolean isSolvable = SMTSolver.isSatisfiable(dopler);
        duration = (System.nanoTime() - startTime) / 1_000_000;
        LOGGER.info("SMT DOPLER is {}: Took {}ms", isSolvable ? "solvable" : "unsolvable", duration);

        if (isSolvable) {
            startTime = System.nanoTime();
            SMTAllSatSolver.ConfigResult configCount = SMTAllSatSolver.getAmountOfConfigs(dopler, true);
            duration = (System.nanoTime() - startTime) / 1_000_000;
            LOGGER.info("Found {} valid configurations: Took {}ms", configCount, duration);
        }

        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext())) {
            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);
            LOGGER.info("CODE\n\n{}", context);
        }
    }
}
