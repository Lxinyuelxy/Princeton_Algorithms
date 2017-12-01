import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private Node root;
	private Bag<Integer>[] adj;
	private SET<String> validWords;
	private BoggleBoard board;
	private int rows, cols;
	private Stack<Integer> visitingDices;
	private boolean[] marked;
	
	public BoggleSolver(String[] dictionary) {
		root = new Node();
		for (int i = 0; i < dictionary.length; i++) {
			put(dictionary[i]);
		}
	}
	
	private static class Node {
		private int val = 0;
		private Node[] next = new Node[26];
	}
	
	private int get(String key) {
		Node x = get(root, key, 0);
		if (x == null) return 0;
		return x.val;
	}
	
	private Node get(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) return x;
		int c = key.charAt(d) - 'A';
		return get(x.next[c], key, d+1);
	}
	
	private void put(String key) {
		root = put(root, key, 0);
	}
	
	private Node put(Node x, String key, int d) {
		if (x == null) x = new Node();
		if (d == key.length()) {
			x.val = 1;
			return x;
		}
		int c = key.charAt(d) - 'A';
		x.next[c] = put(x.next[c], key, d+1);
		return x;
	}

	public Iterable<String> getAllValidWords(BoggleBoard board) {
		this.board = board;
		rows = board.rows();
		cols = board.cols();
		adj = (Bag<Integer>[]) new Bag[rows*cols];	
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int v = i*cols + j;
				adj[v] = new Bag<Integer>();
				if (checkWIsValid(i-1, j)) adj[v].add((i-1) * cols + j);
				if (checkWIsValid(i+1, j)) adj[v].add((i+1) * cols + j);
				if (checkWIsValid(i, j+1)) adj[v].add(i * cols + j+1);
				if (checkWIsValid(i, j-1)) adj[v].add(i * cols + j-1);
				if (checkWIsValid(i+1, j-1)) adj[v].add((i+1) * cols + j-1);
				if (checkWIsValid(i+1, j+1)) adj[v].add((i+1) * cols + j+1);
				if (checkWIsValid(i-1, j-1)) adj[v].add((i-1) * cols + j-1);
				if (checkWIsValid(i-1, j+1)) adj[v].add((i-1) * cols + j+1);
			}
		}
		validWords = new SET<String>();
		for (int v = 0; v < rows*cols; v++) {
			visitingDices = new Stack<Integer>();
			marked = new boolean[rows*cols];
			visitingDices.push(v);
			marked[v] = true;
			if(getLetterOnBoard(v) == 'Q')
				searchValidWords(v, root.next['Q'-'A'].next['U' - 'A'], "QU", visitingDices);
			else
				searchValidWords(v, root.next[getLetterOnBoard(v)-'A'], getLetterOnBoard(v) + "", visitingDices);
		}
		return validWords;
	}
	
	private boolean checkWIsValid(int i, int j) {
		return i>=0 && i<rows && j>=0 && j<cols;
	}
	
	private void searchValidWords(int v, Node x, String prefix, Stack<Integer> visitingDices) {
		if (prefix.length() > 2 && x != null && x.val == 1) {
			validWords.add(prefix);
		}	
		for (int w : adj[v]) {	
			char c = getLetterOnBoard(w);
			if (!marked[w] && x != null && x.next[c -'A'] != null) {
				visitingDices.push(w);
				marked[w] = true;
				if (c == 'Q') {	
					searchValidWords(w, x.next['Q'-'A'].next['U' - 'A'], prefix + "QU", visitingDices);
				}					
				else {
					searchValidWords(w, x.next[c -'A'], prefix + c, visitingDices);
				}
				int index = visitingDices.pop();
				marked[index] = false;			
			} 
		}
	}
	
	private char getLetterOnBoard(int v) {
		int i = v / cols;
		int j = v % cols;
		return board.getLetter(i, j);
	}
	
	public int scoreOf(String word) {
		if (get(word) == 0)
			return 0;
		else {
			int len = word.length();
			if (len <= 2) return 0;
			else if(len == 3 || len == 4) return 1;
			else if (len == 5) return 2;
			else if (len == 6) return 3;
			else if (len == 7) return 5;
			else return 11;
		}
	}
	
	public static void main(String[] args) {
	    In in = new In(args[0]);
	    String[] dictionary = in.readAllStrings();
	    BoggleSolver solver = new BoggleSolver(dictionary);
	    BoggleBoard board = new BoggleBoard(args[1]);
	    int score = 0;
	    for (String word : solver.getAllValidWords(board)) {
	        StdOut.println(word);
	        score += solver.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);
	}
}
