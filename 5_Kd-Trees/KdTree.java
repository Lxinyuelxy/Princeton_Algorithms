import edu.princeton.cs.algs4.*;

public class KdTree {

	private int N = 0;
	private Node root;
	private static final boolean vertical = false;
	private static final boolean horizontal = true;
	
	public KdTree() {
		
	}
	
	private static class Node {
		private Point2D point;
		private RectHV rect;
		private Node lb;
		private Node rt;
		
		public Node(Point2D p, RectHV rect) {
			this.point = p;
			this.rect = rect;
		}
	}

	/*
	 * is the set empty? 
	 */
	public boolean isEmpty() {
		return N == 0;
	}
	
	/*
	 * number of points in the set
	 */
	public int size() {
		return N;
	}
	
	/*
	 * add the point to the set (if it is not already in the set)
	 */
    public void insert(Point2D p) {
    	if(root == null)
    		root = new Node(p, new RectHV(0, 0, 1, 1));
    	else
    		root = insert(null,root, p, horizontal);
    	N++;
	}
    
    private Node insert(Node parent, Node x, Point2D p, boolean orientation) {
    	if(x == null) 
    		return new Node(p, getRect(parent, p, orientation));
    	if(orientation == horizontal) {
    		if(p.x() < x.point.x())
    			x.lb = insert(x, x.lb, p, !orientation);
    		else
    			x.rt = insert(x, x.rt, p, !orientation);
    	}
    	else {
    		if(p.y() < x.point.y())
    			x.lb = insert(x, x.lb, p, !orientation);
    		else
    			x.rt = insert(x, x.rt, p, !orientation);
    	}	
    	return x;
    }
    
    private RectHV getRect(Node parent, Point2D p, boolean orientation) {
    	double minX, minY, maxX, maxY;
		minX = parent.rect.xmin();
		minY = parent.rect.ymin();	
		maxX = parent.rect.xmax();
		maxY = parent.rect.ymax();
		if(orientation == vertical) {
			if(p.x() < parent.point.x()) 
				maxX = parent.point.x();
    		else 
    			minX = parent.point.x();
		}
		else {
			if(p.y() < parent.point.y()) 
				maxY = parent.point.y();
			else 
				minY = parent.point.y();
		}
		return new RectHV(minX, minY, maxX, maxY);
    }
    
    /*
     * does the set contain point p?
     */
	public boolean contains(Point2D p) {
		return contains(root, p, horizontal);
	}
	
	private boolean contains(Node x, Point2D p, boolean orientation) {
		if(x == null) return false;
		if(orientation == horizontal){
			if(p.x() < x.point.x())
				return contains(x.lb, p, !orientation);
			else if(p.x() > x.point.x() || (p.x() == x.point.x() && p.y() != x.point.y()))
				return contains(x.rt, p, !orientation);
			else 
				return true;
		}
		else{
			if(p.y() < x.point.y())
				return contains(x.lb, p, !orientation);
			else if(p.y() > x.point.y() || (p.y() == x.point.y() && p.x() != x.point.x()))
				return contains(x.rt, p, !orientation);
			else 
				return true;
		}
	}
	
	/*
	 * draw all points to standard draw
	 */
	public void draw() {
		draw(root, null, horizontal);
	}
	
	private void draw(Node x, Node parent, boolean orientation) {
		StdDraw.setPenColor(StdDraw.BLACK);
		x.point.draw();
		
		if(orientation == horizontal) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(x.point.x(), x.rect.ymin(), x.point.x(), x.rect.ymax());
		}
		if(orientation == vertical) {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.point.y());
		}
		draw(x.lb, x, !orientation);
		draw(x.rt, x, !orientation);
	}
	
	/*
	 * all points that are inside the rectangle 
	 */
	public Iterable<Point2D> range(RectHV rect) {
		Stack<Point2D> res = new Stack<Point2D>();	
		return range(root, rect, res);
	}
	
	private Iterable<Point2D> range(Node x, RectHV rect, Stack<Point2D> res) {
		if(x == null) return null;
		if(rect.intersects(x.rect)) {
			if(rect.distanceTo(x.point) == 0) 
				res.push(x.point);
			range(x.lb, rect, res);
			range(x.rt, rect, res);
		}
		return res;
	}
		
	/*
	 * a nearest neighbor in the set to point p; null if the set is empty 
	 */
	private Point2D minPoint;
	private double minDist;
	public Point2D nearest(Point2D p) {
		minPoint = root.point;
		minDist = p.distanceTo(minPoint);
		nearest(root, p);
		return minPoint;
	}
	
	private void nearest(Node x, Point2D p) {
		double dist = p.distanceTo(x.point);
		if(dist < minDist) {
			minPoint = x.point;
			minDist = dist;
		}
		
		if(x.lb != null && x.rt != null) {
			double toLeftChild, toRightChild;
			toLeftChild = x.lb.rect.distanceTo(p);
			toRightChild = x.rt.rect.distanceTo(p);
			
			if(toLeftChild < toRightChild) {
				nearest(x.lb, p);
				if(toRightChild < minDist)
					nearest(x.rt, p);
			}
			else {
				nearest(x.rt, p);
				if(toLeftChild < minDist)
					nearest(x.lb, p);
			}	
			return;
		}
		
		if(x.lb != null) {
			if(x.lb.rect.distanceTo(p) < minDist) {
				nearest(x.lb, p);
			}
		}
		
		if(x.rt != null) {
			if(x.rt.rect.distanceTo(p) < minDist) {
				nearest(x.rt, p);
			}
		}
	}
}
