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
package edu.kit.dopler.solvers.ilp;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import edu.kit.dopler.model.decisions.IDecision;
import java.util.*;

/**
 * This class acts as a wrapper around the OR-Tools {@link MPSolver}.
 */
public final class ILPContext implements AutoCloseable {

    private final MPSolver solver;

    // Core config variables separated by type
    private final Map<String, MPVariable> coreBoolVars = new HashMap<>();
    private final Map<String, MPVariable> coreRealVars = new HashMap<>();

    // Constant pooling
    private final Map<Double, MPVariable> constants = new HashMap<>();

    private final Map<IDecision<?>, List<MPVariable>> isTakenConditions = new HashMap<>();
    private int auxCounter = 0;

    private ILPContext(MPSolver solver) {
        this.solver = solver;
    }

    /**
     * Creates a new, basic ILP context without model generation capabilities.
     *
     * @return A new {@link ILPContext} instance.
     */
    public static ILPContext create() {
        return create(ILPConstants.getDefaultModel());
    }

    /**
     * Creates a new, basic ILP context without model generation capabilities.
     *
     * @param model The ILP solver model to use.
     * @return A new {@link ILPContext} instance.
     */
    public static ILPContext create(ILPConstants.ILPModels model) {
        return new ILPContext(MPSolver.createSolver(model.getModelName()));
    }

    /**
     * Retrieves the underlying OR-Tools solver context.
     *
     * @return The {@link MPSolver}.
     */
    public MPSolver solver() {
        return solver;
    }

    /**
     * Creates a binary [0, 1] integer variable acting as a boolean.
     */
    public MPVariable createCoreBoolVar(String name) {
        MPVariable var = solver.makeBoolVar(name);
        coreBoolVars.put(name, var);
        return var;
    }

    /**
     * Creates a core variable.
     */
    public MPVariable createCoreNumVar(String name, double lb, double ub, boolean isInteger) {
        MPVariable var = isInteger ? solver.makeIntVar(lb, ub, name) : solver.makeNumVar(lb, ub, name);
        if (!isInteger) { // There are no Integer types in DOPLER.
            coreRealVars.put(name, var);
        }
        return var;
    }

    /**
     * Creates an auxiliary variable for intermediate logic/Tseitin transformations.
     */
    public MPVariable createAuxVar(String baseName, double lb, double ub, boolean isInteger) {
        String name = baseName + "_aux_" + (auxCounter++);
        return isInteger ? solver.makeIntVar(lb, ub, name) : solver.makeNumVar(lb, ub, name);
    }

    /**
     * Creates an auxiliary binary [0, 1] integer variable acting as a boolean.
     */
    public MPVariable createAuxBoolVar(String baseName) {
        return createAuxVar(baseName, 0, 1, true);
    }

    /**
     * Creates or retrieves a fixed MPVariable representing a numeric constant.
     */
    public MPVariable createConstant(double value) {
        return constants.computeIfAbsent(value, val -> {
            String name = "const_" + val + "_aux_" + (auxCounter++);
            return solver.makeNumVar(val, val, name);
        });
    }

    /**
     * Retrieves a core variable.
     */
    public MPVariable getCoreVar(String name) {
        if (coreBoolVars.containsKey(name)) return coreBoolVars.get(name);
        return coreRealVars.get(name);
    }

    /**
     * Retrieves an unmodifiable map of all declared core boolean variables.
     */
    public Map<String, MPVariable> getCoreBoolVars() {
        return Collections.unmodifiableMap(coreBoolVars);
    }

    /**
     * Retrieves an unmodifiable map of all declared core real numerical variables.
     */
    public Map<String, MPVariable> getCoreRealVars() {
        return Collections.unmodifiableMap(coreRealVars);
    }

    /**
     * Adds the given 'TAKEN' conditions for a specific decision.
     *
     * @param decision The DOPLER decision to start tracking.
     */
    public void addIsTakenConditions(IDecision<?> decision, MPVariable... conditions) {
        List<MPVariable> takenConditions = isTakenConditions.computeIfAbsent(decision, d -> new ArrayList<>());
        takenConditions.addAll(Arrays.asList(conditions));
    }

    /**
     * Retrieves an unmodifiable list of 'TAKEN' conditions for a specific decision.
     *
     * @param decision The target decision.
     * @return A list of {@link MPVariable} representing the taken conditions.
     */
    public List<MPVariable> getIsTakenConditions(IDecision<?> decision) {
        return Collections.unmodifiableList(isTakenConditions.getOrDefault(decision, List.of()));
    }

    /**
     * Closes the context and releases all underlying native solver resources.
     * Must be called (e.g., via try-with-resources) to prevent memory leaks.
     */
    @Override
    public void close() {
        solver.delete();
    }

    /**
     * Generates an LP format representation of the current context state.
     *
     * @return A string containing the LP format equivalent of this context.
     */
    @Override
    public String toString() {
        return solver.exportModelAsLpFormat();
    }
}
