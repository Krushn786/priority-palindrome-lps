/**
 * Manacher's algorithm for Longest Palindromic Substring.
 * Bare version: no logging, just comparison counting.
 */
public class Mancher {

    // Count of character comparisons in the expansion step
    private long comparisonCount = 0L;

    public long getComparisonCount() {
        return comparisonCount;
    }

    // Transformed length helper (same #a#b#c# style as Solution)
    public static int getTransformedLength(int originalLength) {
        return 2 * originalLength + 1;
    }

    public String longestPalindrome(String s) {
        // reset counter
        comparisonCount = 0L;

        if (s == null || s.length() == 0) {
            return "";
        }
        if (s.length() == 1) {
            return s;
        }

        // Transform s -> t with '#' between chars
        StringBuilder sb = new StringBuilder();
        sb.append('#');
        for (char c : s.toCharArray()) {
            sb.append(c).append('#');
        }
        String t = sb.toString();
        int n = t.length();

        int[] p = new int[n]; // radius at each center in transformed string
        int center = 0;
        int right = 0;

        int bestCenter = 0;
        int bestRadius = 0;

        for (int i = 0; i < n; i++) {
            int mirror = 2 * center - i;

            if (i < right) {
                p[i] = Math.min(p[mirror], right - i);
            } else {
                p[i] = 0;
            }

            // Attempt to expand around i
            int a = i + p[i] + 1;
            int b = i - p[i] - 1;

            while (b >= 0 && a < n) {
                comparisonCount++; // we are about to compare t[a] and t[b]
                if (t.charAt(a) == t.charAt(b)) {
                    p[i]++;
                    a++;
                    b--;
                } else {
                    break;
                }
            }

            // Update rightmost palindrome
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }

            // Track best
            if (p[i] > bestRadius) {
                bestRadius = p[i];
                bestCenter = i;
            }
        }

        // Map (bestCenter, bestRadius) back to original indices
        int L = bestCenter - bestRadius;
        int R = bestCenter + bestRadius;

        // First and last odd indices in [L, R] correspond to real characters
        int firstOdd = (L % 2 == 1) ? L : L + 1;
        int lastOdd = (R % 2 == 1) ? R : R - 1;

        int start = (firstOdd - 1) / 2; // index in original string
        int end = (lastOdd - 1) / 2; // index in original string

        if (start < 0)
            start = 0;
        if (end >= s.length())
            end = s.length() - 1;

        return s.substring(start, end + 1);
    }
}
