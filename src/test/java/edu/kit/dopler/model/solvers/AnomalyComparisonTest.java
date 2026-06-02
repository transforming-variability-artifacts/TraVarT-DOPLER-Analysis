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
package edu.kit.dopler.model.solvers;

import static edu.kit.dopler.common.DoplerUtils.readDOPLERModelFromFile;
import static org.junit.jupiter.api.Assertions.*;

import com.google.ortools.Loader;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.solvers.ilp.utils.ILPAnomalityChecker;
import edu.kit.dopler.solvers.shared.AnomalyReport;
import edu.kit.dopler.solvers.smt.NativeLibLoader;
import edu.kit.dopler.solvers.smt.utils.SMTAnomalityChecker;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.sosy_lab.java_smt.SolverContextFactory;

public class AnomalyComparisonTest {

    @BeforeAll
    static void setup() {
        Loader.loadNativeLibraries();
        NativeLibLoader.load(SolverContextFactory.Solvers.Z3, SolverContextFactory.Solvers.CVC5);
    }

    @ParameterizedTest(name = "File \"{0}\"")
    @MethodSource({
        "edu.kit.dopler.model.solvers.TestResources#getSATFileNames",
        "edu.kit.dopler.model.solvers.TestResources#getSATFileNamesConfigCount",
    })
    void testAnomalies(Path csvFile) {
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        AnomalyReport reportSMT = SMTAnomalityChecker.detectAnomalies(dopler);
        AnomalyReport reportILP = ILPAnomalityChecker.detectAnomalies(dopler);
        assertEquals(reportSMT, reportILP);
    }
}
