# CP Encoding Design Decisions

## Google OR-Tools (CP-SAT-Solver)
The CP encoding uses the [Google OR-Tools CP-SAT solver](https://developers.google.com/optimization/cp/cp_solver). 
This library provides a powerful constraint programming solver that can handle boolean, integer, and linear constraints.

## CP Encoding
The CP encoding is implemented in the `Dopler` class via the `toCPModel` method. 
It returns a `CpEncodingResult` which contains the constraints programming (CP) model and the mapping of decisions to their corresponding CP variables.
This enables users to display satisfiable configurations later on.
The encoding is described in detail below.

### Decision Variables
For every decision in the DOPLER model, one or more CP variables are created:
- **Boolean Decisions**: A single `BoolVar`.
- **Enumeration Decisions**: Multiple `BoolVar(s)`, one for each literal in the enumeration. Additionaly constraints enforcing the cardinality of the enumeration decision are added (see [below](#cardinality-enumeration-decision)).
- **Number Decisions**: An `IntVar`. Although the underlying type is an integer, real-valued decisions (e.g., doubles) are supported by a scaling factor (e.g., 0.0001). The `CpUtils` class manages this scaling process to ensure the correct conversion between the scaled integer representation and the actual double value.

Additionally, for each decision, a `BoolVar` named `Decision_<ID>_isTaken` is created to track whether the decision is taken in a current configuration.

The current CP encoding does not support String and Java Decisions because the solver does not natively support them.
Adding support would require substantial custom extensions and represent a significant development effort.

#### Cardinality (Enumeration Decision)
The cardinality of an enumeration decision, such as `1:3`, is encoded using a helper `IntVar`, called `sum`.
This variable describes the number of true enumeration literal CP variables of the enumeration decision:
> $sum = \sum_{i=1}^{\\# enumLiterals} {𝟙}_{enumLiteral_i = true} $

Then we can add the cardinality constraints to the `sum` variable.
Java code to achieve this:
```java
LinearExpr sum = LinearExpr.sum(enumDecisionLiteralVariables);

model.addGreaterOrEqual(sum, minCardinality).onlyEnforceIf(isTaken);
model.addLessOrEqual(sum, maxCardinality).onlyEnforceIf(isTaken);
```

### Constraints
After the decision variables are created, different constraints are added to the model.
These constraints, representing the decision rules, validity conditions, and possible standard values, are described in detail below.

#### Rules
A decision rule consists of a condition expression and associated actions.
If the condition holds and the decision is taken, those actions are executed.
The CP literal `conditionLiteral` encodes this logic:
> $conditionLiteral \iff (Decision\textunderscore\<ID\>\textunderscore isTaken \land ruleCondition)$

The following describes how the actions are encoded in the CP model.

##### BooleanEnforce
To enforce a boolean decision, we add the following constraint:
> $conditionLiteral \implies (booleanDecision = value)$

We use this implication to ensure that the action is executed if the `conditionLiteral` holds.
If the `conditionLiteral` does not hold, we do not care whether the action is executed.
The Java code to achieve this is as follows:
```java
 model.addEquality(booleanDecision, value).onlyEnforceIf(conditionLiteral);
```

##### EnumEnforce
To enforce an enumeration decision, we proceed the same way as we do for boolean decisions.
We only first need to choose the correct CP variable for the given enumeration literal.

##### NumberEnforce
To enforce a number decision, we again proceed the same way as we do for boolean decisions.

##### DisAllows
The disallows action is only relevant for enumeration decisions.
To ensure that a given enumeration literal is not chosen, we perform an enumEnforce where the value we enforce is the false literal.

##### Allows
The allows action is only relevant for enumeration decisions.
To ensure that a given enumeration literal can be chosen, we have to do nothing since the CP solver already checks the whole range of possible values for each variable.


#### Validity Conditions
Some decision types have validity conditions.
These are enforced if they are taken:
> $Decision\textunderscore\<ID\>\textunderscore isTaken \implies enforceValidityCondition$

The following describes how these validity conditions are enforced in more detail.

##### Range (Number Decision)
The range of a number decision, such as `0-10`, is encoded using the corresponding greater than, less than, and equal to expressions.

#### Standard Values
For every decision that is not taken, we add a constraint that enforces the standard value.
> $\lnot Decision\textunderscore\<ID\>\textunderscore isTaken \implies enforceStdValue$


### Expression Literals
For every DOPLER expression, there is a `toCPLiteral` method that returns a corresponding CP literal.
This literal can then be used in constraints (see [section on constraints](#constraints)).
The following describes how each expression is represented as a CP literal.

#### NOT
The CP solver natively supports negations.
> $\lnot expression$

Java code to achieve this:
```java
expression.not();
```

#### IsTAKEN
For the isTaken expression, we use the `Decision_<ID>_isTaken` variable introduced above.
> $Decision\textunderscore\<ID\>\textunderscore isTaken$

A Decision is taken if it is visible or if it was enforced by a rule-action (from another decision).

To represent this logic with the `Decision_<ID>_isTaken` variable, we use a list of helper boolean CP variables (per decision) called `isTakenConditionsList`.
Each entry in `isTakenConditionsList` indicates whether a rule action of another decision was performed or not.
We construct this list by accumulating all the `conditionLiteral` variables (see [section on rules](#rules)) of rule-actions that enforce the associated decision's value.
Then we add the following constraint to the CP model:
> $Decision\textunderscore\<ID\>\textunderscore isTaken = \bigvee_{condition \in (isTakenConditionsList \cup \lbrace Decision\textunderscore\<ID\>\textunderscore IsVisible \rbrace)} $

Java code to achieve this:
```java
List<Literal> conditionsList = isTakenConditionsList;
conditionsList.add(decisionIsVisibleVar);

//ensure that: isTakenVar <=> or(conditionsList)
// "=>" as CNF
model.addBoolOr(conditionsList).onlyEnforceIf(isTakenVar);

// "<=" as CNF
conditionsList.forEach(var -> model.addBoolOr(new Literal[]{var.not(), isTakenVar}));
```

#### Binary Expressions
All binary expressions follow the general form: `leftExpression <operation> rightExpression`.

To represent such a binary expression, a special `BoolVar` (called `equivalentLiteral`) is created.
The following describes how this `equivalentLiteral` is made to represent different binary expressions.
The formulas may seem confusing or unnecessarily complex at first.
However, this is because the CP solver only allows a limited set of internal operators. Therefore, complex logic, such as equivalences, must be encoded via these formulas.

##### AND
For this operation, we want to ensure that the following holds: $equivalentLiteral \iff (leftExpression \land rightExpression)$.

To achieve this, we add constraints representing the bidirectional implications ' $\implies$ ' and ' $\impliedby$ ' to the CP model:

> ' $\implies$ ' : $(\lnot equivalentLiteral \lor leftExpression) \land (\lnot equivalentLiteral \lor rightLiteral)$
>
> ' $\impliedby$ ' : $\lnot leftExpression \lor \lnot rightExpression \lor equivalentLiteral$

Java code to achieve this:
```java
BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

//ensure that: equivalentLiteral <=> (leftLiteral and rightLiteral)
// "=>" as CNF
model.addBoolOr(new Literal[]{equivalentLiteral.not(), leftLiteral});
model.addBoolOr(new Literal[]{equivalentLiteral.not(), rightLiteral});

// "<=" as CNF
model.addBoolOr(new Literal[]{leftLiteral.not(), rightLiteral.not(), equivalentLiteral});
```

##### OR
For this operation, we want to ensure that the following holds: $equivalentLiteral \iff (leftExpression \lor rightExpression)$.

To achieve this, we add constraints representing the bidirectional implications ' $\implies$ ' and ' $\impliedby$ ' to the CP model:

> ' $\implies$ ' : $\lnot equivalentLiteral \lor leftExpression \lor rightExpression$
>
> ' $\impliedby$ ' : $(\lnot leftExpression \lor equivalentLiteral) \land (\lnot rightExpression \lor equivalentLiteral)$

Java code to achieve this:
```java
BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

//ensure that: equivalentLiteral <=> (leftLiteral or rightLiteral)
// "=>" as CNF
model.addBoolOr(new Literal[]{equivalentLiteral.not(), leftLiteral, rightLiteral});

// "<=" as CNF
model.addBoolOr(new Literal[]{leftLiteral.not(), equivalentLiteral});
model.addBoolOr(new Literal[]{rightLiteral.not(), equivalentLiteral});
```

##### XOR
For this operation, we want to ensure that the following holds: $equivalentLiteral \iff (leftExpression \oplus rightExpression)$.

To achieve this, we add constraints representing the bidirectional implications ' $\implies$ ' and ' $\impliedby$ ' to the CP model:

> ' $\implies$ ' : $(\lnot equivalentLiteral \lor  leftExpression \lor rightExpression) \land (\lnot equivalentLiteral \lor \lnot leftExpression \lor \lnot rightExpression)$
>
> ' $\impliedby$ ' : $(equivalentLiteral \lor  leftExpression \lor \lnot rightExpression) \land (equivalentLiteral \lor  \lnot leftExpression \lor rightExpression)$

Java code to achieve this:
```java
BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

//ensure that: equivalentLiteral <=> (leftLiteral xor rightLiteral)
// "=>" as CNF
model.addBoolOr(new Literal[]{equivalentLiteral.not(), leftLiteral, rightLiteral});
model.addBoolOr(new Literal[]{equivalentLiteral.not(), leftLiteral.not(), rightLiteral.not()});

// "<=" as CNF
model.addBoolOr(new Literal[]{equivalentLiteral, leftLiteral, rightLiteral.not()});
model.addBoolOr(new Literal[]{equivalentLiteral, leftLiteral.not(), rightLiteral});
```

##### GreaterThan
For this operation, we want to ensure that the following holds: $equivalentLiteral \iff (leftExpression \gt rightExpression)$.

To achieve this, we add constraints representing the bidirectional implications ' $\implies$ ' and ' $\impliedby$ ' to the CP model:

> ' $\implies$ ' : $equivalentLiteral \implies (leftExpression \gt rightExpression)$
>
> ' $\impliedby$ ' : $\lnot equivalentLiteral \implies (leftExpression \leq rightExpression)$

Java code to achieve this:
```java
BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

//ensure that: equivalentLiteral <=> (left > right)
// "=>": equivalentLiteral => (left > right)
model.addGreaterThan(left, right).onlyEnforceIf(equivalentLiteral);

// "<=": !equivalentLiteral => (left <= right)
model.addLessOrEqual(left, right).onlyEnforceIf(equivalentLiteral.not());
```

##### LessThan
For this operation, we want to ensure that the following holds: $equivalentLiteral \iff (leftExpression \lt rightExpression)$.

To achieve this, we add constraints representing the bidirectional implications ' $\implies$ ' and ' $\impliedby$ ' to the CP model:

> ' $\implies$ ' : $equivalentLiteral \implies (leftExpression \lt rightExpression)$
>
> ' $\impliedby$ ' : $\lnot equivalentLiteral \implies (leftExpression \geq rightExpression)$

Java code to achieve this:
```java
BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

//ensure that: equivalentLiteral <=> (left < right)
// "=>": equivalentLiteral => (left < right)
model.addLessThan(left, right).onlyEnforceIf(equivalentLiteral);

// "<=": !equivalentLiteral => (left >= right)
model.addGreaterOrEqual(left, right).onlyEnforceIf(equivalentLiteral.not());
```

##### EQUALS
For this operation, we want to ensure that the following holds: $equivalentLiteral \iff (leftExpression = rightExpression)$.

To achieve this, we add constraints representing the bidirectional implications ' $\implies$ ' and ' $\impliedby$ ' to the CP model:

> ' $\implies$ ' : $equivalentLiteral \implies (leftExpression = rightExpression)$
>
> ' $\impliedby$ ' : $\lnot equivalentLiteral \implies (leftExpression \neq rightExpression)$

Java code to achieve this:
```java
BoolVar equivalentLiteral = model.newBoolVar("equivalentLiteral");

//ensure that: equivalentLiteral <=> (left == right)
// "=>": equivalentLiteral => (left == right)
model.addEquality(left, right).onlyEnforceIf(equivalentLiteral);

// "<=": !equivalentLiteral => (left != right)
model.addDifferent(left, right).onlyEnforceIf(equivalentLiteral.not());
```
