import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
	private static CircularSuffixArray suffix;
	private static final int R = 256;

	public static void transform() {
		String s = BinaryStdIn.readString();
		suffix = new CircularSuffixArray(s);
		int len = s.length();
		
		for (int i = 0; i < len; i++) {
			if (suffix.index(i) == 0) {
				BinaryStdOut.write(i);
				break;
			}
		}
		for (int i = 0; i < len; i++) {
			int index = (len - 1 + suffix.index(i)) % len;
			char c = s.charAt(index);
			BinaryStdOut.write(c);
		}
        	BinaryStdOut.close();
	}
	
	public static void inverseTransform() {
		int first = BinaryStdIn.readInt();
		char[] t = BinaryStdIn.readString().toCharArray();
		char[] sorted = new char[t.length];
		int[] count = new int[R+1];
        	int[] next = new int[t.length];

        	for (int i = 0; i < t.length; i++)
            		count[t[i] + 1]++;
        	for (int r = 0; r < R; r++)
            		count[r+1] += count[r];
        	for (int i = 0; i < t.length; i++) {
        		next[count[t[i]]] = i;
        		sorted[count[t[i]]++] = t[i];  
       	 	}       
		int p = first;
		for (int i = 0; i < t.length; i++) {
			BinaryStdOut.write(sorted[p]);
			p = next[p];
		}
		BinaryStdOut.close();
	}
	
	public static void main(String[] args) {
		if (args[0].equals("-")) transform();
        	else if (args[0].equals("+")) inverseTransform();
        	else throw new java.lang.IllegalArgumentException();
	}
}
