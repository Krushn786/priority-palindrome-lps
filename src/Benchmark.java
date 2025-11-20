import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {

    public static void main(String[] args) throws IOException {

        File outFolder = new File("output");
        if (!outFolder.exists()) {
            outFolder.mkdir();
        }

        Solution noviceAlgo = new Solution();
        Mancher mancherAlgo = new Mancher();

        String[] tests = {
                "",
                " ",
                "aa",
                "aba",
                "babad",
                "cbbd",
                "racecar",
                generateRandomString(10_000),
                generateRandomString(50_000),
                generateRandomString(100_000),
                generateRandomString(200_000),
                generateRandomString(1_000_000),
                generateRandomString(10_000_000)
        };

        try (PrintWriter csv = new PrintWriter(new FileWriter("output/benchmark_results.csv"));
                PrintWriter noviceTxt = new PrintWriter(new FileWriter("output/Novice.txt"));
                PrintWriter mancherTxt = new PrintWriter(new FileWriter("output/Mancher.txt"))) {

            // Enhanced CSV header
            csv.println(
                    "algorithm,original_length,transformed_length,total_comparisons,outer_loop_count,position_checks,early_terminations,palindrome_length,time_ms");

            // NOVICE
            System.out.println("\n========== RUNNING NOVICE ALGORITHM ==========");
            for (String t : tests) {
                int n = t.length();

                System.out.println("\nProcessing input of length: " + n);

                long start = System.nanoTime();
                String ans = noviceAlgo.longestPalindrome(t);
                long end = System.nanoTime();

                long timeMs = (end - start) / 1_000_000L;
                long comps = noviceAlgo.getComparisonCount();
                long outerLoop = noviceAlgo.getOuterLoopCount();
                long posChecks = noviceAlgo.getPositionChecks();
                long earlyTerm = noviceAlgo.getEarlyTerminations();
                int transformedLength = Solution.getTransformedLength(n);

                System.out.println("Comparisons: " + comps);
                System.out.println("Outer loops: " + outerLoop);
                System.out.println("Position checks: " + posChecks);
                System.out.println("Early terminations: " + earlyTerm);
                System.out.println("Time: " + timeMs + " ms");
                System.out.println("Palindrome length: " + ans.length());

                csv.printf("\"Novice\",%d,%d,%d,%d,%d,%d,%d,%d%n",
                        n, transformedLength, comps, outerLoop, posChecks, earlyTerm, ans.length(), timeMs);

                noviceTxt.println("======================================");
                noviceTxt.println("Input Length: " + n);
                noviceTxt.println("Result Length: " + ans.length());
                noviceTxt.println("Time: " + timeMs + " ms");
                noviceTxt.println("Comparisons: " + comps);
                noviceTxt.println("Outer Loop Count: " + outerLoop);
                noviceTxt.println("Position Checks: " + posChecks);
                noviceTxt.println("Early Terminations: " + earlyTerm);
                if (n <= 200) {
                    noviceTxt.println("Input String: " + t);
                }
                if (ans.length() > 200) {
                    noviceTxt.println("Palindrome (truncated): " + ans.substring(0, 197) + "...");
                } else {
                    noviceTxt.println("Palindrome: " + ans);
                }
                noviceTxt.println("======================================");
                noviceTxt.println();
            }

            // MANACHER
            System.out.println("\n\n========== RUNNING MANACHER ALGORITHM ==========");
            csv.println(
                    "algorithm,original_length,transformed_length,total_comparisons,outer_loop_count,mirror_copies,expansion_attempts,palindrome_length,time_ms");

            for (String t : tests) {
                int n = t.length();

                System.out.println("\nProcessing input of length: " + n);

                long start = System.nanoTime();
                String ans = mancherAlgo.longestPalindrome(t);
                long end = System.nanoTime();

                long timeMs = (end - start) / 1_000_000L;
                long comps = mancherAlgo.getComparisonCount();
                long outerLoop = mancherAlgo.getOuterLoopCount();
                long mirrors = mancherAlgo.getMirrorCopies();
                long expansions = mancherAlgo.getExpansionAttempts();
                int transformedLength = Mancher.getTransformedLength(n);

                System.out.println("Comparisons: " + comps);
                System.out.println("Outer loops: " + outerLoop);
                System.out.println("Mirror copies: " + mirrors);
                System.out.println("Expansion attempts: " + expansions);
                System.out.println("Time: " + timeMs + " ms");
                System.out.println("Palindrome length: " + ans.length());

                csv.printf("\"Manacher\",%d,%d,%d,%d,%d,%d,%d,%d%n",
                        n, transformedLength, comps, outerLoop, mirrors, expansions, ans.length(), timeMs);

                mancherTxt.println("======================================");
                mancherTxt.println("Input Length: " + n);
                mancherTxt.println("Result Length: " + ans.length());
                mancherTxt.println("Time: " + timeMs + " ms");
                mancherTxt.println("Comparisons: " + comps);
                mancherTxt.println("Outer Loop Count: " + outerLoop);
                mancherTxt.println("Mirror Copies: " + mirrors);
                mancherTxt.println("Expansion Attempts: " + expansions);
                if (n <= 200) {
                    mancherTxt.println("Input String: " + t);
                }
                if (ans.length() > 200) {
                    mancherTxt.println("Palindrome (truncated): " + ans.substring(0, 197) + "...");
                } else {
                    mancherTxt.println("Palindrome: " + ans);
                }
                mancherTxt.println("======================================");
                mancherTxt.println();
            }
        }

        System.out.println("\n\n========================================");
        System.out.println("Benchmark complete!");
        System.out.println("========================================");
        System.out.println("Generated files:");
        System.out.println("  output/benchmark_results.csv");
        System.out.println("  output/Novice.txt");
        System.out.println("  output/Mancher.txt");
    }

    private static String generateRandomString(int n) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }
        return sb.toString();
    }
}