import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * Percolation class 
 * - Programming assignment 1 for Princeton Algorithms 1 coursera course 
 * Solution by Deirdre Meehan in September 2018
 * 
 * This class uses unionFind to detect Percolation + does a Monte Carlo simulation to 
 * experimentally find percolation threshold
 * 
 */
public class Percolation {

    private boolean[] grid;
    private final int gridSize;
    private final int gridSquareSize;
    private final int unionFindSize;
    private final WeightedQuickUnionUF unionFind;
    private final int virtualTopIndex;
    private final int virtualBottomIndex;
	
    private void initialiseEmptyGrid() {
        grid = new boolean[unionFindSize];
        for(int i = 0; i < grid.length; i++){
            grid[i] = false;
        }
        grid[virtualTopIndex] = true;
        grid[virtualBottomIndex] = true;
    }
	
	private int rowAndColToGridIndex(int row, int col) 
	{
		if(row > gridSize || col > gridSize){
			return -1;
		}
		return ((row - 1)* gridSize + col-1);	
	}
	private boolean hasLeft(int col) {
		return col != 1;
	}
	
	private boolean hasRight(int col) {
		return col != gridSize;
	}
	
	//API METHODS
	public void open(int row, int col)    // open site (row, col) if it is not open already
	{
		int indexToOpen = rowAndColToGridIndex(row, col);
		safelyOpenCell(indexToOpen);
		
		//openAbove
		if(isValidCell(row - 1, col) && isOpen(row - 1, col)){
			safelyUnion(indexToOpen, indexToOpen - gridSize);
		}
		//openBelow
		if(isValidCell(row + 1, col) && isOpen(row + 1, col)){
			safelyUnion(indexToOpen, indexToOpen + gridSize);
		}
		//openLeft
		if(isValidCell(row, col - 1) && isOpen(row, col - 1) && hasLeft(col)){
			safelyUnion(indexToOpen, indexToOpen - 1);
		}
		//openRight
		if(isValidCell(row, col + 1) && isOpen(row, col + 1) && hasRight(col)){
			safelyUnion(indexToOpen, indexToOpen + 1);
		}		
		//virtualTop
		if(indexToOpen < gridSize) {
			safelyUnion(indexToOpen, virtualTopIndex);
		}
		if(indexToOpen >= gridSquareSize - gridSize){
			safelyUnion(indexToOpen, virtualBottomIndex);
		}
	}
	
	private void safelyOpenCell(int indexToOpen) 
	{
		if(isValidCell(indexToOpen)){
			grid[indexToOpen] = true;
		}
		else{
			throw new IllegalArgumentException(String.format("row index %s out of bounds", indexToOpen));
		}
	}
	
	private boolean isValidCell(int index) 
	{
		return index >= 0 && index < gridSquareSize;
	}
	
	private boolean isValidCell(int row, int col) 
	{
		int index = rowAndColToGridIndex(row, col);
		return isValidCell(index);
	}
	
	private void safelyUnion(int p, int q) 
	{
		if(p >= 0 && p < unionFindSize && q >= 0 && q < unionFindSize){
			unionFind.union(p, q);
		}
	}
	
   public boolean isOpen(int row, int col)  //is site (row, col) open?
   {
	   int index = rowAndColToGridIndex(row, col);
	   if(isValidCell(index)){
		   return grid[index];
	   }
	   throw new IllegalArgumentException();
	   //throw new IllegalArgumentException(String.format("cell %s, %s which gave row index %s out of bounds", row, col, index));
   }
	   
   
   public boolean isFull(int row, int col)  //is site (row, col) full?
   {
	   //is open and percolates through this cell
	   if (!isOpen(row, col)){
		   return false;
	   }
	   int index = rowAndColToGridIndex(row, col);
	   return unionFind.connected(virtualTopIndex, index); //&& unionFind.connected(virtualBottomIndex, index);
   }
	   
   public int numberOfOpenSites()       //number of open sites
   {
	   int numOpenSites = 0;
	   //shouldn't use loop here -> warning in submission
	   for(int i = 0; i < gridSquareSize; i++){
		   if(grid[i]){
			   numOpenSites++;
		   }
	   }
	   return numOpenSites;
   }
	   
   public boolean percolates()              //does the system percolate?
   {
	   return unionFind.connected(virtualTopIndex, virtualBottomIndex);
   }

   public Percolation(int n){
       if(n <= 0 ){
           throw new IllegalArgumentException();
       }

       gridSize = n;
       gridSquareSize = n*n;

       unionFindSize = gridSquareSize + 2;
       virtualTopIndex = gridSquareSize;
       virtualBottomIndex = gridSquareSize + 1;
       initialiseEmptyGrid();
       unionFind = new WeightedQuickUnionUF(unionFindSize);
    }

    public static void main(String[] args) {
        int n = 5;
	    Percolation p = new Percolation(n);
		int row, column;
		int maxTries = n*n;
		int numTries = 0;
		
		while(numTries < maxTries && !p.percolates()){
			row = StdRandom.uniform(1, n+1);
			column = StdRandom.uniform(1, n+1);
			System.out.println("Opening (" + row + ", " + column + ")");
			p.open(row,  column);
			numTries++;
		}
		
		System.out.println("System percolated? " + p.percolates());
		System.out.println("Num open sites: " + p.numberOfOpenSites());
		System.out.println("Number of openings: " + numTries);
	}
}
