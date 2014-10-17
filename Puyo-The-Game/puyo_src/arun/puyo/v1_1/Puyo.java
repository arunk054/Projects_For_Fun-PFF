/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 28-4-2007 
 */

package arun.puyo.v1_1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

/*
 * According to me this is by far the most important class.
 * But who knows, things may change and time is short
 * 
 */

/*THis class models the behaviour of a puyo*/
public class Puyo {
	
	String colour;
	private int column,currentStep;
	private int x,y;
	private int status;//This wud be moveable,rotateable,inactive ie 0,1,2
	private int rotationStatus;
	private static int width=0,height=0;//assuming all puyos are of same height and withh
	
	/*Note these flags are used only for DEBUG mode*/
	//The flags that tell where is the colliding portion
	private boolean isBottom=false,isTop=false,isLeft=false,isRight=false;
	
	public Puyo(String colour,int row,int column,int status)
	{
		this.colour=colour;
		this.column=column;
		this.currentStep=(row*AppConsts.STEPS_PER_GRID);
		if(width==0)width=PuyoManager.getPuyoWidth();//setting the width, only once
		if(height==0)height=PuyoManager.getPuyoHeight();
		//Sett x and Y assuming iam always going to create a Puyo after GamePanel...Obvious!
		x=columnToX();
		y=currentStepToY();
		this.status=status;
		if (status==AppConsts.PUYO_STATUS_ROTATEABLE)
		{
			this.rotationStatus=AppConsts.ROTATION_STATUS_RIGHT;//Assuming only 2 puyos. so by default right
		}
		else
		{
			this.rotationStatus=AppConsts.ROTATION_STATUS_NOT_ROTATEABLE;//Assuming only 2 puyos. so by default non rotateable			
		}
		
	
		
	}

	public void draw(ImageHolder imH, Graphics g, ImageObserver gp) {
		// TODO Auto-generated method stub
		g.drawImage(imH.getImage(colour), x, y, gp);
	}
	
	//This is an overloaded method to draw the background color on the Puyo
	public void draw(ImageHolder imH, Graphics g, ImageObserver gp,boolean dummy) {
		// TODO Auto-generated method stub
		if (AppConsts.CURRENT_MODE==AppConsts.MODE_DEBUG){
			g.drawImage(imH.getImage(colour), x, y, gp);
			if(isTop)
			{drawTopRect(g);}
			if(isBottom)
			{drawBottomRect(g);}		
			if(isLeft)
			{drawLeftRect(g);}
			if(isRight)
			{drawRightRect(g);}	
		}
		else
		{
			g.drawImage(imH.getImage(colour), x, y,AppConsts.COLOR_OBJECTS[getColourInt()],gp);
		}

	}
	
	public int moveDown()
	{
	
		//MyLogger.log("status="+AppConsts.PUYO_STATUS[status]+" CurrentStep="+currentStep);

		//First Check to see if you are already colliding	
		if (currentStep%AppConsts.STEPS_PER_GRID==0) {
			int row=getRow();
			if(row==PuyoManager.top_rows[column]+1)//check for collision
			{
		
				status=AppConsts.PUYO_STATUS_INACTIVE;
				PuyoManager.top_rows[column]=row;
				return AppConsts.PUYO_STATUS_DO_NOT_SET_REMAINING;
			}
		}

		 /* decrement the currentStep*/
		 changeCurrentStep(-1);
		
		/*Check if colliding using same logic again. 
		*I dont know how we can prevent checking twice in a single move.*/
		if (currentStep%AppConsts.STEPS_PER_GRID==0) {
			int row=getRow();
			if(row==PuyoManager.top_rows[column]+1)//check for collision
			{
		
				status=AppConsts.PUYO_STATUS_INACTIVE;
				PuyoManager.top_rows[column]=row;
				return AppConsts.PUYO_STATUS_SET_REMAINING;
			}
		}
		return status;
	}

