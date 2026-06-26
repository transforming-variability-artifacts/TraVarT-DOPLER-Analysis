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
package edu.kit.dopler.model.expressions;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.exceptions.EvaluationException;
import edu.kit.dopler.model.decisions.IDecision;
import java.util.List;
import java.util.Map;

public sealed interface IExpression permits Expression {

    boolean evaluate() throws EvaluationException;

    /**
     * Converts the current expression into a CP literal.
     *
     * @param model
     *            the constraint programming model
     * @param decisionVars
     *            a map associating each decision of a dopler model with a list of
     *            CP variables representing it
     * @param isTakenVars
     *            a map associating each decision of a dopler model with a boolean
     *            literal indicating whether the decision is taken
     * @return a CP literal that represents the expression in the model
     */
    Literal toCpLiteral(
            CpModel model, Map<IDecision<?>, List<IntVar>> decisionVars, Map<IDecision<?>, Literal> isTakenVars);
}
