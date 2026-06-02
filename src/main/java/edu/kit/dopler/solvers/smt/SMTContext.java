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
package edu.kit.dopler.solvers.smt;

import edu.kit.dopler.model.decisions.IDecision;
import java.util.*;
import org.sosy_lab.java_smt.api.*;

/**
 * This class acts as a wrapper around the JavaSMT {@link SolverContext} and {@link ProverEnvironment}.
 */
public final class SMTContext implements AutoCloseable {

    private final SolverContext solverContext;
    private final ProverEnvironment prover;
    private final Map<String, Formula> vars;
    private final List<BooleanFormula> constraints = new ArrayList<>();
    private final Map<IDecision<?>, List<BooleanFormula>> isTakenConditions;

    private SMTContext(
            SolverContext solverContext,
            ProverEnvironment prover,
            Map<String, Formula> vars,
            Map<IDecision<?>, List<BooleanFormula>> isTakenConditions) {
        this.solverContext = solverContext;
        this.prover = prover;
        this.vars = vars;
        this.isTakenConditions = isTakenConditions;
    }

    /**
     * Creates a new, basic SMT context without model generation capabilities.
     *
     * @param solverContext The underlying JavaSMT solver context.
     * @return A new {@link SMTContext} instance.
     */
    public static SMTContext create(SolverContext solverContext) {
        return create(solverContext, false);
    }

    /**
     * Creates a new SMT context, optionally enabling model extraction features.
     *
     * @param solverContext The underlying JavaSMT solver context.
     * @param generateAllModels If {@code true}, the prover will be configured to generate models.
     * @return A new {@link SMTContext} instance.
     */
    public static SMTContext create(SolverContext solverContext, boolean generateAllModels) {
        ProverEnvironment prover;
        if (generateAllModels) {
            prover = solverContext.newProverEnvironment(
                    SolverContext.ProverOptions.GENERATE_ALL_SAT, SolverContext.ProverOptions.GENERATE_MODELS);
        } else {
            prover = solverContext.newProverEnvironment();
        }
        return new SMTContext(solverContext, prover, new HashMap<>(), new HashMap<>());
    }

    /**
     * Retrieves the underlying JavaSMT solver context.
     *
     * @return The {@link SolverContext}.
     */
    public SolverContext solverContext() {
        return solverContext;
    }

    /**
     * Retrieves the global formula manager.
     *
     * @return The {@link FormulaManager}.
     */
    public FormulaManager fm() {
        return solverContext.getFormulaManager();
    }

    /**
     * Retrieves the underlying prover.
     *
     * @return The {@link ProverEnvironment}.
     */
    public ProverEnvironment prover() {
        return prover;
    }

    /**
     * Retrieves an unmodifiable map of all declared variables.
     *
     * @return A map linking variable string names to their SMT {@link Formula} representation.
     */
    public Map<String, Formula> getVars() {
        return Collections.unmodifiableMap(vars);
    }

    /**
     * Registers a new SMT variable in the context.
     *
     * @param name The unique string identifier for the variable.
     * @param formula The SMT {@link Formula} representing the variable.
     */
    public void putVar(String name, Formula formula) {
        vars.put(name, formula);
    }

    /**
     * Retrieves an existing SMT variable by its name.
     *
     * @param name The string identifier of the variable.
     * @return The associated {@link Formula}, or {@code null} if it does not exist.
     */
    public Formula getVar(String name) {
        return vars.get(name);
    }

    /**
     * Checks whether a variable has been registered in the context.
     *
     * @param name The string identifier of the variable.
     * @return {@code true} if the variable exists, {@code false} otherwise.
     */
    public boolean hasVar(String name) {
        return vars.containsKey(name);
    }

    /**
     * Asserts a new boolean constraint to the prover.
     *
     * @param formula The {@link BooleanFormula} constraint to add.
     */
    public void addClause(BooleanFormula formula) {
        try {
            prover.addConstraint(formula);
            constraints.add(formula);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while adding constraint", e);
        }
    }

    /**
     * Adds the given 'TAKEN' conditions for a specific decision.
     *
     * @param decision The DOPLER decision to start tracking.
     */
    public void addIsTakenConditions(IDecision<?> decision, BooleanFormula... conditions) {
        List<BooleanFormula> takenConditions = isTakenConditions.computeIfAbsent(decision, d -> new ArrayList<>());
        takenConditions.addAll(Arrays.asList(conditions));
    }

    /**
     * Retrieves an unmodifiable list of 'TAKEN' conditions for a specific decision.
     *
     * @param decision The target decision.
     * @return A list of {@link BooleanFormula} representing the taken conditions.
     */
    public List<BooleanFormula> getIsTakenConditions(IDecision<?> decision) {
        return Collections.unmodifiableList(isTakenConditions.getOrDefault(decision, List.of()));
    }

    /**
     * Checks if the currently asserted constraints render the model unsatisfiable.
     *
     * @return {@code true} if the model is UNSAT, {@code false} if SAT or UNKNOWN.
     * @throws SolverException If the native solver encounters an error.
     * @throws InterruptedException If the solver's execution is interrupted.
     */
    public boolean isUnsat() throws SolverException, InterruptedException {
        return prover.isUnsat();
    }

    /**
     * Checks if the currently asserted constraints are satisfiable.
     *
     * @return {@code true} if the model is SAT, {@code false} otherwise.
     * @throws SolverException If the native solver encounters an error.
     * @throws InterruptedException If the solver's execution is interrupted.
     */
    public boolean isSat() throws SolverException, InterruptedException {
        return !prover.isUnsat();
    }

    /**
     * Closes the context and releases all underlying native solver resources.
     * Must be called (e.g., via try-with-resources) to prevent memory leaks.
     */
    @Override
    public void close() {
        prover.close();
        solverContext.close();
    }

    /**
     * Generates a raw SMT-LIB representation of the current context state.
     *
     * @return A string containing the SMT-LIB 2 equivalent of this context.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(set-logic ALL)\n"); // ALL is needed for simultaneous NRA and S support
        for (Map.Entry<String, Formula> entry : vars.entrySet()) {
            sb.append("(declare-fun ")
                    .append(entry.getKey())
                    .append(" () ")
                    .append(fm().getFormulaType(entry.getValue()).toSMTLIBString())
                    .append(")\n");
        }
        for (BooleanFormula f : constraints) {
            sb.append("(assert ").append(f.toString()).append(")\n");
        }
        return sb.toString();
    }
}
