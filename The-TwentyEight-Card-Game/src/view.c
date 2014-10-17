#include "fns.h"
#include "resource.h"

void OpenOptions(HWND hw)
{
	int ret;

	ret=DialogBox(GetModuleHandle(NULL),MAKEINTRESOURCE(IDD_OPTIONS),hw,OptionsDlgfn);
	if(ret==-1)//ret=IDOK,IDCANCEL if successful
		MessageBox(hw,"Welcome Dialog Creation Failed!","Error!",MB_ICONEXCLAMATION | MB_OK);
}

int ChangeToSlide(float min,float max,float value)
{
	float tem;

	tem=(value-min)/(max-min);
	
	return tem*100;
}

int SlideToValue(float min,float max,float slide)
{
	float tem;
	
	tem=((slide/100)*(max-min))+min;

	return tem;
}

void OpenCredits(HWND hw)
{
	int ret;

	ret=DialogBox(GetModuleHandle(NULL),MAKEINTRESOURCE(IDD_CREDITS),hw,CreditsDlgfn);
	if(ret==-1)//ret=IDOK,IDCANCEL if successful
		MessageBox(hw,"Credits Dialog Creation Failed!","Error!",MB_ICONEXCLAMATION | MB_OK);
}
void OpenScore(HWND hw)
{
	int ret;

	ret=DialogBox(GetModuleHandle(NULL),MAKEINTRESOURCE(IDD_SCORE),hw,ScoreDlgfn);
	if(ret==-1)//ret=IDOK,IDCANCEL if successful
		MessageBox(hw,"Score Dialog Creation Failed!","Error!",MB_ICONEXCLAMATION | MB_OK);
}

void OpenStats(HWND hw)
{
	int ret;

	ret=DialogBox(GetModuleHandle(NULL),MAKEINTRESOURCE(IDD_GAMESTATS),hw,StatsDlgfn);
	if(ret==-1)//ret=IDOK,IDCANCEL if successful
		MessageBox(hw,"Stats Dialog Creation Failed!","Error!",MB_ICONEXCLAMATION | MB_OK);
}

void OpenPointsMap(HWND hw)
{
	extern HWND hwpm;

	if(hwpm==NULL)
	{
		hwpm = CreateDialog(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_POINTSMAP),hw, PointsMapDlgfn);
			if(hwpm != NULL)
			{
	            ShowWindow(hwpm, SW_SHOW);
			}
			else
			{
	            MessageBox(hw, "CreateDialog returned NULL", "Warning!",  
		            MB_OK | MB_ICONINFORMATION);
			}
	}
		
}