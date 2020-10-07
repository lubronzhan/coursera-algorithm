import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/******************************************************************************
 *  Name:              Lubron Zhan
 *  Coursera User ID:  lubronzhan@gmail.com
 *  Last modified:     10/06/2020
 **************************************************************************** */


public class Percolation {
    private final int n;
    private final WeightedQuickUnionUF uf;
    // storing the connections include connection to bottom
    private final WeightedQuickUnionUF backwash;
    private boolean[][] openPoints;
    private int numOpen;
    private final int VIRTUAL_TOP;
    private final int VIRTUAL_BOTTOM;


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.backwash = new WeightedQuickUnionUF(n * n + 2);
        this.openPoints = new boolean[this.n][this.n];
        this.VIRTUAL_TOP = n * n;
        this.VIRTUAL_BOTTOM = n * n + 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkInputHelper(row, col);
        // only check if the top can connect the current point in uf that without virtual bottom
        return this.uf.find(convertPointToIndex(row, col)) == this.uf.find(VIRTUAL_TOP);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkInputHelper(row, col);
        return this.openPoints[row - 1][col - 1];
    }

    // does the system percolate?
    public boolean percolates() {
        return this.backwash.find(VIRTUAL_TOP) == this.backwash.find(VIRTUAL_BOTTOM);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkInputHelper(row, col);
        if (isOpen(row, col)) return;

        this.openPoints[row - 1][col - 1] = true;
        numOpen++;

        connectBottom(row, col);
        connectTop(row, col);
        connectLeft(row, col);
        connectRight(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    private void checkInputHelper(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    private int convertPointToIndex(int row, int col) {
        return (row - 1) * n + col - 1;
    }

    private void connectTop(int row, int col) {
        if (row - 2 >= 0 && isOpen(row - 1, col)) {
            this.uf.union(convertPointToIndex(row, col), convertPointToIndex(row - 1, col));
            this.backwash.union(convertPointToIndex(row, col), convertPointToIndex(row - 1, col));
        }
        if (row == 1) {
            this.uf.union(convertPointToIndex(row, col), VIRTUAL_TOP);
            this.backwash.union(convertPointToIndex(row, col), VIRTUAL_TOP);
        }
    }

    private void connectLeft(int row, int col) {
        if (col - 2 >= 0 && isOpen(row, col - 1)) {
            this.uf.union(convertPointToIndex(row, col), convertPointToIndex(row, col - 1));
            this.backwash.union(convertPointToIndex(row, col), convertPointToIndex(row, col - 1));
        }
    }

    private void connectRight(int row, int col) {
        if (col < n && isOpen(row, col + 1)) {
            this.uf.union(convertPointToIndex(row, col), convertPointToIndex(row, col + 1));
            this.backwash.union(convertPointToIndex(row, col), convertPointToIndex(row, col + 1));
        }
    }

    private void connectBottom(int row, int col) {
        if (row < n && isOpen(row + 1, col)) {
            this.uf.union(convertPointToIndex(row, col), convertPointToIndex(row + 1, col));
            this.backwash.union(convertPointToIndex(row, col), convertPointToIndex(row + 1, col));
        }
        if (row == n) {
            this.backwash.union(convertPointToIndex(row, col), VIRTUAL_BOTTOM);
        }
    }
}
