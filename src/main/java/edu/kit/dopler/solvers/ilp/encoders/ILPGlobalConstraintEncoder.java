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
package edu.kit.dopler.solvers.ilp.encoders;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.Decision;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.ilp.ILPConstants;
import edu.kit.dopler.solvers.ilp.ILPContext;
import edu.kit.dopler.solvers.ilp.utils.ILPLogic;
import edu.kit.dopler.solvers.shared.SolverUtils;
import edu.kit.dopler.solvers.shared.encoders.AbstractGlobalConstraintEncoder;
import java.util.ArrayList;
import java.util.List;

public final class ILPGlobalConstraintEncoder extends AbstractGlobalConstraintEncoder<ILPContext> {

    private static final ILPGlobalConstraintEncoder INSTANCE = new ILPGlobalConstraintEncoder();

    private ILPGlobalConstraintEncoder() {}

    public static void encodeToILP(Dopler dopler, ILPContext ctx) {
        INSTANCE.encode(dopler, ctx);
    }

    @Override
    protected void createVariables(Dopler dopler, ILPContext ctx) {
        for (IDecision<?> decision : dopler.getDecisions()) {
            String baseName = SolverUtils.toStringConst(decision);
            ctx.createCoreBoolVar(baseName + ILPConstants.TAKEN_SUFFIX);

            switch (decision.getDecisionType()) {
                case BOOLEAN -> ctx.createCoreBoolVar(baseName + ILPConstants.VALUE_SUFFIX);
                case NUMBER ->
                    ctx.createCoreNumVar(
                            baseName + ILPConstants.VALUE_SUFFIX, -MPSolver.infinity(), MPSolver.infinity(), false);
                case STRING -> throw new UnsupportedOperationException("Strings are not supported in ILP.");
                case ENUM -> {
                    EnumerationDecision enumDec = (EnumerationDecision) decision;
                    for (EnumerationLiteral literal : enumDec.getEnumeration().getEnumerationLiterals()) {
                        ctx.createCoreBoolVar(baseName + "_" + literal.getValue());
                    }
                }
            }
        }
    }

    @Override
    protected void mapDecisionLogic(IDecision<?> decision, ILPContext ctx) {
        ILPGlobalDecisionEncoder.mapLogicToConstraints(decision, ctx);
    }

    @Override
    protected void enforceVisibilityConsistency(Dopler dopler, ILPContext ctx) {
        for (IDecision<?> decision : dopler.getDecisions()) {
            String baseName = SolverUtils.toStringConst(decision);
            MPVariable isTaken = ctx.getCoreVar(baseName + ILPConstants.TAKEN_SUFFIX);
            MPVariable isVisible = ILPExpressionEncoder.encodeExpression(decision.getVisibilityCondition(), ctx);

            List<MPVariable> triggers = new ArrayList<>();
            triggers.add(isVisible);
            triggers.addAll(ctx.getIsTakenConditions(decision));

            MPVariable anyTrigger = ILPLogic.or(ctx, triggers.toArray(new MPVariable[0]));

            // isTaken <= anyTrigger (If no triggers are active, it cannot be taken)
            MPConstraint maxConstraint = ctx.solver().makeConstraint(-MPSolver.infinity(), 0.0);
            maxConstraint.setCoefficient(isTaken, 1.0);
            maxConstraint.setCoefficient(anyTrigger, -1.0);

            // isTaken >= anyTrigger (If any trigger is active, it must be taken)
            MPConstraint minConstraint = ctx.solver().makeConstraint(0.0, MPSolver.infinity());
            minConstraint.setCoefficient(isTaken, 1.0);
            minConstraint.setCoefficient(anyTrigger, -1.0);
        }
    }

    @Override
    protected void applyEnumCardinality(Dopler dopler, ILPContext ctx) {
        for (IDecision<?> decision : dopler.getDecisions()) {
            if (decision.getDecisionType() == Decision.DecisionType.ENUM) {
                EnumerationDecision enumDecision = (EnumerationDecision) decision;
                String baseName = SolverUtils.toStringConst(decision);
                MPVariable isTaken = ctx.getCoreVar(baseName + ILPConstants.TAKEN_SUFFIX);

                int min = enumDecision.getMinCardinality();
                int max = enumDecision.getMaxCardinality();

                MPVariable sumVar = ctx.createAuxVar(baseName + "_sum", 0, max, true);
                MPConstraint sumConstraint = ctx.solver().makeConstraint(0, 0);
                sumConstraint.setCoefficient(sumVar, -1.0);

                for (EnumerationLiteral literal : enumDecision.getEnumeration().getEnumerationLiterals()) {
                    MPVariable litVar = ctx.getCoreVar(baseName + "_" + literal.getValue());
                    sumConstraint.setCoefficient(litVar, 1.0);
                }

                // sumVar >= min * isTaken  =>  sumVar - min * isTaken >= 0
                MPConstraint minConstraint = ctx.solver().makeConstraint(0, MPSolver.infinity());
                minConstraint.setCoefficient(sumVar, 1.0);
                minConstraint.setCoefficient(isTaken, -min);

                // sumVar <= max * isTaken  =>  sumVar - max * isTaken <= 0
                MPConstraint maxConstraint = ctx.solver().makeConstraint(-MPSolver.infinity(), 0);
                maxConstraint.setCoefficient(sumVar, 1.0);
                maxConstraint.setCoefficient(isTaken, -max);
            }
        }
    }
}
