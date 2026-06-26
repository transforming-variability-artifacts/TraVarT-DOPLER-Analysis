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

import static edu.kit.dopler.common.CpUtils.CP_ENUM_SEPARATOR;

import com.google.ortools.sat.*;
import edu.kit.dopler.model.basic.EnumerationLiteral;
import edu.kit.dopler.model.decisions.Decision;
import edu.kit.dopler.model.decisions.EnumerationDecision;
import edu.kit.dopler.model.decisions.IDecision;
import edu.kit.dopler.solvers.shared.AnomalyReport;
import edu.kit.dopler.solvers.shared.ValueAnomaly;
import java.util.*;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the result of encoding a DOPLER model in a constraint programming
 * (CP) model. Provides functionalities to check satisfiability, print
 * configurations, calculate the total number of solutions, and perform anomaly
 * analyses.
 */
public final class CpEncodingResult {
    private static final int MAX_SOLUTION_COUNT = 20_000;
    private static final Logger LOGGER = LoggerFactory.getLogger(CpEncodingResult.class);

    private final CpModel model;
    private final List<List<IntVar>> variables;
    private final Map<IDecision<?>, Literal> isTakenVars;
    private final Map<IDecision<?>, List<IntVar>> decisionVars;

    /**
     * Constructs a new CpEncodingResult object.
     *
     * @param model
     *            The constraint programming model associated with this result.
     * @param variables
     *            A list of lists containing CP variables for the model. Each inner
     *            list represents variables of a single decision.
     */
    public CpEncodingResult(
            CpModel model,
            List<List<IntVar>> variables,
            Map<IDecision<?>, Literal> isTakenVars,
            Map<IDecision<?>, List<IntVar>> decisionVars) {
        this.model = model;
        this.variables = variables;
        this.isTakenVars = isTakenVars;
        this.decisionVars = decisionVars;
    }

    public CpModel getModel() {
        return model;
    }

    public List<List<IntVar>> getVariables() {
        return variables;
    }

    /**
     * Determines the satisfiability of the CpEncodingResult.
     *
     * @return True if the model is satisfiable (has feasible or optimal solutions),
     *         false otherwise.
     */
    public boolean checkSat() {
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(this.model);

        return status == CpSolverStatus.FEASIBLE || status == CpSolverStatus.OPTIMAL;
    }

    /**
     * Calculates the total number of configurations (solutions) of the
     * CpEncodingResult. Note: This operation may take a significant amount of time
     * for large models!
     *
     * @return The total number of solutions found.
     */
    public int getAmountOfConfigs() {
        CpSolver solver = new CpSolver();
        solver.getParameters().setEnumerateAllSolutions(true);

        // Local callback that simply counts each solution visited by the solver.
        class SolutionCounter extends CpSolverSolutionCallback {
            private int solutionCount = 0;

            @Override
            public void onSolutionCallback() {
                solutionCount++;
                if (solutionCount >= MAX_SOLUTION_COUNT) {
                    System.out.printf("Stopped counting after %d solutions.%n", MAX_SOLUTION_COUNT);
                    stopSearch();
                }
            }

            int getSolutionCount() {
                return solutionCount;
            }
        }

        SolutionCounter counter = new SolutionCounter();
        solver.solve(this.model, counter);
        return counter.getSolutionCount();
    }

