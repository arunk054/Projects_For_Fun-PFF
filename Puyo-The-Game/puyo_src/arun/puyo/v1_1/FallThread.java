/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 01-05-2007 
 */
 
package arun.puyo.v1_1;

import java.util.ArrayList;

public class FallThread extends Thread {

	private ArrayList[] fallList;
	
	//THis is just a flag to keep track of which ever puyo is finished
	boolean [][] isDone;
	
	private boolean[] allDone;//This flag can be used to find out if the puyos of one column finished or not
	
	private boolean stillFalling=true;
	
	GamePanel gp;
	
	public FallThread(ArrayList[] fallList,GamePanel gp)
	{
		this.gp=gp;
		this.fallList=fallList;
		
		isDone= new boolean[fallList.length][];
		for (int i=0;i<fallList.length;++i)
		{
			isDone[i]= new boolean[fallList[i].size()];
		}
		
		for (int i=0;i<fallList.length;++i)
		{
			for (int j=0;j<isDone[i].length;++j)
			{
				isDone[i][j]=false;	
			}	
		}
		
		allDone=new boolean[fallList.length];
		for (int i=0;i<fallList.length;++i)
		{
			allDone[i]= true;
		}
	
		FallListManager.finishedAll=false;
		this.start();
	}
	
	public void run() {	

	long startTime=System.currentTimeMillis();
	boolean firstTime=true;
	do
	{
		long usedTime=(System.currentTimeMillis()-startTime);
//		the first time var is added so that the fall gets initiated immediately, just for the sake of obeying the laws of gravity!!
	if(usedTime>=(1000-(AppConsts.FALL_THREAD_SLEEP_TIME/2))/AppConsts.FALL_MOVES_PER_SEC || firstTime)
	{
		stillFalling=false;	
		for (int count = 0; count < fallList.length; count++) {
		
			int size=fallList[count].size();
			allDone[count]=true;
			for (int j = 0; j <size; j++) {
				
				if(isDone[count][j]==false)
				{
					Puyo p =(Puyo)fallList[count].get(j);
					MyLogger.log("Count="+count+" Falling "+p.getRow()+", "+p.getColumn());
					p.moveDown();
					if(p.getStatus()==AppConsts.PUYO_STATUS_INACTIVE)
					{
						MyLogger.log("Count="+count+" Falling-Became Inactive "+p.getRow()+", "+p.getColumn());
						isDone[count][j]=true;
					}
					else
					{
						allDone[count]=false;	
					}
							
				}
	
			}
			if(allDone[count]==false)
			{
				MyLogger.log("At count "+count+" Still falling is true");
				stillFalling=true;				
			}

		}	
		gp.callPaint();firstTime=false;
		startTime=System.currentTimeMillis();
		MyLogger.log("Used TIme "+usedTime);	usedTime=0;			
	}
		try {
			Thread.sleep(AppConsts.FALL_THREAD_SLEEP_TIME);
		} catch (InterruptedException e) {
			
		}		
		
		}while(stillFalling);
		
		FallListManager.finishedAll=true;
	}
}
