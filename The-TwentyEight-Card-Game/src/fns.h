#ifndef FNS_H
#define FNS_H

#include<windows.h>
#include<commctrl.h>
#include "defins.h"
#include "structs.h"

HDC setupbgnd(HDC hdcmemtem,HWND hw,HBITMAP hbmpbg,HDC hdcmembg,HDC hdcmembox);
HDC RedrawArea(HDC hdcmemtem,HWND hw,HBITMAP hbmpcover,HDC hdcmemcover);
void setarea(HWND hw,HBITMAP hbmp1);
HDC moveimage(HDC hdcmem,HWND,HBITMAP,HDC);//This is to move the bitmap when mouse drag is done
void DisplayHDCmem(HDC hdcmemtem);
//This funtion returns the HDC mem and also will store the return of SelectObject()
//THis function used only when less no of bitmaps
int LoadMyBmp(HBITMAP *phbmp,UINT ID,HDC hdc,HBITMAP *phbmpold,HDC *hdcmem,UINT Uflags);//1st,2nd,3rd,4th,5th are for bmp to load,Device Context,ID of bitmap,return bitmap,HDC mem are both global
//This is to Select the oldbmp then do DeleteDC() and DeleteObject()
void DeleteMyBmp(HBITMAP *phbmp,HBITMAP *phbmpold,HDC *phdcmem,UINT Uflags);
RECT FindDisplayRect(HWND hw);
void GetQuote(HWND,HDC);

//PLAYERS
void SetPlayerDefName(char*,char*);
void SetUpRect(RECT*,long left,long width,long top,long height);
void DrawPlayerName(HDC hdc,HFONT hf,COLORREF txtcolor,COLORREF bgcolor,int bkmode,PLAYERS player);
void DrawPlayerPoints(HDC hdc,HFONT hf,COLORREF txtcolor,COLORREF bgcolor,int bkmode,PLAYERS player);
void DrawPlayerWinner(HWND hw,HDC hdc,HFONT hf,COLORREF txtcolor,COLORREF bgcolor,int bkmode,PLAYERS player);

//CARDS
void CardsInGame(UINT ncards,...);//1st is the number of cards next will be the card numbers
void SelectACard(int *type,int *number);
void ResetGameData(HWND hw);
void ContinueMatch(void);
void InitRandomNumbers();
void KillAllTimers(HWND hw);
void DestroyMyButtons(HWND hw);
void LoadNewPlayers(HWND);
BOOL RectOverlap(RECT rect1,RECT rect2);//rect2 overlap on rect1
BOOL RectEqual(RECT rect1,RECT rect2);
void MakeVisible(HWND);//this is an automatic function called after every round

//GAME
BOOL FindCard(long x,long y,int *type,int *number);
BOOL IsTurn(int turn,long x,long y);
int CardsInBox(void);
BOOL IsCorrectCard(HWND,int type);
void FindWhoseHand(HWND);
int PointsMap(int number);//number is not array index but card number
BOOL IsGameOver(void);
void DisplayPoints(HWND hw);
BOOL UpdateScore(void);
void CreateMyControl(HWND hw,int id,const char str1[]);
BOOL OpenSave(HWND hw);
BOOL OpenLoad(HWND hw);
BOOL SaveGame(HWND,char *);
BOOL LoadGame(HWND hw,char *flnm);
BOOL IsTrumpFile(char *flnm);

//FONT
HFONT LoadMyFont(UINT fsize,char *fontname);
HFONT SelectMyFont(HDC hdc,HFONT hf);//returns the font previously selected in the HDC

//VIEW
void OpenOptions(HWND);
int ChangeToSlide(float min,float max,float value);
int SlideToValue(float min,float max,float slide);
void OpenCredits(HWND hw);
void OpenScore(HWND hw);
void OpenStats(HWND hw);
void OpenPointsMap(HWND hw);

//Dialog Functions Start Here
BOOL _stdcall WelcomeDlgfn(HWND,UINT,WPARAM,LPARAM);
BOOL _stdcall GetQuoteDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z);
BOOL _stdcall OptionsDlgfn(HWND,UINT,WPARAM,LPARAM);
BOOL _stdcall CreditsDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z);
BOOL _stdcall ScoreDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z);
BOOL _stdcall StatsDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z);
BOOL _stdcall PointsMapDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z);

//useless fns
char *changetotext(long);
HDC doublebuffer(int,HWND,HDC,...);//1st argument=no of HDCs,2nd=HWND,3rd=HDC OF Device,rest HDCs
HBITMAP createmask(HDC,HWND,COLORREF);
#endif
