import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph digraph;
    
    /*
     *  constructor takes a digraph (not necessarily a DAG)
     */
    public SAP(Digraph G) {
        digraph = G;
    }

    /*
     *  length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        validate(v);
        validate(w);

        BreadthFirstDirectedPaths vpaths = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wpaths = new BreadthFirstDirectedPaths(digraph, w);

        return sapHelper(vpaths, wpaths, true);
    }

    /*
     *  a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        validate(v);
        validate(w);

        BreadthFirstDirectedPaths vpaths = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wpaths = new BreadthFirstDirectedPaths(digraph, w);

        return sapHelper(vpaths, wpaths, false);
    }

    /*
     *  length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.IllegalArgumentException();
        validate(v);
        validate(w);

        BreadthFirstDirectedPaths vpaths = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wpaths = new BreadthFirstDirectedPaths(digraph, w);

        return sapHelper(vpaths, wpaths, true);
    }

    /*
     *  a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new java.lang.IllegalArgumentException();
        validate(v);
        validate(w);

        BreadthFirstDirectedPaths vpaths = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wpaths = new BreadthFirstDirectedPaths(digraph, w);

        return sapHelper(vpaths, wpaths, false);
    }

    private void validate(int v) {
        if (v < 0 || v > digraph.V() - 1) throw new java.lang.IllegalArgumentException();
    }

    private void validate(Iterable<Integer> v) {
        for (Integer i : v) {
            validate(i);
        }
    }

    private int sapHelper(BreadthFirstDirectedPaths vpaths, BreadthFirstDirectedPaths wpaths, boolean length) {
        int minlen = INFINITY;
        int ancestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (vpaths.hasPathTo(i) && wpaths.hasPathTo(i)) {
                int vlen = vpaths.distTo(i);
                int wlen = wpaths.distTo(i);
                if (vlen + wlen < minlen) {
                    minlen = vlen + wlen;
                    ancestor = i;
                }
            }
        }
        if (length) 
        	return minlen < INFINITY ? minlen : -1;
        else 
        	return ancestor;
    }

   /*
    *  do unit testing of this class
    */
   public static void main(String[] args) {
    In in = new In(args[0]);
     Digraph G = new Digraph(in);
     SAP sap = new SAP(G);
     while (!StdIn.isEmpty()) {
         int v = StdIn.readInt();
         int w = StdIn.readInt();
         int length   = sap.length(v, w);
         int ancestor = sap.ancestor(v, w);
         StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
     }
   }
}