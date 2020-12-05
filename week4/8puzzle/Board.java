/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private final int[][] t;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.t = copyBoard(tiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(" " + t[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (t[i][j] != i * n + j + 1 && t[i][j] != 0) {
                    ham++;
                }
            }
        }
        return ham;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int man = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (t[i][j] != i * n + j + 1 && t[i][j] != 0) {
                    int targetX = (t[i][j] - 1) / n;
                    int targetY = (t[i][j] - 1) % n;
                    man += Math.abs(targetX - i) + Math.abs(targetY - j);
                }
            }
        }
        return man;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (t[i][j] != i * n + j + 1 && t[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board yb = (Board) y;
        if (yb.t.length != t.length) {
            return false;
        }
        if (!Arrays.deepEquals(yb.t, t)) {
            return false;
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> res = new Queue<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (t[i][j] == 0) {
                    int[][] director = { { 1, 0 }, { -1, 0 }, { 0, -1 }, { 0, 1 } };
                    for (int m = 0; m < 4; m++) {
                        int nextX = i + director[m][0];
                        int nextY = j + director[m][1];
                        if (0 <= nextX && nextX < n && 0 <= nextY && nextY < n) {
                            int[][] copy = copyBoard(t);
                            copy[nextX][nextY] = t[i][j];
                            copy[i][j] = t[nextX][nextY];
                            res.enqueue(new Board(copy));
                        }
                    }
                    return res;
                }
            }
        }
        return res;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = copyBoard(t);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (t[i][j] != 0) {
                    int[][] director = { { 1, 0 }, { -1, 0 }, { 0, -1 }, { 0, 1 } };
                    for (int m = 0; m < 4; m++) {
                        int nextX = i + director[m][0];
                        int nextY = j + director[m][1];
                        if (0 <= nextX && nextX < n && 0 <= nextY && nextY < n
                                && t[nextX][nextY] != 0) {
                            copy[nextX][nextY] = t[i][j];
                            copy[i][j] = t[nextX][nextY];
                            return new Board(copy);
                        }
                    }
                }
            }
        }
        return null;
    }

    private int[][] copyBoard(int[][] tile) {
        int[][] myInt = new int[n][n];
        for (int i = 0; i < n; i++)
            myInt[i] = tile[i].clone();

        return myInt;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] tile = { { 3, 7, 4, 10 }, { 13, 5, 2, 8 }, { 11, 9, 1, 0 }, { 6, 14, 15, 12 } };
        int[][] tile = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };


        Board b = new Board(tile);
        StdOut.println("toString");
        StdOut.println(b.toString());
        StdOut.println("hamming");
        StdOut.println(b.hamming());
        StdOut.println("manhattan");
        StdOut.println(b.manhattan());
        StdOut.println("isGoal");
        StdOut.println(b.isGoal());
        StdOut.println("dimension");
        StdOut.println(b.dimension());
        StdOut.println("equals");
        StdOut.println(b.equals(b.twin()));
        StdOut.println("equals object");
        StdOut.println(b.equals((Object) b));
        StdOut.println("twin");
        StdOut.println(b.twin().toString());

        StdOut.println("neighbor");
        Iterable<Board> iterable = b.neighbors();
        for (Board neighborB : iterable) {
            StdOut.println(neighborB.toString());
        }
    }
}
