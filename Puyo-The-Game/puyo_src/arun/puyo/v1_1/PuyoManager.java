/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 28-4-2007 
 */

package arun.puyo.v1_1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

import javax.swing.JDialog;

public class PuyoManager {
	
	ImageHolder imageHolder;

	/*The use of this array is a bit confusing, 
	 * i could have used just one array ant then check if any is active etc, but found this to be better in performance*/ 
	private Puyo [] activePuyo;
//	private ArrayList inactivePuyo;//This has an array of the inactive puyo
	
	//Assuming all puyos are of same width and height, hence these parameters are kept here
	private static int puyoWidth,puyoHeight;
	
	/*This is a very important array having the topmost row of puyo in each column
	 * THis is used for collision detection*/
	/*************Need to make this as private and non-static***********************************/
	static int top_rows[];
	
	/*This array is the one responsible for the grouping logic of adjacent, identical Puyos
	* Having one gpm per colour reduces lot of complexity in the GroupPuyo Object*/
	GroupPuyoManager [] gpm;
	
	//Finally decided to have all the player data in the PuyoManager Object
	//Later may be we can encapsulate all this in another object
	private int score=0;
	private String playerName;
	private int difficultyLevel;
	
	FallListManager flm;


	//On creating the puyomanager it loads the images
	//Note that the GamePanel object is passed but not stored here
	public PuyoManager(GamePanel gp)
	{
		activePuyo = new Puyo[AppConsts.MAX_ACTIVE_PUYO];
		

		//Load puyo images
		imageHolder=new ImageHolder();
		imageHolder.loadPuyoImages(gp);
		//setting the puyo width and height
		puyoWidth=ImageHolder.getPuyoWidth(gp);puyoHeight=ImageHolder.getPuyoHeight(gp);
		
		//Initialize the gpm
		gpm=new GroupPuyoManager[AppConsts.COLOUR_VALUE.length];
		for(int i = 0; i < gpm.length; i++)
		{
			gpm[i]=new GroupPuyoManager();
		}
		
		initialize(AppConsts.DEFAULT_PLAYER_NAME,1);

	}
	
	public void initialize(String pname,int diff_level)
	{
		top_rows= new int[AppConsts.MAX_COLS];
		for (int i = 0; i < top_rows.length; i++) {
			top_rows[i]=-1;//initializing as there would not be any puyos on the screen
		}
		flm=new FallListManager();
		score=0;
		playerName=pname;	
		difficultyLevel=diff_level;
	}
	
	public static int getPuyoWidth()
	{
		return puyoWidth;
	}
	
	public static int getPuyoHeight()
	{
		return puyoHeight;
	}
	
	//THis returns the whole of the 2D aray of puyos as a picture
	public void getPuyoPicture(Graphics g,ImageObserver gp)
	{
	
		//Drawing the inactive ones first
		for(int i = 0; i < gpm.length; i++)
		{
			gpm[i].draw(this.imageHolder,g,gp);
		}
		
		//drawing the active ones next
		for (int i = 0; i < activePuyo.length; i++) {
			try {
				activePuyo[i].draw(this.imageHolder,g,gp);				
			} catch (NullPointerException e) {
				MyLogger.log("Please ignore this for now.");				
				MyLogger.log("If the promlem causes the game to crash-Please send your complaints to arun14j@gmail.com");									
				//e.printStackTrace();
			}
		}
		
		//drawing the Falling Puyos
		flm.draw(this.imageHolder,g,gp);		
	
		//I guess the score must be drawn last
		Color c=g.getColor();
		g.setColor(Color.black);
		g.drawString("Score: "+score,GamePanel.getGamePanelWidth()-(GamePanel.getGamePanelWidth()/3)-10,16);
		//Boolean b=new Boolean(GamePanel.isAnimate);
		g.drawString(" "+AppConsts.DIFFICULTY_LEVEL[this.difficultyLevel],0,15);
		g.setColor(c);
	}
	
	/*
	 * THis method is just for test purpose
	 */
	public void createPuyo()
	{
		int a=0;
		
		for (int j = 0; j < 1; j++) {
			for (int k = 0; k <1; k++) {
				activePuyo[a++]=new Puyo(AppConsts.PUYO_BLUE, j, k, 0);
			}
			
		}		
	
	}

	public void createPuyo(String colour,int grid_x,int grid_y,int status)
	{
		Puyo p=new Puyo(colour, grid_x, grid_y, 0);
		
		if (status<activePuyo.length)
		{
			activePuyo[status]=p;
		}
		else
		{
			gpm[p.getColourInt()].add(p);
		}
		
	}

