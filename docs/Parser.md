# DOPLER Decision Parser
The DOPLER parser is ANTLR-based with separate lexer and parser grammars and processes an input file (JSON or CSV) in two consecutive listener passes — first to create the Decision objects, second to parse and attach expressions (constraints / visibility conditions) to those previously created decisions. 

This two-pass / multi-listening design is necessary because the input must not enforce a fixed order of decisions and expressions: only after all decisions exist can references like getValue(X) or isTaken(Y) be reliably resolved.

## Setup
Reads the input from the file and creates the corresponding lexer, tokens and parser.
```
		String fileName = "dm_dopler.json";
		CharStream input = CharStreams.fromFileName(fileName);
		DoplerLexer lexer = new DoplerLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DoplerParser parser = new DoplerParser(tokens);
```
## Create Parse Tree
Creates the corresponding parse tree of the earlier input, by calling the first parser rule. The tree can be traversed with a ParseTreeWalker.
```
        ParseTree tree = parser.document(); 
        ParseTreeWalker walker = new ParseTreeWalker();
```

## Two-Pass Walk
Performs two consecutive listener walks: the first constructs all decisions, and the second parses expressions and attaches them to their corresponding decisions.
```
		DoplerDecisionCreator decisionCreator = new DoplerDecisionCreator(fileName);
		walker.walk(decisionCreator, tree);
		DoplerExpressionParser expressionParser = new DoplerExpressionParser(decisionCreator.getDopler());
		walker.walk(expressionParser, tree);
```

## Extract Dopler Model
After walking through both listeners, the resulting Dopler model can be obtained from the DoplerExpressionParser.
```
		Dopler dopler = expressionParser.getDopler();
```

## Write Dopler Model
The DoplerModelWriter can be used to export the parsed model either as JSON or CSV.
```
		DoplerModelWriter dmw = new DoplerModelWriter();
		dmw.writeCSV(dopler, Paths.get("output_dm_dopler.csv"));
		dmw.writeJson(dopler, Paths.get("output_dm_dopler.json"));
```
