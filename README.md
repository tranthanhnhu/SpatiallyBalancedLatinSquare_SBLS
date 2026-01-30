# SBLS - Spatially Balanced Latin Square (Choco CP)

CP model in Choco for the Spatially Balanced Latin Square problem.

## Requirements

- Java 11 or later
- Maven 3.x

## Build

```bash
mvn clean install
```

## Run solver for one order n

```bash
mvn exec:java -Dexec.mainClass="sbls.SBLSSolver" -Dexec.args="4"
```

(Replace `4` with desired n.)

## Run experiments (compare Simple vs Improved)

```bash
mvn exec:java -Dexec.mainClass="sbls.ExperimentRunner" -Dexec.args="6"
```

(Optional argument: max n to try, default 8. Output: time and nodes for each n.)

## Run tests

```bash
mvn test
```

## Project layout

- `src/main/java/sbls/` – SBLSSolver, SBLSCPModel, ExperimentRunner
- `src/test/java/sbls/` – SBLSCPModelTest, ExperimentRunnerTest
- `report.txt` – Detailed report (constraints, filtering, strategies, results)
