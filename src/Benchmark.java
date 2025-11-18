import java.util.Random;

/**
 * Benchmark driver for the priority-based O(n) LPS algorithm.
 *
 * This program:
 * - Generates random test strings
 * - Measures runtime of the algorithm
 * - Prints summary information
 *
 * Author: Krushn Gor
 */
public class Benchmark {

    public static void main(String[] args) {

        Solution algo = new Solution();

        // Test cases of increasing size
        String[] tests = {
                "babad",
                "cbbd",
                "racecar",
                generateRandomString(10_000),
                generateRandomString(50_000),
                generateRandomString(100_000),
                generateRandomString(200_000)
        };

        for (String t : tests) {
            long start = System.currentTimeMillis();
            String ans = algo.longestPalindrome(t);
            long end = System.currentTimeMillis();

            System.out.println("======================================");
            System.out.println("Input Length: " + t.length());
            System.out.println("Result Length: " + ans.length());
            System.out.println("Time: " + (end - start) + " ms");

            if (ans.length() > 60)
                System.out.println("Palindrome (truncated): " + ans.substring(0, 57) + "...");
            else
                System.out.println("Palindrome: " + ans);

            System.out.println("======================================\n");
        }
    }

    /**
     * Generates a random lowercase string of length n.
     */
    private static String generateRandomString(int n) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }
        return sb.toString();
    }
}