	public int moveLeft(PuyoManager pm) {
		// TODO Auto-generated method stub
		
		//check if colliding with any inactive puyo, if so return immediately
		if(this.currentStep%AppConsts.STEPS_PER_GRID==0)
		{
			if(pm.getInactivePuyo(this.getRow(),this.column-1)!=null)
			{
				MyLogger.log("Collided 1");
				return AppConsts.PUYO_STATUS_COLLIDED;	
			}
		
		}
		else
		{
			//find row above and check for collision
			if(pm.getInactivePuyo(findRowAbove(),this.column-1)!=null)
			{
				MyLogger.log("Collided 2");
				return AppConsts.PUYO_STATUS_COLLIDED;	
			}			
			/*find row below - 
			*doing getRow() returns the row below, so no need to calculate*/
			//Andcheck for collision	
			if(pm.getInactivePuyo(this.getRow(),this.column-1)!=null)
			{
				MyLogger.log("Collided 3");
				return AppConsts.PUYO_STATUS_COLLIDED;	
			}			
		}
		
		//If all goes fine we can move left
		changeColumn(-1);
		
		//following logic is to check if colliding with active puyos
		if(this.column==0)
		{
			Puyo [] activeP = pm.getActivePuyo();
			for (int i = 0; i < activeP.length; i++) {
				if(activeP[i]!=this)
				{
					if(activeP[i].getColumn()==column && activeP[i].getCurrentStep()==this.getCurrentStep())
					{
						//It is obvious that we are overlaping, so undo our move
						changeColumn(+1);
						return AppConsts.PUYO_STATUS_BOUNDARY_COLUMN;
					}
				}
			}	
		}
		return this.status;
	}
	
	public int moveRight(PuyoManager pm) {
		// TODO Auto-generated method stub

		//check if colliding with any inactive puyo, if so return immediately
		if(this.currentStep%AppConsts.STEPS_PER_GRID==0)
		{
			if(pm.getInactivePuyo(this.getRow(),this.column+1)!=null)
			{
				MyLogger.log("Collided 1");
				return AppConsts.PUYO_STATUS_COLLIDED;	
			}
		
		}
		else
		{
			//find row above and check for collision
			if(pm.getInactivePuyo(findRowAbove(),this.column+1)!=null)
			{
				MyLogger.log("Collided 2");
				return AppConsts.PUYO_STATUS_COLLIDED;	
			}			
			/*find row below - 
			*doing getRow() returns the row below, so no need to calculate*/
			//Andcheck for collision	
			if(pm.getInactivePuyo(this.getRow(),this.column+1)!=null)
			{
				MyLogger.log("Collided 3");
				return AppConsts.PUYO_STATUS_COLLIDED;	
			}			
		}

		//If all goes fine we can move left
		changeColumn(+1);

		//following logic is to check if colliding with active puyos
		if(this.column==AppConsts.MAX_COLS-1)
		{
			Puyo [] activeP = pm.getActivePuyo();
			for (int i = 0; i < activeP.length; i++) {
				if(activeP[i]!=this)
				{
					if(activeP[i].getColumn()==column && activeP[i].getCurrentStep()==this.getCurrentStep())
					{
						//It is obvious that we are overlaping, so undo our move
						changeColumn(-1);
						break;
					}
				}
			}	
		}
		return this.status;		

	}
	
