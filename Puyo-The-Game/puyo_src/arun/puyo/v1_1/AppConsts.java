/****************************************
 *AppConsts.java 
 * 
 * @author ARUN
 *
 *Created on 28-4-2007
 * 
 */

package arun.puyo.v1_1;

import java.awt.Color;

// This is a static class, just has most of the consts used in this app
public class AppConsts {
	
	//Game const
	public static final int MODE_RUN=0;
	
	public static final int MODE_DEBUG=1;//THis mode is used for testing pupose
	
	public static final int CURRENT_MODE=MODE_RUN;
	
	
	public static final int LOG_MODE_RUN=0;
	public static final int LOG_MODE_DEBUG=1;
	public static final int LOG_MODE=LOG_MODE_RUN;
	
	public static int MAX_COLS=8;
	
	public static int MAX_ROWS=14;
	
	public static final int GAME_FPS=25;

	// File names kept as constants in this version
	public static final String FILE_PUYO_GREEN = "images/puyo_green.png";

	public static final String FILE_PUYO_RED = "images/puyo_red.png";

	public static final String FILE_PUYO_YELLOW = "images/puyo_yellow.png";

	public static final String FILE_PUYO_BLUE = "images/puyo_blue.png";

	//Names associated to each Puyo Image

	public static final String PUYO_GREEN = "green";

	public static final String PUYO_RED = "red";

	public static final String PUYO_YELLOW = "yellow";

	public static final String PUYO_BLUE = "blue";
	
	//Status of PUyos
	
	public static final String [] PUYO_STATUS = {"moveable","rotateable","inactive", "collided"};

	public static final int PUYO_STATUS_ACTIVE=-1;
	public static final int PUYO_STATUS_MOVEABLE=0;
	public static final int PUYO_STATUS_ROTATEABLE=1;
	public static final int PUYO_STATUS_INACTIVE=2;
	public static final int PUYO_STATUS_COLLIDED=3;
	public static final int PUYO_STATUS_DO_NOT_SET_REMAINING=4;
	public static final int PUYO_STATUS_SET_REMAINING=5;
	public static final int PUYO_STATUS_BOUNDARY_COLUMN=6;	
	
	public static final int MAX_ACTIVE_PUYO = 2;

	public static final String [] COLOUR_VALUE = {PUYO_GREEN,PUYO_RED,PUYO_YELLOW,PUYO_BLUE};
	public static final Color [] COLOR_OBJECTS = {Color.green,Color.red,/*Color.yellow*/new Color(205,173,10),Color.BLUE};	
	
	//Controlling the movement of puyos
	
	public static int STEPS_PER_GRID = 20;//this value gives the amount of distance moved in one move
	
	public static int MOVES_PER_SEC =200;//this value gives the the number of moves done in one move
	public static int MOVER_SLEEP_TIME =1000/(MOVES_PER_SEC+1);//this is just the time the mover thread sleeps. this should change according to moves_per_sec

	public static int FALL_MOVES_PER_SEC = 220;//this value gives the the number of moves done in one sec for falling objects	
	public static int FALL_THREAD_SLEEP_TIME = 1000/(FALL_MOVES_PER_SEC+1);//this is just the time the fall thread sleeps when falling
	
	public static final int FALL_LIST_MANAGER_SLEEP=1000;
	
	//For controlling the Puyo Rotation
	public static final int ROTATION_STATUS_RIGHT=0;
	public static final int ROTATION_STATUS_TOP=1;
	public static final int ROTATION_STATUS_LEFT=2;
	public static final int ROTATION_STATUS_DOWN=3;
	public static final int ROTATION_STATUS_NOT_ROTATEABLE=4;

	public static final int ELIMINATE_ON_PUYOS=4; 
	
	public static final int KEYPRESS_TIMEOUT=100;
	
	public static final int FRAME_X_POS=200;
	public static final int FRAME_Y_POS=100;
	public static final String DEFAULT_PLAYER_NAME="PLAYER1";
	
	//Game difficulty levels
	public static final int LEVEL_EASY=0;
	public static final int LEVEL_MED=1;
	public static final int LEVEL_HARD=2;
	public static final int MAX_DIFFICULTY_LEVELS=3;
	public static final String [] DIFFICULTY_LEVEL={"EASY","MEDIUM","HARD"};
	public static final int [] SCORE_PER_PUYO={10,5,1};
	
	public static final String HIGH_SCORE_FILE_NAME="highscores.txt";
	public static final int NO_OF_HIGH_SCORES=5;
}

