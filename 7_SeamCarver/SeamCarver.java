import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
	//private Picture currentPic;
	private double[][] energyMap;
	private int[][] edgeTo;
	private double[][] distTo;
	private static final int VERTICAL = 1;
	private static final int HORIZONTAL = 0;
	private int w, h;
	private Color pixelColor[][];

	public SeamCarver(Picture picture) {
		if(picture == null)
			throw new java.lang.IllegalArgumentException();
		//this.currentPic = picture;
		w = picture.width();
		h = picture.height();	
		pixelColor = new Color[w][h];		
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				pixelColor[x][y] = picture.get(x, y);
			}
		}
		init_energyMap();
	}
	
	private void init_energyMap() {
		energyMap = new double[w][h];
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				energyMap[x][y] = this.energy(x, y);
			}
		}
	}
	
	private void init() {
		edgeTo = new int[w][h];
		distTo = new double[w][h];
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				if(y == 0) 
					distTo[x][y] = energyMap[x][y];
				else 
					distTo[x][y] = Double.POSITIVE_INFINITY;
			}
		}
	}
	
	private void reverse_init() {
		edgeTo = new int[w][h];
		distTo = new double[w][h];
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				if(x == 0) 
					distTo[x][y] = energyMap[x][y];
				else
					distTo[x][y] = Double.POSITIVE_INFINITY;				
			}
		}
	}
	
	public Picture picture() {
		Picture pic = new Picture(w, h);
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				pic.set(x, y, this.pixelColor[x][y]);
			}
		}
		return pic;
	}
	
	public int width() {
		return w;
	}
	
	public int height() {
		return h;
	}
	
	public double energy(int x, int y) {
		if(x<0 || y<0 || x>=w || y>=h)
			throw new java.lang.IllegalArgumentException();
		if(x==0 || y==0 || x==w-1 ||y==h-1)
			return 1000;
		Color pre = pixelColor[x-1][y];
		int pre_r = pre.getRed();
		int pre_g = pre.getGreen();
		int pre_b = pre.getBlue();
			
		Color after = pixelColor[x+1][y];	
		int after_r = after.getRed();
		int after_g = after.getGreen();
		int after_b = after.getBlue();
		
		Color up = pixelColor[x][y-1];
		int up_r = up.getRed();
		int up_g = up.getGreen();
		int up_b = up.getBlue();
		
		Color below = pixelColor[x][y+1];	
		int below_r = below.getRed();
		int below_g = below.getGreen();
		int below_b = below.getBlue();
		
		double squareX = Math.pow((pre_r-after_r), 2) + Math.pow((pre_g-after_g), 2) + Math.pow((pre_b-after_b), 2);
		double squareY = Math.pow((up_r-below_r), 2) + Math.pow((up_g-below_g), 2) + Math.pow((up_b-below_b), 2);
		return Math.sqrt(squareX + squareY);
	}
	
	public int[] findHorizontalSeam() {
		reverse_init();
		for(int x = 0; x < this.width()-1; x++) {
			for(int y = 0; y < this.height(); y++) {
				relax(x, y, x+1, y, HORIZONTAL);
				if(y > 0)
					relax(x, y, x+1, y-1, HORIZONTAL);
				if(y < this.height()-1)
					relax(x, y, x+1, y+1, HORIZONTAL);
			}
		}
		return findHorizontalSP();
	}
	
	public int[] findVerticalSeam() {
		init();
		for(int y = 0; y < this.height()-1; y++) {
			for(int x = 0; x < this.width(); x++) {
				relax(x, y, x, y+1,  VERTICAL);
				if(x > 0)
					relax(x, y, x-1, y+1, VERTICAL);
				if(x < this.width()-1)
					relax(x, y, x+1, y+1, VERTICAL);
			}
		}	
		return findVerticalSP();	
	}
	
	private void relax(int fromX, int fromY, int toX, int toY, int direction) {
		if(distTo[toX][toY] > distTo[fromX][fromY] + energyMap[toX][toY]) {
			distTo[toX][toY] = distTo[fromX][fromY] + energyMap[toX][toY];
			if(direction == VERTICAL)
				edgeTo[toX][toY] = fromX;
			if(direction == HORIZONTAL)
				edgeTo[toX][toY] = fromY;
		}
	}
	
	private int[] findHorizontalSP() {
		double minDis = distTo[this.width()-1][0];
		int index = 0;
		for(int y = 1; y < this.height(); y++) {
			if(distTo[this.width()-1][y] < minDis) {
				minDis = distTo[this.width()-1][y];
				index = y;
			}
		}
		int[] result = new int[this.width()];
		result[this.width()-1] = index;
		for(int i = this.width()-1; i  > 0; i--) {
			result[i-1] = edgeTo[i][index];
			index = edgeTo[i][index];
		}
		return result;
	}
	
	private int[] findVerticalSP() {
		double minDis = distTo[0][this.height()-1];
		int index = 0;
		for(int x = 1; x < this.width(); x++) {
			if(distTo[x][this.height()-1] < minDis) {
				minDis = distTo[x][this.height()-1];
				index = x;
			}
		}
		int[] result = new int[this.height()];
		result[this.height()-1] = index;
		for(int i = this.height()-1; i > 0; i--) {
			result[i-1] = edgeTo[index][i];
			index = edgeTo[index][i];
		}
		return result;
	}
		
	public void removeHorizontalSeam(int[] seam) {
		if(seam == null || seam.length != this.width() || this.height() <= 1)
			throw new java.lang.IllegalArgumentException();
		
		Color[][] newPixelColor = new Color[w][h-1];
		for(int x = 0; x < this.width(); x++) {
			if (seam[x] < 0 || seam[x] >= this.height() || (x > 0 && Math.abs(seam[x] - seam[x - 1]) > 1))
		        throw new IllegalArgumentException();
			for(int y = 0; y < this.height(); y++) {
				if(y < seam[x])
					newPixelColor[x][y] = pixelColor[x][y];
				else if(y > seam[x])
					newPixelColor[x][y-1] = pixelColor[x][y];		
			}
		}
		this.pixelColor = newPixelColor;
		h--;
		init_energyMap();
	}
	
	public void removeVerticalSeam(int[] seam) {
		if(seam == null || seam.length != this.height() || this.width() <= 1)
			throw new java.lang.IllegalArgumentException();
		Color[][] newPixelColor = new Color[w-1][h];
		for(int y = 0; y < this.height(); y++) {
			if (seam[y] < 0 || seam[y] >= this.width() || (y > 0 && Math.abs(seam[y] - seam[y - 1]) > 1)) 
		        throw new java.lang.IllegalArgumentException ();
			for(int x = 0; x < this.width(); x++) {
				if(x < seam[y])
					newPixelColor[x][y] = pixelColor[x][y];
				else if(x > seam[y])
					newPixelColor[x-1][y] = pixelColor[x][y];
			}
		}
		this.pixelColor = newPixelColor;
		w--;
		init_energyMap();
	}
}
