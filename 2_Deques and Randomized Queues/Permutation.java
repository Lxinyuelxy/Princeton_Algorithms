import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
	
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> que = new RandomizedQueue<String>();
		
		while(!StdIn.isEmpty()){
			String item = StdIn.readString();
			que.enqueue(item);
		}
		for(int i = 0; i < k; i++){
			StdOut.println(que.dequeue());
		}
	}
}
