/**
 * Manacher's algorithm with detailed tracking
 */
public class Mancher {

    private long comparisonCount = 0L;
    private long outerLoopCount = 0L;
    private long mirrorCopies = 0L;
    private long expansionAttempts = 0L;

    public long getComparisonCount() {
        return comparisonCount;
    }

    public long getOuterLoopCount() {
        return outerLoopCount;
    }

    public long getMirrorCopies() {
        return mirrorCopies;
    }

    public long getExpansionAttempts() {
        return expansionAttempts;
    }

    public static int getTransformedLength(int originalLength) {
        return 2 * originalLength + 1;
    }

    public String longestPalindrome(String s) {
        // Reset counters
        comparisonCount = 0L;
        outerLoopCount = 0L;
        mirrorCopies = 0L;
        expansionAttempts = 0L;

        if (s == null || s.length() == 0) {
            return "";
        }
        if (s.length() == 1) {
            return s;
        }

        // Transform with '#'
        StringBuilder sb = new StringBuilder();
        sb.append('#');
        for (char c : s.toCharArray()) {
            sb.append(c).append('#');
        }
        String t = sb.toString();
        int n = t.length();

        int[] p = new int[n];
        int center = 0;
        int right = 0;

        int bestCenter = 0;
        int bestRadius = 0;

        for (int i = 0; i < n; i++) {
            outerLoopCount++;

            int mirror = 2 * center - i;

            if (i < right) {
                p[i] = Math.min(p[mirror], right - i);
                mirrorCopies++;
            } else {
                p[i] = 0;
            }

            // Attempt expansion
            int a = i + p[i] + 1;
            int b = i - p[i] - 1;

            while (b >= 0 && a < n) {
                expansionAttempts++;
                comparisonCount++;
                if (t.charAt(a) == t.charAt(b)) {
                    p[i]++;
                    a++;
                    b--;
                } else {
                    break;
                }
            }

            // Update rightmost
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

        // Rebuild result
        int L = bestCenter - bestRadius;
        int R = bestCenter + bestRadius;

        int firstOdd = (L % 2 == 1) ? L : L + 1;
        int lastOdd = (R % 2 == 1) ? R : R - 1;

        int start = (firstOdd - 1) / 2;
        int end = (lastOdd - 1) / 2;

        if (start < 0)
            start = 0;
        if (end >= s.length())
            end = s.length() - 1;

        return s.substring(start, end + 1);
    }
}