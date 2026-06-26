/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * Copyright (c) 2026 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package edu.kit.dopler.parser;

import static edu.kit.dopler.common.DoplerUtils.readDOPLERModelFromFile;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ParserTest {

    static Stream<Path> modelFiles() throws IOException {
        Path modelDir = Paths.get("src/test/resources/modelCSVs/");

        return Files.walk(modelDir).filter(Files::isRegularFile);
    }

    @ParameterizedTest
    @MethodSource("modelFiles")
    void testParseOfAllDM(Path file) throws IOException {
        System.out.println("Parse file " + file.getFileName());
        assertNotNull(readDOPLERModelFromFile(file));

        // TODO Assert For Command Line Prints from the Parser
    }
}
