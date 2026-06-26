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
package edu.kit.dopler.model.solvers;

import edu.kit.dopler.solvers.smt.utils.SMTAllSatSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.provider.Arguments;

public final class TestResources {

    private TestResources() {}

    public static Stream<Path> files(String resourceDir, boolean recursive) throws IOException {
        return listFiles(resourceDir, recursive);
    }

    public static <T> Stream<T> files(String resourceDir, boolean recursive, Function<Path, T> mapper)
            throws IOException {
        return listFiles(resourceDir, recursive).map(mapper);
    }

    private static Stream<Path> listFiles(String resourceDir, boolean recursive) throws IOException {
        Path dir = Paths.get(resourceDir);

        Stream<Path> stream = recursive ? Files.walk(dir) : Files.list(dir);

        return stream.filter(Files::isRegularFile).map(Path::toAbsolutePath);
    }

    public static Stream<Arguments> filesWithConfigCount(String resourceDir, boolean recursive) throws IOException {

        return files(
                resourceDir,
                recursive,
                path -> Arguments.of(Named.of(getFileName(path), path), extractConfigCount(path)));
    }

    private static SMTAllSatSolver.ConfigResult extractConfigCount(Path path) {
        String fileName = path.getFileName().toString();

        Matcher m = Pattern.compile(".*_c(?:(\\d+)|infinite_(\\w+))\\.csv").matcher(fileName);

        if (!m.matches()) {
            throw new IllegalArgumentException(
                    "No config count found in file name: " + fileName + " (format must be: *_c<configCount>.csv)");
        }

        if (m.group(2) != null) {
            return new SMTAllSatSolver.InfiniteConfigs("DECISION_" + m.group(2) + "_VALUE");
        }

        return new SMTAllSatSolver.FiniteConfigs(Integer.parseInt(m.group(1)));
    }

    public static Stream<Arguments> getSATFileNames() throws IOException {
        return files("src/test/resources/sat_models", true)
                .map(path -> Arguments.of(Named.of(getFileName(path), path)));
    }

    public static Stream<Arguments> getSATFileNamesConfigCount() throws IOException {
        return filesWithConfigCount("src/test/resources/sat_models_config_count", true);
    }

    public static Stream<Arguments> getAdvancedSATFileNamesConfigCount() throws IOException {

        return filesWithConfigCount("src/test/resources/advanced_sat_models_config_count", true);
    }

    public static Stream<Arguments> getUNSATFileNames() throws IOException {
        return TestResources.files("src/test/resources/unsat_models", true)
                .map(path -> Arguments.of(Named.of(getFileName(path), path)));
    }

    private static String getFileName(Path path) {
        return path.getFileName().toString();
    }
}
