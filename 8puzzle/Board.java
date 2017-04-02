import edu.princeton.cs.algs4.Stack;

public class Board {
	
	private int[][] blocks;
	private int N;
	/*
	 * construct a board from an n-by-n array of blocks
	 * (where blocks[i][j] = block in row i, column j)
	 */
    public Board(int[][] b) {
    	N = b.length;
    	this.blocks = new int[N][N];
    	for(int i = 0; i < N; i++){
    		for(int j = 0; j < N; j++){
    			this.blocks[i][j] = b[i][j];
    		}
    	}
    }
    
    /*
     * board dimension n
     */
    public int dimension() {
    	return N;
    }
    
    /*
     * number of blocks out of place
     */
    public int hamming(){
    	int count = 0;
    	for(int i = 1; i < N*N; i++) {
    		int x = (i - 1) / N;
    		int y = (i - 1) % N;
    		if(i != blocks[x][y]) 
    			count++;
    	}
    	return count;
    }
    
    /*
     * sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
    	int sumDis = 0;
    	for(int i = 0; i < N; i++){
    		for(int j = 0; j < N; j++){
    			if(blocks[i][j] == 0);
    			else{
    				int x = (blocks[i][j] - 1) / N;
        			int y = (blocks[i][j] - 1) % N;
        			sumDis = sumDis + Math.abs(x - i) + Math.abs(y - j);
    			}	
    		}
    	}
    	return sumDis;
    }
    
    /*
     * is this board the goal board?
     */
    public boolean isGoal() {             
    	for(int i = 0; i < N; i++){
    		for(int j = 0; j < N; j++){
    			if(i == N-1 && j == N-1) break;
    			if(blocks[i][j] != i*N+j+1) return false;
    		}
    	}
    	return true;
    }
    
    /*
     * a board that is obtained by exchanging any pair of blocks
     */
    public Board twin() {
    	Board board = new Board(copyBlocks());
    	int i, j = 0;
    	for(i = 0; i < N; i++){
    		for(j = 0; j < N - 1; j++){
    			if(this.blocks[i][j] != 0 && this.blocks[i][j+1] != 0) break;
    		}
    		if(j < N-1) break;
    	}
    	int swap;
    	swap = board.blocks[i][j];
    	board.blocks[i][j] = board.blocks[i][j+1];
    	board.blocks[i][j+1] = swap;
    	return board;
    }
    
    /*
     * does this board equal y?
     */
    public boolean equals(Object y) { 
    	if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        
    	Board that = (Board) y;
    	if(that.N != this.N)
    		return false;
    	else{
    		for(int i = 0; i < N; i++){
        		for(int j = 0; j < N; j++){
        			if(this.blocks[i][j] != that.blocks[i][j]) return false;
        		}
        	}
        	return true;
    	}	
    }
    
    /*
     * all neighboring boards
     */
    public Iterable<Board> neighbors() {
    	int i, j = 0;
    	Stack<Board> boards = new Stack<Board>();
    	for(i = 0; i < N; i++){
    		for(j = 0; j < N; j++){
    			if(this.blocks[i][j] == 0) break;
    		}
    		if(j < N) break;
    	}
    	if(i > 0){
    		Board neighbor = new Board(copyBlocks());
    		neighbor.blocks[i][j] = neighbor.blocks[i-1][j];
    		neighbor.blocks[i-1][j] = 0;
    		boards.push(neighbor);
    	}
    	if(i < N-1){
    		Board neighbor = new Board(copyBlocks());
    		neighbor.blocks[i][j] = neighbor.blocks[i+1][j];
    		neighbor.blocks[i+1][j] = 0;
    		boards.push(neighbor);
    	}
    	if(j > 0){
    		Board neighbor = new Board(copyBlocks());
    		neighbor.blocks[i][j] = neighbor.blocks[i][j-1];
    		neighbor.blocks[i][j-1] = 0;
    		boards.push(neighbor);
    	}
    	if(j < N-1){
    		Board neighbor = new Board(copyBlocks());
    		neighbor.blocks[i][j] = neighbor.blocks[i][j+1];
    		neighbor.blocks[i][j+1] = 0;
    		boards.push(neighbor);
    	}
    	return boards;
    }
    
   private int[][] copyBlocks(){
	   int[][] arr = new int[N][N];
	   for(int i = 0; i < N; i++){
		   for(int j = 0; j < N; j++){
			   arr[i][j] = blocks[i][j];
		   }
	   }
	   return arr;
   }
    
    /*
     * string representation of this board (in the output format specified below)
     */
    public String toString() {
    	StringBuilder s = new StringBuilder();
    	s.append(N + "\n");
    	for(int i = 0; i < N; i++){
    		for(int j = 0; j < N; j++){
    			s.append(String.format("%2d ", blocks[i][j]));
    		}
    		s.append("\n");
    	}
    	return s.toString();
    }

    /*
     * unit tests (not graded)
     */
    public static void main(String[] args) {
    	
    }
}
