package arun.puyo.v1_1;

import java.util.ArrayList;
import java.awt.*;
import java.awt.image.ImageObserver;

public class FallListManager {

	//This is an array of fallList
	private ArrayList [] fallList;//I know it sounds funny!!
	
	static boolean finishedAll=false;
	
	public FallListManager()
	{
		fallList=new ArrayList[AppConsts.MAX_COLS];
		for (int i = 0; i < fallList.length; i++) {
			fallList[i]=new ArrayList();
		}
	}
	
	public void removeAll()
	{
		for (int i = 0; i < fallList.length; i++) {
			fallList[i]=new ArrayList();
		}
		
	}

	public int getSize(int column)
	{
		return fallList[column].size();
		
	}

	public ArrayList getFallList(int column)
	{
		return fallList[column];
	}
	
	public int getLength()
	{
		return fallList.length;
	}
	
	public void draw(ImageHolder imH, Graphics g, ImageObserver gp)
	{
		for (int i = 0; i < fallList.length; i++) {
			for(int j=0;j<fallList[i].size();++j)
			{
				((Puyo)fallList[i].get(j)).draw(imH,g,gp);
			}
		}
	}
	
	public void add(Puyo[] fallPuyo) {
		// TODO Auto-generated method stub
		for (int j = 0; j < fallPuyo.length; j++) {
			if(fallPuyo[j]!=null)
			{
				fallList[fallPuyo[j].getColumn()].add(fallPuyo[j]);
			}
		}
	}
	
	public void add(Puyo fallPuyo) {
		// TODO Auto-generated method stub
			if(fallPuyo!=null)
			{
				fallList[fallPuyo.getColumn()].add(fallPuyo);
			}
		}

	public void makePuyosFall(GamePanel gp)
	{
		
		MyLogger.log("make puyos fall");
		FallThread f1=new FallThread(fallList,gp);
		f1.setPriority(Thread.MAX_PRIORITY);
		while(FallListManager.finishedAll==false)
		{
			try {
				Thread.currentThread().sleep(AppConsts.FALL_LIST_MANAGER_SLEEP);	
			} catch (InterruptedException e) {MyLogger.log("FallListManager Interrupted");}
		}	
		f1.stop();
		MyLogger.log("$$$HOW DOES IT COME HERE if FINISHE ALL "+FallListManager.finishedAll);
	}


	
	public void makePuyosFall()
	{
		MyLogger.log("make puyos fall");
		//long startTime=System.currentTimeMillis();
		for (int count = 0; count < fallList.length; count++) {
				
			int size=fallList[count].size();
			boolean [] isDone=new boolean[size];

			//Initializing
			for (int i=0;i<size;++i)
			{
					isDone[i]=false;	
			}
			
			boolean allDone=true;
			do
			{
				allDone=true;
				for (int j = 0; j <size; j++) {
					
					if(isDone[j]==false)
					{
						MyLogger.log("Falling "+((Puyo)fallList[count].get(j)).getRow()+", "+((Puyo)fallList[count].get(j)).getColumn());
						((Puyo)fallList[count].get(j)).moveDown();
						if(((Puyo)fallList[count].get(j)).getStatus()==AppConsts.PUYO_STATUS_INACTIVE)
						{
							MyLogger.log("Falling "+((Puyo)fallList[count].get(j)).getRow()+", "+((Puyo)fallList[count].get(j)).getColumn());
							isDone[j]=true;
						}
						else
						{
							allDone=false;	
						}
								
					}
	
				}
	
			}while(allDone==false);	
		}
	}

	public int getNoOfFallPuyos() {
		// TODO Auto-generated method stub
		int count=0;
		for (int i = 0; i < fallList.length; i++) {
			count+=fallList[i].size();
		}
		MyLogger.log("No of Fall Puyos="+count);
		return count;
	}
}	
