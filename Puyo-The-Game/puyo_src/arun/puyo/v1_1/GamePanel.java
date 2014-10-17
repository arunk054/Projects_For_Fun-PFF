/****************************************
 * 
 * GamePanel.java
 * 
 * @author ARUN
 *
 *Created on 28-4-2007 
 */

package arun.puyo.v1_1;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*As the name says this is the Panel which has the game,
* it has the UI objects like PuyoManager*/
public class GamePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6386905433134514827L;

	MoveThread moveThread;//initialized in startGame
	//private MoveTimer moveTimer;//Initialized in start game
	
	//There is a reason why this is kept as static,your guess at this as good as min
	private static int width,height;
	
	private PuyoManager pm;
	private BufferedImage buffer;//The whole image is first drawn on this and then this is put on screen
	
	static boolean gameStarted=false,gamePaused=false,isAnimate=true;
	static String[] introString={"","","Press Enter to start Game"};
	
	public GamePanel()
	{
		super();
		//Create the Puyo Manager
		pm = new PuyoManager(this);		
		initialize();
		//add the listener for keys
		addKeyListener(new PlayerListner(this));
	}
	
	public void initialize()
	{
		width=PuyoManager.getPuyoWidth()*AppConsts.MAX_COLS;
		height=PuyoManager.getPuyoHeight()*(AppConsts.MAX_ROWS);
		//Set the size of this panel
		setPreferredSize(new Dimension(width,height));
		//create the buffer
		buffer=new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
				
	}
	
	public static int getGamePanelWidth()
	{
		return width;
	}
	public static int getGamePanelHeight()
	{
		return height;
	}	
	
	public void startGame()
	{
		//Create a Thread for constantly moving the puyos
		moveThread = new MoveThread (this);

		//Use a Swing TImer instead of Thread to move the Puyos
		//But Not using this because looks like this is consuming on the event dispatch thread
		//moveTimer=new MoveTimer(1000/AppConsts.MOVES_PER_SEC,new MoveTimerListner(this));
		
	}
	public void callPaint()
	{
		//I just found that this method doesnt call the paint immediately as we wud need
		//I really dont know whho on earth wud want to use this
		//repaint();
		paint(this.getGraphics());
		
	}
	//This method is for double buffering technique
	public void paintBuffer()
	{
		Graphics g=(Graphics) buffer.getGraphics();
		//first drawing the background
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);	
		//Then drawing the Puyos
		if(GamePanel.gameStarted)
		{
			pm.getPuyoPicture(g,this);
		}
		else
		{
			Color c=g.getColor();
			g.setColor(Color.black);
			g.drawString(GamePanel.introString[0],25,GamePanel.getGamePanelHeight()/2-20);	
			g.drawString(GamePanel.introString[1],25,GamePanel.getGamePanelHeight()/2);	
			g.drawString(GamePanel.introString[2],20,GamePanel.getGamePanelHeight()/2+20);	
			g.setColor(c);
		}

		
	}
	
	public void paint(Graphics g)
	{
		//super.paintComponent(g);
		paintBuffer();
		g.drawImage(buffer,0,0,this);
	}


	public PuyoManager getPuyoManager()
	{
		return pm;
	}
	
	public static boolean getGameOver()
	{
		return !gameStarted;	
	}
	
}
