#ifndef STRUCTS_H
#define STRUCTS_H

typedef struct
{
char defname[MAX_NAME_LEN+1];	//default name displayed
char name[MAX_NAME_LEN+1];	//name typed
short int score;	//score negative/positive
UINT points;//The points after every game this cannot be negative
RECT rectname;	//area where name displayed on screen
RECT rectcard;//area where card to be put
RECT rectarea;//the player's cards area
UINT quote;
int TrumpCard;//the type of the card is stored that is the trump for the game
}PLAYERS;

typedef struct
{
	HBITMAP hbmp;
	HBITMAP hbmpold;
	HDC hdcmem;
	BOOL present;
	BOOL PlacedAlready;//this is set to FALSE by default
	BOOL visible;//this is set to FALSE by default
	RECT rectcard;//each card's rect
}CARDS;

typedef struct
{
	int type;
	int number;
}CARDLINKS;

#endif