import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.awt.event.*;

public class GameOfLife {
	
	static JPanel panel;
	static JFrame frame;
	static JPanel panel2;
	static JFrame frame2;
	static JLabel label;
	public static int size = 1000;						// defines the size of the screen
    public static int numVals = (size/10)*(size/10);	// defines the number of cells on the screen
   	// defines the dimensions of the cellCount matrix
    public static int xdim = 100;
   	public static int ydim = 100;	
   	public static int celldim = 10;
   	public static int numTimes = 1;
   	public static int[] numAlive = new int[numTimes];
   	public static int LINE_LENGTH = 5000;
   	public static int INCREMENT;
	public static int BUFFER = 100;
	public static int HEIGHT = 10;
	public static int YAXISREP = 6000;
	public static BufferedImage bi;
	public static boolean bool = true;
	public static int num = 2000;
	
	// defines a matrix of all the cells
   	public static int[][] cellCount = new int[size/celldim][size/celldim];
   	
   	public GameOfLife()
   	{
   		// create a screen for the game
	    panel = new JPanel();
	    Dimension dim = new Dimension(size, size);
	    panel.setPreferredSize(dim);
	    frame = new JFrame();
	    frame.setSize(size, size);
	    Container contentPane = frame.getContentPane();
	    contentPane.add(panel);
	    frame.setVisible(true);
	    
	    	    
	    
	    // create screen for graph
		panel2 = new JPanel();
	    Dimension dim2 = new Dimension(LINE_LENGTH+BUFFER*2, size+BUFFER*2);
	    panel.setPreferredSize(dim2);
	    frame2 = new JFrame();
	    frame2.setSize(LINE_LENGTH+BUFFER*2, size+BUFFER*2);
	    Container contentPane2 = frame2.getContentPane();
	    contentPane2.add(panel2);
	    frame2.setVisible(true);
	    

   	}
   	
	public static void main(String[] args) throws InterruptedException{
		GameOfLife GOL = new GameOfLife();
		Graphics g = panel.getGraphics();
		Graphics x = panel2.getGraphics();  
	    // generate the random start screen
	    
	    for(int ix = 0; ix < xdim; ix++) {
        	for(int iy = 0; iy < ydim; iy++) {
        		g.drawRect(ix*celldim, iy*celldim, celldim, celldim);
        		if(Math.random() < Math.random()) {
	                	// Create a live cell
	                	g.setColor(Color.BLACK);
	                    g.fillRect(ix*celldim, iy*celldim, celldim, celldim);
	                    cellCount[iy][ix] = 1;
	                    //System.out.println("live cell is being placed at "+ix+","+iy);
                }
	            else {
	                	// Create a a dead cell
	                	g.setColor(Color.WHITE);
	                	g.fillRect(ix*celldim, iy*celldim, celldim, celldim);
	                	cellCount[iy][ix] = 0;
                }
        	}
       }
	    
	    updateNumAlive();
	    g.dispose();
	    
	    
	    /*
	     * Runs the Game of Life simulation 
	     */
	    
	    while (bool) {
	    //for (int i = 0; i < number; i++ ) {  
	    	
	        if (numTimes > num) {bool = false;}
	        Graphics l = panel.getGraphics();
		    liveDie(l);
		    updateNumAlive();
		    clearScreen(x);
		    updateGraph(x);
		    Thread.sleep(2000);
		    l.dispose();
		    numTimes++;
		    INCREMENT = LINE_LENGTH/numTimes;
	    }
	    // Create a video from all of the pngs that were generated, remove all the pngs, and play the video
	    makeVideo(); 
	    
	}
	
