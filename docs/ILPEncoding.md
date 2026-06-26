# ILP Encoding Design

## OR-Tools Linear Solver

The ILP encoding uses Google OR-Tools `MPSolver`.
Logical expressions are modeled as linear constraints over binary (0/1) and continuous variables.
Because linear programming cannot natively express combined logical expressions, auxiliary variables (denoted as `z`)
are introduced to represent intermediate boolean results.

## Overview

The encoding consists of four steps:

1. One set of global variables per decision.
2. Rules and validity conditions into implications for every decision.
3. Visibility of decisions based on conditions and enforcements are modeled.
4. Min/max selection counts for enumeration decisions are enforced arithmetically.

---

## Step 1: Variable Declaration

Every decision gets a `_TAKEN` binary variable. Its value indicates whether the decision is active.

```
DECISION_NAME_TAKEN  ∈ {0, 1}
```

For enumeration decisions, each literal in the enumeration gets its own binary variable:

```
DECISION_NAME_LiteralA  ∈ {0, 1}
DECISION_NAME_LiteralB  ∈ {0, 1}
```

For boolean, number decisions, a single value variable is declared with the appropriate domain:

| Decision type | Domain   | Example variable name |
|---------------|----------|-----------------------|
| Boolean       | {0, 1}   | `DECISION_NAME_VALUE` |
| Number        | (-∞, +∞) | `DECISION_NAME_VALUE` |

String decisions are not supported in ILP.

A full example:

```
Pizza_TAKEN       ∈ {0, 1}
Pizza_Salami      ∈ {0, 1}
Pizza_Mozzarella  ∈ {0, 1}

Price_TAKEN  ∈ {0, 1}
Price_VALUE  ∈ (-∞, +∞)
```

---

## Step 2: Mapping

For each decision, three kinds of constraints are generated: rules, validity, and defaults.

### 2.1 Rules

A rule belongs to one decision and has a condition and a list of actions.
The rule fires only when the source decision is taken and the rule condition holds.
This combined boolean is called the activation:

```
activation := AND(DECISION_NAME_TAKEN, ruleCondition)
```

The activation is an auxiliary variable produced by the AND encoding (see Logical Operations below). It is passed to
each action in the rule to determine if they should apply.

### 2.2 Validity

If a decision defines validity conditions (e.g., a number range), those conditions must hold whenever the decision is
taken. All validity conditions are combined with AND:

```
DECISION_NAME_TAKEN => AND(validityCondition_1, validityCondition_2, ...)
```

This is encoded as an implication (see Logical Operations below).

### 2.3 Default Values

If a decision is not taken, its value is reset to the standard default defined in the model.

```
NOT(DECISION_NAME_TAKEN) => defaultEquality
```

**Boolean decision default:**

An auxiliary variable `eq` is created such that `eq = 1` iff `DECISION_NAME_VALUE = standardBooleanValue` (0 or 1).

```
NOT(DECISION_NAME_TAKEN) => eq
  where eq encodes (DECISION_NAME_VALUE = standardBooleanValue)
```

**Number decision default:**

An equality auxiliary variable `eq` is created using the Big-M numeric equality (see Numeric Equality below).

```
NOT(DECISION_NAME_TAKEN) => eq
  where eq encodes (DECISION_NAME_VALUE = standardRealValue)
```

**Enumeration decision default:**

For each literal that is a default, its variable must be 1. For each literal that is not a default, its negation must be

1. All of these are combined with AND into a single auxiliary variable.

```
NOT(DECISION_NAME_TAKEN) => AND(litDefaults...)
  where litDefaults[i] = LiteralVar_i      if literal i is a default
        litDefaults[i] = NOT(LiteralVar_i) otherwise
```

---

## Step 3: Visibility Consistency

After all rules have been processed, the `_TAKEN` flag of each decision is defined. A trigger set is assembled
containing the visibility expression and any activation variables registered by enforce actions targeting this decision.
The decision is taken if and only if any trigger in that set is active:

```
anyTrigger := OR(visibilityCondition, enforceTrigger_1, enforceTrigger_2, ...)

DECISION_NAME_TAKEN <= anyTrigger   (if no trigger is active, it cannot be taken)
DECISION_NAME_TAKEN >= anyTrigger   (if any trigger is active, it must be taken)
```

These two constraints force `DECISION_NAME_TAKEN = anyTrigger`. In linear form:

```
DECISION_NAME_TAKEN - anyTrigger <= 0
DECISION_NAME_TAKEN - anyTrigger >= 0
```

---

## Step 4: Enumeration Cardinality

Enumeration decisions allow a minimum and maximum number of literals to be selected at the same time. An auxiliary
integer variable `sumVar` is introduced, bounded to `[0, maxCardinality]`, and constrained to equal the sum of all
literal variables:

```
sumVar = LiteralA + LiteralB + ... + LiteralN
```

Two conditional bounds are then enforced:

```
DECISION_NAME_TAKEN => (minCardinality <= sumVar <= maxCardinality)
NOT(DECISION_NAME_TAKEN) => (sumVar = 0)
```

The second constraint forces all literals to 0 (false) when the decision is not taken, because `sumVar = 0` with all
positive integer summands implies each literal is 0:

```
sumVar - min * DECISION_NAME_TAKEN >= 0
sumVar - max * DECISION_NAME_TAKEN <= 0
```

---

## Actions

Every action receives the activation variable of its parent rule. An action only has an effect when its activation is 1.

### Allows

No constraint is added. The solver already explores the full range of literal combinations within the cardinality
bounds.

### DisAllows

Prevents a specific literal or value from being selected when the rule fires.

**Enumeration literal:**

```
activation => NOT(DECISION_NAME_DisallowedLiteral)
```

