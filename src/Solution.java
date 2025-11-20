/**
 * Priority-based algorithm for Longest Palindromic Substring.
 * WITH DETAILED COMPARISON TRACKING
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

        // Step 2: Calculate best-case potential
        int[] bestCase = new int[n];
        for (int i = 0; i < n; i++) {
            int dist = Math.min(i, n - 1 - i);
            bestCase[i] = 2 * dist + 1;
        }

        int bestTransLen = 1;
        int bestCenter = 0;
        int mid = n / 2;

        // Step 3: Check centers in priority order
        for (int offset = 0; mid - offset >= 0 || mid + offset < n; offset++) {
            outerLoopCount++;

            if (offset == 0) {
                // Check center
                int idx = mid;
                if (bestCase[idx] > bestTransLen) {
                    positionChecks++;
                    int len = expandFromCenter(str, idx);
                    bestCase[idx] = len;

                    if (len > bestTransLen) {
                        bestTransLen = len;
                        bestCenter = idx;
                    }
                }
            } else {
                int leftIdx = mid - offset;
                int rightIdx = mid + offset;

                boolean leftValid = (leftIdx >= 0);
                boolean rightValid = (rightIdx < n);

                if (!leftValid && !rightValid)
                    break;

                boolean leftCanWin = leftValid && bestCase[leftIdx] > bestTransLen;
                boolean rightCanWin = rightValid && bestCase[rightIdx] > bestTransLen;

                // Early termination check
                if (!leftCanWin && !rightCanWin) {
                    earlyTerminations++;
                    break;
                }

                // Check left
                if (leftCanWin) {
                    positionChecks++;
                    int len = expandFromCenter(str, leftIdx);
                    bestCase[leftIdx] = len;

                    if (len > bestTransLen) {
                        bestTransLen = len;
                        bestCenter = leftIdx;
                    }
                }

                // Check right
                if (rightCanWin) {
                    positionChecks++;
                    int len = expandFromCenter(str, rightIdx);
                    bestCase[rightIdx] = len;

                    if (len > bestTransLen) {
                        bestTransLen = len;
                        bestCenter = rightIdx;
                    }
                }
            }
        }

        // Step 4: Rebuild result
        int radius = (bestTransLen - 1) / 2;
        int L = bestCenter - radius;
        int R = bestCenter + radius;

        StringBuilder res = new StringBuilder();
        for (int i = L; i <= R; i++) {
            char ch = str[i];
            if (ch != '#') {
                res.append(ch);
            }
        }

        return res.toString();
    }

    private int expandFromCenter(char[] str, int idx) {
        int left = idx - 1;
        int right = idx + 1;
        int n = str.length;

        while (left >= 0 && right < n) {
            comparisonCount++;
            if (str[left] != str[right])
                break;
            left--;
            right++;
        }

        return right - left - 1;
    }
}