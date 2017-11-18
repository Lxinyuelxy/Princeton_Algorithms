import edu.princeton.cs.algs4.*;

public class PointSET {
	private SET<Point2D> pointsSet;
	
   /*
    * construct an empty set of points 
    */
	public PointSET() {
		pointsSet = new SET<Point2D>();
	}
   
   /*
    * is the set empty?
    */
    public boolean isEmpty() {        
	   return pointsSet.isEmpty();
    }
   
   /*
    * number of points in the set
    */
    public int size() {
    	return pointsSet.size();
    }
   
   /*
    * add the point to the set (if it is not already in the set)
    */
   public void insert(Point2D p) {
	  pointsSet.add(p);
   }
   
   /*
    * does the set contain point p?
    */
    public boolean contains(Point2D p) {
	   return pointsSet.contains(p);
    }
   
   /*
    * draw all points to standard draw
    */
   public void draw() {
	   for(Point2D p : pointsSet) {
		   p.draw();
	   }
   }
   
   /*
    *all points that are inside the rectangle
    */
    public Iterable<Point2D> range(RectHV rect) {
	     Stack<Point2D> res = new Stack<Point2D>();
	     for(Point2D point : pointsSet) {
	    	 if(rect.distanceTo(point) == 0) {
	    		 res.push(point);
	    	 }
	     }
	     return res;
    }
   
   /*
    * a nearest neighbor in the set to point p; null if the set is empty
    */
    public Point2D nearest(Point2D p) {
    	if(size() == 0) return null;
    	Point2D nearestPoint = pointsSet.min();
    	double nearestDis = nearestPoint.distanceTo(p);
    	
    	for(Point2D point : pointsSet) {
    		if(point.equals(nearestPoint)) continue;
    		if(nearestDis > point.distanceTo(p)) {
    			nearestDis = point.distanceTo(p);
    			nearestPoint = point;
    		}
    	}
    	return nearestPoint;
    }
}

