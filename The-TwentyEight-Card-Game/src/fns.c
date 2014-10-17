#include "fns.h"
#include "resource.h"

//69,93 is the width and height of each card
HDC setupbgnd(HDC hdcmemtem,HWND hw,HBITMAP hbmpbg,HDC hdcmembg,HDC hdcmembox)
{
	BITMAP bm1;
	extern HFONT hfgl;
	extern PLAYERS player[NO_OF_PLAYERS];
	extern HWND hs;
	extern BOOL gamestarted;
	int i;
		
	GetObject(hbmpbg,sizeof(BITMAP),&bm1);
	
	//Status bar calculation not needed here bmpbg is larger than
	//FindDisplayRect(hw);
	//for (i=0;i<rect.bottom;i+=bm1.bmHeight)
	//	for (j=0;j<rect.right;j+=bm1.bmWidth){
	BitBlt(hdcmemtem,0,0,bm1.bmWidth,bm1.bmHeight,hdcmembg,0,0,SRCCOPY);//When bg image is bigger than client
	//BitBlt(hdc,j,i,bm1.bmWidth ,bm1.bmHeight,hdcmemtem,0,0,SRCCOPY);
	//}

	//Draw Player names on this
		for (i=0;i<NO_OF_PLAYERS;++i)
			DrawPlayerName(hdcmemtem,hfgl,RGB(0,0,0),RGB(255,255,255),TRANSPARENT,player[i]);
	
	if(gamestarted)
	{
	//The Card Placing Positions drawn
		for (i=0;i<NO_OF_PLAYERS;++i)
			BitBlt(hdcmemtem,player[i].rectcard.left,player[i].rectcard.top,player[i].rectcard.right-player[i].rectcard.left,player[i].rectcard.bottom-player[i].rectcard.top,hdcmembox,0,0,SRCAND);
	}

	if(!gamestarted)
	{
		//Draw Points
		for (i=0;i<NO_OF_PLAYERS;++i)
			DrawPlayerPoints(hdcmemtem,hfgl,RGB(0,0,0),RGB(255,255,255),TRANSPARENT,player[i]);

	}

	return hdcmemtem;
	
}

HDC RedrawArea(HDC hdcmemtem,HWND hw,HBITMAP hbmpcover,HDC hdcmemcover)
{
	extern CARDS card[4][13];
	extern BOOL buttondown;
	extern CARDLINKS link2;
	int i,j;
	BITMAP bm1;

	GetObject(hbmpcover,sizeof(BITMAP),&bm1);

	for(i=0;i<4;++i)
	{
		for(j=0;j<13;++j)
		{
			if (card[i][j].present==TRUE && card[i][j].visible==FALSE)
				BitBlt(hdcmemtem,card[i][j].rectcard.left,card[i][j].rectcard.top,card[i][j].rectcard.right-card[i][j].rectcard.left,card[i][j].rectcard.bottom-card[i][j].rectcard.top,hdcmemcover,0,0,SRCCOPY);
		}
	}
	for(i=0;i<4;++i)
	{
		for(j=0;j<13;++j)
		{
			if (card[i][j].visible==TRUE)//Both whose present is FALSE/TRUE
				BitBlt(hdcmemtem,card[i][j].rectcard.left,card[i][j].rectcard.top,card[i][j].rectcard.right-card[i][j].rectcard.left,card[i][j].rectcard.bottom-card[i][j].rectcard.top,card[i][j].hdcmem,0,0,SRCCOPY);
		}
		
		//This is an extra code to always keep the movement card on top of others
		if(buttondown)
			BitBlt(hdcmemtem,card[link2.type][link2.number].rectcard.left,card[link2.type][link2.number].rectcard.top,card[link2.type][link2.number].rectcard.right-card[link2.type][link2.number].rectcard.left,card[link2.type][link2.number].rectcard.bottom-card[link2.type][link2.number].rectcard.top,card[link2.type][link2.number].hdcmem,0,0,SRCCOPY);

	}
	return hdcmemtem;
}

