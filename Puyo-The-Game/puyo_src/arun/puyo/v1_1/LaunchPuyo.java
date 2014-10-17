/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 28-4-2007 
 */
package arun.puyo.v1_1;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.awt.*;
import javax.swing.JFrame;


public class LaunchPuyo extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//THis is the panel which will hold all the other panels like gamepanel option or any other in future
	MainPanel mainPanel;
	
	public LaunchPuyo()
	{
		super("Puyo-Puyo");
		mainPanel=new MainPanel();
		Container pane=this.getContentPane();
		pane.add(mainPanel);

		//this.setSize(100,100);//No need to do this because it takes the size of the Panels 
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setLocation(AppConsts.FRAME_X_POS,AppConsts.FRAME_Y_POS);//Setting location must be based on Screen resolution
		this.pack();
		this.setVisible(true);
	}
	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LaunchPuyo puyoTheGame= new LaunchPuyo();
		//puyoTheGame.mainPanel.getGamePanel().startGame();
		puyoTheGame.initializeGameOptions();
	}



	private void initializeGameOptions() {
		// TODO Auto-generated method stub
		GameOptions options=new GameOptions(this);	
		
		//THe following can be added inside windowclosing event of GameOptions
		try {
			AppConsts.MOVES_PER_SEC=Integer.parseInt(options.gameSpeed.getText());
			AppConsts.MOVER_SLEEP_TIME=1000/(AppConsts.MOVES_PER_SEC+1);
		} catch (Exception e) {	}
		
		try {
			AppConsts.FALL_MOVES_PER_SEC=Integer.parseInt(options.fallSpeed.getText());
			AppConsts.FALL_THREAD_SLEEP_TIME=1000/(AppConsts.FALL_MOVES_PER_SEC+1);
		} catch (Exception e) {	}		

		try {
			AppConsts.STEPS_PER_GRID=Integer.parseInt(options.smoothness.getText());
		} catch (Exception e) {	}	
		
		try {
			AppConsts.MAX_COLS=Integer.parseInt(options.xSize.getText());
		} catch (Exception e) {	}	
		
		try {
			AppConsts.MAX_ROWS=Integer.parseInt(options.ySize.getText());
		} catch (Exception e) {	}			
		
		this.mainPanel.getGamePanel().getPuyoManager().initialize(options.getPlayerName(),options.getDifficultyLevel());
		this.mainPanel.getGamePanel().initialize();
		this.pack();
		
		
	}
		
}
