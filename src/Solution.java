import java.util.*;

/**
 * A Novel O(n) Priority-Guided Algorithm for the Longest Palindromic Substring.
 *
 * This implementation:
 * - Transforms the string with '#' to unify even/odd palindromes
 * - Computes a best-case potential for each center (pyramid shape)
 * - Visits centers in a center-outward priority order (no sorting)
 * - Applies early termination when remaining centers cannot beat the current
 * best
 *
 * Author: Krushn Gor
 */
import java.util.*;

class Solution {
    public String longestPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return (s == null) ? "" : s;
        }

        // Step 1: Transform string with '#'
        // Example: "abba" -> "#a#b#b#a#"
        StringBuilder modified = new StringBuilder();
        modified.append('#');
        for (char c : s.toCharArray()) {
            modified.append(c).append('#');
        }
        String str = modified.toString();
        int n = str.length();  // transformed length

        // Step 2: bestCase[i] = max possible palindrome length at center i
        // Pattern: 1,3,5,...,N,...,5,3,1
        int[] bestCase = new int[n];
        for (int i = 0; i < n; i++) {
            int distToEdge = Math.min(i, n - 1 - i);
            bestCase[i] = 2 * distToEdge + 1;
        }

        // Step 3: track best palindrome in transformed string
        int bestTransLen = 1; // length in transformed string
        int bestCenter   = 0; // center index in transformed string

        // Step 4: generate checking order (start from center, go outward) - O(n)
        List<Integer> order = new ArrayList<>();
        int mid = n / 2;
        order.add(mid);
        for (int k = 1; mid - k >= 0 || mid + k < n; k++) {
            if (mid - k >= 0) order.add(mid - k);
            if (mid + k < n) order.add(mid + k);
        }

        // Step 5: expand around centers in priority order
        for (int idx : order) {
            // Early termination: if even the best-case from this center
            // cannot beat the current best, we stop.
            if (bestCase[idx] <= bestTransLen) break;

            int left = idx - 1;
            int right = idx + 1;

            while (left >= 0 && right < n && str.charAt(left) == str.charAt(right)) {
                left--;
                right++;
            }

            int len = right - left - 1; // palindrome length in transformed string

            if (len > bestTransLen) {
                bestTransLen = len;
                bestCenter = idx;
            }
        }

        // Step 6: map (bestCenter, bestTransLen) back to original string indices

        int radius = (bestTransLen - 1) / 2;  // radius in transformed string
        int center = bestCenter;

        int L = center - radius;
        int R = center + radius;

        // Find first and last odd indices in [L, R] (these correspond to letters)
        int firstOdd = (L % 2 == 1) ? L : L + 1;
        int lastOdd  = (R % 2 == 1) ? R : R - 1;

        int jStart = (firstOdd - 1) / 2;  // start index in original string
        int jEnd   = (lastOdd - 1) / 2;   // end index in original string

        // Safety clamp (should not be necessary if math is right, but prevents crashes)
        if (jStart < 0) jStart = 0;
        if (jEnd >= s.length()) jEnd = s.length() - 1;

        return s.substring(jStart, jEnd + 1);
    }

    // Optional: simple main to test quickly
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println("babad -> " + sol.longestPalindrome("babad")); // bab or aba
        System.out.println("cbbd  -> " + sol.longestPalindrome("cbbd"));  // bb
        System.out.println("a     -> " + sol.longestPalindrome("a"));     // a
        System.out.println("ac    -> " + sol.longestPalindrome("ac"));    // a or c
    }
}