void setarea(HWND hw,HBITMAP hbmpcover)
{
	RECT rect;
	BITMAP bm1;
	int hgap,wgap,hg,wg,i,j;
	extern HWND hs;
	extern CARDS card[4][13];
	extern PLAYERS player[NO_OF_PLAYERS];
	extern CARDLINKS link1[NO_OF_CARDS*4];
	int type,number,index=0;//This does not disturb the extern index

	rect=FindDisplayRect(hw);
	
	//Assuming the size of the cover and the card is the same
	GetObject(hbmpcover,sizeof(BITMAP),&bm1);

	//Calculating the gaps in between the cards
	wgap=(rect.right-(5*bm1.bmWidth))/6;//5 length,has 6 gaps
	hgap=(rect.bottom-(4*bm1.bmHeight))/6;//4 for cards,has 4 + 2(for middle) gaps
		
	//For layer 1 cards
	for (i=1,hg=hgap;i<=4;++i)
	{
		for (j=1,wg=wgap;j<=4;++j)
		{
			SelectACard(&type,&number);
			card[type][number].PlacedAlready=TRUE;
			card[type][number].visible=FALSE;
			SetUpRect(&(card[type][number].rectcard),wg,bm1.bmWidth,hg,bm1.bmHeight);
			wg=wg+wgap+bm1.bmWidth;
			//Update the link
			link1[index].type=type;
			link1[index].number=number;
			++index;
		}
		hg=hg+hgap+bm1.bmHeight;
		if (i==2)
		{	
			//For double gap in between
			hg=hg+hgap;
		}
	}
	//For Layer 2 cards
	for (i=1,hg=hgap+5;i<=4;++i)
	{
		for (j=1,wg=wgap+5;j<=4;++j)
		{
			SelectACard(&type,&number);
			card[type][number].PlacedAlready=TRUE;
			card[type][number].visible=TRUE;
			SetUpRect(&(card[type][number].rectcard),wg,bm1.bmWidth,hg,bm1.bmHeight);
			wg=wg+wgap+bm1.bmWidth;
			//Update the link
			link1[index].type=type;
			link1[index].number=number;
			++index;
		}
		
		hg=hg+hgap+bm1.bmHeight;
		if (i==2)
		{
			//To SetUp the rect areas of both players here
			SetUpRect(&(player[0].rectarea),rect.left,rect.right,rect.top,hg);
			SetUpRect(&(player[1].rectarea),rect.left,rect.right,hg,rect.bottom-hg);
			//To Introduce the double gap even for layer2
			hg=hg+hgap;
		}
	}
	
}

char *changetotext(long x){
	char *mpx;
	int len=1,i;
	long tmp;
	tmp=x/10;
	while((tmp)>=1)
	{++len;
	tmp=tmp/10;
	}
	mpx=malloc(len+1);//+1 for '\0'
	mpx[0]=48;//Special case for x=0
	tmp=x;
	for (i=len-1;i>=0;--i){
		mpx[i]=(tmp%10)+48;
		tmp=(tmp/10);
	}
	mpx[len]='\0';
	//MessageBox(NULL,mpx,"asd",0);
	
	return mpx;//Dont forget to free this
}

HDC moveimage(HDC hdc,HWND hw,HBITMAP hbmp1,HDC hdcmem1)
{
	BITMAP bm1;
	LPPOINT mp1=NULL;
	extern HWND hs;

	
	GetObject(hbmp1,sizeof(BITMAP),&bm1);
	
	//Find the coordinates of mouse
	GetCursorPos(mp1);
	ScreenToClient(hw,mp1);
	
	BitBlt(hdc,mp1->x-((bm1.bmWidth-150)/2),mp1->y-((bm1.bmHeight-100)/2),bm1.bmWidth,bm1.bmHeight,hdcmem1,150,100,SRCCOPY);
	
	return hdc;
}


