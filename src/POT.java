import java.util.*;
import java.util.concurrent.*;

public class POT implements Programm {
    static boolean details = false;

    // simple POD to hold each thread’s partial outcome
    private static class Result {
        long chainSteps, operations;
        int maxLen, bestStart;
        Result(long chainSteps, long operations, int maxLen, int bestStart) {
            this.chainSteps = chainSteps;
            this.operations = operations;
            this.maxLen      = maxLen;
            this.bestStart   = bestStart;
        }
    }

    @Override
    public int run(int x, boolean detailsFlag) {
        POT.details = detailsFlag;

        // how many threads?
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<Result>> futures = new ArrayList<>(threads);

        // record start time
        long t0 = System.nanoTime();

        // launch one task per thread, each handling
        // i ∈ {threadId+1, threadId+1+threads, …} up to x
        for (int threadId = 0; threadId < threads; threadId++) {
            final int id = threadId;
            futures.add(pool.submit(() -> computeRange(id, threads, x)));
        }

        // shut down the pool and wait
        pool.shutdown();

        // merge results
        long totalChains = 0, totalOps = 0;
        int  globalMaxLen = 0, globalBest = 1;
        for (Future<Result> f : futures) {
            try {
                Result r = f.get();
                totalChains += r.chainSteps;
                totalOps     += r.operations;
                if (r.maxLen > globalMaxLen ||
                        (r.maxLen == globalMaxLen && r.bestStart < globalBest)) {
                    globalMaxLen = r.maxLen;
                    globalBest   = r.bestStart;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;

        // same style of output as P/PO
        System.out.println("POT: Longest sequence starts at " + globalBest +
                " with length " + globalMaxLen);
        System.out.println("POT: Total chain-steps = " + totalChains);
        System.out.println("POT: Total operations = " + totalOps);
        System.out.printf  ("POT: Elapsed time = %.3f ms%n", elapsedMs);

        return globalMaxLen;
    }

    // Compute for i = startIdx+1, startIdx+1+step, ... ≤ x
    private Result computeRange(int startIdx, int step, int x) {
        long localChains = 0, localOps = 0;
        int  localMaxLen = 0, localBest = 1;

        for (int i = startIdx + 1; i <= x; i += step) {
            if (i <= x/2 && (i & 1) == 1)       continue;
            if ((i - 1) % 3 == 0 && ((i - 1) & 1) == 1) continue;

            // inline collatz to track localOps
            long n = i;
            int  len = 0;
            while (n != 1) {
                n = collatzStep(n);
                len++;
                localOps++;
            }
            localChains += len;

            if (len > localMaxLen ||
                    (len == localMaxLen && i < localBest)) {
                localMaxLen = len;
                localBest   = i;
            }
        }

        return new Result(localChains, localOps, localMaxLen, localBest);
    }

    // we don’t use the interface’s default collatz; we need our own here
    @Override
    public int collatz(long x) {
        throw new UnsupportedOperationException(
                "POT uses inline collatz in computeRange()");
    }
}
