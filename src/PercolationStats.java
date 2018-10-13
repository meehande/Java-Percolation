import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	
	private int numTrials;
	private int gridSize;
	private double[] trialResults;
	private static final double CONFIDENCE_THRESHOLD = 1.96;
	private double mean;
	private double confidenceLo;
	private double confidenceHigh;
	private double stdDev;
	
   public PercolationStats(int n, int trials)    //perform trials independent experiments on an n-by-n grid
   {
	   if(n <= 0 || trials <= 0){
		   throw new IllegalArgumentException();
	   }
	   numTrials = trials;
	   gridSize = n;
	   trialResults = new double[trials];
	   
	   for(int i = 0; i<numTrials; i++){
		   RunTrial(gridSize, i);
	   }
	   
   }
   
   
   private void RunTrial(int gridSize, int trialNumber){
	   
	   Percolation p = new Percolation(gridSize);
	   int row, column;
	   boolean percolates = false;
	   while(!percolates){
			row = StdRandom.uniform(1, gridSize+1);
			column = StdRandom.uniform(1, gridSize+1);
			p.open(row,  column);
			percolates = p.percolates();
		}
	   trialResults[trialNumber] = percolationThreshold(gridSize*gridSize, p.numberOfOpenSites());
   }
   
   public double mean()                        //sample mean of percolation threshold
   {
	   return StdStats.mean(trialResults);
   }
   public double stddev() //sample standard deviation of percolation threshold
   {
	   return StdStats.stddev(trialResults);
	   
   	}
   public double confidenceLo() //low  endpoint of 95% confidence interval
   {
	   double mean = mean();
	   double std = stddev();
	   
	   return mean - ((CONFIDENCE_THRESHOLD * std )/Math.sqrt(numTrials));
   }
   public double confidenceHi() //high endpoint of 95% confidence interval
   {
	   double mean = mean();
	   double std = stddev();
	   
	   return mean + ((CONFIDENCE_THRESHOLD * std )/ Math.sqrt(numTrials));
   }

   
   
   private double percolationThreshold(double totalNumSites, double numOpenToPercolate) {
	   return numOpenToPercolate / totalNumSites;
   }
   
   public static void main(String[] args) //test client (described below)
   {
	   int gridSize = Integer.parseInt(args[0]);
	   int numTrials = Integer.parseInt(args[1]);
	   PercolationStats percStats = new PercolationStats(gridSize, numTrials);
	   
	   System.out.println("mean \t\t\t = \t" + percStats.mean());
	   System.out.println("stddev \t\t\t = \t" + percStats.stddev());
	   System.out.println("95% confidence interval \t = \t[" +  percStats.confidenceLo() + ", " + percStats.confidenceHi()+ "]");  
   }
}