// This function needs lot of modifications
//Implementing this idea reduces the performance
HDC doublebuffer(int len,HWND hw,HDC hdc,...){

	HDC hdctem,hdctem2,hdcmask;
	va_list arglist;//Defining the arguments list
	RECT rect;
	extern HWND hs;
	HBITMAP hbmptem,hbmpold,hbmpmask,hbmpmaskold;
	int i;
	
	va_start(arglist,hdc);//Defining the start point in arguments list
	//hw=va_arg (arglist,HWND);//Getting the arguments,if used (arglist,len)
	//hdc=va_arg (arglist,HDC);

	rect=FindDisplayRect(hw);
	hdctem=CreateCompatibleDC(hdc);
	hbmptem=CreateCompatibleBitmap(hdc,rect.right,rect.bottom);
	hbmpold=SelectObject(hdctem,hbmptem);
	FillRect(hdctem,&rect,(HBRUSH)(2));

	//create mask bitmap hdc
	hdcmask=CreateCompatibleDC(hdc);
			
	for (i=0;i<len;++i){
		hdctem2=va_arg (arglist,HDC);
		//First Create the mask 
		//transparent color=RGB(0,127,127)is (HBRUSH)2
		hbmpmask=createmask(hdctem2,hw,RGB(0,127,127));
		hbmpmaskold=SelectObject(hdcmask,hbmpmask);//This can be done globally
		BitBlt(hdctem,0,0,rect.right,rect.bottom,hdcmask,0,0,SRCAND);	
		BitBlt(hdctem,0,0,rect.right,rect.bottom,hdctem2,0,0,SRCPAINT);	
		SelectObject(hdcmask,hbmpmaskold);
		DeleteDC(hdctem2);
		DeleteObject(hbmpmask);
		}
	va_end (arglist);
	return hdctem;
	//SelectObject(hdctem,hbmpold);
	//DeleteDC(hdctem);

}


HBITMAP createmask(HDC hdccolor,HWND hw,COLORREF crtransparent)
{

    HDC hdcmem;
    HBITMAP hbmmask,hbmmaskold;
    RECT rect;
	

    // Create monochrome (1 bit) mask bitmap.  

    GetClientRect(hw,&rect);
	hbmmask = CreateBitmap(rect.right,rect.bottom, 1, 1, NULL);
    
    // Get some HDCs that are compatible with the display driver

	hdcmem = CreateCompatibleDC(0);
    hbmmaskold=SelectObject(hdcmem,hbmmask);
	
    // Set the background colour of the colour image to the colour
    // you want to be transparent.
    SetBkColor(hdccolor, crtransparent);

    // Copy the bits from the colour image to the B+W mask... everything
    // with the background colour ends up white while everythig else ends up
    // black...Just what we wanted.

    BitBlt(hdcmem, 0, 0,rect.right,rect.bottom, hdccolor, 0, 0, SRCCOPY);

    // Take our new mask and use it to turn the transparent colour in our
    // original colour image to black so the transparency effect will
    // work right.
    BitBlt(hdccolor, 0, 0, rect.right, rect.bottom, hdcmem, 0, 0, SRCINVERT);

    // Clean up.
	SelectObject(hdcmem,hbmmaskold);
    DeleteDC(hdcmem);
    
    return hbmmask;

}

//This is a useless function composed of effectively 3 lines but looks neat
//might slowdown the process
void DisplayHDCmem(HDC hdcmemtem)
{
	extern HBITMAP hbmptem;
	BITMAP bm1;
	extern HDC hdcgl;

	GetObject(hbmptem,sizeof(BITMAP),&bm1);
	//Displaying
	BitBlt(hdcgl,0,0,bm1.bmWidth,bm1.bmHeight,hdcmemtem,0,0,SRCCOPY);
	
}

int LoadMyBmp(HBITMAP *phbmp,UINT ID,HDC hdc,HBITMAP *phbmpold,HDC *phdcmem,UINT Uflags)
{
	//First Extract each bits
	//1st bit set=WITH_BMP,2nd=WITH_DC,3rd=WITHOUT_BMP,4th=WITHOUT_DC
	//But we need only the lowest 2 bits
	
	if ((Uflags&0x1)==WITH_BMP){
		//MessageBox(NULL, "Status Bar Creation Failed!", "WITH bmp",MB_ICONEXCLAMATION | MB_OK);
		*phbmp=LoadBitmap(GetModuleHandle(NULL),MAKEINTRESOURCE(ID));
		if (*phbmp==NULL)
			return 0;
	}
	if ((Uflags&0x2)==WITH_DC)
	{
		*phdcmem=CreateCompatibleDC(hdc);
		*phbmpold=SelectObject(*phdcmem,*phbmp);//There is need for hbmpold...
		if(*phdcmem==NULL)
			return 0;
	}
	return 1;
}

