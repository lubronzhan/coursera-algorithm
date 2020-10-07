/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96D;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        double[] times = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            }
            times[i] = (double) p.numberOfOpenSites() / (n * n);
        }
        this.mean = StdStats.mean(times);
        this.stddev = StdStats.stddev(times);
        this.confidenceLo = mean - CONFIDENCE_95 * stddev / Math.sqrt(trials);
        this.confidenceHi = mean + CONFIDENCE_95 * stddev / Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(a, b);
        StdOut.printf("mean = %f\n", ps.mean);
        StdOut.printf("stddev = %f\n", ps.stddev);
        StdOut.printf("95%% confidence interval = [%f, %f]\n", ps.confidenceLo, ps.confidenceHi);
    }
}
