package sbls;

import org.chocosolver.solver.Solver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SBLSCPModel: solution exists for small n, Latin property, spatial balance.
 */
class SBLSCPModelTest {

    /** Check that grid is a Latin square: each row and column is a permutation of 1..n. */
    private static boolean isLatin(int[][] grid, int n) {
        for (int r = 0; r < n; r++) {
            boolean[] seen = new boolean[n + 1];
            for (int c = 0; c < n; c++) {
                int v = grid[r][c];
                if (v < 1 || v > n || seen[v]) return false;
                seen[v] = true;
            }
        }
        for (int c = 0; c < n; c++) {
            boolean[] seen = new boolean[n + 1];
            for (int r = 0; r < n; r++) {
                int v = grid[r][c];
                if (v < 1 || v > n || seen[v]) return false;
                seen[v] = true;
            }
        }
        return true;
    }

    /** Compute total distance for pair (color i, color j): sum over rows of |col_i - col_j|. */
    private static long totalDistance(int[][] grid, int n, int colorI, int colorJ) {
        long sum = 0;
        for (int r = 0; r < n; r++) {
            int colI = -1, colJ = -1;
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == colorI) colI = c;
                if (grid[r][c] == colorJ) colJ = c;
            }
            if (colI >= 0 && colJ >= 0) sum += Math.abs(colI - colJ);
        }
        return sum;
    }

    /** Check spatial balance: all pairs have the same total distance. */
    private static boolean isSpatiallyBalanced(int[][] grid, int n) {
        Long ref = null;
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                long d = totalDistance(grid, n, i, j);
                if (ref == null) ref = d;
                else if (ref != d) return false;
            }
        }
        return true;
    }

    @Test
    void n2_hasSolution_andIsLatin_andBalanced() {
        SBLSCPModel sbls = new SBLSCPModel(2, true, true);
        Solver solver = sbls.getSolver();
        assertTrue(solver.solve());
        int[][] grid = sbls.getSolutionGrid();
        assertTrue(isLatin(grid, 2));
        assertTrue(isSpatiallyBalanced(grid, 2));
    }

    @Test
    void n3_hasSolution_andIsLatin_andBalanced() {
        SBLSCPModel sbls = new SBLSCPModel(3, true, true);
        Solver solver = sbls.getSolver();
        assertTrue(solver.solve());
        int[][] grid = sbls.getSolutionGrid();
        assertTrue(isLatin(grid, 3));
        assertTrue(isSpatiallyBalanced(grid, 3));
    }

    @Test
    void n4_hasSolution_andIsLatin_andBalanced() {
        SBLSCPModel sbls = new SBLSCPModel(4, true, true);
        Solver solver = sbls.getSolver();
        assertTrue(solver.solve());
        int[][] grid = sbls.getSolutionGrid();
        assertTrue(isLatin(grid, 4));
        assertTrue(isSpatiallyBalanced(grid, 4));
    }

    @Test
    void symmetryBreaking_fixesFirstRow() {
        SBLSCPModel sbls = new SBLSCPModel(4, true, true);
        Solver solver = sbls.getSolver();
        assertTrue(solver.solve());
        int[][] grid = sbls.getSolutionGrid();
        for (int c = 0; c < 4; c++) {
            assertEquals(c + 1, grid[0][c]);
        }
    }
}
