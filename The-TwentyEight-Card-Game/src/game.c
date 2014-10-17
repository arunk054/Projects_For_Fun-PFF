#include "fns.h"
#include "resource.h"
#include<stdio.h>

BOOL IsCorrectCard(HWND hw,int type)
{
	extern CARDS card[4][13];
	extern PLAYERS player[NO_OF_PLAYERS];
	extern int WhoseTurn;
	extern const char *CardType[4];
	int HowMany,i,j,PreviousType=0;//gen initialize
	char str1[MAX_STATUS_LEN+MAX_NAME_LEN];

	//First find the no of cards in the box
	HowMany=CardsInBox();

	if(HowMany==2)
	{
		
		MessageBox(hw,"You have already placed your card","Wait",0);
		return FALSE;//This means both boxes are full
	}
	else if (HowMany==1)//Only one card so checking required
	{
		//loop to find the card's type that is in the box already
		for(i=0;i<4;++i)
		{
			for(j=0;j<13;++j)
			{
				if(RectEqual(card[i][j].rectcard,player[(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].rectcard))
				{
					PreviousType=i;
					j=12;
					i=3;//This is to break the whole loop
				}
			}
		}

		//Check for the passed card's type with PreviousType
		if(type!=PreviousType)
		{
			//Find why the player is giving card of another type
			//Loop checks if the player has a card of previous type
			for(i=0;i<4;++i)
			{
				for(j=0;j<13;++j)
				{
					//This is to find the card of this player which is of Previous Type
					if(RectOverlap(player[WhoseTurn].rectarea,card[i][j].rectcard) && card[i][j].present==TRUE && card[i][j].visible==TRUE && i==PreviousType)
					{
						wsprintf(str1," %s! This is %s round and i guess you have it",player[WhoseTurn].name,CardType[i]);
						MessageBox(hw,str1,"Wrong Card",0);
						return FALSE;
					}
				}
			}
		}
			
		return TRUE;//this is because type==PreviousType or the player is right

	}
	else
		return TRUE;//no card in any box so return true

}

int CardsInBox(void)
{
	extern CARDS card[4][13];
	extern PLAYERS player[NO_OF_PLAYERS];
	int i,j,k,tem=0;
	for(i=0;i<4;++i)
	{
		for(j=0;j<13;++j)
		{
			for(k=0;k<NO_OF_PLAYERS;++k)
			{
				if(card[i][j].visible==TRUE && RectEqual(card[i][j].rectcard,player[k].rectcard))
					++tem;
			}

		}
	}
	return tem;
}

void FindWhoseHand(HWND hw)
{

	//When this function is called the turn is the one who had started the round

	extern PLAYERS player[NO_OF_PLAYERS];
	extern CARDS card[4][13];
	extern int WhoseQuote,WhoseTurn,WhoseHand;
	int i,j,k;
	CARDLINKS link[NO_OF_PLAYERS];


	for(k=0;k<NO_OF_PLAYERS;++k)
	{
		for(i=0;i<4;++i)
		{
			for(j=0;j<13;++j)
			{
				if(card[i][j].visible==TRUE && RectEqual(player[k].rectcard,card[i][j].rectcard))
				{
					link[k].type=i;
					link[k].number=j;
					break;
				}
			}
		}
	}
	if(link[WhoseTurn].type==link[(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].type)
	{
		if(PointsMap(link[(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].number+1)>PointsMap(link[WhoseTurn].number+1))
			WhoseHand=(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;
		else if(PointsMap(link[(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].number+1)<PointsMap(link[WhoseTurn].number+1))
			WhoseHand=WhoseTurn;
		else//equal
		{
			if(link[(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].number==0 || link[(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].number==12)//checking who has Ace/King
				WhoseHand=(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;
			else
				WhoseHand=WhoseTurn;
		}
	}
	else//type inequal
	{//find who has put the trump
		if(link[(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].type==player[WhoseQuote].TrumpCard)
			WhoseHand=(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;
		else if(link[WhoseTurn].type==player[WhoseQuote].TrumpCard)
			WhoseHand=WhoseTurn;
		else
			WhoseHand=WhoseTurn;//the one who had started since none have put the trump
	}

	//Points calculation starts
	//The Total points of the round is updated
	for(i=0;i<NO_OF_PLAYERS;++i)
	{
		player[WhoseHand].points+=PointsMap(link[i].number+1);
	}

}

int PointsMap(int number)//number is not array index but card number
{
	switch(number)
	{
	case 1:
		return 1;
	case 2:
		return 10;
	case 3:
		return 5;
	case 9:
		return 2;
	case 10:
		return 1;
	case 11:
		return 3;
	case 12:
		return 0;
	case 13:
		return 0;
	default:
		return -1;
	}
}

BOOL IsGameOver(void)
{
	extern CARDS card[4][13];
	int i,j;

	for(i=0;i<4;++i)
	{
		for(j=0;j<13;++j)
		{
			if (card[i][j].present==TRUE)
				return FALSE;//atleast one card is present
		}
	}
	return TRUE;//no cards are present
}

void DisplayPoints(HWND hw)
{
	extern CARDS card[4][13];
	extern PLAYERS player[NO_OF_PLAYERS];
	extern HFONT hfgl;
	extern int Timer3Speed;

	BITMAP bm1;
	RECT rect;
	int wgap,hgap,wg,hg,i,j,k;
	int wno;

	rect=FindDisplayRect(hw);
	
	GetObject(card[0][0].hbmp,sizeof(BITMAP),&bm1);

	//Calculating the gaps in between the cards//rect.right-160 is the left of player's rectname
	wgap=(player[0].rectname.left)/(NO_OF_CARDS*2);//a player could have had max NO_OF_CARDS*4 cards so 16 in one row
	hgap=(rect.bottom-(4*bm1.bmHeight))/6;//4 for cards,has 4 + 2(for middle) gaps

	//First for player1
	//Initialise tem variables
	wg=0;
	hg=hgap;
	wno=0;

	for(k=0;k<NO_OF_PLAYERS;++k)
	{

		for(i=0;i<4;++i)
		{
			for(j=0;j<13;++j)
			{
				if(RectOverlap(player[k].rectarea,card[i][j].rectcard))
				{
					card[i][j].visible=TRUE;
					SetUpRect(&(card[i][j].rectcard),wg,bm1.bmWidth,hg,bm1.bmHeight);
					++wno;
					wg=wg+wgap;
					if(wno==16)
					{
						wno=0;
						wg=0;
						hg=hg+hgap+bm1.bmHeight;
					}
	
				}
			}
		}
	
		//Next for player2
		//tem variables
		wg=0;
		hg=(hgap*5)+(bm1.bmHeight*2);//actually hgap*4, but for more space
		wno=0;
	}

	//Now the winner will be displayed
	if(!SetTimer(hw,ID_TIMER3,Timer3Speed,NULL))//returns 0 on error
		MessageBox(hw,"Unable to load timer","ERROR!",0);
}

BOOL UpdateScore(void)
{
	extern PLAYERS player[NO_OF_PLAYERS];
	extern int WhoseQuote;
	extern int MaxScore;
	int i;
	
	if(player[WhoseQuote].points>=player[WhoseQuote].quote)//player[(WhoseQuote+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].points)
	{
		player[WhoseQuote].score+=1;
		player[(WhoseQuote+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].score+=-1;
	}
	else
	{
		player[WhoseQuote].score+=-2;
		player[(WhoseQuote+NO_OF_PLAYERS-1)%NO_OF_PLAYERS].score+=2;
	}

	for(i=0;i<NO_OF_PLAYERS;++i)
	{
		if(player[i].score>=MaxScore)
			return TRUE;
	}

	return FALSE;
}

void CreateMyControl(HWND hw,int id,const char str1[])
{
	HWND hb;
	RECT rect;


	rect=FindDisplayRect(hw);

	//Button Control
	hb = CreateWindowEx(WS_EX_CLIENTEDGE,"BUTTON",str1,WS_CHILD | WS_VISIBLE, //BS_DEFPUSHBUTTON makes it  like a def button 
	(rect.right/2)-37,(rect.bottom/2),75, 25, hw, (HMENU)id, GetModuleHandle(NULL), NULL);
	if(hb == NULL)
	MessageBox(hw, "Could not create Continue Button", "Error", MB_OK | MB_ICONERROR);

}

BOOL OpenSave(HWND hw)
{
	extern const char MyExtension[];
	extern BOOL Animation,gamestarted;
	OPENFILENAME *ofn;
	char flnm[MAX_PATH] = "";//MAX_PATH is defined in windows.h for the maximum allowed path in windows
	//char str1[50];

	//Dont show this dialog under these conditions
	if(!gamestarted)
	{
		MessageBox(hw,"You can save the game only if u r playing it","Game not yet Started",0);
		return FALSE;
	}
	else if(Animation)
	{
		MessageBox(hw,"Wait until all cards are STATIC","Cards In Motion",0);
		return FALSE;
	}
	
	ofn=malloc(sizeof(OPENFILENAME));
	ZeroMemory(ofn, sizeof(OPENFILENAME));//This is needed to call GetSaveFileName


	ofn->lStructSize = sizeof(OPENFILENAME);
	ofn->hwndOwner = hw;
	//wsprintf(str1,"Trump Files (*.%s)\0*.%s\0",MyExtension,MyExtension);//All Files (*.*)\0*.*\0;

	ofn->lpstrFilter = "Trump Files (*.726)\0*.726\0";
	ofn->lpstrFile = flnm;
	ofn->nMaxFile = MAX_PATH;
	ofn->lpstrDefExt = MyExtension;
	ofn->Flags = OFN_EXPLORER | OFN_PATHMUSTEXIST | OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT | OFN_FILEMUSTEXIST;
	ofn->lpstrTitle="Save This Damn Game!";

	if(GetSaveFileName(ofn))//This means that the save button is clicked
	{
		return SaveGame(hw, flnm);
	}

	return TRUE;
}

BOOL SaveGame(HWND hw,char *flnm)
{
	extern int Timer1Speed,Timer2Step,MaxScore;
	extern BOOL buttondown;
	extern PLAYERS player[NO_OF_PLAYERS];
	extern CARDS card[4][13];
	extern CARDLINKS link1[NO_OF_CARDS*4];
	extern CARDLINKS link2;
	extern RECT OldRect;
	extern int WhoseTurn;//This value is the player index whose turn it is
	extern int WhoseQuote;//This value is the player index whose quote it is
	extern int WhoseHand;//This is after one round, it will tell whose hand it was
	extern int WhoStarted;//This will keep track of switching between players turn after every game
	extern char StatusText[MAX_STATUS_LEN];

	FILE *fp;
	int i,j;

	//Check if the file is saved as MyExtension only adn check for fp==NULL
	if(!IsTrumpFile(flnm) || (fp=fopen(flnm,"w"))==NULL)
	{
		MessageBox(hw,"Save Failed","Error!",0);
		return FALSE;
	}
	//part0
	//fprintf(fp,"******************************************************************\n*Now that u r readin this thing, i would like to just warn u that*\n*changing any character of this thing could not only have adverse*\n*effects on all ur gorgeous \"windows\" but also on ur this very   *\n*broad mind.--All's Well That Ends Well--Let me end this by givin*\n*u a very good tip.Just associate this file to \"Always Open With\"*\n*the game's exe file and that's it,u no more have to trouble ur  *\n*NOTEPAD, all u have to do is just Double-Click on this thing.   *\n******************************************************************\n");

	//part1
	fprintf(fp,"%d %d %d %d\n",Timer1Speed,Timer2Step,MaxScore,buttondown);
	//part2
	for(i=0;i<NO_OF_CARDS*4;++i)
		fprintf(fp,"%d %d\n",link1[i].type,link1[i].number);
	//part3
	fprintf(fp,"%d %d\n",link2.type,link2.number);
	//part4
	fprintf(fp,"%d %d %d %d\n",OldRect);
	//part5
	//The rectarea is also stored because it is initialised only by setarea() function.But we need it when Loading with file
	for(i=0;i<NO_OF_PLAYERS;++i)
		fprintf(fp,"%s\n%d %d %d %d %d %d %d %d\n",player[i].name,player[i].quote,player[i].score,player[i].TrumpCard,player[i].rectarea,player[i].points);
	//part6
	for(i=0;i<4;++i)
		for(j=0;j<13;++j)
			fprintf(fp,"%d %d %d %d %d %d\n",card[i][j].present,card[i][j].rectcard,card[i][j].visible);
	//part7
	fprintf(fp,"%d %d %d %d\n",WhoseTurn,WhoseQuote,WhoseHand,WhoStarted);
	//As always finally the status bar
	fprintf(fp,"%s",StatusText);

	fclose(fp);
	return TRUE;
}

BOOL OpenLoad(HWND hw)
{
	extern const char MyExtension[];
	OPENFILENAME ofn;
	char flnm[MAX_PATH] = "";//MAX_PATH is defined in windows.h for the maximum allowed path in windows
	//char str1[50];

	
	ZeroMemory(&ofn, sizeof(ofn));//This is needed to call GetSaveFileName

	
	ofn.lStructSize = sizeof(ofn);
	ofn.hwndOwner = hw;
//	wsprintf(str1,"Trump Files (*.%s)\0*.%s\0",MyExtension,MyExtension);//"All Files (*.*)\0*.*\0\0"
	
	ofn.lpstrFilter = "Trump Files (*.726)\0*.726\0";
	ofn.lpstrFile = flnm;
	ofn.nMaxFile = MAX_PATH;
	ofn.lpstrDefExt = MyExtension;
	ofn.Flags = OFN_EXPLORER | OFN_PATHMUSTEXIST | OFN_HIDEREADONLY | OFN_FILEMUSTEXIST;
	ofn.lpstrTitle="Did you mean L-O-A-D?";

	if(GetOpenFileName(&ofn))//This means that the Load button is clicked
	{
		return LoadGame(hw, flnm);
	}

	return FALSE;//return false even if cancel button is pressed
}

BOOL LoadGame(HWND hw,char *flnm)
{
	extern int Timer1Speed,Timer2Step,MaxScore;
	extern BOOL buttondown;
	extern PLAYERS player[NO_OF_PLAYERS];
	extern CARDS card[4][13];
	extern CARDLINKS link1[NO_OF_CARDS*4];
	extern CARDLINKS link2;
	extern RECT OldRect;
	extern int WhoseTurn;//This value is the player index whose turn it is
	extern int WhoseQuote;//This value is the player index whose quote it is
	extern int WhoseHand;//This is after one round, it will tell whose hand it was
	extern int WhoStarted;//This will keep track of switching between players turn after every game
	extern char StatusText[MAX_STATUS_LEN];

	FILE *fp;
	int i,j;

	//Check if the file is saved as MyExtension only adn check for fp==NULL
	if(!IsTrumpFile(flnm) || (fp=fopen(flnm,"r"))==NULL)
	{
		MessageBox(hw,"Load Failed","Error!",0);
		return FALSE;
	}
	//part1
	fscanf(fp,"%d %d %d %d\n",&Timer1Speed,&Timer2Step,&MaxScore,&buttondown);
	//part2
	for(i=0;i<NO_OF_CARDS*4;++i)
		fscanf(fp,"%d %d\n",&(link1[i].type),&(link1[i].number));
	//part3
	fscanf(fp,"%d %d\n",&link2.type,&link2.number);
	//part4
	fscanf(fp,"%d %d %d %d\n",&OldRect.left,&OldRect.top,&OldRect.right,&OldRect.bottom);
	//part5
	for(i=0;i<NO_OF_PLAYERS;++i)
		fscanf(fp,"%[^\n]%d %d %d %d %d %d %d %d\n",player[i].name,&player[i].quote,&player[i].score,&player[i].TrumpCard,&player[i].rectarea.left,&player[i].rectarea.top,&player[i].rectarea.right,&player[i].rectarea.bottom,&player[i].points);
	//part6
	for(i=0;i<4;++i)
		for(j=0;j<13;++j)
			fscanf(fp,"%d %d %d %d %d %d\n",&card[i][j].present,&card[i][j].rectcard.left,&card[i][j].rectcard.top,&card[i][j].rectcard.right,&card[i][j].rectcard.bottom,&card[i][j].visible);
	//part7
	fscanf(fp,"%d %d %d %d\n",&WhoseTurn,&WhoseQuote,&WhoseHand,&WhoStarted);
	//As always finally the status bar
	fscanf(fp,"%[^\n]",StatusText);

	fclose(fp);
	return TRUE;
}

BOOL IsTrumpFile(char *flnm)
{
	extern const char MyExtension[];
	int i;

	for(i=strlen(flnm)-1;i>0;--i)
	{
		if(flnm[i]=='.')
			break;
	}
	if(!strcmp(MyExtension,flnm+i+1))//0 if they are equal
		return TRUE;
	else 
		return FALSE;
}
