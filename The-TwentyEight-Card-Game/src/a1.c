#include "fns.h"
#include "resource.h"

//The Bitmap loading and deleting procedure is as follows:
/*1.I call the fn LoadMyBmp() with certain flags and the bmp is loaded
as well as SelectObject() is also done.
2.Then i immediately call DeleteMyBmp() with parameters that deletes only
the bmp object and not the memory HDC of the bmps.
3.Finally i delete my memory HDCs along with theis bmps(to be safe) in WM_DESTROY.
*/
/*But what i wonder is that, in most of my functions,i pass the hbmps 
(that were supposed to be deleted) and apply GetObject() on them.
I dont how it works perfectly when the bmp was supposed to be DELETED.
*/

//VARIABLES
const char clsnm[]="class1";
const char *CardType[4]={"SPADE","CLUBS","HEARTS","DIAMONDS"};
const char MyExtension[]="726";

//Timer Speeds -- It is in milliseconds
int Timer1Speed=300;//the ranges are defined iin defins.h
int Timer2Speed=50;//const
int Timer2Step=20;//ranges are defined in defins.h
int Timer3Speed=200;

//Match winning Score
int MaxScore=7;

//ACCELERATOR HANDLE
HACCEL haccel1;
//FONT HANDLE
HFONT hfgl;

//The actual bitmap handles
HBITMAP hbmpbg=NULL,hbmpcover=NULL,hbmptem=NULL,hbmpbox=NULL;

//The corresponding returns of their SelectObject() not needed
HBITMAP hbmpbgold,hbmpcoverold,hbmptemold,hbmpboxold;

/*An important note:The returns are actually the default memory DCs
So one needs to replace them before calling DeleteDC(),no matter
howmany bmps u select to that memory DC, in order to prevent GDI leaks,
which is nothing but piling up of resources.
*/

//Their corresponding HDC in mem
HDC hdcmembg,hdcmemcover,hdcmemtem,hdcmembox;
//A global HDC for device
HDC hdcgl=NULL;
HWND hs=NULL;
HWND hwpm=NULL;

//Initialised in ResetGame();
BOOL buttondown=FALSE,welcomed=FALSE,gamestarted=FALSE;
BOOL Animation=FALSE;//This is to prevent malpractice during animations,default=FALSE set in ResetGameData()

PLAYERS player[NO_OF_PLAYERS];
CARDS card[4][13];
CARDLINKS link1[NO_OF_CARDS*4];//Only 32 cards in two player trump
CARDLINKS link2;//This is for use in click and drag of cards
RECT OldRect;////This is also for use in click and drag of cards,to store the rectcard before moving it

//The following are initialised in ResetGameData()
int index=0;//This is to keep track of the link1;This could be made static but it's main use is in WM_KEYDOWN
int WhoseTurn;//This value is the player index whose turn it is
int WhoseQuote;//This value is the player index whose quote it is
int WhoseHand;//This is after one round, it will tell whose hand it was
int WhoStarted;//This will keep track of switching between players turn after every game

char StatusText[MAX_STATUS_LEN];
char *LoadThis;//to store the argv value

//FUNCTIONS
long _stdcall wndfn(HWND hw, UINT msg, WPARAM y, LPARAM z);


