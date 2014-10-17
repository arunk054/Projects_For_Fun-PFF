/****************************************
 *AppConsts.java 
 * 
 * @author ARUN
 *
 *Created on 30-4-2007
 * 
 */

package arun.puyo.v1_1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerListner implements KeyListener {

	GamePanel gp;
	private long startTime;
	public PlayerListner(GamePanel gp)
	{
		startTime=System.currentTimeMillis();
		this.gp=gp;
		gp.requestFocus();
		gp.setFocusable(true);
	}
	public void keyPressed(KeyEvent e) {//Just trying out if the keyReleased method is enuf
		// TODO Auto-generated method stub
        if(System.currentTimeMillis()-startTime<AppConsts.KEYPRESS_TIMEOUT)
        {
            return;
        }
        startTime=System.currentTimeMillis();
        
		if(!GamePanel.gamePaused && GamePanel.gameStarted && !gp.getPuyoManager().needsPuyoCreation())
		{
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_DOWN:
					gp.getPuyoManager().movePuyoDown(gp);//for this callPaint done inside pm
					break;			
				case KeyEvent.VK_LEFT:
					gp.getPuyoManager().movePuyoLeft();
					gp.callPaint();
					break;
				case KeyEvent.VK_RIGHT:
					gp.getPuyoManager().movePuyoRight();
					gp.callPaint();
					break;	
				case KeyEvent.VK_UP:
					gp.getPuyoManager().rotatePuyo();
					gp.callPaint();
					break;				
				default:	
					//MyLogger.log("Error in detecting the key "+e.getKeyCode());
					break;
			}
		}
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_ESCAPE:
			if(GamePanel.gameStarted){
				if(!GamePanel.gamePaused)
				{
					gp.moveThread.suspend();
					GamePanel.gamePaused=true;
				}
				else
				{
					gp.moveThread.resume();
					GamePanel.gamePaused=false;
				}
			}
			break;
		case KeyEvent.VK_ENTER:
			if(!GamePanel.gameStarted){
				GamePanel.gameStarted=true;
				gp.startGame();
			}
			break;	
		/*case KeyEvent.VK_BACK_SPACE:
			if(GamePanel.isAnimate){
				GamePanel.isAnimate=false;
			}
			else
			{
				GamePanel.isAnimate=true;
			}
			gp.callPaint();
			break;	*/			
		default:	
			//MyLogger.log("Error in detecting the key "+e.getKeyCode());
			break;
		
		}

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
		
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