	//This will create a puyo with random colourd
	public void createPuyo(int grid_x,int grid_y,int status)
	{
		double d=Math.random();
		int i = ((int)(d*12))%4;
		
		MyLogger.log("Random number generated= "+d+" "+i);
		String colour=AppConsts.COLOUR_VALUE[i];
		
		Puyo p=new Puyo(colour, grid_x, grid_y, status);
		
		if (status<AppConsts.MAX_ACTIVE_PUYO)
		{
			activePuyo[status]=p;
		}
		else//Adding to the inactive group if status not active
		{
			gpm[p.getColourInt()].add(p);
		}		
		
	}

	public void generateActivePuyos()
	{
		createPuyo(AppConsts.MAX_ROWS,(AppConsts.MAX_COLS/2)-1,AppConsts.PUYO_STATUS_MOVEABLE);//This is moveable one
		createPuyo(AppConsts.MAX_ROWS,AppConsts.MAX_COLS/2,AppConsts.PUYO_STATUS_ROTATEABLE);//This is rotateable one
		
	}
	
	public Puyo [] getActivePuyo() //This will include both rotateable and moveable
	{
		return activePuyo;
		
		
	}

	public Puyo getInactivePuyo(int row,int column)
	{
		Puyo p =null; 
		for (int i=0;i<gpm.length;++i)
		{
			p =gpm[i].getPuyo(row,column);
			if (p!=null)
			{
				return p;	
			}
		}
		return null;
	}
	
	//I guess this is not being used in my application!!
	public boolean needsPuyoCreation() {
		for (int i = 0; i < activePuyo.length; i++) {
			if (activePuyo[i]!=null) {
				return false;
			}
		}
		return true;
		// TODO Auto-generated method stub
		
	}

	/*Following are the methods to move the puyos
	 *Could have a single method with a parameter for direction, but chose this tp be more effective*/
	//Make this blocking to be on the safer side
	public void movePuyoDown(GamePanel gp) {
		boolean isGeneratePuyo=false;
		try
		{
			for (int i = 0; i < activePuyo.length; i++) {
					int returnValue=activePuyo[i].moveDown();
					
					if(activePuyo[i].getStatus()==AppConsts.PUYO_STATUS_INACTIVE)
					{				
						//set flag to generate new puyos
						isGeneratePuyo=true;
						//Need to set the moves of the remaining active ones
						if(returnValue==AppConsts.PUYO_STATUS_SET_REMAINING)
						{
							for (int j = i+1;j < activePuyo.length; j++)
							{
								activePuyo[j].changeCurrentStep(-1);
							}
						}
						else if (returnValue==AppConsts.PUYO_STATUS_DO_NOT_SET_REMAINING)
						{
							//reset moves of earlier ones
							for (int j = 0;j < i; j++)
							{
								activePuyo[j].changeCurrentStep(+1);
							}
						}
						
						//put this one into inactive list and remove active one
						gpm[activePuyo[i].getColourInt()].add(activePuyo[i]);
						activePuyo[i]=null;
						
						//make the rest inactive, copy them to the fallPuyo
						flm.add(activePuyo);
						for (int j = 0; j < activePuyo.length; j++) {
							//fallPuyo[j]=activePuyo[j];
							activePuyo[j]=null;
							//But Remember that u need to add inactive puyos only after they fall							
						}
												
					}
					if (isGeneratePuyo)
					{
						//If we know that we need to generate new Puyos no need to go through the loop again
						break;
					}		
			}
			//Create new Puyos
			if (isGeneratePuyo)
			{
				do{
					//Make the Puyos Fall
					if(GamePanel.isAnimate)
						flm.makePuyosFall(gp);
					else
						flm.makePuyosFall();
					
					//Add to gpm the fall Puyos
					for(int j=0;j<flm.getLength();++j)
					{
						for (Iterator iter = flm.getFallList(j).iterator(); iter.hasNext();) {
							Puyo element = (Puyo) iter.next();
							gpm[element.getColourInt()].add(element);
						}
					}
				
					//Clear the fall puyo list after making the fall
					flm.removeAll();
					
					//Check if any group has more than AppConsts.ELIMINATE_ON_PUYOS puyos
					boolean eliminated=false;
					for(int j=0;j<gpm.length;++j)
					{
						if(gpm[j].eliminateLargeGroups(this)==true)
						{
							eliminated=true;
						}
						
					}
					MyLogger.log("Eliminated= "+eliminated);
					//After Eliminating need to reset the values in top_rows
					//This needs to be done only if the eliminateLargeGroups returns true
					if(eliminated)
					{
						
						for(int j=0;j<AppConsts.MAX_COLS;++j)
						{
							int current_top_row=top_rows[j];
							for(int k=0;k<=current_top_row;++k)
							{
								//first find if any empty space in j column, between puyos
								if(getInactivePuyo(k,j)==null)
								{
									MyLogger.log("Column "+j+" Empty at "+k);
									//set a top_rows val again
									top_rows[j]=k-1;
									for (int i = k+1; i <=current_top_row; i++) {
										Puyo p=getInactivePuyo(i,j);
										if(p!=null)
										{
											MyLogger.log("Column "+j+" FallPuyo at "+i);
											//Remove from inactive before falling 
											gpm[p.getColourInt()].remove(p);
											p.setStatus(AppConsts.PUYO_STATUS_ACTIVE);
											//Add to fall puyo list Manager
											flm.add(p);
										}
									}
									break;
								}
							}
						}
					}
				}while(flm.getNoOfFallPuyos()>0);

				//finally Check for GameOver
				for(int i=0;i<top_rows.length;++i)
				{
					if(top_rows[i]>=AppConsts.MAX_ROWS-1)
					{
							GamePanel.gameStarted=false;GamePanel.gamePaused=false;
							this.doGameOver();
							gp.callPaint();	
							//JDialog jd=new JDialog();jd.show();
							return;		
					}	
				}				

				/********NEED TO CREATE PUYOS ONLY AFTER FALL FINISHES*************/
				generateActivePuyos();
			}
			//call repaint and see
			gp.callPaint();		
		}
		catch(Exception e)
		{}
	}

