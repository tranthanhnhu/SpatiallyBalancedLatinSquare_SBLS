package sbls;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/**
 * CP model for Spatially Balanced Latin Square (SBLS).
 * Variables: grid[r][c] (color 1..n), pos[r][s] (column of color s in row r).
 * Constraints: (1) Latin rows (2) Latin cols (3) Channeling (4) D_ij definition (5) Spatial balance (6) Symmetry breaking.
 */
public class SBLSCPModel {

    private final int n;
    private final Model model;
    private final IntVar[][] grid;
    private final IntVar[][] pos;
    private final IntVar D;
    private final boolean useSymmetryBreaking;
    private final boolean useAC; // true = AC allDifferent (improved), false = NEQS-style (simple/baseline)

    public SBLSCPModel(int n, boolean useSymmetryBreaking, boolean useAC) {
        this.n = n;
        this.useSymmetryBreaking = useSymmetryBreaking;
        this.useAC = useAC;
        this.model = new Model("SBLS-" + n);

        // Variables: grid[r][c] in [1, n]
        grid = model.intVarMatrix("g", n, n, 1, n);

        // pos[r][k]: column (0..n-1) where color (k+1) appears in row r
        pos = model.intVarMatrix("p", n, n, 0, n - 1);

        // D: common total distance for all pairs
        int maxDist = n * n * (n - 1); // upper bound: n rows * max |col diff| per row
        D = model.intVar("D", 0, maxDist);

        buildConstraints();
        setSearchStrategy();
    }

    private void buildConstraints() {
        // (1) Latin: allDifferent per row
        for (int r = 0; r < n; r++) {
            model.allDifferent(grid[r]).post();
        }

        // (2) Latin: allDifferent per column
        for (int c = 0; c < n; c++) {
            IntVar[] col = new IntVar[n];
            for (int r = 0; r < n; r++) col[r] = grid[r][c];
            model.allDifferent(col).post();
        }

        // (3) Channeling: grid[r][pos[r][k]] = k+1  (color k+1 at column pos[r][k] in row r)
        for (int r = 0; r < n; r++) {
            // pos[r] must be a permutation of [0, n-1]
            model.allDifferent(pos[r]).post();
            for (int k = 0; k < n; k++) {
                int color = k + 1;
                // element constraint: grid[r][pos[r][k]] = color
                // Using element(value, array, index, offset) where array[index+offset] = value
                // Try using table constraint or direct channeling
                for (int c = 0; c < n; c++) {
                    // If pos[r][k] = c, then grid[r][c] = color
                    BoolVar isEqual = model.arithm(pos[r][k], "=", c).reify();
                    model.ifThen(isEqual, model.arithm(grid[r][c], "=", color));
                }
            }
        }

        // (4) & (5) D_ij = sum_r |pos[r][i] - pos[r][j]|, and D_ij = D for all pairs (i,j) with i < j
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                IntVar[] rowDiffs = new IntVar[n];
                for (int r = 0; r < n; r++) {
                    IntVar diff = model.intVar("d_r" + r + "_" + i + "_" + j, -n, n);
                    model.arithm(diff, "=", pos[r][i], "-", pos[r][j]).post();
                    IntVar absDiff = diff.abs().intVar();
                    rowDiffs[r] = absDiff;
                }
                IntVar D_ij = model.intVar("D_" + i + "_" + j, 0, n * n * (n - 1));
                // sum(rowDiffs) = D_ij
                model.sum(rowDiffs, "=", D_ij).post();
                model.arithm(D_ij, "=", D).post();
            }
        }

        // (6) Symmetry breaking: fix first row so grid[0][c] = c+1
        if (useSymmetryBreaking) {
            for (int c = 0; c < n; c++) {
                model.arithm(grid[0][c], "=", c + 1).post();
            }
        }
    }

    private void setSearchStrategy() {
        Solver solver = model.getSolver();
        // Flatten grid for search (branch on grid variables first)
        IntVar[] flat = new IntVar[n * n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                flat[r * n + c] = grid[r][c];
            }
        }
        // First fail: variable with smallest domain; value = min (INT_MIN)
        solver.setSearch(Search.intVarSearch(
                (vars) -> {
                    IntVar best = null;
                    int bestSize = Integer.MAX_VALUE;
                    for (IntVar v : vars) {
                        if (!v.isInstantiated() && v.getDomainSize() < bestSize) {
                            bestSize = v.getDomainSize();
                            best = v;
                        }
                    }
                    return best;
                },
                var -> var.getLB(),
                flat
        ));
    }

    public Model getModel() {
        return model;
    }

    public Solver getSolver() {
        return model.getSolver();
    }

    public IntVar[][] getGrid() {
        return grid;
    }

    public int getN() {
        return n;
    }

    /** Get solution as 2D int matrix (1-based colors). */
    public int[][] getSolutionGrid() {
        int[][] sol = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                sol[r][c] = grid[r][c].getValue();
            }
        }
        return sol;
    }
}
