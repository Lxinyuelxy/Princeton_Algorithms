import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	private int N;
	private int numOfSeg = 0;
	private Point[] mPoints;
	
   // finds all line segments containing 4 or more points
   public FastCollinearPoints(Point[] points) {
	   checkArguments(points);
	   checkDuplicateArguments(points);
	   N = points.length;
	   mPoints = new Point[N];
	   for(int i = 0; i < N; i++){
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
	   ArrayList<Point> maxPoints = new ArrayList<Point>();
	   ArrayList<Point> minPoints = new ArrayList<Point>();
	   
	   
	   for(int i = 0; i < N; i++){
		   Arrays.sort(mPoints, mPoints[i].slopeOrder());
		   
		   for(int j = 1; j < N; j++){
			   ArrayList<Point> a = new ArrayList<Point>();
			   a.add(mPoints[0]);
			   a.add(mPoints[j]);
			   double curSlope = mPoints[0].slopeTo(mPoints[j]);
			   
			   while((j+1 < N) && curSlope == mPoints[0].slopeTo(mPoints[j+1])) {
				   a.add(mPoints[++j]);
				   curSlope = mPoints[0].slopeTo(mPoints[j]);
			   }
			   if(a.size() >= 4 ) {
				   Point max = getMaxPoint(a);
				   Point min = getMinPoint(a);
				   if(!hasDuplicateInLineSegment(maxPoints, minPoints, max, min)){
					   maxPoints.add(max);
					   minPoints.add(min);
					   res.add(new LineSegment(max, min));	
					   numOfSeg++;
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
   
   private boolean hasDuplicateInLineSegment(ArrayList<Point> maxPoints, ArrayList<Point> minPoints, Point max, Point min) {
	   for(int i = 0; i < maxPoints.size(); i++){
		   if(max.compareTo(maxPoints.get(i)) == 0 && min.compareTo(minPoints.get(i)) == 0) return true;
	   }
	   return false;
   }
   
    private Point getMaxPoint(ArrayList<Point> a) {   
		Point max = a.get(0);
		for(Point temp : a) {
			if(max.compareTo(temp) < 0) {max = temp;}
		}
		return max;
	}
	
	private Point getMinPoint(ArrayList<Point> a) {
		Point min = a.get(0);
		for(Point temp : a) {
			if(min.compareTo(temp) > 0) {min = temp;}
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
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    System.out.println("numOfSeg = "+collinear.numberOfSegments());
	    StdDraw.show();
		
	}
}
