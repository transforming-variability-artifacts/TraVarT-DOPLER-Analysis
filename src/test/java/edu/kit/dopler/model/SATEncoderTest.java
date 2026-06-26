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

//
// public class SATEncoderTest extends TestCase {
//
//
/// / public int getFeatureIDConfigs(Path filePath) throws IOException {
/// / FileSystem.INSTANCE = new JavaFileSystem();
/// / LongRunningWrapper.INSTANCE = new LongRunningCore();
/// / Logger.logger = new JavaLogger();
/// /
/// /
// FMFactoryManager.getInstance().addExtension(DefaultFeatureModelFactory.getInstance());
/// /
// FMFactoryManager.getInstance().addExtension(MultiFeatureModelFactory.getInstance());
/// / FMFactoryManager.getInstance().setWorkspaceLoader(new
// CoreFactoryWorkspaceLoader());
////
//// CLIFunctionManager.getInstance().addExtension(new
// ConfigurationGenerator());
////
//// String content = Files.readString(filePath);
//// IFeatureModel featureIdeFm = new MultiFeatureModelFactory().create();
//// UVLFeatureModelFormat format = new UVLFeatureModelFormat();
//// format.read(featureIdeFm, content);
////
//// FeatureModelFormula formula = new FeatureModelFormula(featureIdeFm);
//// List<LiteralSet> configs = LongRunningWrapper.runMethod(new
// AllConfigurationGenerator(formula.getCNF()));
//// return configs.size();
////
//// }
//
//
// public void testVariantsWaterfilter() throws
// NotSupportedVariabilityTypeException, IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir")
// +"/VariabilityEval/BoolCSV/product_waterfilter.csv"));
// assertEquals(10,getAmountOfConfigs(dopler,""));
// }
//
// public void testVariantsHICSSDM() throws
// NotSupportedVariabilityTypeException, IOException {
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/VariabilityEval/BoolCSV/HICSSDM.csv"));
// assertEquals(4185,getAmountOfConfigs(dopler,"(assert (=
// END_DECISION_0_Particular_Packages true)) (assert (=
// END_DECISION_3_Document_Management true)) (assert (=
// END_DECISION_3_Customer_Management true)) (assert (=
// END_DECISION_3_Claims_Management true))"));
//
// }
//
// public void testVariantsHICSSDMOnlySolution() throws
// NotSupportedVariabilityTypeException, IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/VariabilityEval/BoolCSV/HICSSDM.csv"));
// assertEquals(2,getAmountOfConfigs(dopler,"(assert (= END_DECISION_0_Solution
// true))"));
//
// }
//
// public void testVariantsVaMoS() throws NotSupportedVariabilityTypeException,
// IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/VariabilityEval/BoolCSV/VaMoS.csv"));
// assertEquals(30,getAmountOfConfigs(dopler,"(assert (= END_DECISION_2
// true))"));
//
// }
//
// public void testVariantsDoplerTools() throws
// NotSupportedVariabilityTypeException, IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir")
// +"/VariabilityEval/BoolCSV/DOPLERTools2.csv"));
// String asserts = " (assert (= END_DECISION_12 true)) (assert (=
// END_DECISION_13 true)) (assert (= END_DECISION_16_Table-based true)) (assert
// (= END_DECISION_21_After-first-selection true)) (assert (= END_DECISION_17
// true)) (assert (= END_DECISION_20 true)) (assert (= END_DECISION_5 true))
// (assert (= END_DECISION_8 true)) (assert (= END_DECISION_10 true)) (assert (=
// END_DECISION_3 true))";
// assertEquals(2048,getAmountOfConfigs(dopler,asserts));
//
// }
//
// public void testVariantsrockerswitch() throws
// NotSupportedVariabilityTypeException, IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir")
// +"/VariabilityEval/BoolCSV/product_rockerswitch.csv"));
// String asserts = " (assert (= END_DECISION_0 true))(assert (= END_DECISION_8
// true)) (assert (= END_DECISION_13 true)) (assert (= END_DECISION_15 true))
// (assert (= END_DECISION_21 true)) (assert (= END_DECISION_22 true))";
// assertEquals(1536,getAmountOfConfigs(dopler,asserts));
//
// }
//
// public void testVariantseShop() throws NotSupportedVariabilityTypeException,
// IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/VariabilityEval/BoolCSV/eShop.csv"));
//
// assertEquals(76,getAmountOfConfigs(dopler,""));
//
// }
//
// public void testVariantsASEJ1() throws NotSupportedVariabilityTypeException,
// IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/VariabilityEval/BoolCSV/ASEJ1.csv"));
// Path filePath = Paths.get(System.getProperty("user.dir") +
// "/VariabilityEval/BoolUVL/ASEJ1.uvl");
//
// assertEquals(getFeatureIDConfigs(filePath),getAmountOfConfigs(dopler,""));
//
// }
//
// public void testVariantsDissModel() throws
// NotSupportedVariabilityTypeException, IOException {
//
// Dopler dopler =readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/VariabilityEval/BoolCSV/DissModel.csv"));
// Path filePath = Paths.get(System.getProperty("user.dir") +
// "/VariabilityEval/BoolUVL/DissModel.uvl");
// assertEquals(getFeatureIDConfigs(filePath),getAmountOfConfigs(dopler,""));
//
// }
//
// public void testVariantsPizzas() throws NotSupportedVariabilityTypeException,
// IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/VariabilityEval/BoolCSV/Pizzas.csv"));
// Path filePath = Paths.get(System.getProperty("user.dir") +
// "/VariabilityEval/BoolUVL/pizza.uvl");
// assertEquals(getFeatureIDConfigs(filePath),getAmountOfConfigs(dopler,""));
//
// }
//
// public void testPerformanceFeatureIde() throws IOException {
// Path filePath = Paths.get(System.getProperty("user.dir") +
// "/VariabilityEval/BoolCSV/dm_DissModel.uvl");
// System.out.println(getFeatureIDConfigs(filePath));
// }
//
// public void testPerformanceSMT() throws IOException,
// NotSupportedVariabilityTypeException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir")
// +"/VariabilityEval/BoolCSV/dm_DissModel.csv"));
// getAmountOfConfigs(dopler,"");
// }
//
//
// public void testVariantsPizzas2() throws
// NotSupportedVariabilityTypeException, IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/VariabilityEval/BoolCSV/Pizzas2.csv"));
//
// assertEquals(21,getAmountOfConfigs(dopler,""));
//
// }
//
// public void testVariantsShiftfork() throws
// NotSupportedVariabilityTypeException, IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir")
// +"/VariabilityEval/BoolCSV/product_shiftfork.csv"));
//
// assertEquals(4,getAmountOfConfigs(dopler,""));
//
// }
//
// public void testVariantsTruck() throws NotSupportedVariabilityTypeException,
// IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir")
// +"/VariabilityEval/BoolCSV/product_truck.csv"));
//
// assertEquals(4,getAmountOfConfigs(dopler,""));
//
// }
//
// public void testVariantsChesspiece() throws
// NotSupportedVariabilityTypeException, IOException {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir")
// +"/VariabilityEval/BoolCSV/product_chesspiece.csv"));
//
// assertEquals(6,getAmountOfConfigs(dopler,""));
//
// }
// public void testGetModelsFromCsv() throws Exception {
//
//
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/modelCSVs/dm_DOPLERTools.csv"));
// dopler.toSMTStream().build().forEach(System.out::println);
// assertTrue(checkSat(dopler.toSMTStream()));
// /**
// Stream.Builder<String> builder = dopler.toSMTStream();
// builder.add("(assert (= END_DECISION_2_Sicilian true))");
// assertTrue(checkSat(builder));
// **/
// }
//
// public void testEShopModelwithUnsatAsserts() throws Exception {
//
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/modelCSVs/dm_eShop_DM.csv"));
// Stream.Builder<String> builder = dopler.toSMTStream();
// builder.add("(assert (= END_DECISION_5_Payments true))");
// builder.add("(assert (= END_DECISION_4 false))");
// assertFalse(checkSat(builder));
//
// }
//
//
// /**
// * Asserts that a decision is not Taken but another isTaken with the
// visibility condition (if the first isTaken) -> so it should be unsat
// * @throws Exception
// */
// public void testDissModelwithUnsatAssert() throws Exception {
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/modelCSVs/dm_DissModel.csv"));
// Stream.Builder<String> builder = dopler.toSMTStream();
// builder.add("(assert (= DECISION_0_TAKEN_POST false))");
// builder.add("(assert (= DECISION_2_TAKEN_POST true))");
// assertFalse(checkSat(builder));
// }
//
// public void testEShopModelwithUnsatAssertsForInvalidCardinality() throws
// Exception {
//
//
// Dopler dopler = readDOPLERModelFromFile(Path.of(
// System.getProperty("user.dir") +"/modelCSVs/dm_eShop_DM.csv"));
// Stream.Builder<String> builder = dopler.toSMTStream();
// builder.add("(assert (= END_DECISION_5_Payments false))");
// builder.add("(assert (= END_DECISION_5_Security false))");
// builder.add("(assert (= END_DECISION_5_Orders false))");
// builder.add("(assert (= END_DECISION_5_Wishlist false))");
// assertFalse(checkSat(builder));
//
// }
//
//
//
// public void testDoplerModelVaMoS() throws Exception {
//
// Dopler dopler = new Dopler(new HashSet<>(),new HashSet<>(),new
// HashSet<>(),"");
// BooleanDecision decision1 = new BooleanDecision("test", "Support GSM 1900
// protocol?","",new BooleanLiteralExpression(true), new HashSet<>());
// dopler.addDecision(decision1);
//
//
// Enumeration enumerationDecision2 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("WAV"));
// add(new EnumerationLiteral("MP3"));
// }});
// EnumerationDecision decision2 = new EnumerationDecision("test", "Which audio
// formats shall be supported?","", new BooleanLiteralExpression(true), new
// HashSet<>(),enumerationDecision2,1,2);
// dopler.addDecision(decision2);
//
// BooleanDecision decision3 = new BooleanDecision("test", "Support for taking
// photos?","",new BooleanLiteralExpression(true), new HashSet<>());
// dopler.addDecision(decision3);
//
//
// Enumeration enumerationDecision4 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("2.1MP"));
// add(new EnumerationLiteral("5MP"));
// add(new EnumerationLiteral("3.1MP"));
// }});
//
// IExpression expression = new Equals(new
// DecisionValueCallExpression(decision3),new BooleanLiteralExpression(true));
// EnumerationDecision decision4 = new EnumerationDecision("test", "Required
// camera resolution?","", expression, new
// HashSet<>(),enumerationDecision4,1,1);
// dopler.addDecision(decision4);
//
//
//
//
//
// BooleanDecision decision5 = new BooleanDecision("test", "Support for
// recording MP3 audio?","",new BooleanLiteralExpression(true), new
// HashSet<>());
// IExpression expression2 = new Equals(new
// DecisionValueCallExpression(decision5),new BooleanLiteralExpression(true));
//
// Rule rule = new Rule(expression2,new HashSet<>(){{
// add(new EnumEnforce(decision2, new StringValue("MP3")));
// }});
// decision5.addRule(rule);
// dopler.addDecision(decision5);
//
// dopler.toSMTStream().build().forEach(System.out::println);
// assertTrue(checkSat(dopler.toSMTStream()));
//
// }
//
// public void testDoplerModelEShop() throws Exception {
//
// Dopler dopler = new Dopler(new HashSet<>(),new HashSet<>(),new
// HashSet<>(),"");
// BooleanDecision decision1 = new BooleanDecision("test", "Do you want to
// create an Onlineshop?","",new BooleanLiteralExpression(true), new
// HashSet<>());
// dopler.addDecision(decision1);
//
//
// Enumeration enumerationDecision2 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("DebitCard"));
// add(new EnumerationLiteral("CreditCard"));
// }});
// EnumerationDecision decision2 = new EnumerationDecision("test", "Which
// payment methods should be supported??","", new
// BooleanLiteralExpression(true), new HashSet<>(),enumerationDecision2,1,1);
// dopler.addDecision(decision2);
//
// BooleanDecision decision3 = new BooleanDecision("test", "Should a search
// function be suppoted?","",new BooleanLiteralExpression(true), new
// HashSet<>());
// IExpression expressionDecision3 = new Equals(new
// DecisionValueCallExpression(decision3),new BooleanLiteralExpression(false));
//
// Enumeration enumerationDecision6 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Orders"));
// add(new EnumerationLiteral("Security"));
// add(new EnumerationLiteral("Payments"));
// add(new EnumerationLiteral("Wishlist"));
// }});
//
//
//
//
//
// BooleanDecision decision4 = new BooleanDecision("test", "Split products into
// categories?","",new BooleanLiteralExpression(true), new HashSet<>());
//
//
// BooleanDecision decision5 = new BooleanDecision("test", "Should products be
// sortable?","",new BooleanLiteralExpression(true), new HashSet<>());
//
// dopler.addDecision(decision4);
// dopler.addDecision(decision5);
//
// EnumerationDecision decision6 = new EnumerationDecision("test", "Which user
// management enhancements should be supported?","", new
// BooleanLiteralExpression(true), new HashSet<>(),enumerationDecision6,1,4);
// dopler.addDecision(decision6);
//
// Rule rule1Decision3 = new Rule(expressionDecision3,new HashSet<>(){{
// add(new BooleanEnforce(decision5, BooleanValue.getTrue()));
// }});
//
// IExpression expression2Decision3 = new Equals(new
// DecisionValueCallExpression(decision3),new BooleanLiteralExpression(true));
//
// Rule rule2Decision3 = new Rule(expression2Decision3,new HashSet<>(){{
// add(new EnumEnforce(decision6, new StringValue("Security")));
// }});
// decision3.addRule(rule1Decision3);
// decision3.addRule(rule2Decision3);
// dopler.addDecision(decision3);
//
// Rule rule1Decision5 = new Rule(
// new Equals(new DecisionValueCallExpression(decision5),new
// BooleanLiteralExpression(false)),
// new HashSet<>(){{
// add(new BooleanEnforce(decision3, BooleanValue.getTrue()));
// }});
//
// decision5.addRule(rule1Decision5);
//
// decision6.addRule(
// new Rule(
// new NOT(new Equals(new DecisionValueCallExpression(decision6),new
// EnumeratorLiteralExpression(new EnumerationLiteral("Security")))),
// new HashSet<>(){{
// add(new Allows(decision6, new StringValue("Payments")));
// }}
// )
// );
//
// decision6.addRule(
// new Rule(
// new Equals(new DecisionValueCallExpression(decision6),new
// EnumeratorLiteralExpression(new EnumerationLiteral("Security"))),
// new HashSet<>(){{
// add(new DisAllows(decision6, new StringValue("Payments")));
// }}
// )
// );
//
// decision6.addRule(
// new Rule(
// new NOT(new Equals(new DecisionValueCallExpression(decision6),new
// EnumeratorLiteralExpression(new EnumerationLiteral("Payments")))),
// new HashSet<>(){{
// add(new Allows(decision6, new StringValue("Security")));
// }}
// )
// );
//
// decision6.addRule(
// new Rule(
// new Equals(new DecisionValueCallExpression(decision6),new
// EnumeratorLiteralExpression(new EnumerationLiteral("Payments"))),
// new HashSet<>(){{
// add(new DisAllows(decision6, new StringValue("Security")));
// }}
// )
// );
//
//
//
//
// dopler.toSMTStream().build().forEach(System.out::println);
// assertTrue(checkSat(dopler.toSMTStream()));
//
// }
//
//
// public void testDoplerModelPizzas() throws Exception {
// Dopler dopler = new Dopler(new HashSet<>(),new HashSet<>(),new
// HashSet<>(),"");
//
//
//
// Enumeration enumerationDecision2 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Salami"));
// add(new EnumerationLiteral("Ham"));
// add(new EnumerationLiteral("Mozzarella"));
// }});
// EnumerationDecision decision2 = new EnumerationDecision("test", "Which
// Topping do you want on your pizza?","", new BooleanLiteralExpression(true),
// new HashSet<>(),enumerationDecision2,1,3);
// dopler.addDecision(decision2);
//
//
// Enumeration enumerationDecision3 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Normal"));
// add(new EnumerationLiteral("Big"));
// }});
// EnumerationDecision decision3 = new EnumerationDecision("test", "Which Size
// should the pizza have?","", new BooleanLiteralExpression(true), new
// HashSet<>(),enumerationDecision3,1,1);
// dopler.addDecision(decision3);
//
//
// Enumeration enumerationDecision4 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Neapolitan"));
// add(new EnumerationLiteral("Sicilian"));
// }});
// EnumerationDecision decision4 = new EnumerationDecision("test", "Which Dough
// do you prefer??","", new BooleanLiteralExpression(true), new
// HashSet<>(),enumerationDecision4,1,1);
// dopler.addDecision(decision4);
//
//
// BooleanDecision decision1 = new BooleanDecision("test", "Should the pizza
// have a cheesy crust?","",new BooleanLiteralExpression(true), new
// HashSet<>());
// dopler.addDecision(decision1);
//
// IExpression expressionDecision1 = new Equals(new
// DecisionValueCallExpression(decision1),new BooleanLiteralExpression(true));
//
// Rule ruleDecision1 = new Rule(expressionDecision1,new HashSet<>(){{
// add(new EnumEnforce(decision3, new StringValue("Big")));
// }});
// decision1.addRule(ruleDecision1);
//
// dopler.toSMTStream().build().forEach(System.out::println);
// assertTrue(checkSat(dopler.toSMTStream()));
//
// }
//
// public void testreviewerTest() throws Exception {
//
//
// Dopler dopler = new Dopler(new HashSet<>(),new HashSet<>(),new
// HashSet<>(),"");
// BooleanDecision decision1 = new BooleanDecision("test", "Do you want to
// create an Onlineshop?","",new BooleanLiteralExpression(true), new
// HashSet<>());
//
//
//
// BooleanDecision decision3 = new BooleanDecision("test", "Should a search
// function be suppoted?","",new BooleanLiteralExpression(true), new
// HashSet<>());
// decision3.setVisibilityCondition(new IsTaken(decision1));
// decision1.setVisibilityCondition(new IsTaken(decision3));
//
//
//
// dopler.addDecision(decision1);
// dopler.addDecision(decision3);
// dopler.toSMTStream().build().forEach(System.out::println);
// getAmountOfConfigs(dopler,"");
//
// assertTrue(checkSat(dopler.toSMTStream()));
// }
//
//
// public void testDoplerModelPizzas2() throws Exception {
// Dopler dopler = new Dopler(new HashSet<>(),new HashSet<>(),new
// HashSet<>(),"");
//
//
//
// Enumeration enumerationDecision2 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Salami"));
// add(new EnumerationLiteral("Ham"));
// add(new EnumerationLiteral("Mozzarella"));
// }});
// EnumerationDecision decision2 = new EnumerationDecision("test", "Which
// Topping do you want on your pizza?","", new BooleanLiteralExpression(true),
// new HashSet<>(),enumerationDecision2,1,3);
// dopler.addDecision(decision2);
//
//
// Enumeration enumerationDecision3 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Normal"));
// add(new EnumerationLiteral("Big"));
// }});
// EnumerationDecision decision3 = new EnumerationDecision("test", "Which Size
// should the pizza have?","", new BooleanLiteralExpression(true), new
// HashSet<>(),enumerationDecision3,1,1);
// dopler.addDecision(decision3);
//
//
// Enumeration enumerationDecision4 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Neapolitan"));
// add(new EnumerationLiteral("Sicilian"));
// }});
// EnumerationDecision decision4 = new EnumerationDecision("test", "Which Dough
// do you prefer??","", new BooleanLiteralExpression(true), new
// HashSet<>(),enumerationDecision4,1,1);
// dopler.addDecision(decision4);
//
//
// BooleanDecision decision1 = new BooleanDecision("test", "Should the pizza
// have a cheesy crust?","",new BooleanLiteralExpression(true), new
// HashSet<>());
// dopler.addDecision(decision1);
//
// IExpression expressionDecision3 = new Equals(new
// DecisionValueCallExpression(decision3),new EnumeratorLiteralExpression(new
// EnumerationLiteral("Big")));
//
// Rule ruleDecision3 = new Rule(expressionDecision3,new HashSet<>(){{
// add(new EnumEnforce(decision4, new StringValue("Neapolitan")));
// }});
// decision3.addRule(ruleDecision3);
//
// IExpression expressionDecision4 = new Equals(new
// DecisionValueCallExpression(decision4),new EnumeratorLiteralExpression(new
// EnumerationLiteral("Sicilian")));
//
// Rule ruleDecision4 = new Rule(expressionDecision4,new HashSet<>(){{
// add(new BooleanEnforce(decision1, BooleanValue.getTrue()));
// }});
// decision4.addRule(ruleDecision4);
//
//
//
// IExpression expressionDecision1 = new Equals(new
// DecisionValueCallExpression(decision1),new BooleanLiteralExpression(true));
//
// Rule ruleDecision1 = new Rule(expressionDecision1,new HashSet<>(){{
// add(new EnumEnforce(decision3, new StringValue("Big")));
// }});
// decision1.addRule(ruleDecision1);
//
// dopler.toSMTStream().build().forEach(System.out::println);
// assertTrue(checkSat(dopler.toSMTStream()));
//
// }
//
// public void testDoplerModelPizzas2withUnsatAssert() throws Exception {
// Dopler dopler = new Dopler(new HashSet<>(),new HashSet<>(),new
// HashSet<>(),"");
//
//
//
// Enumeration enumerationDecision2 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Salami"));
// add(new EnumerationLiteral("Ham"));
// add(new EnumerationLiteral("Mozzarella"));
// }});
// EnumerationDecision decision2 = new EnumerationDecision("test", "Which
// Topping do you want on your pizza?","", new BooleanLiteralExpression(true),
// new HashSet<>(),enumerationDecision2,1,3);
// dopler.addDecision(decision2);
//
//
// Enumeration enumerationDecision3 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Normal"));
// add(new EnumerationLiteral("Big"));
// }});
// EnumerationDecision decision3 = new EnumerationDecision("test", "Which Size
// should the pizza have?","", new BooleanLiteralExpression(true), new
// HashSet<>(),enumerationDecision3,1,1);
// dopler.addDecision(decision3);
//
//
// Enumeration enumerationDecision4 = new Enumeration(new HashSet<>(){{
// add(new EnumerationLiteral("Neapolitan"));
// add(new EnumerationLiteral("Sicilian"));
// }});
// EnumerationDecision decision4 = new EnumerationDecision("test", "Which Dough
// do you prefer??","", new BooleanLiteralExpression(true), new
// HashSet<>(),enumerationDecision4,1,1);
// dopler.addDecision(decision4);
//
//
// BooleanDecision decision1 = new BooleanDecision("test", "Should the pizza
// have a cheesy crust?","",new BooleanLiteralExpression(true), new
// HashSet<>());
// dopler.addDecision(decision1);
//
// IExpression expressionDecision3 = new Equals(new
// DecisionValueCallExpression(decision3),new EnumeratorLiteralExpression(new
// EnumerationLiteral("Big")));
//
// Rule ruleDecision3 = new Rule(expressionDecision3,new HashSet<>(){{
// add(new EnumEnforce(decision4, new StringValue("Neapolitan")));
// }});
// decision3.addRule(ruleDecision3);
//
// IExpression expressionDecision4 = new Equals(new
// DecisionValueCallExpression(decision4),new EnumeratorLiteralExpression(new
// EnumerationLiteral("Sicilian")));
//
// Rule ruleDecision4 = new Rule(expressionDecision4,new HashSet<>(){{
// add(new BooleanEnforce(decision1, BooleanValue.getTrue()));
// }});
// decision4.addRule(ruleDecision4);
//
//
//
// IExpression expressionDecision1 = new Equals(new
// DecisionValueCallExpression(decision1),new BooleanLiteralExpression(true));
//
// Rule ruleDecision1 = new Rule(expressionDecision1,new HashSet<>(){{
// add(new EnumEnforce(decision3, new StringValue("Big")));
// }});
// decision1.addRule(ruleDecision1);
//
// dopler.toSMTStream().build().forEach(System.out::println);
// Stream.Builder<String> builder = dopler.toSMTStream();
// builder.add("(assert (= END_DECISION_2_Sicilian true))");
// assertFalse(checkSat(builder));
//
// }
//
// }