	public void rotate(PuyoManager pm, Puyo moveP) {
		// TODO Auto-generated method stub
		int currentStep_backup=currentStep, column_backup=column;		
		switch (rotationStatus) {
		case AppConsts.ROTATION_STATUS_RIGHT://No collision checking for this
			changeColumn(-1);
			this.changeCurrentStep(AppConsts.STEPS_PER_GRID);
			rotationStatus=AppConsts.ROTATION_STATUS_TOP;
			break;
		case AppConsts.ROTATION_STATUS_TOP:
			//First Do a left and then a grids amnt of moveDown
			int leftReturnVal=moveLeft(pm);
			if(leftReturnVal!=AppConsts.PUYO_STATUS_COLLIDED && this.column!=column_backup/*THis means that the moveLeft has done something*/)
			{
				//go ahead and move down after checking for free space
				int diff = this.currentStep-((PuyoManager.top_rows[column]+1)*AppConsts.STEPS_PER_GRID);
				if(diff>=AppConsts.STEPS_PER_GRID)
				{
					this.changeCurrentStep(-AppConsts.STEPS_PER_GRID);
					rotationStatus=AppConsts.ROTATION_STATUS_LEFT;
				}
				
			}
			else if(leftReturnVal==AppConsts.PUYO_STATUS_COLLIDED || this.column==column_backup)
			{
				//try with the other puyo
				int moveP_currentStep_backup=moveP.getCurrentStep(), moveP_column_backup=moveP.getColumn();
				int rightReturnVal=moveP.moveRight(pm);
				if(rightReturnVal!=AppConsts.PUYO_STATUS_COLLIDED && moveP.getColumn()!=moveP_column_backup)
				{
					//go ahead and proceed up, the root!! is clear
					moveP.changeCurrentStep(AppConsts.STEPS_PER_GRID);
					rotationStatus=AppConsts.ROTATION_STATUS_LEFT;
					
					//Restore the Values back of the rotateable object not of moveP
					currentStep=currentStep_backup;currentStepToY();
					column=column_backup;columnToX();					
				}
				else
				{
					//Restore the Values back of the moveable
					moveP.setCurrentStep(moveP_currentStep_backup);
					moveP.setColumn(moveP_column_backup);			
				}
				
			}
			if (rotationStatus==AppConsts.ROTATION_STATUS_TOP)
			{
				//Restore the Values back
				currentStep=currentStep_backup;currentStepToY();
				//THis statement sometimes may bot work, as the same devil is here too
				//column=column_backup;columnToX();
				if(column!=column_backup)
					moveRight(pm);
			}
			
			break;	
		case AppConsts.ROTATION_STATUS_LEFT:
			//First see if down has enuf space
			if(this.currentStep-((PuyoManager.top_rows[column]+1)*AppConsts.STEPS_PER_GRID)>=AppConsts.STEPS_PER_GRID)
			{
				this.changeCurrentStep(-AppConsts.STEPS_PER_GRID);
				//make the right move next
				int rightReturnVal=moveRight(pm);
				if(rightReturnVal!=AppConsts.PUYO_STATUS_COLLIDED && this.column!=column_backup)
				{
					rotationStatus=AppConsts.ROTATION_STATUS_DOWN;
				}
			}
			
			if (rotationStatus==AppConsts.ROTATION_STATUS_LEFT)
			{
				//Restore the Values back
				currentStep=currentStep_backup;currentStepToY();
				column=column_backup;columnToX();
			}
			break;		
		case AppConsts.ROTATION_STATUS_DOWN:
			//First see if you can make a move to Right
			int rightReturnVal=moveRight(pm);
			if(rightReturnVal!=AppConsts.PUYO_STATUS_COLLIDED && this.column!=column_backup)
			{
				//go ahead and proceed up, the root!! is clear
				this.changeCurrentStep(AppConsts.STEPS_PER_GRID);
				rotationStatus=AppConsts.ROTATION_STATUS_RIGHT;
			}
			else if(rightReturnVal==AppConsts.PUYO_STATUS_COLLIDED || this.column==column_backup)
			{
				//try with other Puyo
				int moveP_currentStep_backup=moveP.getCurrentStep(), moveP_column_backup=moveP.getColumn();
				leftReturnVal=moveP.moveLeft(pm);
				if(leftReturnVal!=AppConsts.PUYO_STATUS_COLLIDED && moveP.getColumn()!=moveP_column_backup)
				{
					//go ahead and move down after checking for free space
					int diff = moveP.getCurrentStep()-((PuyoManager.top_rows[moveP.getColumn()]+1)*AppConsts.STEPS_PER_GRID);
					if(diff>=AppConsts.STEPS_PER_GRID)
					{
						MyLogger.log("IS IT COMING HERE diff="+diff);
						moveP.changeCurrentStep(-AppConsts.STEPS_PER_GRID);
						rotationStatus=AppConsts.ROTATION_STATUS_RIGHT;
			
						//Restore the Values back of the rotateable object not of moveP
						currentStep=currentStep_backup;currentStepToY();
						column=column_backup;columnToX();											
					}
					else
					{
						MyLogger.log("$$$IS IT COMING HERE movpx="+moveP.getX());										
						//Restore the Values back of the moveable
						moveP.setCurrentStep(moveP_currentStep_backup);
						/*There is one big devil in this part of the code
						*To overcome the devil iam having to do a moveRight instead of 
						*just doing a setColumn with the backup value*/
						moveP.moveRight(pm);
						//moveP.setColumn(moveP_column_backup);
						MyLogger.log("$$$IS IT COMING HERE movpx="+moveP.getX());													
					}
					
				}
				else
				{
					//Restore the Values back of the moveable
					moveP.setCurrentStep(moveP_currentStep_backup);
					moveP.setColumn(moveP_column_backup);						
				}
							
				
			}				
			if (rotationStatus==AppConsts.ROTATION_STATUS_DOWN)
			{
				//Restore the Values back
				currentStep=currentStep_backup;currentStepToY();
				column=column_backup;columnToX();
			}
			break;			
		default:
			break;
		}

		//x=columnToX();
	}	
	
