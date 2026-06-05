/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Copyright 2024 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *******************************************************************************/
package edu.kit.dopler.model;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import edu.kit.dopler.common.CpEncodingResult;
import edu.kit.dopler.model.basic.Assets;
import edu.kit.dopler.model.basic.Enumeration;
import edu.kit.dopler.model.decisions.IDecision;
import java.util.*;

public class Dopler {

    Set<IDecision<?>> decisions;
    Set<Assets> assets;
    Set<Enumeration> enumSet;
    String name;

    public Dopler() {
        this(new HashSet<>(), new HashSet<>(), new HashSet<>(), "");
    }

    public Dopler(Set<IDecision<?>> decisions, Set<Assets> assets, Set<Enumeration> enumSet, String name) {
        this.decisions = decisions;
        this.assets = assets;
        this.enumSet = enumSet;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDecision(IDecision<?> decisionType) {
        decisions.add(decisionType);
    }

    public void removeDecision(IDecision<?> decisionType) {
        decisions.remove(decisionType);
    }

    public Set<IDecision<?>> getDecisions() {
        return decisions;
    }

    public void setDecisions(Set<IDecision<?>> decisions) {
        this.decisions = decisions;
    }

    public Set<Assets> getAssets() {
        return assets;
    }

    public void setAssets(Set<Assets> assets) {
        this.assets = assets;
    }

    public Set<Enumeration> getEnumSet() {
        return enumSet;
    }

    public void setEnumSet(Set<Enumeration> enumSet) {
        this.enumSet = enumSet;
    }

    public void addEnum(Enumeration e) {
        this.enumSet.add(e);
    }

    /**
     * Creates a CP encoding of the DOPLER model.
     *
     * @return A CpEncodingResult object containing the generated CP model and the
     *         variables associated with each decision.
     */
    public CpEncodingResult toCpModel() {
        CpModel model = new CpModel();

        Map<IDecision<?>, List<IntVar>> decisionVars = new HashMap<>(); // maps each decision to a list of CP variables
        // that will represent it in the constraint
        // programming model

        Map<IDecision<?>, Literal> isTakenVars = new HashMap<>(); // maps each decision to a CP boolean literal that
        // will be logically equivalent to that decision being
        // taken (or not)
        Map<IDecision<?>, List<Literal>> isTakenConditions = new HashMap<>(); // (this is a helper, that) maps each
        // decision to a list of CP variables that
        // are used to add the constraints for
        // isTakenVars to be logically correct

        // Multiple loops are needed in the following because the maps (from above) need
        // to be filled for all decisions before they can be used for the next step(s):

        // 1. Initialize maps for tracking IsTaken status
        initializeTakenMapsInCp(isTakenVars, model, isTakenConditions);

        // 2. Create CP variables for each decision
        this.decisions.forEach(decision -> {
            decision.createCpDecisionVariables(model, decisionVars, isTakenVars); // initialize the decisionVars (in the
            // following there will only be
            // reading accesses to the
            // decisionVars)
        });

        // 3. Map model-level logic (rules, standard values and validity) to CP
        // constraints
        this.decisions.forEach(
                decision -> { // (For this loop, the decisionVars and the isTakenVars need to be
                    // initialized!)
                    decision.mapLogicToConstraintsInCp(model, decisionVars, isTakenVars, isTakenConditions);
                });

        // 4. Ensure logical consistency for isTaken literals
        enforceIsTakenConsistencyInCp(isTakenVars, model, decisionVars, isTakenConditions);

        return new CpEncodingResult(model, decisionVars.values().stream().toList(), isTakenVars, decisionVars);
    }

    private void initializeTakenMapsInCp(
            Map<IDecision<?>, Literal> isTakenVars, CpModel model, Map<IDecision<?>, List<Literal>> isTakenConditions) {
        this.decisions.forEach(decision -> {
            isTakenVars.put(
                    decision, model.newBoolVar("Decision_" + decision.getDisplayId() + "_isTaken")); // initialize
            // the
            // isTakenVars
            // (in the
            // following
            // there
            // will
            // only be
            // reading
            // accesses
            // to the
            // isTakenVars)
            isTakenConditions.put(decision, new ArrayList<>()); // initialize the helper map for the isTakenVars (these
            // lists will be filled when the rules are mapped to CP)
        });
    }

    private void enforceIsTakenConsistencyInCp(
            Map<IDecision<?>, Literal> isTakenVars,
            CpModel model,
            Map<IDecision<?>, List<IntVar>> decisionVars,
            Map<IDecision<?>, List<Literal>> isTakenConditions) {
        this.decisions.forEach(
                decision -> { // (For this loop, the decisionVars and the isTakenVars need to be
                    // initialized; and the isTakenConditions need to be completely filled!)
                    // Add the CP constraints that ensure that the isTakenVars are logically
                    // correct.
                    // A decision is taken if it is visible (1) or if it was enforced by a
                    // rule-action (from another decision) (2):
                    Literal isTakenVar = isTakenVars.get(decision);
                    Literal isVisibleVar =
                            decision.getVisibilityCondition().toCpLiteral(model, decisionVars, isTakenVars); // (1)
                    List<Literal> isTakenConditionsList = isTakenConditions.get(decision); // (2)
                    isTakenConditionsList.add(isVisibleVar);

                    // ensure that: isTakenVar <=> or(isTakenConditionsList)
                    // "=>" as CNF
                    model.addBoolOr(isTakenConditionsList).onlyEnforceIf(isTakenVar);

                    // "<=" as CNF
                    isTakenConditionsList.forEach(var -> model.addBoolOr(new Literal[] {var.not(), isTakenVar}));
                });
    }
}
