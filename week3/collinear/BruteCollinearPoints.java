/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> seg;
    private final Point[] points;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] p) {
        checkNullInput(p);

        seg = new ArrayList<>();
        this.points = Arrays.copyOf(p, p.length);
        Arrays.sort(this.points);
        checkDuplicate(this.points);

        calculateSegments();
    }

    private void calculateSegments() {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int x = j + 1; x < points.length; x++) {
                    for (int y = x + 1; y < points.length; y++) {
                        double slope1 = points[i].slopeTo(points[j]);
                        double slope2 = points[x].slopeTo(points[y]);
                        double slope3 = points[j].slopeTo(points[x]);
                        if (slope1 == slope2 && slope1 == slope3) {
                            seg.add(new LineSegment(points[i], points[y]));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return seg.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[seg.size()];
        return seg.toArray(res);
    }

    private void checkNullInput(Point[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkDuplicate(Point[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (i > 0 && arr[i].compareTo(arr[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