void DeleteMyBmp(HBITMAP *phbmp,HBITMAP *phbmpold,HDC *phdcmem,UINT Uflags)
{
	if ((Uflags&0x2)==WITH_DC)
	{
		SelectObject(*phdcmem,*phbmpold);
		DeleteDC(*phdcmem);

	}
	if ((Uflags&0x1)==WITH_BMP){
		DeleteObject(*phbmp);
	}
}

RECT FindDisplayRect(HWND hw)
{
	extern HWND hs;
	RECT rect,rs;
	GetClientRect(hw,&rect);
	GetWindowRect(hs,&rs);
	rect.bottom=rect.bottom-(rs.bottom-rs.top);
	return rect;
}

void SetPlayerDefName(char *pdefname,char *thename)
{
	strcpy(pdefname,thename);
}

HFONT LoadMyFont(UINT fsize,char *fontname)
{
	HDC hdc;
	HFONT hf=NULL;
	long fheight;

	
	//hf=GetStockObject(DEFAULT_GUI_FONT);
	/*Note:This is easiest way to get a descent font and looks much
	  better than the default system font
	*/

	//This is always a good practice
	DeleteObject(hf);
	
	//The other way
	hdc=GetDC(NULL);
	//1st.Convert fsize in points to Logical units
	fheight = -MulDiv(fsize, GetDeviceCaps(hdc, LOGPIXELSY), 72);
	ReleaseDC(NULL,hdc);

	//CreateFont
	hf = CreateFont(fheight, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,fontname);
	if(hf)
    {
		return hf;
    }
    else
    {
        MessageBox(NULL, "Font creation failed!", "Error", MB_OK | MB_ICONEXCLAMATION);
		return NULL;
    } 
	
}

void SetUpRect(RECT *rect,long left,long width,long top,long height)
{
rect->left=left;
rect->right=left+width;
rect->top=top;
rect->bottom=top+height;
}

void DrawPlayerName(HDC hdc,HFONT hf,COLORREF txtcolor,COLORREF bgcolor,int bkmode,PLAYERS player)
{
	HFONT hfold;
	
	SetBkColor(hdc,bgcolor);
	SetTextColor(hdc,txtcolor);
	SetBkMode(hdc,bkmode);
	
	hfold=SelectObject(hdc,hf);
	DrawText(hdc,player.name,-1,&(player.rectname),DT_SINGLELINE|DT_CENTER);//-1 does calculation of the string size automatically
	SelectObject(hdc,hfold);//in the above line DT_VCENTER can be used as well
}

void DrawPlayerPoints(HDC hdc,HFONT hf,COLORREF txtcolor,COLORREF bgcolor,int bkmode,PLAYERS player)
{
	HFONT hfold;
	RECT TemRect;
	char str1[20];
	
	SetBkColor(hdc,bgcolor);
	SetTextColor(hdc,txtcolor);
	SetBkMode(hdc,bkmode);

	SetUpRect(&(TemRect),player.rectname.left,player.rectname.right-player.rectname.left,player.rectname.bottom+5,player.rectname.bottom-player.rectname.top);
	
	hfold=SelectObject(hdc,hf);
	
	wsprintf(str1,"Points = %d",player.points);
	DrawText(hdc,str1,-1,&TemRect,DT_SINGLELINE|DT_CENTER);//-1 does calculation of the string size automatically
	SelectObject(hdc,hfold);//in the above line DT_VCENTER can be used as well
}

