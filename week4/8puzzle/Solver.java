/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final int moves;
    private final boolean isSolvable;
    private final SearchNode lastBoard;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        // use two PQ, one for initial board, one for twin of initial board.
        // one of them will be solvable for sure
        // pq is sorted based on manhattan value
        MinPQ<SearchNode> pq = new MinPQ<>();
        // twin is for storing the twin's PQ
        MinPQ<SearchNode> twinPq = new MinPQ<>();

        pq.insert(new SearchNode(initial, null, 0));
        twinPq.insert(new SearchNode(initial.twin(), null, 0));

        // until one of the board find result
        while (true) {
            SearchNode nextNode = pq.delMin();
            SearchNode nextTwinNode = twinPq.delMin();
            if (nextNode.board.isGoal()) {
                isSolvable = true;
                moves = nextNode.step;
                lastBoard = nextNode;
                break;
            }
            if (nextTwinNode.board.isGoal()) {
                isSolvable = false;
                moves = nextTwinNode.step;
                lastBoard = null;
                break;
            }


            makeMove(nextNode, pq);
            makeMove(nextTwinNode, twinPq);

        }
    }

    private void makeMove(SearchNode node, MinPQ<SearchNode> pq) {
        int nextStep = node.step + 1;
        for (Board b : node.board.neighbors()) {
            if (node.previousNode == null || !b
                    .equals(node.previousNode.board)) {
                pq.insert(new SearchNode(b, node, nextStep));
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int step;
        private final SearchNode previousNode;
        // The Manhattan priority function is the Manhattan distance of a board plus the number of moves
        // made so far to get to the search node.
        private final int priority;

        public SearchNode(Board board, SearchNode previousNode, int step) {
            this.board = board;
            this.step = step;
            this.previousNode = previousNode;
            this.priority = step + board.manhattan();
        }

        public int compareTo(SearchNode n) {
            return this.priority - n.priority;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable ? moves : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.lastBoard == null) {
            return null;
        }

        // return the result by back tracing the searchNode
        Stack<Board> res = new Stack<>();
        SearchNode tmp = lastBoard;
        while (tmp != null) {
            res.push(tmp.board);
            tmp = tmp.previousNode;
        }

        return res;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
