import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	private int numOfSeg = 0;
	private Point[] mPoints;
	private int N;

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {
		checkArguments(points);
		checkDuplicateArguments(points);
		N = points.length;
		mPoints = new Point[N];
		for(int i = 0; i < N; i++) {
			mPoints[i] = points[i];
		}
	}
	
	// the number of line segments
	public int numberOfSegments() {     
		return numOfSeg;
	}
	
	// the line segments
	public LineSegment[] segments() {
		ArrayList<LineSegment> res = new ArrayList<LineSegment>();
		for(int i = 0; i < N; i++){
			for(int j = i + 1; j < N; j++){
				double k1 = mPoints[i].slopeTo(mPoints[j]);
				for(int m = j + 1; m < N; m++){
					double k2 = mPoints[j].slopeTo(mPoints[m]);
					if(k1 != k2) continue;
					for(int n = m + 1; n < N; n++){
						double k3 = mPoints[m].slopeTo(mPoints[n]);
						if(k3 == k2){
							res.add(new LineSegment(
									getMaxPoint(mPoints[i], mPoints[j], mPoints[m], mPoints[n]),
									getMinPoint(mPoints[i], mPoints[j], mPoints[m], mPoints[n])));
							numOfSeg++;
							
						}
					}
				}
			}
		}
		int index = 0;
		LineSegment[] lineSeg = new LineSegment[res.size()];
		for (LineSegment temp : res){
			   lineSeg[index++] = temp;
		   }
		return lineSeg;
	}
	
	private Point getMaxPoint(Point a, Point b, Point c, Point d) {
		Point max = a;
		if(max.compareTo(b) < 0){
			max = b;
		}
		if(max.compareTo(c) < 0){
			max = c;
		}
		if(max.compareTo(d) < 0){
			max = d;
		}
		return max;
	}
	
	private Point getMinPoint(Point a, Point b, Point c, Point d) {
		Point min = a;
		if(min.compareTo(b) > 0){
			min = b;
		}
		if(min.compareTo(c) > 0){
			min = c;
		}
		if(min.compareTo(d) > 0){
			min = d;
		}
		return min;
	}
	
	private void checkArguments(Point[] points) {
		if(points == null) throw new java.lang.NullPointerException("exception");
		for(int i = 0; i < points.length; i++){
			if(points[i] == null) throw new java.lang.NullPointerException("exception");
		}
	}
	
	private void checkDuplicateArguments(Point[] points) {
		for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicated entries in given points.");
                }
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