int _stdcall WinMain(HINSTANCE hi, HINSTANCE hp,LPSTR argv, int argn)
{
	WNDCLASSEX wc;
    HWND hw;
    MSG msg;

	//This is for removing the two " form argv
//	LoadThis=GlobalAlloc(GPTR,strlen(argv)+1);
//	for(i=1,j=0;i<strlen(argv);++i,++j)
//		LoadThis[j]=argv[i];
//	if(j)
//		--j;
//	LoadThis[j]='\0';
	 
	//Instead of doing just the following line i had to do all the above
	LoadThis=argv;

	InitCommonControls();//This is for Status bar,Tool bar,Combobox,etc

	//Fill the wndclass(its not a class) structre
    wc.cbSize        = sizeof(WNDCLASSEX);
    wc.style         = 0;
    wc.lpfnWndProc   = wndfn;
    wc.cbClsExtra    = 0;
    wc.cbWndExtra    = 0;
    wc.hInstance     = hi;
    wc.hIcon         = LoadIcon(GetModuleHandle(NULL), MAKEINTRESOURCE(IDI_ICON1));
    wc.hCursor       = LoadCursor(NULL, IDC_ARROW);
    wc.hbrBackground = (HBRUSH)(COLOR_WINDOW+1);//(HBRUSH)2;//7=black
    wc.lpszMenuName  = MAKEINTRESOURCE(IDR_MENU1);
    wc.lpszClassName = clsnm;
    wc.hIconSm       = LoadIcon(GetModuleHandle(NULL), MAKEINTRESOURCE(IDI_ICON1));

     if(!RegisterClassEx(&wc))
    {
        MessageBox(NULL, "Window Registration Failed!", "Error!",MB_ICONEXCLAMATION | MB_OK);
        return 0;
    }
 

	hw = CreateWindowEx(WS_EX_CLIENTEDGE,clsnm,"The Trump",WS_SYSMENU|WS_MINIMIZEBOX,80, 0, 640, 540,
        NULL, NULL, hi, NULL);
 
    if(hw == NULL)
    {
        MessageBox(NULL, "Window Creation Failed!", "Error!",MB_ICONEXCLAMATION | MB_OK);
        return 0;
    }
 
	ShowWindow(hw, argn);
    //UpdateWindow(hw);//I dont know what this does

	while(GetMessage(&msg, NULL, 0, 0))
    {
		//I couldnt find any other way
		if(msg.message==WM_COMMAND && LOWORD(msg.wParam)==ID_GAME_NEWGAME && gamestarted==FALSE)
			srand(msg.time);//seed the random generation evry new game;

		//Translate the accelereato messages
		TranslateAccelerator(hw,haccel1,&msg);

		if(!IsDialogMessage(hwpm,&msg))//this is for default processing...
		{//...of dialog box when used CreateDialog

		//To look after certain default key inputs...
		TranslateMessage(&msg);//...if this is removed then Alt+Space and things like that dont work	
		//Finally to Dispatchs the message in the quee to our approriate windows procedures
		DispatchMessage(&msg);

		}
		 
    }
    return msg.wParam;//wparam is the value passed to PostQuitMessage();
}

