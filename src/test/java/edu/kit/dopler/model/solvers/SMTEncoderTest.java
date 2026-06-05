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

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.solvers.smt.NativeLibLoader;
import edu.kit.dopler.solvers.smt.utils.SMTAllSatSolver;
import edu.kit.dopler.solvers.smt.utils.SMTSolver;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.sosy_lab.java_smt.SolverContextFactory;

class SMTEncoderTest {

    @BeforeAll
    static void setup() {
        NativeLibLoader.load(SolverContextFactory.Solvers.Z3, SolverContextFactory.Solvers.CVC5);
    }

    @ParameterizedTest(name = "File \"{0}\" is solvable")
    @MethodSource("edu.kit.dopler.model.solvers.TestResources#getSATFileNames")
    void testSATModels(Path csvFile) {
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        boolean isSat = SMTSolver.isSatisfiable(dopler);

        assertTrue(isSat, "Expected SAT for: " + csvFile.getFileName().toString());
    }

    @ParameterizedTest(name = "File \"{0}\" has {1} solutions")
    @MethodSource({
        "edu.kit.dopler.model.solvers.TestResources#getSATFileNamesConfigCount",
        "edu.kit.dopler.model.solvers.TestResources#getAdvancedSATFileNamesConfigCount"
    })
    void testSATModelsConfigCount(Path csvFile, SMTAllSatSolver.ConfigResult expectedConfigCount) {
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        SMTAllSatSolver.ConfigResult configCount = SMTAllSatSolver.countConfigurations(dopler, true);
        assertEquals(
                expectedConfigCount,
                configCount,
                "Expected " + expectedConfigCount + " config(s) for: "
                        + csvFile.getFileName().toString() + ", got " + configCount);
    }

    @ParameterizedTest(name = "File \"{0}\" is not solvable")
    @MethodSource("edu.kit.dopler.model.solvers.TestResources#getUNSATFileNames")
    void testUNSATModels(Path csvFile) {
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        boolean isSat = SMTSolver.isSatisfiable(dopler);

        assertFalse(isSat, "Expected UNSAT for: " + csvFile.getFileName().toString());
    }
}
