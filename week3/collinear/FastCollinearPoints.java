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

public class FastCollinearPoints {
    private final List<LineSegment> seg;
    private final Point[] points;
    private final int n;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] p) {
        checkNullInput(p);

        seg = new ArrayList<>();
        this.points = Arrays.copyOf(p, p.length);
        this.n = p.length;
        Arrays.sort(this.points);
        checkDuplicate(this.points);
        calculateSegments();
    }

    private void calculateSegments() {
        for (int i = 0; i < n - 3; i++) {
            calculateSegmentForPoint(i);
        }
    }

    private void calculateSegmentForPoint(int cur) {
        // For each other point q, determine the slope it makes with p.
        Point[] tmp = Arrays.copyOf(points, n);

        Arrays.sort(tmp, points[cur].slopeOrder());

        double[] slops = new double[n];
        for (int i = 0; i < tmp.length; i++) {
            slops[i] = points[cur].slopeTo(tmp[i]);
        }

        int same = 1;
        for (int i = 1; i < slops.length; i++) {
            if (slops[i] == slops[i - 1]) {
                same++;
            }
            else {
                if (same >= 3) {
                    LineSegment s = getSegmentFromPoints(tmp, cur, i - same, i - 1);
                    if (s != null) {
                        seg.add(s);
                    }
                }
                same = 1;
            }
        }
        if (same >= 3) {
            LineSegment s = getSegmentFromPoints(tmp, cur, tmp.length - same, tmp.length - 1);
            if (s != null) {
                seg.add(s);
            }
        }
    }

    private LineSegment getSegmentFromPoints(Point[] arr, int cur, int start, int end) {
        if (points[cur].compareTo(arr[start]) < 0) {
            return new LineSegment(points[cur], arr[end]);
        }
        return null;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