long _stdcall wndfn(HWND hw, UINT msg, WPARAM y, LPARAM z)
{
    switch(msg)
    {
	case WM_CREATE:
		{

			HWND Deskhw;
			RECT rect,DeskRect;
			BITMAP bm1;
			int i,j,id;
			//Get the device context
			hdcgl=GetDC(hw);
			if (hdcgl==NULL)
				MessageBox(hw,"Does not support graphics","Worst case",0);
			//Create a memory HDC for tem use,
			hdcmemtem=CreateCompatibleDC(hdcgl);
			if (hdcmemtem==NULL)
				MessageBox(NULL, "HDC of memory creation failed", "Error!",MB_ICONEXCLAMATION | MB_OK);
			//tem bmp created in WM_PAINT but *not* in WM_CREATE

			//Start Loading bmps
			if (!LoadMyBmp(&hbmpbg,IDB_BITMAP4,hdcgl,&hbmpbgold,&hdcmembg,WITH_BMP|WITH_DC))
				MessageBox(hw,"Colud Not Load The Bitmap","ERROR!",0);
			if (!LoadMyBmp(&hbmpcover,IDB_BITMAP5,hdcgl,&hbmpcoverold,&hdcmemcover,WITH_BMP|WITH_DC))
				MessageBox(hw,"Colud Not Load The Bitmap","ERROR!",0);
			if (!LoadMyBmp(&hbmpbox,IDB_BITMAP8,hdcgl,&hbmpboxold,&hdcmembox,WITH_BMP|WITH_DC))
				MessageBox(hw,"Colud Not Load The Bitmap","ERROR!",0);
			DeleteMyBmp(&hbmpbg,&hbmpbgold,&hdcmembg,WITH_BMP|WITHOUT_DC);
			DeleteMyBmp(&hbmpcover,&hbmpcoverold,&hdcmemcover,WITH_BMP|WITHOUT_DC);
			DeleteMyBmp(&hbmpbox,&hbmpboxold,&hdcmembox,WITH_BMP|WITHOUT_DC);

			//Now starts the loading of card bmps
			for(i=0,id=CARD_STARTING_ID;i<4;++i){
				for(j=0;j<13;++j){
					if (!LoadMyBmp(&(card[i][j].hbmp),id,hdcgl,&(card[i][j].hbmpold),&(card[i][j].hdcmem),WITH_BMP|WITH_DC))
						MessageBox(hw,"Colud Not Load The Bitmap","ERROR!",0);
					DeleteMyBmp(&(card[i][j].hbmp),&(card[i][j].hbmpold),&(card[i][j].hdcmem),WITH_BMP|WITHOUT_DC);
					++id;
				}
			}

				
			//Create Status bar
			hs = CreateWindowEx(0, STATUSCLASSNAME ,"Select New Game from menu to start the game",WS_CHILD | WS_VISIBLE|WS_THICKFRAME
				,0, 0, 0, 0,hw, (HMENU)IDC_RUN_STATUS, GetModuleHandle(NULL), NULL);
			if(hs == NULL)
				MessageBox(hw, "Status Bar Creation Failed!", "Error!", MB_ICONEXCLAMATION | MB_OK);
			
			//Load Accelerator
			haccel1=LoadAccelerators(GetModuleHandle(NULL),MAKEINTRESOURCE(IDR_ACCELERATOR1));
			
			//Create Font
			hfgl=LoadMyFont(12,"Cheap Fire");
			
			//Set players Default Name
			SetPlayerDefName(player[0].defname,"PLAYER1");
			SetPlayerDefName(player[1].defname,"PLAYER2");
			//Find the rect of display area
			rect=FindDisplayRect(hw);
			//Set players rectname
			SetUpRect(&(player[0].rectname),rect.right-160,155,rect.top+80,15);
			SetUpRect(&(player[1].rectname),rect.right-160,155,rect.bottom-105,15);
			//Set players rectcard
			GetObject(hbmpbox,sizeof(BITMAP),&bm1);
			SetUpRect(&(player[0].rectcard),rect.right-120,bm1.bmWidth,rect.top+160,bm1.bmHeight);
			SetUpRect(&(player[1].rectcard),rect.right-120+(bm1.bmWidth/2),bm1.bmWidth,rect.top+160+(bm1.bmHeight/2),bm1.bmHeight);
			//Set Players rectarea done in setarea()
	
			Deskhw=GetDesktopWindow();
			GetWindowRect(Deskhw,&DeskRect);
			if(DeskRect.right<800 || DeskRect.bottom<600)
				MessageBox(hw,"Better play under 800*600 or higher screen resolutions","Error Possible!",0);

        }
		break;
	case WM_PAINT:
		{
			HDC hdc;
			PAINTSTRUCT ps;
			RECT rect;
			hdc=BeginPaint(hw,&ps);//GetDC will not work well
			rect=FindDisplayRect(hw);
			//creating hbmptem and select it to hdcmemtem
			if(!welcomed)
			{
				welcomed=TRUE;
				hbmptem=CreateCompatibleBitmap(hdcgl,rect.right,rect.bottom);
				if (hbmptem==NULL)
					MessageBox(NULL, "Bitmap Creation Failed!", "Error!",MB_ICONEXCLAMATION | MB_OK);
				hbmptemold=SelectObject(hdcmemtem,hbmptem);
				FillRect(hdcmemtem,&rect,(HBRUSH)2);//This color is transparent RGB(0,127,127),useless statement here
				//Draw the background--This is to display only the bg image,no names
				hdcmemtem=setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox);
				BitBlt(hdc,0,0,rect.right,rect.bottom,hdcmemtem,0,0,SRCCOPY);
				//Do the automatic loading when started with a file
				if(LoadThis[0]!='\0' && LoadGame(hw,LoadThis))
				{	
					KillAllTimers(hw);
					//Since the save is done only if gamestarted
					gamestarted=TRUE;
					//Remove unecessary buttons
					DestroyMyButtons(hw);
					//Now redraw
					hdcmemtem=RedrawArea(setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox),hw,hbmpcover,hdcmemcover);
					//Display on screen
					DisplayHDCmem(hdcmemtem);
					//Status Update
					SetWindowText(hs,StatusText);
				}
			}

			else{
			BitBlt(hdc,0,0,rect.right,rect.bottom,hdcmemtem,0,0,SRCCOPY);
			}
			
			//Could also use the Doublebuffering function instead of BitBlt()
			//hdctem=doublebuffer(2,hw,hdc,hdcmemtem1,hdcmemtem2);
			
			//DisplayHDCmem(hdcmemtem);
			//Using the above function causes problem in this event dont know why
			//But doesnt make any difference here as Finding rect is must anyways
			EndPaint(hw,&ps);
		}
		break;
		case WM_COMMAND:
			switch(LOWORD(y))
			{
			case ID_GAME_EXIT:
				PostMessage(hw,WM_CLOSE,0,0);
				break;

			case ID_GAME_NEWGAME:
				if(!gamestarted){
					//Immediately do kill all timers just precaution
					KillAllTimers(hw);
					//Draw the background--This is to display bg to clear if any thing were present
					hdcmemtem=setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox);
					DisplayHDCmem(hdcmemtem);
					//Display the Welcome Dialog
					LoadNewPlayers(hw);
					/*Important:*///gamestarted=TRUE; is set only when timer1 finishes
					//Initialise Random Number Generation.
					//Tell the cards present,ect
					//All this is done in this function
					ResetGameData(hw);
					//Draw the background--This is to display the bg along with the names entered
					hdcmemtem=setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox);
					DisplayHDCmem(hdcmemtem);
					//Place the cards in memory(not hdc) by setting up CARDLINKS variable link1[NO_OF_CARDS*4]
					setarea(hw,hbmpcover);
					//Update Status Bar
					wsprintf(StatusText,"Wait until the cards are placed or Press Any Key");
					SetWindowText(hs,StatusText);
					//If you Set the timer to 0.5 second=500.
					//The first WM_TIMER message goes after 0.5 second after the following statement
					if(!SetTimer(hw,ID_TIMER1,Timer1Speed,NULL))//reWhoseturns 0 on error
						MessageBox(hw,"Unable to load timer","ERROR!",0);
					}
				else{
					if (MessageBox(hw,"Do u want to start a new game!!","New Game",4)==6)
					{
						gamestarted=FALSE;
						PostMessage(hw,WM_COMMAND,ID_GAME_NEWGAME,0);
					}
				}
				break;

			case IDC_RUN_BUTTON1:
				KillAllTimers(hw);
				//Draw the background--This is to display bg to clear if any thing were present
				hdcmemtem=setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox);
				DisplayHDCmem(hdcmemtem);
				
				ContinueMatch();
				
				//Place the cards in memory(not hdc) by setting up CARDLINKS variable link1[NO_OF_CARDS*4]
				setarea(hw,hbmpcover);
				
				//Update Status Bar
				wsprintf(StatusText,"Wait until the cards are placed or Press Any Key");
				SetWindowText(hs,StatusText);

				if(!SetTimer(hw,ID_TIMER1,Timer1Speed,NULL))//reWhoseturns 0 on error
					MessageBox(hw,"Unable to load timer","ERROR!",0);
				
				DestroyWindow(GetDlgItem(hw,IDC_RUN_BUTTON1));
				break;

			case IDC_RUN_BUTTON2:
				PostMessage(hw,WM_COMMAND,ID_GAME_NEWGAME,0);				
				DestroyWindow(GetDlgItem(hw,IDC_RUN_BUTTON2));
				break;

			case ID_VIEW_OPTIONS:
				OpenOptions(hw);
				break;
			case ID_VIEW_SCORE:
				OpenScore(hw);
				break;
			case ID_VIEW_GAMESTATS:
				OpenStats(hw);
				break;
			case ID_VIEW_POINTSMAP:
				OpenPointsMap(hw);
				break;
			case ID_VIEW_CREDITS:
				OpenCredits(hw);
				break;
			case ID_GAME_SAVEGAME:
				OpenSave(hw);
				break;
			case ID_GAME_LOADGAME:
				if(OpenLoad(hw))
				{
					KillAllTimers(hw);
					//Since the save is done only if gamestarted
					gamestarted=TRUE;
					//Remove unecessary buttons
					DestroyMyButtons(hw);
					//Now redraw
					hdcmemtem=RedrawArea(setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox),hw,hbmpcover,hdcmemcover);
					//Display on screen
					DisplayHDCmem(hdcmemtem);
					//Status Update
					SetWindowText(hs,StatusText);
				}
				break;

			}
			break;
		case WM_LBUTTONDOWN:
			{
				POINT mp;

				//Capture the mouse input so that this program gets the mouse input always
				SetCapture(hw);

				//First to put the card back in place for nclbuttonup...
				//But this will never occur because of SetCapture
				//Ha!Ha! Butt still needed as the user may do this and rbdwn simultaneously
				if(buttondown && card[link2.type][link2.number].present==TRUE && card[link2.type][link2.number].visible==TRUE)//This is to prevent WM_NCLBUTTONUP move of the card
				{
				card[link2.type][link2.number].rectcard=OldRect;
				//Now redraw
				hdcmemtem=RedrawArea(setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox),hw,hbmpcover,hdcmemcover);
				//Display on screen
				DisplayHDCmem(hdcmemtem);
				}

				if (!GetCursorPos(&mp))
					MessageBox(hw,"get cursor not worked","ERROR!",0);

				ScreenToClient(hw,&mp);
								
				if(gamestarted && IsTurn(WhoseTurn,mp.x,mp.y) && FindCard(mp.x,mp.y,&(link2.type),&(link2.number)))
				{
					OldRect=card[link2.type][link2.number].rectcard;
					buttondown=TRUE;
				}

			}
			break;
		case WM_MOUSEMOVE:
			{
				BITMAP bm1;
				POINT mp;

				if(buttondown && gamestarted)
				{
					GetCursorPos(&mp);
					ScreenToClient(hw,&mp);				
					GetObject(card[link2.type][link2.number].hbmp,sizeof(BITMAP),&bm1);
					SetUpRect(&(card[link2.type][link2.number].rectcard),mp.x-(bm1.bmWidth/2),bm1.bmWidth,mp.y-(bm1.bmHeight/2),bm1.bmHeight);
					
					//Now redraw
					hdcmemtem=RedrawArea(setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox),hw,hbmpcover,hdcmemcover);
					//Display on screen
					DisplayHDCmem(hdcmemtem);
				}
							
			}
			break;
		case WM_LBUTTONUP:
			{
				if(buttondown && gamestarted)
				{
					if(!Animation && RectOverlap(card[link2.type][link2.number].rectcard,player[WhoseTurn].rectcard) && IsCorrectCard(hw,link2.type))
					{
						card[link2.type][link2.number].rectcard=player[WhoseTurn].rectcard;
						card[link2.type][link2.number].present=FALSE;
						//turn changed
						WhoseTurn=(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;							
						if(CardsInBox()==2)
						{
							FindWhoseHand(hw);//UpdatePoints also done in this
							//change turn
							WhoseTurn=WhoseHand;
 							//AnimateCards
							if(!SetTimer(hw,ID_TIMER2,Timer2Speed,NULL))//returns 0 on error
								MessageBox(hw,"Unable to load timer","ERROR!",0);
							
							//Open up those cards that are covered
							MakeVisible(hw);
							
							//(IsGameOver()),DisplayPoints(),(IsMatchOver()),UpdateScore() all this done in ID_TIMER2 itself
						}
						//Update Status bar
						wsprintf(StatusText,"It's your turn %s",player[WhoseTurn].name);
						SetWindowText(hs,StatusText);
					}
					else
					{
						card[link2.type][link2.number].rectcard=OldRect;
					}
					
					//Now redraw
					hdcmemtem=RedrawArea(setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox),hw,hbmpcover,hdcmemcover);
					//Display on screen
					DisplayHDCmem(hdcmemtem);
					
					//This is done only after redraw to keep that card on top in the box
					buttondown=FALSE;

				}
				
				if (GetCapture()==hw)
				{
					//we dont need that anymore
					ReleaseCapture();
				}

			}
			break;
		//The following case check was not done even in MS-Solitaire
		/*case WM_NCLBUTTONDOWN:// if the user drags to non-client area
			buttondown=FALSE;
			break;*///But try it and see what happens
		case WM_RBUTTONDOWN:
			{
				POINT mp;
				int type,number;
				
				GetCursorPos(&mp);
				ScreenToClient(hw,&mp);
				//MessageBox(hw,"dbl","df",0);
				if(gamestarted && !Animation && IsTurn(WhoseTurn,mp.x,mp.y) && FindCard(mp.x,mp.y,&type,&number) && IsCorrectCard(hw,type))
				{
					//This is important to nullify the effect when button up may be executed
					buttondown=FALSE;
					card[type][number].present=FALSE;
					card[type][number].rectcard=player[WhoseTurn].rectcard;
					//turn changed
					WhoseTurn=(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;							
					if(CardsInBox()==2)//End of round
					{
						FindWhoseHand(hw);//UpdatePoints also done in this
						//change turn
						WhoseTurn=WhoseHand;
 						//AnimateCards
						if(!SetTimer(hw,ID_TIMER2,Timer2Speed,NULL))//returns 0 on error
							MessageBox(hw,"Unable to load timer","ERROR!",0);
							
						//Open up those cards that are covered
						MakeVisible(hw);		

					}
					//Update Status bar
					wsprintf(StatusText,"It's your turn %s",player[WhoseTurn].name);
					SetWindowText(hs,StatusText);
					//Now redraw
					hdcmemtem=RedrawArea(setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox),hw,hbmpcover,hdcmemcover);
					//Display on screen
					DisplayHDCmem(hdcmemtem);
				}
			}
			break;
		
		case WM_TIMER:
			switch(LOWORD(y))
			{
				case ID_TIMER1:
					{
						if(index<(NO_OF_CARDS*4))
						{
							int temindex=index;
							//These if statements are to place cards alternatively for players
							if(WhoseTurn==0)
							{
							if ((index%16)>3 && (index%16)<8)//To Display 4th row...
								temindex=index+8;//instead of 2nd row for both layers.
							else if((index%16)>7 && (index%16)<16)//To Display 2nd row...
								temindex=index-4;//instead of 3rd row,3rd instead of 4th, for both layers.
							}
							else
							{
							if ((index%16)>=0 && (index%16)<4)//To Display 4th row...
								temindex=index+12;//instead of 1st row for both layers.
							else if((index%16)>3 && (index%16)<8)//To Display 1st row...
								temindex=index-4;//instead of 2nd row
							else if((index%16)>11 && (index%16)<16)//To Display 2nd row...
								temindex=index-8;//instead of 4th row
							}
		
							//If the card's visible attribute is false i display the cover instaed of the card
							if (card[link1[temindex].type][link1[temindex].number].visible==TRUE)
								BitBlt(hdcmemtem,card[link1[temindex].type][link1[temindex].number].rectcard.left,card[link1[temindex].type][link1[temindex].number].rectcard.top,(card[link1[temindex].type][link1[temindex].number].rectcard.right-card[link1[temindex].type][link1[temindex].number].rectcard.left),(card[link1[temindex].type][link1[temindex].number].rectcard.bottom-card[link1[temindex].type][link1[temindex].number].rectcard.top),card[link1[temindex].type][link1[temindex].number].hdcmem,0,0,SRCCOPY);
							else
								BitBlt(hdcmemtem,card[link1[temindex].type][link1[temindex].number].rectcard.left,card[link1[temindex].type][link1[temindex].number].rectcard.top,(card[link1[temindex].type][link1[temindex].number].rectcard.right-card[link1[temindex].type][link1[temindex].number].rectcard.left),(card[link1[temindex].type][link1[temindex].number].rectcard.bottom-card[link1[temindex].type][link1[temindex].number].rectcard.top),hdcmemcover,0,0,SRCCOPY);
							DisplayHDCmem(hdcmemtem);
							++index;
						}
						else
						{
							gamestarted=TRUE;//Game has stared, could be done in GetQuote() function but the boxes wont appear
							//Redraw the entire stuff
							hdcmemtem=RedrawArea(setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox),hw,hbmpcover,hdcmemcover);
							DisplayHDCmem(hdcmemtem);
							//Get Quote for trump;
							index=0;
							KillTimer(hw,ID_TIMER1);
							GetQuote(hw,hdcgl);
						}
					}
					break;
				case ID_TIMER2:
					{
					static CARDLINKS slink;
					static int done;//This will keep track of no of cards
					static int WhichPlayer;
					int step,i,j;
					
					//first time when gets in done is made 0
					if(!Animation)
						done=0;
					Animation=TRUE;
					if(done==0)
						WhichPlayer=WhoseHand;
					else if (done==1)
						WhichPlayer=(WhoseHand+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;
					
					if(WhoseHand==0)//Player1
					{
						step=-Timer2Step;

						for(i=0;i<4;++i)
						{
							for(j=0;j<13;++j)
							{
								if(card[i][j].visible==TRUE && RectEqual(player[WhichPlayer].rectcard,card[i][j].rectcard))
								{
									slink.type=i;
									slink.number=j;
								}
							}
						}
						if(card[slink.type][slink.number].rectcard.bottom>player[WhoseHand].rectname.top)
						{
							SetUpRect(&(card[slink.type][slink.number].rectcard),card[slink.type][slink.number].rectcard.left,card[slink.type][slink.number].rectcard.right-card[slink.type][slink.number].rectcard.left,card[slink.type][slink.number].rectcard.top+step,card[slink.type][slink.number].rectcard.bottom-card[slink.type][slink.number].rectcard.top);
						}
						else
						{
							card[slink.type][slink.number].visible=FALSE;
							//check if the card has flown off the playarea
							if(!RectOverlap(card[slink.type][slink.number].rectcard,player[WhoseHand].rectarea))
								SetUpRect(&(card[slink.type][slink.number].rectcard),card[slink.type][slink.number].rectcard.left,card[slink.type][slink.number].rectcard.right-card[slink.type][slink.number].rectcard.left,0,card[slink.type][slink.number].rectcard.bottom-card[slink.type][slink.number].rectcard.top);
							++done;
						}
					}
					else//player2
					{
						step=Timer2Step;

						for(i=0;i<4;++i)
						{
							for(j=0;j<13;++j)
							{
								if(RectEqual(player[WhichPlayer].rectcard,card[i][j].rectcard))
								{
									slink.type=i;
									slink.number=j;
								}
							}
						}
						if(card[slink.type][slink.number].rectcard.top<player[WhoseHand].rectname.bottom)
						{
							SetUpRect(&(card[slink.type][slink.number].rectcard),card[slink.type][slink.number].rectcard.left,card[slink.type][slink.number].rectcard.right-card[slink.type][slink.number].rectcard.left,card[slink.type][slink.number].rectcard.top+step,card[slink.type][slink.number].rectcard.bottom-card[slink.type][slink.number].rectcard.top);
						}
						else
						{
							card[slink.type][slink.number].visible=FALSE;
							//check if the card has flown off the area
							if(!RectOverlap(card[slink.type][slink.number].rectcard,player[WhoseHand].rectarea))
								SetUpRect(&(card[slink.type][slink.number].rectcard),card[slink.type][slink.number].rectcard.left,card[slink.type][slink.number].rectcard.right-card[slink.type][slink.number].rectcard.left,player[WhoseHand].rectname.top,card[slink.type][slink.number].rectcard.bottom-card[slink.type][slink.number].rectcard.top);
							++done;
						}
					}

					if (done==2)
					{
						//char str1[MAX_STATUS_LEN+MAX_NAME_LEN];
						
						//reset done value
						done=0;
						Animation=FALSE;
						//MakeVisible(hw);//is done in WM_LBUTTONUP, because doing here causes problems if the user keeps a card dragging during Animation
						if(IsGameOver())
						{

							gamestarted=FALSE;

							DisplayPoints(hw);
														
							if(UpdateScore()) //and it checks for match over as well
							{
								CreateMyControl(hw,IDC_RUN_BUTTON2,"&New Game");
								//Update status bar
								wsprintf(StatusText,"Its GAME OVER!! Click on the button to start a New Game again");
								SetWindowText(hs,StatusText);

							}
							else
							{
								CreateMyControl(hw,IDC_RUN_BUTTON1,"&Continue");								
								//Update status bar
								wsprintf(StatusText,"Its not yet over!! Click on the button to continue");
								SetWindowText(hs,StatusText);
							}

						}
						
						KillTimer(hw,ID_TIMER2);//The lines below wll still execute ever after this
					}
					
					//Now redraw
					hdcmemtem=RedrawArea(setupbgnd(hdcmemtem,hw,hbmpbg,hdcmembg,hdcmembox),hw,hbmpcover,hdcmemcover);
					//Display on screen
					DisplayHDCmem(hdcmemtem);

				}
				break;
				case ID_TIMER3:
					{
						static int i=0;
						int j;
						
						if(player[0].points>player[1].points)
							j=0;
						else if(player[0].points<player[1].points)
							j=1;
						else//equal
							j=WhoseQuote;

						DrawPlayerWinner(hw,hdcgl,hfgl,RGB(255,i,0),RGB(255,255,255),TRANSPARENT,player[j]);
						if(i==200)
							i=0;
						else
							i=200;
					}
					break;
			}
			break;
			case WM_KEYDOWN:
				//This is just to stop the animation
				if(index!=0)
				{
					index=NO_OF_CARDS*4;//You can pass any value greater than (NO_OF_CARDS*4)-1
					SendMessage(hw,WM_TIMER,ID_TIMER1,0);
				}
				break;
		 case WM_CLOSE:
			if (MessageBox(hw,"Do u really want to!!"," Im Serious",4)==6)
				DestroyWindow(hw);
				break;
        case WM_DESTROY:
			{
				int i,j;
				DeleteMyBmp(&hbmpbg,&hbmpbgold,&hdcmembg,WITH_BMP|WITH_DC);
				DeleteMyBmp(&hbmpcover,&hbmpcoverold,&hdcmemcover,WITH_BMP|WITH_DC);
				DeleteMyBmp(&hbmpbox,&hbmpboxold,&hdcmembox,WITH_BMP|WITH_DC);
				DeleteMyBmp(&hbmptem,&hbmptemold,&hdcmemtem,WITH_BMP|WITH_DC);//need to delete hbmptem also

				//The Deletion of card bmps starts now
				for(i=0;i<4;++i){
					for(j=0;j<13;++j){
						DeleteMyBmp(&(card[i][j].hbmp),&(card[i][j].hbmpold),&(card[i][j].hdcmem),WITH_BMP|WITH_DC);
					}
				}

				DestroyAcceleratorTable(haccel1);
				DeleteObject(hfgl);//font object
				ReleaseDC(hw,hdcgl);//Device Context
				PostQuitMessage(0);//0 is reWhoseturned by WinMain and that is success
			}
        break;
        default:
            return DefWindowProc(hw, msg, y, z);
    }
    return 0;
}


