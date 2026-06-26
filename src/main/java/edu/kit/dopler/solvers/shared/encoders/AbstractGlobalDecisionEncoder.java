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
package edu.kit.dopler.solvers.shared.encoders;

import edu.kit.dopler.model.decisions.IDecision;

/**
 * Maps the logic of a decision into the underlying solver.
 *
 * <p>
 * This is done in 3 steps:
 * <ol>
 * <li>Rules: If this decision is taken and a rule condition holds, trigger the actions.</li>
 * <li>Validity: If this decision is taken, its value must respect validity limits (e.g., ranges).</li>
 * <li>Defaults: If this decision is NOT taken, its value must be set to the standard default.</li>
 * </ol>
 * </p>
 *
 * @param <C> The solver context type.
 * @param <V> The formula/variable type representing the "Taken" state.
 */
public abstract class AbstractGlobalDecisionEncoder<C, V> {

    /**
     * Maps the rules, validity, and defaults of this decision into solver constraints.
     *
     * @param decision The DOPLER decision to map.
     * @param ctx      The context holding active constraints and variables.
     */
    protected final void mapLogicToConstraintsInternal(IDecision<?> decision, C ctx) {
        V isTaken = getTakenVariable(decision, ctx);

        // Map Rules (Triggers actions if Taken + Condition is met)
        mapRules(decision, isTaken, ctx);

        // Enforce Validity Limits (Only applicable if the decision is Taken)
        enforceValidity(decision, isTaken, ctx);

        // Enforce Default Value (Only applicable if the decision is not Taken)
        enforceDefaultValue(decision, isTaken, ctx);
    }

    /**
     * Retrieves the variable representing the "Taken" state of the decision.
     */
    protected abstract V getTakenVariable(IDecision<?> decision, C ctx);

    /**
     * Maps the rules associated with this decision into the solver.
     * <p>
     * A rule only fires if the parent decision is taken AND the rule's condition is
     * true. If these are met, an activation literal is passed to the actions so
     * they can assert their specific constraints and update the
     * isTakenConditions for their targets.
     * </p>
     */
    protected abstract void mapRules(IDecision<?> decision, V isTaken, C ctx);

    /**
     * Enforces the validity conditions (e.g., Number Ranges, constraints) for this decision.
     * <p>
     * This generates the constraint:
     * {@code isTaken => (Condition1 AND Condition2...)}.
     * </p>
     */
    protected abstract void enforceValidity(IDecision<?> decision, V isTaken, C ctx);

    /**
     * Enforces the standard/default value for this decision.
     * <p>
     * This generates the constraint: {@code !isTaken => (Value == StandardValue)}.
     * </p>
     */
    protected abstract void enforceDefaultValue(IDecision<?> decision, V isTaken, C ctx);
}
