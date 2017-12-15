public class CircularSuffixArray {
	private String s;
	private int[] index;
	private int len;
	private static final int R = 256;

	public CircularSuffixArray(String s) {
		if (s == null) throw new java.lang.IllegalArgumentException();
		this.s = s;
		len = s.length();
		index = new int[len];
		for (int i = 0; i < len; i++) index[i] = i;
		sort(len);
	}
	
	private void sort(int w) { //fixed-length W strings
		int[] aux = new int[len];
		
		for (int d = w-1; d >= 0; d--) {
			int[] count = new int[R+1];
			for (int i = 0; i < len; i++)
				count[s.charAt((index[i]+d) % len) + 1]++;
			for (int r = 0; r < R; r++) 
				count[r+1] += count[r];
			for (int i = 0; i < len; i++)
				aux[count[s.charAt((index[i]+d) % len)]++] = index[i];
			for (int i = 0; i < len; i++)
				index[i] = aux[i];	
		}
	}

	public int length() {
		return len;
	}
	
	public int index(int i) {
		if (i >= 0 && i < len) return index[i];
		else throw new java.lang.IllegalArgumentException();	
	}
	
	public static void main(String[] args) {
		CircularSuffixArray suffix = new CircularSuffixArray("ABRACADABRA!");
		System.out.println(suffix.length());
        	for (int i = 0; i < suffix.length(); i++) {
            		System.out.println(suffix.index(i));
        	}		
	}
}
