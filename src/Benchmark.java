import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {

    public static void main(String[] args) throws IOException {

        // Ensure output folder exists
        File outFolder = new File("output");
        if (!outFolder.exists()) {
            outFolder.mkdir();
        }

        // Your two algorithms
        Solution noviceAlgo = new Solution();
        Mancher mancherAlgo = new Mancher();

        // Test cases
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
                generateRandomString(10_000_000),
                generateRandomString(50_000_000)
        };

        // Output files inside /output folder
        try (PrintWriter csv = new PrintWriter(new FileWriter("output/benchmark_results.csv"));
                PrintWriter noviceTxt = new PrintWriter(new FileWriter("output/Novice.txt"));
                PrintWriter mancherTxt = new PrintWriter(new FileWriter("output/Mancher.txt"))) {

            // CSV header
            csv.println(
                    "algorithm,original_length,transformed_length,total_comparisons,palindrome_length,time_ms,input_string,palindrome_found");

            // ----------------------------------------------------
            // NOVICE
            // ----------------------------------------------------
            System.out.println("\n========== RUNNING NOVICE ALGORITHM ==========");
            for (String t : tests) {
                int n = t.length();

                System.out.println("\nProcessing input of length: " + n);
                if (n <= 100) {
                    System.out.println("Input string: \"" + t + "\"");
                }

                long start = System.nanoTime();
                String ans = noviceAlgo.longestPalindrome(t);
                long end = System.nanoTime();

                long timeMs = (end - start) / 1_000_000L;
                long comps = noviceAlgo.getComparisonCount();
                int transformedLength = Solution.getTransformedLength(n);

                System.out.println("Found palindrome of length: " + ans.length());
                if (ans.length() <= 100) {
                    System.out.println("Palindrome: \"" + ans + "\"");
                } else {
                    System.out.println("Palindrome (truncated): \"" + ans.substring(0, 97) + "...\"");
                }
                System.out.println("Time: " + timeMs + " ms, Comparisons: " + comps);

                // Escape strings for CSV
                String safeInput = escapeCSV(n <= 1000 ? t : t.substring(0, 1000) + "...");
                String safePalindrome = escapeCSV(ans.length() <= 1000 ? ans : ans.substring(0, 1000) + "...");

                csv.printf("\"Novice\",%d,%d,%d,%d,%d,%s,%s%n",
                        n, transformedLength, comps, ans.length(), timeMs, safeInput, safePalindrome);

                noviceTxt.println("======================================");
                noviceTxt.println("Input Length: " + n);
                if (n <= 200) {
                    noviceTxt.println("Input String: " + t);
                } else {
                    noviceTxt.println("Input String (truncated): " + t.substring(0, 197) + "...");
                }
                noviceTxt.println("Result Length: " + ans.length());
                noviceTxt.println("Time: " + timeMs + " ms");
                noviceTxt.println("Comparisons: " + comps);
                if (ans.length() > 200) {
                    noviceTxt.println("Palindrome (truncated): " + ans.substring(0, 197) + "...");
                } else {
                    noviceTxt.println("Palindrome: " + ans);
                }
                noviceTxt.println("======================================");
                noviceTxt.println();
            }

            // ----------------------------------------------------
            // MANACHER
            // ----------------------------------------------------
            System.out.println("\n\n========== RUNNING MANACHER ALGORITHM ==========");
            for (String t : tests) {
                int n = t.length();

                System.out.println("\nProcessing input of length: " + n);
                if (n <= 100) {
                    System.out.println("Input string: \"" + t + "\"");
                }

                long start = System.nanoTime();
                String ans = mancherAlgo.longestPalindrome(t);
                long end = System.nanoTime();

                long timeMs = (end - start) / 1_000_000L;
                long comps = mancherAlgo.getComparisonCount();
                int transformedLength = Mancher.getTransformedLength(n);

                System.out.println("Found palindrome of length: " + ans.length());
                if (ans.length() <= 100) {
                    System.out.println("Palindrome: \"" + ans + "\"");
                } else {
                    System.out.println("Palindrome (truncated): \"" + ans.substring(0, 97) + "...\"");
                }
                System.out.println("Time: " + timeMs + " ms, Comparisons: " + comps);

                // Escape strings for CSV
                String safeInput = escapeCSV(n <= 1000 ? t : t.substring(0, 1000) + "...");
                String safePalindrome = escapeCSV(ans.length() <= 1000 ? ans : ans.substring(0, 1000) + "...");

                csv.printf("\"Manacher\",%d,%d,%d,%d,%d,%s,%s%n",
                        n, transformedLength, comps, ans.length(), timeMs, safeInput, safePalindrome);

                mancherTxt.println("======================================");
                mancherTxt.println("Input Length: " + n);
                if (n <= 200) {
                    mancherTxt.println("Input String: " + t);
                } else {
                    mancherTxt.println("Input String (truncated): " + t.substring(0, 197) + "...");
                }
                mancherTxt.println("Result Length: " + ans.length());
                mancherTxt.println("Time: " + timeMs + " ms");
                mancherTxt.println("Comparisons: " + comps);

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

    /**
     * Escape a string for CSV output (wrap in quotes, escape internal quotes)
     */
    private static String escapeCSV(String s) {
        if (s == null)
            return "\"\"";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}