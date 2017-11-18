import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

	private WordNet wordnet;
	private static final int MINMUM = Integer.MIN_VALUE;
	
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}
	
	public String outcast(String[] nouns) {
		int dt = MINMUM, dis;
		String result = null;
		for(int i = 0; i < nouns.length; i++){
			dis  = 0;
			for(int j = 0; j < nouns.length; j++) {
				dis += this.wordnet.distance(nouns[i], nouns[j]);
			}
			if (dis > dt) {
				dt = dis;
				result = nouns[i];
			}
		}
		return result;
	}
	
	public static void main (String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}	
	}
}
