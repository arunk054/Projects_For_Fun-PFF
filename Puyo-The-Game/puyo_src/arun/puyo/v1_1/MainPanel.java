/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 28-4-2007 
 */

package arun.puyo.v1_1;

import java.awt.*;

import javax.swing.*;

/*THis is the class in which you can have all the panels 
 * that you want to have in the game*/
public class MainPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GamePanel gp;
	
	public MainPanel()
	{
		super();
		this.setLayout(new GridLayout(1,1));
		gp=new GamePanel();
		this.add(gp);
	}
	
	public GamePanel getGamePanel()
	{
		return gp;
	}
}