void DrawPlayerWinner(HWND hw,HDC hdc,HFONT hf,COLORREF txtcolor,COLORREF bgcolor,int bkmode,PLAYERS player)
{
	HFONT hfold;
	RECT TemRect,rect;
	char str1[20];
	
	SetBkColor(hdc,bgcolor);
	SetTextColor(hdc,txtcolor);
	SetBkMode(hdc,bkmode);

	rect=FindDisplayRect(hw);

	SetUpRect(&(TemRect),rect.left,rect.right,(rect.bottom/2)-20,(rect.bottom/2));
	
	hfold=SelectObject(hdc,hf);
	
	wsprintf(str1,"%s Wins ",player.name);
	DrawText(hdc,str1,-1,&TemRect,DT_SINGLELINE|DT_CENTER);//-1 does calculation of the string size automatically
	SelectObject(hdc,hfold);//in the above line DT_VCENTER can be used as well
}

void CardsInGame(UINT ncards,...)
{
	extern CARDS card[4][13];
	UINT i,j,tem;
	va_list arglist;//Defining the arguments list
	
	//First set all cards to FALSE along with their visiblity as FALSE;
	for(i=0;i<4;++i){
		for(j=0;j<13;++j){
			card[i][j].present=FALSE;
			card[i][j].PlacedAlready=FALSE;
			card[i][j].visible=FALSE;
		}
	}

	if(ncards>13)//There are not more than 13 cards in a type
		ncards=13;
	va_start(arglist,ncards);//Defining the start point in arguments list
	for (i=0;i<ncards;++i){
		tem=va_arg(arglist,UINT);//Assuming that the player passes them as UINT ie only positive integers
		if(tem>13 || tem==0)
			tem=1;		//ie A will be selected
		for(j=0;j<4;++j){//0-3 for each card type eg:spade,clubs...
			card[j][tem-1].present=TRUE;
		}
	}
	
	//Finally you can also remove all the hbmps,hdcmems of cards not going to be present
	va_end(arglist);//This is needed as only an optional statement
}
				 
void SelectACard(int *type,int *number){
	
	extern CARDS card[4][13];

	do
	{
		*type=rand()%4;
		*number=rand()%13;

		//while(card[*type][*number].present==FALSE){
		//	*number=rand()%13;
		//}
	}while((card[*type][*number].PlacedAlready==TRUE) || (card[*type][*number].present==FALSE));
}

