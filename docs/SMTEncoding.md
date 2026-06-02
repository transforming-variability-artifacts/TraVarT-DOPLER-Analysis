# SMT Encoding Design

## SMT-LIB Standard
The SMT-LIB standard defines a file format for describing decision problems.
The benefit of a standardized file format is that it is easy to experiment with a range of solvers, and to replace the solver used in case better solvers are developed.

## Overview

The encoding consists of four steps:

1. One set of global constants per decision.
2. Rules and validity conditions into implications for every decision.
3. Visibility of decisions based on conditions and enforcements are modeled.
4. Min/max selection counts for enumeration decisions are enforced arithmetically.

---

## Step 1: Variable Declaration

Every decision gets a `_TAKEN` boolean constant. Its value indicates whether the decision is active.

```
(declare-const DECISION_NAME_TAKEN Bool)
```

For enumeration decisions, each literal in the enumeration gets its own boolean constant:

```
(declare-const DECISION_NAME_LiteralA Bool)
(declare-const DECISION_NAME_LiteralB Bool)
```

For boolean, number, and string decisions, a single value constant is declared with the appropriate sort:

| Decision type | Sort   | Example constant name      |
|---------------|--------|----------------------------|
| Boolean       | Bool   | `DECISION_NAME_VALUE`      |
| Number        | Real   | `DECISION_NAME_VALUE`      |
| String        | String | `DECISION_NAME_VALUE`      |

Number decisions use the Real sort to support decimal (double) values.

A full example:

```
(declare-const Pizza_TAKEN Bool)
(declare-const Pizza_Salami Bool)
(declare-const Pizza_Mozzarella Bool)

(declare-const Price_TAKEN Bool)
(declare-const Price_VALUE Real)
```

---

## Step 2: Mapping

For each decision, three kinds of constraints are generated: rules, validity, and defaults.

### 2.1 Rules

A rule belongs to one decision and has a condition and a list of actions. 
The rule fires only when the source decision is taken and the rule condition holds.
This combined boolean is called the activation:

```
activation := DECISION_NAME_TAKEN AND ruleCondition
```

The activation is passed to each action in the rule to determine if they should apply.

### 2.2 Validity

If a decision defines validity conditions (e.g., a number range), those conditions must hold whenever the decision is taken.
All validity conditions are combined with AND:

```
DECISION_NAME_TAKEN => (validityCondition_1 AND validityCondition_2 AND ...)
```

### 2.3 Default Values

If a decision is not taken, its value is reset to the standard default defined in the model.

```
NOT DECISION_NAME_TAKEN => defaultEquality
```

**Boolean decision default:**
```
NOT DECISION_NAME_TAKEN => (DECISION_NAME_VALUE = standardBooleanValue)
```

**Number decision default:**
```
NOT DECISION_NAME_TAKEN => (DECISION_NAME_VALUE = standardRealValue)
```

**String decision default:**
```
NOT DECISION_NAME_TAKEN => (DECISION_NAME_VALUE = "standardStringValue")
```

**Enumeration decision default:** Every literal is forced to false:
```
NOT DECISION_NAME_TAKEN => (LiteralA = false AND LiteralB = false AND ...)
```

The default constraint does not conflict with enforce actions from other decisions.

---

## Step 3: Visibility Consistency

After all rules have been processed, the `_TAKEN` flag of each decision is defined. A decision is taken if and only if it is visible or at least one enforce action that targets it is currently active:

```
DECISION_NAME_TAKEN = (visibilityCondition OR enforceTrigger_1 OR enforceTrigger_2 OR ...)
```

---

## Step 4: Enumeration Cardinality

Enumeration decisions allow a minimum and maximum number of literals to be selected at the same time.
This is modeled by converting each literal boolean to an integer with an if-then-else expression and summing the results:

```
sum := (ite LiteralA 1 0) + (ite LiteralB 1 0) + ... + (ite LiteralN 1 0)
```

Two constraints are then added:

```
DECISION_NAME_TAKEN     => (minCardinality <= sum AND sum <= maxCardinality)
NOT DECISION_NAME_TAKEN => (sum = 0)
```

The second constraint forces all literals to false when the decision is not taken.

---

## Actions

Every action receives the activation literal of its parent rule. An action only has an effect when its activation is true.

### Allows

No constraint is added. The solver already explores the full range of literal combinations within the cardinality bounds.

### DisAllows

Prevents a specific literal from being selected when the rule fires:

```
activation => NOT DECISION_NAME_DisallowedLiteral
```

Same for other types.

### Enforce (Boolean, Number, String)

Forces the value of the target decision to a specific value when the rule fires:

```
activation => (TARGET_DECISION_VALUE = enforcedValue)
```

The enforce action also registers its activation as an isTaken trigger for the target decision:

```
TARGET_DECISION_TAKEN triggers += activation
```

This ensures that the target decision is considered taken whenever it is enforced, even if its own visibility condition is false.

### Enforce (Enum)

Forces a specific literal of the target enumeration decision to true when the rule fires:

```
activation => (TARGET_DECISION_EnforcedLiteral = true)
```

The activation is also registered as an isTaken trigger for the target decision, as described above.

### JavaAction

Not yet implemented because it's almost impossible to turn Java code into logical statements.

---

## Expressions

### AND

```
(and leftExpression rightExpression)
```

Both operands must be boolean.

### OR

```
(or leftExpression rightExpression)
```

Both operands must be boolean.

### XOR

```
(xor leftExpression rightExpression)
```

Both operands must be boolean.

### NOT

```
(not operand)
```

The operand must be boolean.

### Equals

Equals has two cases depending on whether one of its operands is an enumeration literal:

```
DECISION_NAME_VALUE
```

When one side is an `EnumeratorLiteralExpression` and the other is a `DecisionValueCallExpression`, the encoding checks the boolean variable for that literal directly.

For other types (Boolean, Number, String), both sides are evaluated normally and compared:

```
(= leftExpression rightExpression)
```

### GreaterThan

```
(> leftExpression rightExpression)
```

Both operands must be arithmetic (Real).

### LessThan

```
(< leftExpression rightExpression)
```

Both operands must be arithmetic (Real).

### IsTaken

Returns the `_TAKEN` constant of the referenced decision directly:

```
DECISION_NAME_TAKEN
```

### DecisionValueCallExpression

Returns the `_VALUE` constant of the referenced decision:

```
DECISION_NAME_VALUE
```

### DecisionVisibilityCallExpression

Returns the result of encoding the visibility condition of the referenced decision.

If the visibility condition is a `LiteralExpression`, it is evaluated immediately at encoding time and replaced with a boolean constant.

Otherwise, the visibility condition expression is encoded recursively.

### JavaExpression

Not yet implemented. Always returns `true`.

---

## Naming Conventions

The suffixes appended to that base name are:

| Suffix            | Sort                 | Meaning                                      |
|-------------------|----------------------|----------------------------------------------|
| `_TAKEN`          | Bool                 | Whether the decision is active               |
| `_VALUE`          | Bool / Real / String | The current value                            |
| `_<LiteralValue>` | Bool                 | Whether this enumeration literal is selected |
