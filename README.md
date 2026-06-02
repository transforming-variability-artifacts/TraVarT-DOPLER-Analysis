# Analysis of Decision Models

## Run

Put the following dll or so files into the dependencies folder: `libcvc5jni`, `libz3`, `libz3java`.
This works both for development and with the final jar, which also requires the dependencies folder next to it.

This tool invokes the SMT solver for a given dopler model file:

- As first argument: `java -jar smt_dopler-2.0.0.jar <path to model>`

Help:

- `java -jar smt_dopler-2.0.0.jar --help`

## Parser

Information about the parser see [here](docs/Parser.md)

## DOPLER META-MODEL

In the following the metamodel of the DOPLER decision model is presented.

#### Validity Condition
Defines the set of allowed values (with respect to the decision type and additional user-defined constraints)
Post condition which has to be fulfilled after a user takes a decision and before assigning a value to the decision variable

#### Visibility Condition
Defines when a decision, becomes relevant and can be answered during product derivation.
If there is a visibility condition associated with a decision, the user has to first take the decisions appearing in the visibility condition
![Dopler Metamodel](/images/dOPLER.svg)


## SMT Encoding

Detailed Explanation of the SMT Encoding you can find [here](docs/SMTEncoding.md)

## ILP Encoding

Detailed Explanation of the ILP Encoding you can find [here](docs/ILPEncoding.md)


## CP Encoding

Detailed Explanation of the CP Encoding you can find [here](docs/CPEncoding.md)

## CI Pipeline

Information about the CI pipeline see [here](docs/CIPipeline.md)