void ResetGameData(HWND hw)
{
	extern int WhoseTurn,WhoStarted;
	extern CARDLINKS link2;
	extern PLAYERS player[NO_OF_PLAYERS];
	extern BOOL Animation,buttondown;
	int i;

	DestroyMyButtons(hw);

	//This is actually done in the message loop itself for every new game
	//InitRandomNumbers();//Doing this once in the application is enough, but its ok

	buttondown=FALSE;//This is very important

	CardsInGame(NO_OF_CARDS,1,2,3,9,10,11,12,13);//Understood
	//Resetting the WhoseTurn to point to player1
	WhoseTurn=0;//But reseting of WhoseQuote, WhoseHand is not needed
	WhoStarted=0;//Always player1 starts
	//index=0;//Resetting the link//done while initialisation and in ID_TIMER1

	//reset player data score/points
	for(i=0;i<NO_OF_PLAYERS;++i)
	{
		player[i].score=0;
		player[i].points=0;
	}

}
void ContinueMatch(void)
{
	extern int WhoseTurn,WhoStarted;
	extern CARDLINKS link2;
	extern PLAYERS player[NO_OF_PLAYERS];
	extern BOOL Animation,buttondown;
	int i;

	//This is actually done in the message loop itself for every new game
	//InitRandomNumbers();//Doing this once in the application is enough, but its ok

	buttondown=FALSE;//This is also very important

	CardsInGame(NO_OF_CARDS,1,2,3,9,10,11,12,13);//Understood
	//Calculate WhoseTurn using WhoStarted
	WhoStarted=(WhoStarted+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;
	WhoseTurn=WhoStarted;

	//reset player data points
	for(i=0;i<NO_OF_PLAYERS;++i)
	{
		player[i].points=0;
	}
}

void DestroyMyButtons(HWND hw)
{
 	DestroyWindow(GetDlgItem(hw,IDC_RUN_BUTTON1));//the Continue button
	DestroyWindow(GetDlgItem(hw,IDC_RUN_BUTTON2));//the New Game button
}

void KillAllTimers(HWND hw)
{
	extern int Animation;
	int i;
	for(i=0;i<NO_OF_TIMERS;++i)
		KillTimer(hw,i);
	Animation=FALSE;
}
void LoadNewPlayers(HWND hw)
{
	int ret;

	ret=DialogBox(GetModuleHandle(NULL),MAKEINTRESOURCE(IDD_WELCOME),hw,WelcomeDlgfn);
	if(ret==-1)//ret=IDOK,IDCANCEL if successful
		MessageBox(hw,"Welcome Dialog Creation Failed!","Error!",MB_ICONEXCLAMATION | MB_OK);
}

void InitRandomNumbers()
{
	/*LPSYSTEMTIME alltime;//to store the time
	//Find the time (for generating random numbers)
	GetSystemTime(alltime);
	//Initialise random number generation
	srand(alltime->wMilliseconds);*/

	//The above method was working very bad

}

void GetQuote(HWND hw,HDC hdc)
{
	extern PLAYERS player[NO_OF_PLAYERS];
	extern CARDS card[4][13];
	extern WhoseTurn;
	extern WhoseQuote;
	extern const char *CardType[4];
	extern HWND hs;
	extern char StatusText[MAX_STATUS_LEN];
	char str1[50+MAX_NAME_LEN];
	int i;
	MSG msg;
	int type=0,number=0;
	
	//First display dialog to get quotes
	for (i=0;i<NO_OF_PLAYERS;++i){
		if(DialogBox(GetModuleHandle(NULL),MAKEINTRESOURCE(IDD_GETQUOTE),hw,GetQuoteDlgfn)==-1)
			MessageBox(hw,"Get Quote Dialog Creation Failed!","Error!",MB_ICONEXCLAMATION | MB_OK);
	}
	//Next Process the quote to find whose quote
	if(player[WhoseTurn].quote>=player[(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].quote)
		WhoseQuote=WhoseTurn;
	else
		WhoseQuote=(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;

	//Next  ask for the trump
	wsprintf(StatusText,"Now %s!, It's time to Select the trump",player[WhoseQuote].name);
	MessageBox(hw,StatusText,"Select The Trump Card",0);
	SetWindowText(hs,StatusText);
	
	do
	{
		do
		{
			do
			{
				do{
					GetMessage(&msg,NULL,0,0);
					/*if(msg.message==WM_PAINT)//handle WM_PAINT(FOR ,minimize,move,etc) 
						SendMessage(hw,msg.message,msg.wParam,msg.lParam);//Still needed when u do the following check
					if(msg.message==WM_NCLBUTTONDOWN)//
					{
						wsprintf(str1,"%s!, You cannot get away without selecting the trump",player[WhoseQuote].name);
						MessageBox(hw,str1,"ALARM!!",0);
					}*/
					if(msg.message==WM_SYSKEYDOWN)
					{//do nothing, to prevent the alt+space or alt+f4
					}
					else if(msg.message == WM_CLOSE || (msg.message == WM_NCLBUTTONDOWN && (msg.wParam==HTCLOSE || msg.wParam==HTSYSMENU)) || msg.message == WM_NCRBUTTONDOWN)//not allow to quit
					{//WM_NCHITTEST could also be used and dont wonder what 20 is it is just HTCLOSE
						wsprintf(str1,"%s!, You cannot get away without selecting the trump",player[WhoseQuote].name);
						MessageBox(hw,str1,"ALARM!!",0);
					}
					else if(msg.message==WM_COMMAND && (LOWORD(msg.wParam)==ID_GAME_NEWGAME || LOWORD(msg.wParam)==ID_GAME_SAVEGAME || LOWORD(msg.wParam)==ID_GAME_LOADGAME))//to start another game etc
					{
						wsprintf(str1,"%s!, You cannot get away without selecting the trump",player[WhoseQuote].name);
						MessageBox(hw,str1,"ALARM!!",0);
					}
					else if(msg.message!=WM_RBUTTONDOWN && msg.message!=WM_LBUTTONDOWN)
					{
						TranslateMessage(&msg);
						DispatchMessage(&msg);
					}

				}while(msg.message!=WM_LBUTTONDOWN);
				ScreenToClient(hw,&(msg.pt));
			}while(!IsTurn(WhoseQuote,msg.pt.x,msg.pt.y));
		}while(!(FindCard(msg.pt.x,msg.pt.y,&type,&number)));
		wsprintf(str1,"The trump is %s\nDo you want to continue?",CardType[type]);
	}while(MessageBox(hw,str1,"Confirm Trump",1)!=1);//ok=1,cancel=2

	player[WhoseQuote].TrumpCard=type;

	wsprintf(StatusText,"%s! You have to play first this time.Drag the card to the box or Right click on the card",player[WhoseTurn].name);
	SetWindowText(hs,StatusText);

}


BOOL FindCard(long x,long y,int *type,int *number)
{
	int i,j;
	extern CARDS card[4][13];
	for(i=0;i<4;++i)
	{
		for (j=0;j<13;++j)
		{
			if(card[i][j].visible==TRUE && card[i][j].present==TRUE){
				//Not use >= or <= because it may cause problems later
				if(x>(card[i][j].rectcard.left) && (x<card[i][j].rectcard.right) && (y>card[i][j].rectcard.top) && (y<card[i][j].rectcard.bottom))
				{
				*type=i;
				*number=j;
				return TRUE;
				}
			}
		}
	}
	return FALSE;

}

BOOL IsTurn(int turn,long x,long y)
{
	extern PLAYERS player[NO_OF_PLAYERS];
	if (y>player[turn].rectarea.top && y<player[turn].rectarea.bottom)
		return TRUE;

	return FALSE;
	
}

//Lot of work can still be done to improve this function
BOOL RectOverlap(RECT rect1,RECT rect2)//rect2 overlap on rect1
{
	
	//The check is like >,< because overlap does not mean even when they are next to each other
	//Remember this is no hittest
	if((rect2.bottom>rect1.top && rect2.bottom<rect1.bottom) || (rect2.top>rect1.top && rect2.top<rect1.bottom))
		if((rect2.right>rect1.left && rect2.right<rect1.right) || (rect2.left>rect1.left && rect2.left<rect1.right))
			return TRUE;


	//One should also do the check for complete overlap
	/*This function cna be modified to calculate the intersection rect
	for eg:
	flag=0;
	if((rect2.bottom>rect1.top && rect2.bottom<rect1.bottom))
	{
		flag=1;
		rect3.top=rect1.top;rect3.bottom=rect2.bottom
	}
	else if	(rect2.top>rect1.top && rect2.top<rect1.bottom)
	{
		flag=1;
		rect3.top=rect2.top;rect3.bottom=rect1.bottom
	}

	if(flag==1 && if((rect2.right>rect1.left && rect2.right<rect1.right))
	{
		rect3.left=rect1.left;rect3.right=rect2.right;
		return TRUE;
	else if (flag==1 && rect2.left>rect1.left && rect2.left<rect1.right)
	{
		rect3.left=rect2.left;rect3.right=rect1.right;
		return FALSE;
	}
	*/
	return FALSE;
}

BOOL RectEqual(RECT rect1,RECT rect2)
{
	if(rect1.left==rect2.left && rect1.right==rect2.right && rect1.top==rect2.top && rect1.bottom==rect2.bottom)
		return TRUE;
	return FALSE;
}

void MakeVisible(HWND hw)
{
	extern CARDS card[4][13];
	int i,j,k,l;
	BOOL NoneAbove=TRUE;//a flag Assuming that no card is above
	
	//First Find if any card overlap's then do not make visible
	for(i=0;i<4;++i)
	{
		for(j=0;j<13;++j)
		{
			//For each card that is masked
			if(card[i][j].present==TRUE && card[i][j].visible==FALSE)
			{
				//check for overlap with a visible card
				for(k=0;k<4;++k)
				{
					for(l=0;l<13;++l)
					{
						if(card[k][l].visible==TRUE && RectOverlap(card[i][j].rectcard,card[k][l].rectcard))
						{
							NoneAbove=FALSE;//means there is overlap
						}
					}
				}
				if(NoneAbove)
					 card[i][j].visible=TRUE;
				else
				NoneAbove=TRUE;
			}
		}
	}

}
