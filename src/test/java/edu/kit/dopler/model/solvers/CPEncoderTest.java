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
import edu.kit.dopler.common.CpEncodingResult;
import edu.kit.dopler.model.Dopler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CPEncoderTest {

    @BeforeAll
    public static void beforeClass() throws Exception {
        Loader.loadNativeLibraries();
    }

    static Stream<Path> getSATFileNames() throws IOException {
        Path dir = Paths.get("src/test/resources/sat_models");
        try (Stream<Path> files = Files.list(dir)) {
            return files.filter(Files::isRegularFile).map(Path::toAbsolutePath).toList().stream();
        }
    }

    static Stream<Arguments> getSATFileNamesConfigCount() throws IOException {
        Path dir = Paths.get("src/test/resources/sat_models_config_count");
        try (Stream<Path> files = Files.list(dir)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(Path::toAbsolutePath)
                    .map(path -> Arguments.of(path, extractConfigCount(path)))
                    .toList()
                    .stream();
        }
    }

    private static int extractConfigCount(Path path) {
        String fileName = path.getFileName().toString(); // e.g. "foo_c42.csv"
        Matcher m = Pattern.compile(".*_c(\\d+)\\.csv").matcher(fileName);
        if (!m.find()) {
            throw new IllegalArgumentException(
                    "No config count found in file name: " + fileName + " (format must be: *_c<configCount>.csv)");
        }
        return Integer.parseInt(m.group(1));
    }

    static Stream<Path> getUNSATFileNames() throws IOException {
        Path dir = Paths.get("src/test/resources/unsat_models");
        try (Stream<Path> files = Files.list(dir)) {
            return files.filter(Files::isRegularFile).map(Path::toAbsolutePath).toList().stream();
        }
    }

    @Test
    void testSpecificModel() {
        Path csvFile = Path.of("src/test/resources/sat_models/enumeration_decision_test2.csv"); // specify model csv
        // here

        System.out.println("Testing: " + csvFile.getFileName().toString());
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        CpEncodingResult cpEncoding = dopler.toCpModel();
        cpEncoding.printAllConfigs();

        assertTrue(
                cpEncoding.checkSat(),
                "Expected SAT for: " + csvFile.getFileName().toString());
    }

    @ParameterizedTest
    @MethodSource("getSATFileNames")
    void testSATModels(Path csvFile) {
        System.out.println("Testing: " + csvFile.getFileName().toString());
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        CpEncodingResult cpEncoding = dopler.toCpModel();

        assertTrue(
                cpEncoding.checkSat(),
                "Expected SAT for: " + csvFile.getFileName().toString());
    }

    @ParameterizedTest
    @MethodSource("getSATFileNamesConfigCount")
    void testSATModelsConfigCount(Path csvFile, int expectedConfigCount) {
        System.out.println("Testing: " + csvFile.getFileName().toString());
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        CpEncodingResult cpEncoding = dopler.toCpModel();

        if (expectedConfigCount == 0) {
            assertFalse(
                    cpEncoding.checkSat(),
                    "Expected UNSAT for: " + csvFile.getFileName().toString());
        } else {
            assertTrue(
                    cpEncoding.checkSat(),
                    "Expected SAT for: " + csvFile.getFileName().toString());
        }

        int configCount = cpEncoding.getAmountOfConfigs();
        assertEquals(
                expectedConfigCount,
                configCount,
                "Expected " + expectedConfigCount + " config(s) for: "
                        + csvFile.getFileName().toString() + ", got " + configCount);
    }

    @ParameterizedTest
    @MethodSource("getUNSATFileNames")
    void testUNSATModels(Path csvFile) {
        System.out.println("Testing: " + csvFile.getFileName().toString());
        Dopler dopler = assertDoesNotThrow(() -> readDOPLERModelFromFile(csvFile), "DOPLER model creation failed!");

        CpEncodingResult cpEncoding = dopler.toCpModel();

        assertFalse(
                cpEncoding.checkSat(),
                "Expected UNSAT for: " + csvFile.getFileName().toString());
    }
}
