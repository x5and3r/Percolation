package algorithm;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int size;
    private final WeightedQuickUnionUF grid;
    private final WeightedQuickUnionUF full;
    private final int virtualTop;
    private final int virtualBottom;
    private final boolean[] openedSites;
    private int openSitesCount;

    public Percolation(int n) {                // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size must be greater than 0!");
        }
        size = n;
        grid = new WeightedQuickUnionUF(n * n + 2);
        full = new WeightedQuickUnionUF(n * n + 1);
        virtualTop = 0;
        virtualBottom = n * n + 1;
        openedSites = new boolean[n * n];
    }

    public void open(int row, int col) {       // open site (row, col) if it is not open already
        if (isOpen(row, col)) {
            return;
        }
        checkArguments(row, col);
        final int position = xyTo1D(row, col);
        openedSites[position - 1] = true;
        openSitesCount++;
        if (row == 1) {
            grid.union(virtualTop, position);
            full.union(virtualTop, position);
        }
        if (row == size) {
            grid.union(position, virtualBottom);
        }
        if (row > 1 && isOpen(row - 1, col)) {

            grid.union(xyTo1D(row - 1, col), position);
            full.union(xyTo1D(row - 1, col), position);

        }
        if (row < size && isOpen(row + 1, col)) {
            grid.union(position, xyTo1D(row + 1, col));
            full.union(position, xyTo1D(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            grid.union(position, xyTo1D(row, col - 1));
            full.union(position, xyTo1D(row, col - 1));
        }
        if (col < size && isOpen(row, col + 1)) {
            grid.union(position, xyTo1D(row, col + 1));
            full.union(position, xyTo1D(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        checkArguments(row, col);
        return openedSites[xyTo1D(row, col) - 1];
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        checkArguments(row, col);
        return full.connected(xyTo1D(row, col), virtualTop);
    }

    public int numberOfOpenSites() {          // number of open sites
        return openSitesCount;
    }

    public boolean percolates() {             // does the system percolate?
        return grid.connected(virtualTop, virtualBottom);
    }

    /**
     * Finds a position of element in 1D array by its coordinates in 2D array.
     *
     * @param row - row index.
     * @param col - column index.
     * @return position in 1D array.
     */
    private int xyTo1D(int row, int col) {
        return size * (row - 1) + col;
    }

    /**
     * Checks if arguments are inside their prescribed range. If not - throws {@link IllegalArgumentException}.
     *
     * @param row - number of the row in grid.
     * @param col - number of the column in grid.
     */
    private void checkArguments(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            throw new IllegalArgumentException("Argument is outside its prescribed range!");
        }
    }

}
