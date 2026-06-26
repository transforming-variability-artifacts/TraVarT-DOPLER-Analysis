# Analysis of Decision Models

## Run

To run the code, follow the instructions based on the environment:

* Maven: Libraries for SMT are downloaded automatically.
* IntelliJ: Execute `mvn generate-resources` once to download the required libraries for the current OS and
  architecture.

To analyze a model, run the JAR file with the following command:

`java -jar smt_dopler-2.0.0.jar -a <SMT|ILP|CP> <path to model>`.

For ILP and SMT the solvers can be changed with the `--smt-solver <CVC5|Z3>` and `--ilp-model <CBC|SCIP>` arguments.

### Troubleshooting & Configuration

* Dependencies: SMT and OR-Tools dependencies are extracted automatically. In case of issues with SMT solvers, manually
  place `libcvc5jni`, `libz3`, and `libz3java` (with `dll/so/dylib` extension) into a `./dependencies` folder located in
  the same directory as the JAR.

* Help: For a list of arguments and configuration options use:
  `java -jar smt_dopler-2.0.0.jar --help`

## Build

To create the final JAR, use the following command:

mvn clean package -Pdist

> Note: The -Pdist argument is required to download and bundle all SMT libraries for all supported operating systems (
> Windows, Linux, and macOS) and architectures (x64 and arm64).

## Parser

Information about the parser see [here](docs/Parser.md)

## DOPLER META-MODEL

In the following the metamodel of the DOPLER decision model is presented.

#### Validity Condition

Defines the set of allowed values (with respect to the decision type and additional user-defined constraints)
Post condition which has to be fulfilled after a user takes a decision and before assigning a value to the decision
variable

#### Visibility Condition

Defines when a decision, becomes relevant and can be answered during product derivation.
If there is a visibility condition associated with a decision, the user has to first take the decisions appearing in the
visibility condition
![Dopler Metamodel](/images/dOPLER.svg)

## SMT Encoding

Detailed Explanation of the SMT Encoding you can find [here](docs/SMTEncoding.md)

## ILP Encoding

Detailed Explanation of the ILP Encoding you can find [here](docs/ILPEncoding.md)

## CP Encoding

Detailed Explanation of the CP Encoding you can find [here](docs/CPEncoding.md)

## CI Pipeline

Information about the CI pipeline see [here](docs/CIPipeline.md)

## Contributors

Information about the contributors can be found [here](CONTRIBUTORS.md)