public class P implements Programm {
    static boolean details = false;
    long operationCount = 0; // actual collatzStep calls

    public P() { }

    @Override
    public int run(int x, boolean details) {
        long collatzSteps  = 0;         // sum of all chain lengths
        operationCount = 0; // count of all collatzStep calls
        P.details = details;

        int maxLength = 0, bestStart = 1;
        long t0 = System.nanoTime();

        for (int i = 1; i <= x; i++) {
            int length = collatz(i);
            collatzSteps += length;
            if (length > maxLength) {
                maxLength = length;
                bestStart = i;
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;

        // Outputs:
        System.out.println("P: Longest sequence starts at " + bestStart +
                " with length " + maxLength);
        System.out.println("P: Total chain-steps = " + collatzSteps);
        System.out.println("P: Total operations = " + operationCount);
        System.out.printf  ("P: Elapsed time = %.3f ms%n", elapsedMs);

        return maxLength;
    }

    @Override
    public int collatz(long x) {
        int steps = 0;
        while (x != 1) {
            x = collatzStep(x);
            steps++;
            // count each real step
            operationCount++;
        }
        return steps;
    }
}