	private int columnToX()
	{
		return GamePanel.getGamePanelWidth()-(width*(AppConsts.MAX_COLS-column));
	}
	
	//THis method is no longer in use, see currentStepToY()
	/*private int rowToY()
	{
		return GamePanel.getGamePanelHeight()-(height*(row+1));
	}*/
	
	//A similar function need to be written for columnto X also
	private int currentStepToY()
	{
		int i= GamePanel.getGamePanelHeight()-(height*(currentStep+AppConsts.STEPS_PER_GRID))/AppConsts.STEPS_PER_GRID;
		return i;

	}	
	
	//It is important to note that the currentStep should be changed only using this method
	public void changeCurrentStep(int i)
	{
		this.currentStep+=i;
		//doing boundary check
		if (currentStep<0)
		{
			currentStep=0;
		}
		this.y=currentStepToY();//Important step, somehow i remembered to add this here
		
	}
	
	public void changeColumn(int i)
	{
		this.column+=i;
		//doing boundary check
		if (column<0)
		{
			column=0;
		}
		else if (column>AppConsts.MAX_COLS-1)
		{
			column=AppConsts.MAX_COLS-1;
		}
		this.x=columnToX();//Important step, somehow i remembered to add this here
		
	}
	
	/*All Getter/Setter Methods*/
	
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int s)
	{
		this.status=s;
	}

	public void setColumn(int c)
	{
		this.column=c;
		this.columnToX();
	}	
	
	public void setCurrentStep(int c)
	{
		this.currentStep=c;
		this.currentStepToY();
	}		
		
	public int getColumn()
	{
		return column;
	}	
	
	//It is advisable to call this method only when currentStep%AppConsts.STEPS_PER_GRID==0, But who Cares!!
	public int getRow()
	{
		return currentStep/AppConsts.STEPS_PER_GRID;
	}	
	
		
	public int getCurrentStep()
	{
		return currentStep;
	}

	//returns the integer value corresponding to the colour
	public int getColourInt()
	{
		for (int i=0;i<AppConsts.COLOUR_VALUE.length;++i)
		{
			if(this.colour.equalsIgnoreCase(AppConsts.COLOUR_VALUE[i]))
			{
				return i;
			}
		}
		//If none found sending -1, but it is a bit risky doing this
		return -1;
	}	
	
	private int findRowAbove()
	{
		int i=0;
		do
		{
			++i;
		}while((currentStep+i)%AppConsts.STEPS_PER_GRID!=0);
		
		return (currentStep+i)/AppConsts.STEPS_PER_GRID;
	}
	
	/*Following are sttter functions for setting the colliding part of this puyo
	 *  and then drawing that
	 * All these are used only in DEBUG mode */
	public void setTop()
	{ isTop=true;	}
	public void setLeft()
	{ isLeft=true;	}	
	public void setRight()
	{ isRight=true;	}	
	public void setBottom()
	{ isBottom=true;	}
	
	public void drawTopRect(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(AppConsts.COLOR_OBJECTS[getColourInt()]);
		g.fillRect(x, y, width, height/2);
		g.setColor(c);
	}
	public void drawBottomRect(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(AppConsts.COLOR_OBJECTS[getColourInt()]);
		g.fillRect(x, y+height/2, width, height/2);
		g.setColor(c);
	}
	
	public void drawLeftRect(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(AppConsts.COLOR_OBJECTS[getColourInt()]);
		g.fillRect(x, y, width/2, height);
		g.setColor(c);
	}
	
	public void drawRightRect(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(AppConsts.COLOR_OBJECTS[getColourInt()]);
		g.fillRect(x+width/2, y, width/2, height);
		g.setColor(c);
	}			
	
	public int getX()
	{
		return x;	
	}
}
