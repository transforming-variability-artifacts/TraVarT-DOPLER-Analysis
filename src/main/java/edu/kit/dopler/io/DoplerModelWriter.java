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
package edu.kit.dopler.io;

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.basic.Rule;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.model.decisions.NumberDecision;
import edu.kit.dopler.model.expressions.DoubleLiteralExpression;
import edu.kit.dopler.model.expressions.GreaterThan;
import edu.kit.dopler.model.expressions.IExpression;
import edu.kit.dopler.model.expressions.LessThan;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

public class DoplerModelWriter {

    public void writeCSV(Dopler dm, Path path) throws IOException {
        Objects.requireNonNull(dm);
        Objects.requireNonNull(path);

        try (FileWriter out = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
            // Write Header
            out.write("ID;Question;Type;Range;Cardinality;Constraint/Rule;Visible/relevant if\n");

            for (IDecision<?> obj : dm.getDecisions()) {
                String rangeString = createRangeString(obj);
                String rulesString = createRulesString(obj);
                String cardinalityString = createCardinalityString(obj);

                // Write lines
                String line = obj.getDisplayId() + ";" + obj.getQuestion() + ";" + obj.getDecisionType() + ";"
                        + rangeString + ";" + cardinalityString + ";" + rulesString + ";" + obj.getVisibilityCondition()
                        + "\n";
                out.write(line);
            }
        }
    }

    public void writeJson(Dopler dm, Path path) throws IOException {
        Objects.requireNonNull(dm);
        Objects.requireNonNull(path);

        try (FileWriter out = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
            // Write Header
            out.write("{ \n  \"DOPLER\": {\n");

            // Write Decisions
            String space = "      ";
            List<IDecision<?>> decisions = new ArrayList<>(dm.getDecisions());
            for (int i = 0; i < decisions.size(); i++) {
                IDecision<?> obj = decisions.get(i);

                out.write("    \"" + obj.getDisplayId() + "\": {\n");
                out.write(space + "\"Question\": \"" + obj.getQuestion() + "\",\n");
                out.write(space + "\"Type\": \"" + obj.getDecisionType() + "\",\n");
                out.write(space + "\"Range\": \"" + createRangeString(obj) + "\",\n");
                if (createCardinalityString(obj).equals("")) {
                    out.write(space + "\"Cardinality\": \"" + "\",\n");
                } else {
                    out.write(space + "\"Cardinality\": \"" + createCardinalityString(obj) + "\",\n");
                }
                if (createRulesString(obj).equals("")) {
                    out.write(space + "\"Constraint/Rule\": \"" + "\",\n");
                } else {
                    out.write(space + "\"Constraint/Rule\": " + createRulesString(obj) + ",\n");
                }
                out.write(space + "\"Visible/relevant if\": \"" + obj.getVisibilityCondition() + "\"\n");

                out.write("    }");

                if (i < decisions.size() - 1) {
                    out.write(",");
                }
                out.write("\n");
            }

            // Write Closing brackets
            out.write("  }\n}");
        }
    }

    private String createCardinalityString(IDecision<?> decision) {
        if (decision instanceof EnumerationDecision enumDecision) {
            return enumDecision.getMinCardinality() + ":" + enumDecision.getMaxCardinality();
        }
        return "";
    }

    private String createRulesString(IDecision<?> decision) {
        String rulesString;
        Set<Rule> rulesSet = decision.getRules();

        if (rulesSet.isEmpty()) {
            return "";
        }

        StringBuilder rulesSetBuilder = new StringBuilder("\"");
        for (Rule rule : rulesSet) {
            rulesSetBuilder.append(rule);
        }
        rulesSetBuilder.append("\"");
        rulesString = rulesSetBuilder.toString();
        return rulesString;
    }

    private String createRangeString(IDecision<?> decision) {
        switch (decision.getDecisionType()) {
            case BOOLEAN -> {
                return "false | true";
            }
            case NUMBER -> {
                String numberRange = "-";
                NumberDecision numberDecision = (NumberDecision) decision;
                Set<IExpression> validityConditions = numberDecision.getValidityConditions();
                for (IExpression con : validityConditions) {
                    if (con instanceof GreaterThan gt) {
                        DoubleLiteralExpression ngt = (DoubleLiteralExpression) gt.getLeftExpression();
                        numberRange = (ngt.getLiteral() + 1) + numberRange;
                    } else if (con instanceof LessThan lt) {
                        DoubleLiteralExpression nlt = (DoubleLiteralExpression) lt.getLeftExpression();
                        numberRange = numberRange + (nlt.getLiteral() - 1);
                    }
                }
                return numberRange.equals("-") ? "" : numberRange;
            }
            case ENUM -> {
                EnumerationDecision enumDecision = (EnumerationDecision) decision;
                List<EnumerationLiteral> enumeration =
                        new ArrayList<>(enumDecision.getEnumeration().getEnumerationLiterals());
                enumeration.sort(Comparator.comparing(EnumerationLiteral::getValue));
                return String.join(
                        " | ",
                        enumeration.stream().map(EnumerationLiteral::getValue).toList());
            }
            default -> {
                return "";
            }
        }
    }
}
