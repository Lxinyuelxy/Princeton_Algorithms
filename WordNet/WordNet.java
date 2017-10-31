import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle ;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Queue;
import java.util.HashMap;

public class WordNet {
	private HashMap<Integer, String> idmap;
	private HashMap<String, Bag<Integer>> wordmap;
	
	private final Digraph digraph;
	private final SAP sap;
	private int V;

    public WordNet(String synsets, String hypernyms) {
	   In in_synsets = new In(synsets);
	   In in_hypernyms = new In(hypernyms);
	   idmap = new HashMap<Integer, String>();
	   wordmap = new HashMap<String, Bag<Integer>>();
       V = 0;

       while (in_synsets.hasNextLine()) {
           String line = in_synsets.readLine();
           String[] tokens = line.split(",");
           
           Integer id = Integer.parseInt(tokens[0]);
           idmap.put(id, tokens[1]);
           
           String[] nouns = tokens[1].split(" ");
           for (int i = 0; i < nouns.length; i++) {
               Bag<Integer> ids = new Bag<Integer>();
               if (this.wordmap.containsKey(nouns[i]))
                   ids = this.wordmap.get(nouns[i]);
               ids.add(id);
               this.wordmap.put(nouns[i], ids);
           }      
           this.V = this.V + 1;
       }
	  
	   this.digraph = new Digraph(this.V);
	   while (in_hypernyms.hasNextLine()) {
		   String line = in_hypernyms.readLine();
		   String[] fields = line.split(",");		   
		   int v = Integer.parseInt(fields[0]); 
		   for (int i = 1; i < fields.length; i++) {
			   int w = Integer.parseInt(fields[i]);
			   this.digraph.addEdge(v, w);
		   }
	   }
	   sap = new SAP(digraph);
	   checkCycle();
	   checkRoot();
   }

   public Iterable<String> nouns() {
	   return this.wordmap.keySet();
   }


   public boolean isNoun(String word) {
	   if (word == null)
		   throw new java.lang.IllegalArgumentException();
	   return wordmap.containsKey(word);
   }


   public int distance(String nounA, String nounB){
	   if (!isNoun(nounA) || !isNoun(nounB)) 
		   throw new java.lang.IllegalArgumentException();
	   
	   Iterable<Integer> v = wordmap.get(nounA);
	   Iterable<Integer> w = wordmap.get(nounB);
	   
	   return sap.length(v, w);
   }

   public String sap(String nounA, String nounB) {
	   if (!isNoun(nounA) || !isNoun(nounB)) 
		   throw new java.lang.IllegalArgumentException();
	   
	   Iterable<Integer> v = wordmap.get(nounA);
	   Iterable<Integer> w = wordmap.get(nounB);
	   
	   int ancestorID = sap.ancestor(v, w);
	   String ancestors = idmap.get(ancestorID);
	   return ancestors;
   }
   
   private void checkCycle() {
	   DirectedCycle finder = new DirectedCycle(digraph);
	   if (finder.hasCycle()) 
		   throw new java.lang.IllegalArgumentException();
   }
   
   private void checkRoot() {
       Digraph Gr = this.digraph.reverse();
       for (int v = 0; v < V; v++) {
           if (this.digraph.outdegree(v) == 0) {
               int count = 1;
               boolean[] marked = new boolean[V];
               marked[v] = true;
               Queue<Integer> visited = new Queue<Integer>();
               visited.enqueue(v);
               while (!visited.isEmpty()) {
                   int w = visited.dequeue();
                   for (int i : Gr.adj(w)) {
                       if (!marked[i]) {
                           marked[i] = true;
                           count++;
                           visited.enqueue(i);
                       }
                   }
               }
               if (count != V) throw new java.lang.IllegalArgumentException();
           }
       }
   }
}