	public void movePuyoLeft() {
		// TODO Auto-generated method stub
		try {
				for (int i = 0; i < activePuyo.length; i++) {
		
					if(activePuyo[i].moveLeft(this)==AppConsts.PUYO_STATUS_COLLIDED)
					{
						
						//revert back moves of earlier puyos
						for (int j=0;j<i;++j)
						{
							activePuyo[j].changeColumn(+1);
						}
						break;	
					}
				}
		} catch (RuntimeException e) {
		}		
	}

	public void movePuyoRight() {
		// TODO Auto-generated method stub
		try {
				for (int i = 0; i < activePuyo.length; i++) {
		
					if(activePuyo[i].moveRight(this)==AppConsts.PUYO_STATUS_COLLIDED)
					{
						
						//revert back moves of earlier puyos
						for (int j=0;j<i;++j)
						{
							activePuyo[j].changeColumn(-1);
						}
						break;	
					}
				}		
		} catch (RuntimeException e) {
		}			
	}
	public synchronized void rotatePuyo() {
		// TODO Auto-generated method stub
			/*assuming only two puyos, one of which is rotateable
			 * If more Puyos then need to change*/
			try
			{
				//need to call this method on all of the active puyos, if more than one rotateable ppuyo
				activePuyo[AppConsts.PUYO_STATUS_ROTATEABLE].rotate(this,activePuyo[AppConsts.PUYO_STATUS_MOVEABLE]);
			} catch (RuntimeException e) {
				MyLogger.log("No rotateable puyo found");
				e.printStackTrace();
			}
	}	
		
	public void setEliminatedPuyos(int n)
	{
		score+=(n*AppConsts.SCORE_PER_PUYO[difficultyLevel]);
	}
	
	public void doGameOver()
	{
		activePuyo = new Puyo[AppConsts.MAX_ACTIVE_PUYO];
		
		top_rows= new int[AppConsts.MAX_COLS];
		for (int i = 0; i < top_rows.length; i++) {
			top_rows[i]=-1;//initializing as there would not be any puyos on the screen
		}				
		
		//Initialize the gpm again, easier way rather than going and removing all from the existing gpm
		gpm=new GroupPuyoManager[AppConsts.COLOUR_VALUE.length];
		for(int i = 0; i < gpm.length; i++)
		{
			gpm[i]=new GroupPuyoManager();
		}
		
		//Reset the intro String
		GamePanel.introString[0]="!!GAMEOVER!!";
		GamePanel.introString[1]=playerName+" Your Score is "+score;
		
		HighScoresManager hsm=new HighScoresManager(difficultyLevel,playerName,score);
		new HighScoreDialog(hsm.getHighScoreObjects(),hsm.getIsCurrentAdded());
		
		score=0;		
	}
		
	public void setPlayerName(String p)
	{
		playerName=p;
	}
	public void setDifficultyLevel(int d)
	{
		difficultyLevel=d;
	}	
}

