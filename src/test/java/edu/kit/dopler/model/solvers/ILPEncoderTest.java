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
import edu.kit.dopler.solvers.ilp.utils.ILPSolverUtils;
import edu.kit.dopler.solvers.smt.utils.SMTAllSatSolver;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ILPEncoderTest {

    @BeforeAll
    static void setup() {
        Loader.loadNativeLibraries();
    }

    @ParameterizedTest
    @MethodSource("edu.kit.dopler.model.solvers.TestResources#getSATFileNames")
    void testSATModels(Path csvFile) {
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        boolean isSat = ILPSolverUtils.isSatisfiable(dopler);

        assertTrue(isSat, "Expected SAT for: " + csvFile.getFileName().toString());
    }

    @ParameterizedTest
    @MethodSource({
        "edu.kit.dopler.model.solvers.TestResources#getSATFileNamesConfigCount",
        "edu.kit.dopler.model.solvers.TestResources#getAdvancedSATFileNamesConfigCount",
    })
    void testSATModelsConfigCount(Path csvFile, SMTAllSatSolver.ConfigResult expectedConfigCount) {
        if (csvFile.getFileName().toString().contains("string")) {
            return;
        }
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        if (!(expectedConfigCount instanceof SMTAllSatSolver.FiniteConfigs(int count))) {
            throw new AssertionError("ILP only supports finite configs for now.");
        }

        int configCount = ILPSolverUtils.countConfigurations(dopler);

        assertEquals(
                count,
                configCount,
                "Expected " + expectedConfigCount + " config(s) for: "
                        + csvFile.getFileName().toString() + ", got " + configCount);
    }

    @ParameterizedTest
    @MethodSource("edu.kit.dopler.model.solvers.TestResources#getUNSATFileNames")
    void testUNSATModels(Path csvFile) {
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        boolean isSat = ILPSolverUtils.isSatisfiable(dopler);

        assertFalse(isSat, "Expected UNSAT for: " + csvFile.getFileName().toString());
    }
}
