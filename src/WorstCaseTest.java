public class WorstCaseTest {

    public static void main(String[] args) {
        System.out.println("Testing with ThreeBlocks pattern (a^n b^n c^n)");
        System.out.println();
        testThreeBlocksNovice();
        System.out.println("\n\n");
        testThreeBlocksManacher();
        System.out.println("\n\n");
        compareAlgorithms();
    }

    public static void testThreeBlocksNovice() {
        Solution sol = new Solution();

        System.out.println("========================================");
        System.out.println("NOVICE ALGORITHM: ThreeBlocks (a^n b^n c^n)");
        System.out.println("========================================");
        System.out.println();

        int[] sizes = { 100, 200, 400, 800, 1600, 3200, 6400 };

        System.out.printf("%-15s %-15s %-15s %-20s %-15s%n",
                "Input Size", "Comparisons", "Size Ratio", "Comparison Ratio", "Time (ms)");
        System.out.println("-----------------------------------------------------------------------------------");

        long prevComps = 0;
        int prevSize = 0;

        for (int n : sizes) {
            // Build ThreeBlocks: a^(n/3) b^(n/3) c^(n/3)
            StringBuilder sb = new StringBuilder();
            int blockSize = n / 3;

            for (int i = 0; i < blockSize; i++)
                sb.append('a');
            for (int i = 0; i < blockSize; i++)
                sb.append('b');
            for (int i = 0; i < n - 2 * blockSize; i++)
                sb.append('c');

            String test = sb.toString();
            int length = test.length();

            long startTime = System.nanoTime();
            String result = sol.longestPalindrome(test);
            long endTime = System.nanoTime();

            long comps = sol.getComparisonCount();
            long timeMs = (endTime - startTime) / 1_000_000L;

            if (prevComps > 0) {
                double sizeRatio = (double) length / prevSize;
                double compRatio = (double) comps / prevComps;

                System.out.printf("%-15d %-15d %-15.2fx %-20.2fx %-15d",
                        length, comps, sizeRatio, compRatio, timeMs);

                // Check if it's linear or quadratic
                if (compRatio > sizeRatio * 1.5) {
                    System.out.print(" ⚠️  QUADRATIC!");
                } else if (compRatio <= sizeRatio * 1.2) {
                    System.out.print(" ✓ Linear");
                }
                System.out.println();
            } else {
                System.out.printf("%-15d %-15d %-15s %-20s %-15d%n",
                        length, comps, "baseline", "baseline", timeMs);
            }

            prevComps = comps;
            prevSize = length;
        }

        System.out.println();
        System.out.println("Analysis:");
        System.out.println("  O(n):  Comparison ratio ≈ size ratio (~2.0x)");
        System.out.println("  O(n²): Comparison ratio ≈ size ratio² (~4.0x)");
        System.out.println();
        System.out.println("Expected: ~1.0x growth (linear) due to bestCase optimization ✓");
    }

    public static void testThreeBlocksManacher() {
        Mancher manacher = new Mancher();

        System.out.println("========================================");
        System.out.println("MANACHER ALGORITHM: ThreeBlocks (a^n b^n c^n)");
        System.out.println("========================================");
        System.out.println();

        int[] sizes = { 100, 200, 400, 800, 1600, 3200, 6400 };

        System.out.printf("%-15s %-15s %-15s %-20s %-15s%n",
                "Input Size", "Comparisons", "Size Ratio", "Comparison Ratio", "Time (ms)");
        System.out.println("-----------------------------------------------------------------------------------");

        long prevComps = 0;
        int prevSize = 0;

        for (int n : sizes) {
            // Build ThreeBlocks: a^(n/3) b^(n/3) c^(n/3)
            StringBuilder sb = new StringBuilder();
            int blockSize = n / 3;

            for (int i = 0; i < blockSize; i++)
                sb.append('a');
            for (int i = 0; i < blockSize; i++)
                sb.append('b');
            for (int i = 0; i < n - 2 * blockSize; i++)
                sb.append('c');

            String test = sb.toString();
            int length = test.length();

            long startTime = System.nanoTime();
            String result = manacher.longestPalindrome(test);
            long endTime = System.nanoTime();

            long comps = manacher.getComparisonCount();
            long timeMs = (endTime - startTime) / 1_000_000L;

            if (prevComps > 0) {
                double sizeRatio = (double) length / prevSize;
                double compRatio = (double) comps / prevComps;

                System.out.printf("%-15d %-15d %-15.2fx %-20.2fx %-15d",
                        length, comps, sizeRatio, compRatio, timeMs);

                // Check if it's linear or quadratic
                if (compRatio > sizeRatio * 1.5) {
                    System.out.print(" ⚠️  QUADRATIC!");
                } else if (compRatio <= sizeRatio * 1.2) {
                    System.out.print(" ✓ Linear");
                }
                System.out.println();
            } else {
                System.out.printf("%-15d %-15d %-15s %-20s %-15d%n",
                        length, comps, "baseline", "baseline", timeMs);
            }

            prevComps = comps;
            prevSize = length;
        }

        System.out.println();
        System.out.println("Analysis:");
        System.out.println("  O(n):  Comparison ratio ≈ size ratio (~2.0x)");
        System.out.println("  O(n²): Comparison ratio ≈ size ratio² (~4.0x)");
    }

    public static void compareAlgorithms() {
        System.out.println("========================================");
        System.out.println("SIDE-BY-SIDE COMPARISON: ThreeBlocks");
        System.out.println("========================================");
        System.out.println();

        Solution sol = new Solution();
        Mancher manacher = new Mancher();

        int[] sizes = { 100, 200, 400, 800, 1600, 3200, 6400, 12800 };

        System.out.printf("%-12s | %-20s | %-20s | %-15s%n",
                "Input Size", "Novice Comparisons", "Manacher Comparisons", "Ratio (N/M)");
        System.out.println("------------------------------------------------------------------------------");

        for (int n : sizes) {
            // Build ThreeBlocks: a^(n/3) b^(n/3) c^(n/3)
            StringBuilder sb = new StringBuilder();
            int blockSize = n / 3;

            for (int i = 0; i < blockSize; i++)
                sb.append('a');
            for (int i = 0; i < blockSize; i++)
                sb.append('b');
            for (int i = 0; i < n - 2 * blockSize; i++)
                sb.append('c');

            String test = sb.toString();
            int length = test.length();

            // Test Novice
            sol.longestPalindrome(test);
            long noviceComps = sol.getComparisonCount();

            // Test Manacher
            manacher.longestPalindrome(test);
            long mancherComps = manacher.getComparisonCount();

            double ratio = (double) noviceComps / mancherComps;

            System.out.printf("%-12d | %-20d | %-20d | %-15.2fx",
                    length, noviceComps, mancherComps, ratio);

            if (ratio > 2.0) {
                System.out.print(" ⚠️");
            } else if (ratio < 1.5) {
                System.out.print(" ✓");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Summary:");
        System.out.println("  Ratio < 1.5x: Novice is competitive ✓");
        System.out.println("  Ratio > 2.0x: Novice significantly slower ⚠️");
        System.out.println();
        System.out.println("Expected for ThreeBlocks: Both should be ~O(n) with similar performance ✓");
    }
}