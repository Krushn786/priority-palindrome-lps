/**
 * Optimized O(n) Priority-based algorithm for Longest Palindromic Substring.
 * Uses bestCase-driven pointer movement for guaranteed linear complexity.
 *
 * Author: Krushn Gor
 */
public class Solution {

    private long comparisonCount = 0L;
    private long outerLoopCount = 0L;
    private long positionChecks = 0L;
    private long earlyTerminations = 0L;

    public long getComparisonCount() {
        return comparisonCount;
    }

    public long getOuterLoopCount() {
        return outerLoopCount;
    }

    public long getPositionChecks() {
        return positionChecks;
    }

    public long getEarlyTerminations() {
        return earlyTerminations;
    }

    public static int getTransformedLength(int originalLength) {
        return 2 * originalLength + 1;
    }

    public String longestPalindrome(String s) {
        // Reset all counters
        comparisonCount = 0L;
        outerLoopCount = 0L;
        positionChecks = 0L;
        earlyTerminations = 0L;

        if (s == null || s.length() == 0) {
            return "";
        }
        if (s.length() == 1) {
            return s;
        }

        // Step 1: Transform with '#'
        int origLen = s.length();
        int n = 2 * origLen + 1;
        char[] str = new char[n];

        str[0] = '#';
        for (int i = 0; i < origLen; i++) {
            str[i * 2 + 1] = s.charAt(i);
            str[i * 2 + 2] = '#';
        }

        // Step 2: Calculate best case radius (mutable - updates as we find actual
        // values)
        int[] bestCase = new int[n];
        for (int i = 0; i < n; i++) {
            bestCase[i] = Math.min(i, n - 1 - i);
        }

        int bestRadius = 0;
        int bestCenter = 0;
        int mid = n / 2;

        // Initialize pointers
        int parentPtr = mid;
        int leftPtr = mid;
        int rightPtr = mid;

        // Step 3: Check positions using bestCase-driven pointer movement
        while (leftPtr >= 0 || rightPtr < n) {
            outerLoopCount++;

            // Get bestCase values for both pointers
            int leftBest = (leftPtr >= 0) ? bestCase[leftPtr] : -1;
            int rightBest = (rightPtr < n) ? bestCase[rightPtr] : -1;

            // Early termination: neither pointer can beat current best
            if (leftBest <= bestRadius && rightBest <= bestRadius) {
                earlyTerminations++;
                break;
            }

            // CRITICAL: Choose next pointer based on bestCase comparison
            if (leftBest > rightBest && leftPtr >= 0) {
                parentPtr = leftPtr;
            } else if (rightBest >= leftBest && rightPtr < n) {
                parentPtr = rightPtr;
            } else {
                earlyTerminations++;
                break;
            }

            // Check current parent position
            if (bestCase[parentPtr] > bestRadius) {
                positionChecks++;
                int originalBestCase = bestCase[parentPtr]; // Save original before updating
                int foundRadius = expandFromCenter(str, parentPtr, bestCase[parentPtr]);

                if (foundRadius > bestRadius) {
                    bestRadius = foundRadius;
                    bestCenter = parentPtr;
                }

                // CRITICAL: Update bestCase to actual found value
                bestCase[parentPtr] = foundRadius;

                // If matched original bestCase, we found longest possible
                if (foundRadius == originalBestCase) {
                    earlyTerminations++;
                    return extractPalindrome(str, bestCenter, bestRadius);
                }
            }

            // Move the pointer we just checked
            if (parentPtr == leftPtr) {
                leftPtr--;
            } else if (parentPtr == rightPtr) {
                rightPtr++;
            }
        }

        return extractPalindrome(str, bestCenter, bestRadius);
    }

    private int expandFromCenter(char[] str, int center, int maxRadius) {
        int left = center - 1;
        int right = center + 1;
        int n = str.length;
        int radius = 0;

        while (left >= 0 && right < n && radius < maxRadius) {
            comparisonCount++;
            if (str[left] != str[right]) {
                break;
            }
            radius++;
            left--;
            right++;
        }

        return radius;
    }

    private String extractPalindrome(char[] str, int center, int radius) {
        int L = center - radius;
        int R = center + radius;

        // Count non-# characters
        int resultLen = 0;
        for (int i = L; i <= R; i++) {
            if (str[i] != '#') {
                resultLen++;
            }
        }

        // Build result directly
        char[] result = new char[resultLen];
        int idx = 0;
        for (int i = L; i <= R; i++) {
            if (str[i] != '#') {
                result[idx++] = str[i];
            }
        }

        return new String(result);
    }
}