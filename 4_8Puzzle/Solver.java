import edu.princeton.cs.algs4.MinPQ; 
import edu.princeton.cs.algs4.Queue;

public class Solver {
	private boolean isSolverable;
	private Queue<Board> resBoards = new Queue<Board>();
	
	private class Node implements Comparable<Node>{
		Board board;
		int move;
		Node previous;
		
		public Node(Board board, int move, Node previous){
			this.board = board;
			this.move = move;
			this.previous = previous;
		}
		
		@Override
		public int compareTo(Node that) {
			if(this.getPriority() > that.getPriority()) 
				return 1;
			else if(this.getPriority() < that.getPriority())
				return -1;
			else
				return 0;
		}
		
		private int getPriority(){
			return move + board.manhattan();
		}	
	}

	/*
	 * find a solution to the initial board (using the A* algorithm)
	 */
    public Solver(Board initial) {
    	MinPQ<Node> minPQ = new MinPQ<Node>();
    	MinPQ<Node> minPQTwin = new MinPQ<Node>();
    	minPQ.insert(new Node(initial, 0, null));
    	minPQTwin.insert(new Node(initial.twin(), 0, null));
    	resBoards.enqueue(initial);
    	
    	if(initial.isGoal()) {
    		isSolverable = true;		
    		return;
    	}
    	else if(initial.twin().isGoal()){
    		isSolverable = false;
    		return;
    	}
    	
    	while(true){
    		Node node = minPQ.delMin();
    		if(node.board.isGoal()) {
    			isSolverable = true;
    			while(node.previous != null) {
    				node = node.previous;
    				resBoards.enqueue(node.board);
    			}
    			return;
    		}else{
    			node.move++;
    			Iterable<Board> neighbors = node.board.neighbors();
        		for(Board b : neighbors) {
        			if(node.previous != null && b.equals(node.previous.board));
        			else
        				minPQ.insert(new Node(b, node.move, node));
        		}
    		}
    		
    		Node nodeTwin = minPQTwin.delMin();
    		if(nodeTwin.board.isGoal()){
    			isSolverable = false;
    			return;
    		}
    		else{
    			nodeTwin.move++;
    			Iterable<Board> neighborsTwin = nodeTwin.board.neighbors();
        		for(Board bt : neighborsTwin){
        			if(nodeTwin.previous != null && bt.equals(nodeTwin.previous.board));
        			else
        				minPQTwin.insert(new Node(bt, nodeTwin.move++, nodeTwin));
        		}
    		}	
    	}
    }
    
    /*
     * is the initial board solvable?
     */
    public boolean isSolvable()  {
    	return isSolverable;
    }
    
    /*
     * min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
    	if(!isSolverable)
    		return -1;
    	else
    		return resBoards.size() - 1;
    }
    
    /*
     * sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
    	if(isSolverable) 
    		return resBoards;
    	else 
    		return null;
    }
    
    /*
     * solve a slider puzzle (given below)
     */
    public static void main(String[] args) {
    	
    }
}
