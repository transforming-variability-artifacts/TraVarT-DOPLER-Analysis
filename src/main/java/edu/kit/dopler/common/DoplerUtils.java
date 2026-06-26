/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * Copyright (c) 2024 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package edu.kit.dopler.common;

import edu.kit.dopler.io.DoplerModelWriter;
import edu.kit.dopler.io.antlr.DoplerDecisionCreator;
import edu.kit.dopler.io.antlr.DoplerExpressionParser;
import edu.kit.dopler.io.antlr.resources.DoplerLexer;
import edu.kit.dopler.io.antlr.resources.DoplerParser;
import edu.kit.dopler.model.Dopler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public final class DoplerUtils {

    private DoplerUtils() {}

    public static void writeDoplerToFile(final Dopler dopler, final String fileName) throws IOException {
        if (dopler == null || fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Dopler model and fileName must not be null or empty.");
        }

        // Write Dopler Model in csv and json
        DoplerModelWriter dmw = new DoplerModelWriter();
        // Strip extension if it exists
        String baseName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;

        dmw.writeCSV(dopler, Paths.get(baseName + ".csv"));
        dmw.writeJson(dopler, Paths.get(baseName + ".json"));
    }

    public static Dopler readDOPLERModelFromFile(Path file) throws IOException {
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IOException("File not found or is not a valid file: " + file.toAbsolutePath());
        }
        String fileName = file.getFileName().toString().toLowerCase();
        if (!fileName.endsWith(".json") && !fileName.endsWith(".csv")) {
            throw new IllegalArgumentException(
                    "Unsupported file format: " + fileName + ". Only .json and .csv are supported.");
        }

        // ANTLR Setup
        CharStream input = CharStreams.fromPath(file);
        DoplerLexer lexer = new DoplerLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DoplerParser parser = new DoplerParser(tokens);

        // Create parse tree
        ParseTree tree = parser.document();
        ParseTreeWalker walker = new ParseTreeWalker();

        // Walk through both listeners, first to create the decisions, second to create
        // the expressions
        DoplerDecisionCreator decisionCreator =
                new DoplerDecisionCreator(file.getFileName().toString());
        walker.walk(decisionCreator, tree);
        DoplerExpressionParser expressionParser = new DoplerExpressionParser(decisionCreator.getDopler());
        walker.walk(expressionParser, tree);

        // Extract Dopler Model
        return expressionParser.getDopler();
    }
}