	public static void clearScreen( Graphics g ) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,LINE_LENGTH+BUFFER*2,size+BUFFER);
		g.setColor(Color.BLACK);
		// draw x-axis
		g.drawLine(BUFFER, size+BUFFER, LINE_LENGTH+BUFFER, size+BUFFER);
		// draw y-axis
		g.drawLine(BUFFER, BUFFER, BUFFER, size+BUFFER);
		// label axes
		g.setFont(new Font("default", Font.BOLD, 35));
		g.drawString("Number of Times the Simulation Has Been Run",(LINE_LENGTH+BUFFER*2)/4,size+BUFFER);
		g.drawString("6", BUFFER-20, BUFFER);
		g.drawString("5", BUFFER-20, BUFFER+size/6);
		g.drawString("4", BUFFER-20, BUFFER+size/3);
		g.drawString("3", BUFFER-20, BUFFER+size/2);
		g.drawString("2", BUFFER-20, BUFFER+2*size/3);
		g.drawString("1", BUFFER-20, BUFFER+5*size/6);
		g.drawString("0", BUFFER-20, size+BUFFER);
		g.setFont(new Font("default", Font.BOLD, 30));
		g.drawString("Num", 0, BUFFER+(size/2)-75);
		g.drawString("of", 15, BUFFER+(size/2)-25);
		g.drawString("Cells", 0, BUFFER+(size/2)+25);
		g.setFont(new Font("default", Font.ITALIC, 30));
		g.drawString("1000s", 0, BUFFER+(size/2)+75);
	}
	
	public static void updateNumAlive() {
		// calculate the number of cells that are currently black and add it to the numAlive array
		int[] tempArray = numAlive;
		numAlive = new int[numTimes];
		int sum = 0;
		for(int ix=0;ix<xdim;ix++){
        	for(int iy=0;iy<ydim;iy++){
        		sum+=cellCount[iy][ix];
        	}
		}
		for (int i = 0; i < numTimes; i++) {
			if (i == numTimes-1) {
				numAlive[i] = sum;
			}
			else {
				numAlive[i] = tempArray[i];
			}
		}
	}
	
	public static void updateGraph( Graphics g ) {
		double xtemp, ytemp;
		int xval, yval;
	    g.setColor(Color.BLACK);
		for (int i = 0; i < numTimes; i++) {
			ytemp = BUFFER+size-(size*((double)(numAlive[i])/YAXISREP));
			xtemp = (i*INCREMENT)+BUFFER;
			xval = (int)(xtemp);
			yval = (int)(ytemp);
			g.fillOval(xval, yval, HEIGHT, HEIGHT);
		}
	}
	
	public static int[][] getNeighbors()
    {
        int numSurround = 0;	// defines the number of live cells surrounding a particular cell
        int[][] neighborArray = new int[ydim][xdim];
        for(int ix=0;ix<xdim;ix++){
        	for(int iy=0;iy<ydim;iy++){
	            if (iy == 0 && ix == 0)
	            {
	            	numSurround+=(cellCount[iy][ix+1])+(cellCount[iy+1][ix])+(cellCount[iy+1][ix+1]);
	            	// only 3 cells can be checked in this case as the cell is in the top left corner
	            	// figure out if the cells to the right, right bottom diagonal, and bottom exist
	            }
	            else if (iy == 0 && ix == xdim-1)
	            {
	            	numSurround+=(cellCount[iy][ix-1])+(cellCount[iy+1][ix])+(cellCount[iy+1][ix-1]);
	            	// only 3 cells can be checked in this case as the cell is in the TOP RIGHT corner
	            	// figure out if the cells to the left, left bottom diagonal, and bottom exist
	            }
	            else if (iy == ydim-1 && ix == 0)
	            {
	            	numSurround+=(cellCount[iy][ix+1])+(cellCount[iy-1][ix])+(cellCount[iy-1][ix+1]);
	            	// only 3 cells can be checked in this case as the cell is in the BOTTOM LEFT corner
	            	// figure out if the cells to the right, right top diagonal, and top exist
	            }
	            else if (iy == ydim-1 && ix == xdim-1)
	            {
	            	numSurround+=(cellCount[iy][ix-1])+(cellCount[iy-1][ix])+(cellCount[iy-1][ix-1]);
	            	// only 3 cells can be checked in this case as the cell is in the BOTTOM RIGHT corner
	            	// figure out if the cells to the left, left top diagonal, and top exist
	            }
	            else if (ix == 0)
	            {
	            	numSurround+=(cellCount[iy][ix+1])+(cellCount[iy-1][ix])+(cellCount[iy-1][ix+1])+(cellCount[iy+1][ix])+(cellCount[iy+1][ix+1]);
	            	// only 5 cells can be checked in this case as the cell is on the LEFT side, but not a corner
	            	// figure out if the cells to the right, right top diagonal, right bottom diagonal, top, and bottom exist
	            }
	            else if (ix == xdim-1)
	            {
	            	numSurround+=(cellCount[iy][ix-1])+(cellCount[iy-1][ix])+(cellCount[iy-1][ix-1])+(cellCount[iy+1][ix])+(cellCount[iy+1][ix-1]);
	            	// only 5 cells can be checked in this case as the cell is on the RIGHT side, but not a corner
	            	// figure out if the cells to the left, left top diagonal, left bottom diagonal, top, and bottom exist
	            }
	            else if (iy == 0)
	            {
	            	numSurround+=(cellCount[iy][ix-1])+(cellCount[iy+1][ix-1])+(cellCount[iy][ix+1])+(cellCount[iy+1][ix+1])+(cellCount[iy+1][ix]);
	            	// only 5 cells can be checked in this case as the cell is on the TOP side, but not a corner
	            	// figure out if the cells to the left, left bottom diagonal, right, right bottom diagonal, and bottom exist
	            }
	            else if (iy == ydim-1)
	            {
	            	numSurround+=(cellCount[iy][ix-1])+(cellCount[iy-1][ix-1])+(cellCount[iy][ix+1])+(cellCount[iy-1][ix+1])+(cellCount[iy-1][ix]);
	            	// only 5 cells can be checked in this case as the cell is on the BOTTOm side, but not a corner
	            	// figure out if the cells to left, left top diagonal, right, right top diagonal, and top exist
	            }
	            else
	            {
	            	numSurround+=(cellCount[iy][ix-1])+(cellCount[iy-1][ix])+(cellCount[iy-1][ix-1])+(cellCount[iy+1][ix])+(cellCount[iy+1][ix-1])+(cellCount[iy][ix+1])+(cellCount[iy+1][ix+1])+(cellCount[iy-1][ix+1]);
	            	// regular case
	            	// check all 8 surrounding cells
	            }
	            // place all the value of numSurround in a matrix
	            neighborArray[iy][ix] = numSurround;
	            numSurround = 0;
        	}
        }
        return neighborArray;
    }
	
	
	
	/*
	 * Follow the rules:
	 * -----------------
	 * 
	 * If any live cell with fewer than two live neighbors, it dies.
	 * If any live cell with more than three live neighbors, it dies.
	 * Else if any dead cell with exactly three live neighbors becomes
	 * a live cell, and any live cell with 3 (or 2) neighbors stays alive.
	 */
 public static void liveDie(Graphics d) {
	 	// retrieve neighborArray from getNeighbors method to use in liveDie
    	int[][] neighborMatrix = getNeighbors();	
    	String str;
    	
		bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		
    	
    	for(int ix=0;ix<xdim;ix++){
        	for(int iy=0;iy<ydim;iy++){
        		/*
	    		 * If any live cell with fewer than two live neighbors, it dies.
	    		 * If any live cell with more than three live neighbors, it dies.
	    		 * 
	    		 * Else if any dead cell with exactly three live neighbors becomes
	    		 * a live cell, and any live cell with 3 (or 2) neighbors stays alive.
	    		 */
        		//g.setColor(Color.WHITE);
        		//g.fillRect(ix*celldim, iy*celldim, celldim, celldim);
        	    //d.setColor(Color.WHITE);
        		//d.fillRect(ix*celldim, iy*celldim, celldim, celldim);
        		if (neighborMatrix[iy][ix] < 2 || neighborMatrix[iy][ix] > 3)
	    		{
	    			// create a new dead cell in the place the old cell used to be
	    			// print to bi
	    			g.setColor(Color.WHITE);
	    			g.fillRect(ix*celldim, iy*celldim, celldim, celldim);
	    			// print to panel
	    			d.setColor(Color.WHITE);
	    			d.fillRect(ix*celldim, iy*celldim, celldim, celldim);
	    			// set the value in cellCount now to show up as dead
	    			cellCount[iy][ix] = 0;
	    		}
	    		else if (neighborMatrix[iy][ix] == 3)
	    		{
	    			// create a new live cell in the place the old cell used to be
	    			// print to bi
	    			g.setColor(Color.BLACK);
	    			g.fillRect(ix*celldim, iy*celldim, celldim, celldim);
	    			// print to panel
	    			d.setColor(Color.BLACK);
	    			d.fillRect(ix*celldim, iy*celldim, celldim, celldim);
	    			// set the value in cellCount now to show up as alive
	    			cellCount[iy][ix] = 1;
	    		}
        	}
    	}
    		
        	if (numTimes < 10) { str = "0";}
        	else { str = ""; }
        	
            // render the buffered image and save it in your files
            RenderedImage RI = (RenderedImage) bi;
            File f = new File("/home/holly/Desktop/Holly/Ames/timestep"+str+numTimes+".png");
            try {
    			ImageIO.write(RI, "PNG", f);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
 		}
 
 
 public static void makeVideo() {
	// doesn't seem to have permission to remove or execute files
    String command = "rm GOL.mp4 & ffmpeg -i timestep%02d.png GOL.mp4 && rm timestep*.png && xdg-open GOL.mp4";
	String[] path = {"PATH=$PATH"};
	File fwkrdir = new File("/home/holly/Desktop/Holly/Ames");
    Process proc=null;
    
	try {
		proc = Runtime.getRuntime().exec(command,path,fwkrdir);
		System.out.println("MOVIE MADE");
	} catch (IOException e) {
		e.printStackTrace();
	}
	
    BufferedReader reader =  
          new BufferedReader(new InputStreamReader(proc.getInputStream()));

    String line = "";
    try {
		while((line = reader.readLine()) != null) {
		     System.out.print(line + "\n");
		 }
	} catch (IOException e) {
		e.printStackTrace();
	}
    
    try {
		proc.waitFor();
		System.out.println("waiting");
	} catch (InterruptedException e) {
		e.printStackTrace();
	}   
    System.out.println("done waiting");
 }
}
