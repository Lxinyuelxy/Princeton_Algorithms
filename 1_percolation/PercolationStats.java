import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
	private double[] fraction;
	private double numOfMean;
	private double numOfStddev;
	private int T;
 
	public PercolationStats(int n, int trials){    // perform trials independent experiments on an n-by-n grid
		validate(n,trials);
   	 	fraction = new double[trials];
   	 	T = trials;
   	 	int row, col;
   	 	for(int i = 0; i < trials; i++){
   	 		Percolation per = new Percolation(n);
   	 		while(!per.percolates()){
   	 			row = StdRandom.uniform(n) + 1;
   	 			col = StdRandom.uniform(n) + 1;
   	 			per.open(row, col);
   	 		}
   	 		fraction[i] = per.numberOfOpenSites()*1.0/(n*n);
   	 	}
	}
	private void validate(int n, int trials) {
		if (n <= 0 || trials<= 0) {
			throw new java.lang.IllegalArgumentException("IllegalArgument");
		}
	}

	public double mean(){
		numOfMean = StdStats.mean(fraction);
		return numOfMean;
	}

	public double stddev(){
		numOfStddev = StdStats.stddev(fraction);
		return numOfStddev;  
	} 
 
	public double confidenceLo(){
		return numOfMean - 1.96*Math.sqrt(numOfStddev)/Math.sqrt(T);
	}

	public double confidenceHi(){
		return numOfMean + 1.96*Math.sqrt(numOfStddev)/Math.sqrt(T);
	}
 
	public static void main(String[] args) {
		int n = StdIn.readInt();
		int T = StdIn.readInt();
		PercolationStats perStats = new PercolationStats(n, T);
		StdOut.printf("mean                    = %.8f\n", perStats.mean());
		StdOut.printf("stddev                  = %.8f\n", perStats.stddev());
		StdOut.printf("95%% confidence interval = [%.16f, %.16f]", perStats.confidenceLo(),perStats.confidenceHi());
	}
}
