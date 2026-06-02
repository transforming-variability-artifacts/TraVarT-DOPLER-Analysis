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
package edu.kit.dopler.solvers.shared.encoders;

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.model.decisions.IDecision;

/**
 * Entry point for encoding DOPLER to an underlying solver.
 * <p>
 * The transformation is done in 4 steps:
 * </p>
 * <ol>
 * <li>Variable Declaration: Defining variables/constants for each decision.</li>
 * <li>Logic Mapping: Recursively translating Rules and Validity conditions.</li>
 * <li>Visibility/Taken Consistency: Ensuring decisions are active only when
 * visible or enforced.</li>
 * <li>Enumeration Constraints: Modeling Enumeration cardinality (Min/Max selection).</li>
 * </ol>
 *
 * @param <C> The solver context type (e.g., ILPContext, SMTContext).
 */
public abstract class AbstractGlobalConstraintEncoder<C> {

    /**
     * Encodes this DOPLER configuration model into the solver constraints.
     *
     * @param dopler The core DOPLER model.
     * @param ctx    The context to encode constraints into.
     */
    protected final void encode(Dopler dopler, C ctx) {
        createVariables(dopler, ctx);

        // Map the rules to boolean expressions
        for (IDecision<?> decision : dopler.getDecisions()) {
            mapDecisionLogic(decision, ctx);
        }

        // Must know the "Taken" conditions to choose whether a decision has all
        // conditions fulfilled
        enforceVisibilityConsistency(dopler, ctx);
        applyEnumCardinality(dopler, ctx);
    }

    /**
     * Initializes solver variables/constants for every decision in the model.
     * <p>
     * For each decision, this method typically creates:
     * <ul>
     * <li>A "TAKEN" boolean variable indicating if the decision is active/visible.</li>
     * <li>For Enumerations: A boolean variable for each literal to represent selection.</li>
     * <li>For others (strings, numbers, booleans): A value variable of the appropriate sort to hold the decision's data.</li>
     * </ul>
     * </p>
     */
    protected abstract void createVariables(Dopler dopler, C ctx);

    /**
     * Delegates the logic mapping to the corresponding decision encoder.
     */
    protected abstract void mapDecisionLogic(IDecision<?> decision, C ctx);

    /**
     * Enforces the consistency of the "Taken" state.
     * <p>
     * A decision is "Taken" if and only if:
     * {@code isTaken <=> (visibilityCondition == true OR any EnforceAction targets this decision)}.
     * If a decision is NOT taken, its value should be ignored.
     * </p>
     */
    protected abstract void enforceVisibilityConsistency(Dopler dopler, C ctx);

    /**
     * Translates Enum cardinalities into solver constraints.
     * <p>
     * Cardinality Modeling:
     * </p>
     * Each {@code EnumerationLiteral} is represented as a Boolean variable.
     * The sum of these selected literals must be constrained: {@code min <= sum <= max}.
     * <p>
     * This constraint is wrapped in an implication:
     * {@code isTaken => (min <= sum <= max)}. If the decision is not taken, the sum
     * is forced to 0.
     * </p>
     */
    protected abstract void applyEnumCardinality(Dopler dopler, C ctx);
}
