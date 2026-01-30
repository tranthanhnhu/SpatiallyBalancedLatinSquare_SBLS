package sbls;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ExperimentRunner: does not crash, output contains time and nodes.
 */
class ExperimentRunnerTest {

    @Test
    void runSmallExperiment_doesNotThrow() {
        assertDoesNotThrow(() -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream prev = System.out;
            System.setOut(new PrintStream(out));
            try {
                ExperimentRunner.main(new String[]{"3"});
            } finally {
                System.setOut(prev);
            }
            String s = out.toString();
            assertTrue(s.contains("n = 2") || s.contains("n=2"));
            assertTrue(s.contains("time") || s.contains("nodes") || s.contains("Nodes"));
        });
    }

    @Test
    void runWithMaxN2_printsResults() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream prev = System.out;
        System.setOut(new PrintStream(out));
        try {
            ExperimentRunner.main(new String[]{"2"});
        } finally {
            System.setOut(prev);
        }
        String s = out.toString();
        assertTrue(s.contains("2") && (s.contains("Simple") || s.contains("Improved")));
    }
}