**Boolean or numeric value:**

An equality variable `eq` is computed for `DECISION_NAME_VALUE = disallowedValue` using boolean or numeric equality
encoding, then:

```
activation => NOT(eq)
```

The activation is also registered as an `isTaken` trigger for the target decision.

### Enforce (Boolean, Number)

Forces the value of the target decision to a specific value when the rule fires. An equality variable `eq` is computed,
then:

```
activation => eq
  where eq encodes (TARGET_DECISION_VALUE = enforcedValue)
```

The activation is registered as an `isTaken` trigger for the target decision so that it is considered taken even if its
own visibility condition is false.

### Enforce (Enum)

Forces a specific literal of the target enumeration decision to true when the rule fires:

```
activation => TARGET_DECISION_EnforcedLiteral
```

The activation is also registered as an `isTaken` trigger for the target decision.

### JavaAction

Not yet implemented.

---

## Expressions

Every expression returns an `MPVariable` rather than adding constraints directly and returning nothing. This
allows expressions to be composed freely, e.g. an `AND` can be fed into an `OR`, an implication.
The trade-off is that every intermediate result is represented as a variable in the solver, which increases the problem
size.

### AND

```
z = AND(left, right)
```

Encoded with auxiliary variable `z ∈ {0, 1}`:

```
z <= left
z <= right
z >= left + right - 1
```

For n operands:

```
z <= vi   for all i
z >= sum(vi) - (n - 1)
```

### OR

```
z = OR(left, right)
```

Encoded with auxiliary variable `z ∈ {0, 1}`:

```
z >= left
z >= right
z <= left + right
```

For n operands:

```
z >= vi   for all i
z <= sum(vi)
```

### XOR

```
z = XOR(x, y)
```

Encoded with auxiliary variable `z ∈ {0, 1}`:

```
z >= x - y
z >= y - x
z <= x + y
z <= 2 - x - y
```

If `x` and `y` are the same variable, `z` is fixed to 0.

### NOT

```
z = NOT(x)
```

Encoded with auxiliary variable `z ∈ {0, 1}`:

```
z + x = 1
```

### Implication

```
x => y
```

No auxiliary variable is needed:

```
y - x >= 0
```

### Equals (Boolean)

```
z = (x == y)
```

Functionally equivalent to `NOT(XOR(x, y))`, but uses fewer variables. Encoded with auxiliary variable `z ∈ {0, 1}`:

```
z <= 1 - x + y
z <= 1 + x - y
z >= x + y - 1
z >= 1 - x - y
```

If `x` and `y` are the same variable, `z` is fixed to 1.

### Equals (Numeric)

```
z = (x == y)   for continuous variables
```

Uses the Big-M method with local M if possible. Two auxiliary variables are introduced: `z ∈ {0, 1}` (equality
indicator) and `b ∈ {0, 1}`.
The `z = 0` case requires knowing the direction of the inequality, so `b` functions as a direction.

When `z = 1`, forces `x = y`:

```
x - y + M*z <= M
x - y - M*z >= -M
```

When `z = 0`, forces `x ≠ y` (either `x > y` or `x < y`):

```
x - y + M*z + M*b >= ε      (if b=1, direction is x < y; if b=0, direction is x > y)
x - y + M*z - M*b <= M - ε
```

Where `ε = EPSILON` is a small positive constant used to enforce strict inequality.

### Equals (Enum Literal)

When one side of an equals expression is an `EnumeratorLiteralExpression` and the other is a
`DecisionValueCallExpression`, no arithmetic is needed. The encoding returns the binary variable for that literal
directly:

```
DECISION_NAME_LiteralValue
```

### GreaterThan

```
z = (left > right)
```

Encoded with auxiliary variable `z ∈ {0, 1}` and Big-M:

```
left - right - M*z >= ε - M
left - right - M*z <= 0
```

### LessThan

```
z = (left < right)
```

Symmetric to GreaterThan, encoding `right > left`:

```
right - left - M*z >= ε - M
right - left - M*z <= 0
```

### IsTaken

Returns the `_TAKEN` variable of the referenced decision directly:

```
DECISION_NAME_TAKEN
```

### DecisionValueCallExpression

Returns the `_VALUE` variable of the referenced decision:

```
DECISION_NAME_VALUE
```

### DecisionVisibilityCallExpression

Returns the `_TAKEN` variable of the referenced decision:

```
DECISION_NAME_TAKEN
```

### BooleanLiteralExpression

Returns a constant auxiliary variable fixed to 1.0 or 0.0.

### DoubleLiteralExpression

Returns a constant auxiliary variable fixed to the literal's value.

### StringLiteralExpression

Not supported.

### JavaExpression

Not yet implemented. Always returns a constant fixed to 1.0.

---

## Big-M

Big-M is a large constant used to conditionally activate or deactivate constraints. Big-M is computed from the actual
bounds of the variables involved in each constraint. If any bound is
infinite (e.g., a number with no range), the fallback constant `FALLBACK_BIG_M` is used. This keeps
Big-M as tight as possible for bounded variables while remaining hopefully correct for unbounded ones.

```
M = max(|lb_i|, |ub_i|) * 2.0 + 1.0    for all variables i
```

---

## Naming Conventions

The suffixes appended to the base decision name are:

| Suffix            | Domain     | Meaning                                      |
|-------------------|------------|----------------------------------------------|
| `_TAKEN`          | {0, 1}     | Whether the decision is active               |
| `_VALUE`          | {0,1} or ℝ | The current value                            |
| `_<LiteralValue>` | {0, 1}     | Whether this enumeration literal is selected |

Auxiliary variables introduced use descriptive prefixes such as `and_n`, `or_n`, `not`, `eq_bool`, and `_sum`.
