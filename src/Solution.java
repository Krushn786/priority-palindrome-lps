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
public class Solution {

    /**
     * Returns the longest palindromic substring of s using a priority-based O(n)
     * algorithm.
     */
    public String longestPalindrome(String s) {
        // Handle trivial cases
        if (s == null || s.length() <= 1) {
            return s == null ? "" : s;
        }

        // Step 1: Transform string with '#' to handle even/odd palindromes uniformly.
        // Example: "abba" -> "#a#b#b#a#"
        StringBuilder modified = new StringBuilder("#");
        for (char c : s.toCharArray()) {
            modified.append(c).append('#');
        }
        String str = modified.toString();
        int n = str.length();

        // Step 2: Compute best-case potential for each center.
        // bestCase[i] = maximum possible palindrome length centered at i.
        // This forms a pyramid: 1,3,5,...,max,...,5,3,1
        int[] bestCase = new int[n];
        for (int i = 0; i < n; i++) {
            int distToEdge = Math.min(i, n - 1 - i);
            bestCase[i] = 2 * distToEdge + 1;
        }

        // Step 3: Track best palindrome in transformed coordinates.
        int bestTransLen = 1; // best palindrome length in transformed string
        int bestCenter = 0; // center index in transformed string

        // Step 4: Generate priority order: mid, mid-1, mid+1, mid-2, mid+2, ...
        // This effectively visits centers in descending bestCase potential.
        List<Integer> order = new ArrayList<>();
        int mid = n / 2;
        order.add(mid);
        for (int k = 1; mid - k >= 0 || mid + k < n; k++) {
            if (mid - k >= 0)
                order.add(mid - k);
            if (mid + k < n)
                order.add(mid + k);
        }

        // Step 5: Expand around centers in priority order with early termination.
        for (int idx : order) {
            // Early termination:
            // If even the best-case palindrome at this center cannot beat current best,
            // stop.
            if (bestCase[idx] <= bestTransLen)
                break;

            int left = idx - 1;
            int right = idx + 1;

            // Standard expand-around-center.
            while (left >= 0 && right < n && str.charAt(left) == str.charAt(right)) {
                left--;
                right++;
            }

            int len = right - left - 1; // palindrome length in transformed string.

            if (len > bestTransLen) {
                bestTransLen = len;
                bestCenter = idx;
            }
        }

        // Step 6: Map back to original string indices.
        //
        // Relationship:
        // - Original palindrome length L -> transformed length T = 2L - 1
        // - So L = (T + 1) / 2
        // - Transformed radius R = (T - 1) / 2
        // - Start index in transformed = bestCenter - R
        // - Original index for that start = startTrans / 2
        int radiusTrans = (bestTransLen - 1) / 2;
        int startTrans = bestCenter - radiusTrans;
        int startOriginal = startTrans / 2;
        int lenOriginal = radiusTrans + 1;

        return s.substring(startOriginal, startOriginal + lenOriginal);
    }

    /**
     * Simple manual test harness.
     */
    public static void main(String[] args) {
        Solution sol = new Solution();
        String[] tests = {
                "babad", // "bab" or "aba"
                "cbbd", // "bb"
                "racecar", // "racecar"
                "a", // "a"
                "aaabaaa", // "aaabaaa"
                "abcdefghi", // any single char
                "xabacdfgdcaba" // "aba" or "xacdfgdcax" variants, etc.
        };

        for (String t : tests) {
            System.out.println(t + " -> " + sol.longestPalindrome(t));
        }
    }
}
