import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	private static final int R = 256;
	private static char[] code;
	
	private static void initCode() {
		code = new char[R];
		for (int i = 0; i < R; i++) {
			code[i] = (char)i;
		}
	}

	public static void encode() {
		initCode();
		char[] input = BinaryStdIn.readString().toCharArray();
		for (int i = 0; i < input.length; i++) {
			char index = 0;
			while (input[i] != code[index])
				index++;
			BinaryStdOut.write(index);
			while (index > 0) {
				code[index] = code[--index];
			}
			code[0] = input[i];
		}
		BinaryStdOut.close();
	}
	
	public static void decode() {
		initCode();
		char[] input = BinaryStdIn.readString().toCharArray();
		for (int i = 0; i < input.length; i++) {
			int index = (int)input[i];
			char output = code[index];
			BinaryStdOut.write(output);
			while (index > 0) {
				code[index] = code[--index];
			}
			code[0] = output;	
		}
		BinaryStdOut.close();
	}
	
	public static void main(String[] args) {
		if (args[0].equals("-")) encode();
        	else if (args[0].equals("+")) decode();
        	else throw new java.lang.IllegalArgumentException();
	}
}
