# SBLS - Spatially Balanced Latin Square (Choco CP)

CP model in Choco for the Spatially Balanced Latin Square problem.

## Project overview

This project models and solves the **Spatially Balanced Latin Square (SBLS)** problem using **Constraint Programming** with **Choco Solver**.

- **`SBLSCPModel`**: builds the CP model (Latin constraints + spatial balance + optional symmetry breaking).
- **`SBLSSolver`**: solves a single order `n` and prints the square plus **Nodes** and **Time (ms)**.
- **`ExperimentRunner`**: compares **Simple (baseline)** vs **Improved** configurations for multiple `n` values.

Output metrics:
- **Nodes**: number of search nodes visited (lower is generally better).
- **Time (ms)**: solver runtime.

## Requirements

- Java 11 or later
- Maven 3.x

## How to run (Windows PowerShell)

Open PowerShell in the **project root folder** 

### 1) Compile

```powershell
mvn clean compile
```

### 2) Run solver for one n

```powershell
mvn '-Dexec.mainClass=sbls.SBLSSolver' '-Dexec.args=3' exec:java
# or:
mvn '-Dexec.mainClass=sbls.SBLSSolver' '-Dexec.args=5' exec:java
```


### 3) Run experiments (Simple vs Improved)

```powershell
mvn '-Dexec.mainClass=sbls.ExperimentRunner' '-Dexec.args=6' exec:java
```

(Optional argument: max n to try, default 8. Output: time and nodes for each n.)


## Project layout

- `src/main/java/sbls/` – SBLSSolver, SBLSCPModel, ExperimentRunner
- `REPORT.md` – Report (Markdown, exam-style, results-focused)
- `evidence/` – Terminal screenshots (proof)
