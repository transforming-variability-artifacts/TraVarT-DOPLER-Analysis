## CI Pipeline

This project uses a GitHub Actions CI pipeline to automatically build the project and run all tests. The pipeline does this by:

- Downloading the required CSV test data from an external repository.  
  (see: https://github.com/transforming-variability-artifacts/DOPLER-models)
- Building the project and running unit tests using the **Maven Wrapper**.
- Publishing test reports as GitHub checks in a pull requests.

The **Maven Wrapper** is used to ensure a consistent Maven version across all environments, making builds reproducible and setup easier.