    /**
     * Solves the CpEncodingResult and prints one satisfiable configuration, if
     * available.
     */
    public void printOneConfig() {
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(this.model);

        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            System.out.printf("Solution found, time = %.2f s%n", solver.wallTime());
            for (List<IntVar> vars : this.variables) {
                printCPDecisionVariables(vars, solver::value);
            }
        } else {
            System.out.println("No solution found.");
        }
    }

    /**
     * Solves the CpEncodingResult and prints all possible configurations. Note:
     * This operation may take a significant amount of time for large models!
     */
    public void printAllConfigs() {
        CpSolver solver = new CpSolver();

        // Local callback that prints each solution visited by the solver.
        class VarArraySolutionPrinter extends CpSolverSolutionCallback {
            private final List<List<IntVar>> variables;
            private int solutionCount = 0;

            public VarArraySolutionPrinter(List<List<IntVar>> variables) {
                this.variables = variables;
            }

            @Override
            public void onSolutionCallback() {
                System.out.printf("Solution #%d, time = %.2f s%n", solutionCount, wallTime());
                for (List<IntVar> vars : this.variables) {
                    printCPDecisionVariables(vars, this::value);
                }
                solutionCount++;
            }

            public int getSolutionCount() {
                return solutionCount;
            }
        }

        VarArraySolutionPrinter printer = new VarArraySolutionPrinter(this.variables);
        solver.getParameters().setEnumerateAllSolutions(true);
        solver.solve(this.model, printer);
        System.out.println("#solutions: " + printer.getSolutionCount());
    }

    private void printCPDecisionVariables(List<IntVar> vars, ToLongFunction<IntVar> getValue) {
        if (vars == null || vars.isEmpty()) return;

        if (vars.size() == 1) {
            IntVar variable = vars.getFirst();
            long value = getValue.applyAsLong(variable);
            if (variable instanceof BoolVar) {
                // bool:
                System.out.printf("  %s = %s%n", variable.getName(), value == 1 ? "true" : "false");
            } else {
                // number:
                System.out.printf("  %s = %f%n", variable.getName(), CpUtils.scaleLongToDouble(value));
            }
        } else {
            // enum:
            String literals = vars.stream()
                    .filter(var -> getValue.applyAsLong(var) == 1)
                    .map(var -> var.getName().split(CP_ENUM_SEPARATOR)[1])
                    .sorted()
                    .collect(Collectors.joining(", "));
            System.out.printf("  %s = [%s]%n", vars.getFirst().getName().split(CP_ENUM_SEPARATOR)[0], literals);
        }
    }

    /**
     * Detects logical anomalies in a DOPLER model.
     * Evaluates Dead/False Optional decisions and Dead/False Optional decision values.
     *
     * @return The {@link AnomalyReport}.
     */
    public AnomalyReport detectAnomalies() {
        if (!this.checkSat()) {
            LOGGER.warn("Model is unsatisfiable!");
            return AnomalyReport.empty(true);
        }

        List<IDecision<?>> deadDecisions = new ArrayList<>();
        List<IDecision<?>> falseOptionalDecisions = new ArrayList<>();
        List<ValueAnomaly> deadValues = new ArrayList<>();
        List<ValueAnomaly> falseOptionalValues = new ArrayList<>();

        for (Map.Entry<IDecision<?>, Literal> entry : isTakenVars.entrySet()) {
            IDecision<?> d = entry.getKey();
            Literal isTaken = entry.getValue();

            // Check Dead Decision: Does forcing the decision to be taken result in an invalid model?
            CpModel testDead = this.model.getClone();
            testDead.addBoolOr(new Literal[] {isTaken});
            CpSolver solver = new CpSolver();
            if (solver.solve(testDead) == CpSolverStatus.INFEASIBLE) {
                deadDecisions.add(d);
                continue; // A dead decision cannot have valid values.
            }

            List<IntVar> vars = decisionVars.get(d);
            if (vars != null && !vars.isEmpty()) {
                if (d.getDecisionType() == Decision.DecisionType.ENUM) {
                    EnumerationDecision ed = (EnumerationDecision) d;
                    int i = 0;
                    for (EnumerationLiteral lit : ed.getEnumeration().getEnumerationLiterals()) {
                        IntVar litVar = vars.get(i++);

                        // If forcing this literal results in INFEASIBLE, it's a dead value.
                        CpModel tm1 = testDead.getClone();
                        tm1.addEquality(litVar, 1);
                        if (solver.solve(tm1) == CpSolverStatus.INFEASIBLE) {
                            deadValues.add(new ValueAnomaly(d, lit));
                        }

                        // If forbidding this literal results in INFEASIBLE, it must be chosen.
                        CpModel tm2 = testDead.getClone();
                        tm2.addEquality(litVar, 0);
                        if (solver.solve(tm2) == CpSolverStatus.INFEASIBLE) {
                            falseOptionalValues.add(new ValueAnomaly(d, lit));
                        }
                    }
                } else if (d.getDecisionType() == Decision.DecisionType.BOOLEAN) {
                    IntVar valVar = vars.getFirst();

                    // If setting to true breaks the model, true is dead and false is implicitly forced.
                    CpModel tm1 = testDead.getClone();
                    tm1.addEquality(valVar, 1);
                    if (solver.solve(tm1) == CpSolverStatus.INFEASIBLE) {
                        deadValues.add(new ValueAnomaly(d, true));
                        falseOptionalValues.add(new ValueAnomaly(d, false));
                    }

                    // If setting to false breaks the model, false is dead and true is implicitly forced.
                    CpModel tm2 = testDead.getClone();
                    tm2.addEquality(valVar, 0);
                    if (solver.solve(tm2) == CpSolverStatus.INFEASIBLE) {
                        deadValues.add(new ValueAnomaly(d, false));
                        falseOptionalValues.add(new ValueAnomaly(d, true));
                    }
                } else if (d.getDecisionType() == Decision.DecisionType.NUMBER) {
                    IntVar valVar = vars.getFirst();
                    if (solver.solve(testDead) == CpSolverStatus.FEASIBLE
                            || solver.solve(testDead) == CpSolverStatus.OPTIMAL) {

                        long val = solver.value(valVar);

                        // Forbid the currently valid number to see if it's the only valid number.
                        CpModel tm1 = testDead.getClone();
                        tm1.addDifferent(valVar, val);
                        if (solver.solve(tm1) == CpSolverStatus.INFEASIBLE) {
                            falseOptionalValues.add(new ValueAnomaly(d, val));
                        }
                    }
                }
            }

            // Check False Optional Decision: Does omitting the decision result in an invalid model?
            CpModel testFalseOpt = this.model.getClone();
            testFalseOpt.addBoolOr(new Literal[] {isTaken.not()});
            if (new CpSolver().solve(testFalseOpt) == CpSolverStatus.INFEASIBLE) {
                falseOptionalDecisions.add(d);
            }
        }

        Collections.sort(deadValues);
        Collections.sort(falseOptionalValues);
        return new AnomalyReport(false, deadDecisions, falseOptionalDecisions, deadValues, falseOptionalValues);
    }
}
