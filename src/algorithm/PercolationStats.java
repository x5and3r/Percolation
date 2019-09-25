package algorithm;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int experimentsCount;
    private final double[] fractions;
    private double mean;
    private double stddev;

    public PercolationStats(int n, int trials) {  // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Size or trials is not valid!");
        }
        experimentsCount = trials;
        fractions = new double[trials];
        int i = 0;
        while (i < trials) {
            final Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                final int row = StdRandom.uniform(1, n + 1);
                final int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            fractions[i] = percolation.numberOfOpenSites() / (double) (n * n);
            i++;
        }
        mean = 0D;
        stddev = 0D;
    }

    public double mean() {                          // sample mean of percolation threshold
        mean = StdStats.mean(fractions);
        return mean;
    }

    public double stddev() {                        // sample standard deviation of percolation threshold
        if (experimentsCount == 1) {
            stddev = Double.NaN;
        }
        stddev = StdStats.stddev(fractions);
        return stddev;
    }

    public double confidenceLo() {                  // low  endpoint of 95% confidence interval
        setMeanAndStandardDeviation();
        return mean - CONFIDENCE_95 * stddev / Math.sqrt(experimentsCount);
    }

    public double confidenceHi() {                  // high endpoint of 95% confidence interval
        setMeanAndStandardDeviation();
        return mean + CONFIDENCE_95 * stddev / Math.sqrt(experimentsCount);
    }

    /**
     * Calculates mean and stddev for confidence interval.
     */
    private void setMeanAndStandardDeviation() {
        if (mean == 0) {
            mean = mean();
        }
        if (stddev == 0) {
            stddev = stddev();
        }
    }

    /**
     * Method performs T independent computational experiments on an n-by-n grid, and prints the sample mean,
     * sample standard deviation, and the 95% confidence interval for the percolation threshold.
     *
     * @param args - two command-line arguments n and T.
     */
    public static void main(String[] args) {        // test client
        if (args.length == 0) {
            throw new IllegalArgumentException("Grid size is not set!");
        }
        final int n = Integer.parseInt(args[0]);
        if (args.length == 1) {
            throw new IllegalArgumentException("Number of trials is not set!");
        }
        final int trials = Integer.parseInt(args[1]);
        final PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = " + "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

}
