/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 30-4-2007 
 */

package arun.puyo.v1_1;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public class GroupPuyo extends ArrayList{
	
	public GroupPuyo()
	{
		super();
	}
	
	public GroupPuyo(Puyo p)
	{
		super();
		add(p);
	}	
	

	//This method enables to add a grouppuyo into this object
	public void add(GroupPuyo gp)
	{
		for (int i = 0; i < gp.size(); i++) {
			add(gp.get(i));
		}	
	}	
	
	//Here lies the logic of grouping
	public boolean check(Puyo p)
	{
		//To come to this part of the code definitely, the groupofpuyos must have atlerast one
		for (int i=0;i<size();++i)
		{
			//Check if the puyo in the list is adjacent to p
			if(isAdjacent((Puyo)get(i),p))
			{
				return true;	
			}
			
		}
		return false;
	}
	
	public void draw(ImageHolder imH, Graphics g, ImageObserver gp)
	{
		if(size()>1)
		{
			for (int i=0;i<size();++i)	{
					((Puyo)get(i)).draw(imH,g,gp,true);
			}
		}
		else
		{
			for (int i=0;i<size();++i)	{
					((Puyo)get(i)).draw(imH,g,gp);
			}			
		}
		
	}
		
	//Nothing great in here, simple checking only
	private boolean isAdjacent(Puyo p1,Puyo p2)
	{
		if(p1.getRow()==p2.getRow())
		{
			if(p1.getColumn()==p2.getColumn()-1)
			{
				//Just setting the colliding area - But this is only for debug purpose
				if(AppConsts.CURRENT_MODE==AppConsts.MODE_DEBUG)
				{
					p1.setRight();
					p2.setLeft();
				}
				return true;	
			}
			else if(p1.getColumn()==p2.getColumn()+1)
			{
				if(AppConsts.CURRENT_MODE==AppConsts.MODE_DEBUG)
				{				
					p1.setLeft();
					p2.setRight();
				}
				return true;	
			}			
		}
		else if(p1.getColumn()==p2.getColumn())
		{
			if(p1.getRow()==p2.getRow()-1)
			{
				if(AppConsts.CURRENT_MODE==AppConsts.MODE_DEBUG)
				{
					p1.setTop();
					p2.setBottom();
				}
				return true;	
			}
			if(p1.getRow()==p2.getRow()+1)
			{
				if(AppConsts.CURRENT_MODE==AppConsts.MODE_DEBUG)
				{
					p1.setBottom();
					p2.setTop();
				}
				return true;	
			}			
		}
		return false;	
	}

	public boolean canBeEliminated(PuyoManager pm) {
		// TODO Auto-generated method stub
		if (size()>=AppConsts.ELIMINATE_ON_PUYOS)
		{
			pm.setEliminatedPuyos(size());
			return true;
		}
		return false;
	}
	
	public Puyo getPuyo(int row,int column)
	{
		Puyo p = null;
		for (Iterator iter = this.iterator(); iter.hasNext();) {
			p = (Puyo) iter.next();
			if (p.getRow()==row && p.getColumn()==column)
			{
				//MyLogger.log("Matched");
				return p;	
			}			
		}
		return null;
	}
	public void remove(Puyo p,boolean dummy)
	{
		Puyo temp=getPuyo(p.getRow(),p.getColumn());
		remove(temp);
	}
}