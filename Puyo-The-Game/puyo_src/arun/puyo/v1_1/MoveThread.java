/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 29-4-2007 
 */
package arun.puyo.v1_1;

public class MoveThread extends Thread {

		GamePanel gp;
		public MoveThread(GamePanel gp)
		{
			this.gp=gp;
			//Obviously there are no puyos in the game at this point , so no need to check
				gp.getPuyoManager().generateActivePuyos();			
		
			this.start();
		}
		
		public void run() {

			long startTime=System.currentTimeMillis();
			while(GamePanel.getGameOver()==false)
			{
				long usedTime=(System.currentTimeMillis()-startTime);
				
				//THis logic will give an error of max +/- MOVER_SLEEP_TIME/2
				if(usedTime>=(1000-(AppConsts.MOVER_SLEEP_TIME/2))/AppConsts.MOVES_PER_SEC)
				{
					//UPDATEWORLD
					gp.getPuyoManager().movePuyoDown(gp);
					//Call Repaint
					//gp.callPaint();//This is now done in PuyoManager
					startTime=System.currentTimeMillis();
					/*MyLogger.log(usedTime);*/	usedTime=0;
	
				}

				
				try {
					Thread.sleep(AppConsts.FALL_THREAD_SLEEP_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		

}
