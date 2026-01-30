package sbls;

import org.chocosolver.solver.Solver;

/**
 * Run experiments: for each n, solve with "simple" (baseline) and "improved" method,
 * record time and node count. Output suitable for report.
 */
public class ExperimentRunner {

    /** Timeout in seconds per run. */
    public static final int TIMEOUT_SEC = 600;

    public static void main(String[] args) {
        int nMax = 8;
        if (args.length >= 1) {
            try {
                nMax = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }

        System.out.println("SBLS Experiment: Simple (baseline) vs Improved (symmetry breaking + strategy)");
        System.out.println("Timeout per run: " + TIMEOUT_SEC + " s");
        System.out.println();

        for (int n = 2; n <= nMax; n++) {
            runOne(n);
        }
    }

    private static void runOne(int n) {
        System.out.println("--- n = " + n + " ---");

        // Simple (baseline): no symmetry breaking, default allDifferent
        SBLSCPModel simple = new SBLSCPModel(n, false, false);
        Solver sSimple = simple.getSolver();
        sSimple.limitTime(TIMEOUT_SEC + "s");
        long t0 = System.currentTimeMillis();
        boolean foundSimple = sSimple.solve();
        long timeSimpleMs = System.currentTimeMillis() - t0;
        long nodesSimple = sSimple.getNodeCount();

        System.out.println("Simple (baseline):  solution=" + foundSimple
                + "  time_ms=" + timeSimpleMs
                + "  nodes=" + nodesSimple);

        // Improved: symmetry breaking, same search strategy
        SBLSCPModel improved = new SBLSCPModel(n, true, true);
        Solver sImproved = improved.getSolver();
        sImproved.limitTime(TIMEOUT_SEC + "s");
        t0 = System.currentTimeMillis();
        boolean foundImproved = sImproved.solve();
        long timeImprovedMs = System.currentTimeMillis() - t0;
        long nodesImproved = sImproved.getNodeCount();

        System.out.println("Improved:            solution=" + foundImproved
                + "  time_ms=" + timeImprovedMs
                + "  nodes=" + nodesImproved);
        System.out.println();
    }
}
