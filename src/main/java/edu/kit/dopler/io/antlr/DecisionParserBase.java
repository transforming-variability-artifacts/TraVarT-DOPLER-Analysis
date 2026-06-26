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
package edu.kit.dopler.io.antlr;

import edu.kit.dopler.io.antlr.resources.DoplerParserBaseListener;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.IDecision;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public abstract class DecisionParserBase extends DoplerParserBaseListener {
    protected final int column_ID = 0;
    protected Dopler dopler;
    protected String currentID = "";
    protected String currentQuestion = "";
    protected String currentDescription = "";

    public Dopler getDopler() {
        return dopler;
    }

    protected boolean matchesColumn(ParserRuleContext ctx, int column) {
        return ctx.getParent() == ctx.getParent().getParent().children.get(column_ID);
    }

    protected IDecision<?> findDecisionByID(String ID) {
        return dopler.getDecisions().stream()
                .filter(d -> d.getDisplayId().equals(ID))
                .findFirst()
                .orElse(null);
    }

    protected EnumerationLiteral findEnumerationLiteralByName(String name) {
        return dopler.getEnumSet().stream()
                .flatMap(e -> e.getEnumerationLiterals().stream())
                .filter(lit -> lit.getValue().equals(name))
                .findFirst()
                .orElse(null);
    }

    protected List<TerminalNode> getAllTerminalNodes(ParseTree tree) {
        List<TerminalNode> terminals = new ArrayList<>();
        collectTerminals(tree, terminals);
        return terminals;
    }

    protected void collectTerminals(ParseTree tree, List<TerminalNode> result) {
        if (tree instanceof TerminalNode) {
            result.add((TerminalNode) tree);
        } else {
            for (int i = 0; i < tree.getChildCount(); i++) {
                collectTerminals(tree.getChild(i), result);
            }
        }
    }
}
