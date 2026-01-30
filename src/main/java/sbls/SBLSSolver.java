package sbls;

import org.chocosolver.solver.Solver;

/**
 * Main entry: solve SBLS for a given order n.
 * Usage: pass n as first program argument (e.g. 4).
 */
public class SBLSSolver {

    public static void main(String[] args) {
        int n = 4;
        if (args.length >= 1) {
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Usage: SBLSSolver <n>");
                System.exit(1);
            }
        }
        if (n < 2) {
            System.err.println("n must be >= 2");
            System.exit(1);
        }

        SBLSCPModel sbls = new SBLSCPModel(n, true, true); // improved: symmetry breaking, AC-style
        Solver solver = sbls.getSolver();

        if (solver.solve()) {
            int[][] grid = sbls.getSolutionGrid();
            System.out.println("SBLS solution for n=" + n);
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    System.out.print(grid[r][c] + (c < n - 1 ? " " : ""));
                }
                System.out.println();
            }
            System.out.println("Nodes: " + solver.getNodeCount());
            System.out.println("Time (ms): " + solver.getTimeCount());
        } else {
            System.out.println("No solution found for n=" + n);
            System.out.println("Nodes: " + solver.getNodeCount());
            System.out.println("Time (ms): " + solver.getTimeCount());
        }
    }
}
