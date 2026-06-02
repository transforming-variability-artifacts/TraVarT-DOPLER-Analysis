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
package edu.kit.dopler.solvers.smt.utils;

import edu.kit.dopler.model.Dopler;
import edu.kit.dopler.solvers.smt.SMTContext;
import edu.kit.dopler.solvers.smt.encoders.SMTGlobalConstraintEncoder;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sosy_lab.java_smt.api.*;
import org.sosy_lab.java_smt.api.Model;

public final class SMTAllSatSolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTAllSatSolver.class);
    private static final Random RANDOM = new Random();

    public sealed interface ConfigResult permits FiniteConfigs, InfiniteConfigs {}

    public record FiniteConfigs(int count) implements ConfigResult {
        @Override
        public String toString() {
            return String.valueOf(count);
        }
    }

    public record InfiniteConfigs(String cause) implements ConfigResult {
        @Override
        public String toString() {
            return "Infinite (through '" + cause + "')";
        }
    }

    private SMTAllSatSolver() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    /**
     * Counts the total number of valid configurations in the DOPLER model.
     * <p>
     * Algorithm: All-SAT<br>
     * This method uses Blocking Clauses:
     * <ol>
     * <li>Ask the solver to find a valid model.</li>
     * <li>Extract the exact values for every variable in that model.</li>
     * <li>Add a new constraint (blocking clause) to not get the same configuration
     * again.</li>
     * <li>Repeat until the solver returns UNSATISFIABLE.</li>
     * <li>If an unbound number is detected by the heuristic, there are infinite solutions.</li>
     * </ol>
     * </p>
     *
     * @param dopler       The DOPLER model to analyze.
     * @param useHeuristic If true, enables interval testing to detect continuous Real-valued domains for infinite solutions.
     * @return The total number of unique, valid configurations. Returns 0 if the
     * model is UNSAT and if detected by the heuristics Infinite for continues Real-valued domains or unbound Strings.
     */
    public static ConfigResult getAmountOfConfigs(final Dopler dopler, boolean useHeuristic) {
        int configCount = 0;

        try (SMTContext context = SMTContext.create(SMTUtils.createSolverContext(), true)) {
            SMTGlobalConstraintEncoder.encodeToSMT(dopler, context);

            ProverEnvironment prover = context.prover();
            FormulaManager fm = context.fm();
            BooleanFormulaManager bfm = fm.getBooleanFormulaManager();

            while (!prover.isUnsat()) {
                configCount++;

                List<BooleanFormula> blockingClauses = new ArrayList<>();
                List<BooleanFormula> currentConfigBools = new ArrayList<>();

                try (Model model = prover.getModel()) {
                    for (Map.Entry<String, Formula> entry : context.getVars().entrySet()) {
                        Formula var = entry.getValue();
                        FormulaType<?> type = fm.getFormulaType(var);

                        // Evaluate and handle unconstrained variables (mimics Z3's evaluate(var, true)) for all solvers
                        Object evaluatedVal = model.evaluate(var);
                        Formula evaluatedFormula = createConcreteFormula(fm, type, evaluatedVal);

                        if (!useHeuristic) {
                            // Block every exact assignment (leads to infinite loop on Reals)
                            blockingClauses.add(makeEqual(fm, type, var, evaluatedFormula));
                        } else {
                            // Separate discrete variables to formulate a base configuration
                            if (type.isBooleanType() || type.isIntegerType()) {
                                BooleanFormula assignment = makeEqual(fm, type, var, evaluatedFormula);
                                currentConfigBools.add(assignment);
                                blockingClauses.add(assignment);
                            }
                        }
                    }

                    if (useHeuristic) {
                        InfiniteConfigs entry =
                                checkHeuristic(fm, currentConfigBools, context, model, prover, blockingClauses);
                        if (entry != null) {
                            return entry;
                        }
                    }
                }

                // Add the blocking clause: !(var1=val1 AND var2=val2 ...) to find the next model
                BooleanFormula exactModelMatch = bfm.and(blockingClauses);
                prover.addConstraint(bfm.not(exactModelMatch));
            }

        } catch (SolverException | InterruptedException e) {
            LOGGER.error("SMT Error occurred while counting configurations: {}", e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred while counting configurations: {}", e.getMessage(), e);
        }

        return new FiniteConfigs(configCount);
    }

    private static InfiniteConfigs checkHeuristic(
            FormulaManager fm,
            List<BooleanFormula> currentConfigBools,
            SMTContext context,
            Model model,
            ProverEnvironment prover,
            List<BooleanFormula> blockingClauses)
            throws SolverException, InterruptedException {

        BooleanFormulaManager bfm = fm.getBooleanFormulaManager();
        RationalFormulaManager rfm = fm.getRationalFormulaManager();

        // Lock in the current state to test continuous variables independently
        BooleanFormula baseConfigConstraint = bfm.and(currentConfigBools);

        for (Map.Entry<String, Formula> entry : context.getVars().entrySet()) {
            Formula var = entry.getValue();
            FormulaType<?> type = fm.getFormulaType(var);

            if (type.isRationalType()) {
                NumeralFormula.RationalFormula rVar = (NumeralFormula.RationalFormula) var;

                Object val = model.evaluate(var);
                NumeralFormula.RationalFormula evaluatedFormula =
                        (NumeralFormula.RationalFormula) createConcreteFormula(fm, type, val);

                prover.push();
                prover.addConstraint(baseConfigConstraint);

                // Generate a tiny random epsilon
                String epsilonStr = generateTinyRandomEpsilon();
                NumeralFormula.RationalFormula epsilon = rfm.makeNumber(epsilonStr);

                // Test if evaluating the exact value +/- the epsilon is still satisfiable
                NumeralFormula.RationalFormula plusTest = rfm.add(evaluatedFormula, epsilon);
                NumeralFormula.RationalFormula minusTest = rfm.subtract(evaluatedFormula, epsilon);

                BooleanFormula isContinuous = bfm.or(rfm.equal(rVar, plusTest), rfm.equal(rVar, minusTest));

                prover.addConstraint(isContinuous);

                // If shifting by the random amount is valid, the variable is likely unbound
                if (!prover.isUnsat()) {
                    prover.pop();
                    return new InfiniteConfigs(entry.getKey());
                }

                prover.pop();

                // If the heuristic fails, assume it is a strictly bound, discrete real point.
                blockingClauses.add(rfm.equal(rVar, evaluatedFormula));
            } else if (type.isStringType()) {
                StringFormulaManager sfm = fm.getStringFormulaManager();
                StringFormula sVar = (StringFormula) var;

                // Must be done before push due to the model changing.
                Object evaluated = model.evaluate(entry.getValue());

                prover.push();
                prover.addConstraint(baseConfigConstraint);

                // If a random string is valid, there will likely be infinite configurations.
                String probe = generateRandomProbeString();
                BooleanFormula isProbeString = sfm.equal(sVar, sfm.makeString(probe));
                prover.addConstraint(isProbeString);

                if (!prover.isUnsat()) {
                    prover.pop();
                    return new InfiniteConfigs(entry.getKey());
                }

                prover.pop();

                StringFormula evaluatedFormula = (StringFormula) createConcreteFormula(fm, type, evaluated);
                blockingClauses.add(sfm.equal(sVar, evaluatedFormula));
            }
        }
        return null;
    }

    /**
     * Generates a random string of alphanumeric symbols.
     */
    private static String generateRandomProbeString() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Mimics Z3's evaluate(var, true) model completion feature by supplying
     * concrete default values for unconstrained/null elements.
     */
    private static Formula createConcreteFormula(FormulaManager fm, FormulaType<?> type, Object value) {
        if (type.isBooleanType()) {
            boolean val = (value != null) ? (Boolean) value : false;
            return fm.getBooleanFormulaManager().makeBoolean(val);
        } else if (type.isRationalType()) {
            String val = (value != null) ? value.toString() : "0.0";
            return fm.getRationalFormulaManager().makeNumber(val);
        } else if (type.isIntegerType()) {
            String val = (value != null) ? value.toString() : "0";
            return fm.getIntegerFormulaManager().makeNumber(val);
        } else if (type.isStringType()) {
            String val = (value != null) ? value.toString() : "";
            return fm.getStringFormulaManager().makeString(val);
        }
        throw new IllegalArgumentException("Unsupported SMT Sort: " + type);
    }

    private static BooleanFormula makeEqual(FormulaManager fm, FormulaType<?> type, Formula var, Formula val) {
        if (type.isBooleanType()) {
            return fm.getBooleanFormulaManager().equivalence((BooleanFormula) var, (BooleanFormula) val);
        } else if (type.isRationalType()) {
            return fm.getRationalFormulaManager()
                    .equal((NumeralFormula.RationalFormula) var, (NumeralFormula.RationalFormula) val);
        } else if (type.isIntegerType()) {
            return fm.getIntegerFormulaManager()
                    .equal((NumeralFormula.IntegerFormula) var, (NumeralFormula.IntegerFormula) val);
        } else if (type.isStringType()) {
            return fm.getStringFormulaManager().equal((StringFormula) var, (StringFormula) val);
        }
        throw new IllegalArgumentException("Unsupported SMT Sort for equality constraint: " + type);
    }

    /**
     * Generates a string representation of a highly precise, random rational fraction.
     * E.g., "0.00349...[50 digits]...1".
     */
    private static String generateTinyRandomEpsilon() {
        StringBuilder sb = new StringBuilder("0.");

        sb.repeat("0", 40);
        // Generate 10 random digits
        for (int i = 0; i < 10; i++) {
            sb.append(RANDOM.nextInt(10));
        }

        // Ensure the digit is between 1 and 9 so the fraction never is 0.0
        sb.append(RANDOM.nextInt(9) + 1);

        return sb.toString();
    }
}
