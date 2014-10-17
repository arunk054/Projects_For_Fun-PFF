#include "fns.h"
#include "resource.h"

BOOL _stdcall WelcomeDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z)
{
	extern PLAYERS player[NO_OF_PLAYERS];
	switch(msg)
    {
        case WM_INITDIALOG:
			SetDlgItemText(hw,IDC_PLAYER1,player[0].defname);
			SetDlgItemText(hw,IDC_PLAYER2,player[1].defname);
			CheckRadioButton(hw,IDC_RADIO1,IDC_RADIO2,IDC_RADIO1);
			break;
        case WM_COMMAND:
            switch(LOWORD(y))
            {
                case IDOK:
					{
						//This is to store the players names
						GetDlgItemText(hw,IDC_PLAYER1,player[0].name,MAX_NAME_LEN+1);
						GetDlgItemText(hw,IDC_PLAYER2,player[1].name,MAX_NAME_LEN+1);
						if (player[0].name[0]=='\0' || !strcmp(player[0].name,player[1].name))//names are equal
							strcpy(player[0].name,player[0].defname);

						if (player[1].name[0]=='\0' || !strcmp(player[0].name,player[1].name))
							strcpy(player[1].name,player[1].defname);

						EndDialog(hw, IDOK);
					}
                break;
                case IDCANCEL:
					if (MessageBox(hw,"Do u really want to!!","Im Serious",4)==6)
					PostQuitMessage(0);//the return value wparam is 0 for success
                break;
			}
        break;
        default:
            return FALSE;
    }
    return TRUE;

}

BOOL _stdcall GetQuoteDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z)
{
	extern HWND hs;
	extern PLAYERS player[NO_OF_PLAYERS];
	extern int WhoseTurn;
	switch(msg)
    {
        case WM_INITDIALOG:
			{
				extern char StatusText[MAX_STATUS_LEN];
				//Set where it appears
				SetWindowPos(hw,NULL,player[WhoseTurn].rectname.left+30,player[WhoseTurn].rectname.top,0,0,SWP_NOSIZE);
				//To display the name of the player
				wsprintf(StatusText,"%s! please enter your quote",player[WhoseTurn].name);
				SetDlgItemText(hw,IDC_STATIC1,StatusText);
				//Set an default value
				SetDlgItemText(hw,IDC_QUOTE,"44");
				//Update Status Bar
				SetWindowText(hs,StatusText);

			}
			break;
        case WM_COMMAND:
            switch(LOWORD(y))
            {
                case IDOK:
					{	
						BOOL bSuccess;
						int quote;
						quote=GetDlgItemInt(hw, IDC_QUOTE, &bSuccess, FALSE);
						if(quote>=44 && quote<=88)
						{
							player[WhoseTurn].quote=quote;
							WhoseTurn=(WhoseTurn+NO_OF_PLAYERS-1)%NO_OF_PLAYERS;//change WhoseTurn
							EndDialog(hw, IDOK);
						}
						else
							MessageBox(hw,"Your Quote must be between 44 and 88","ERROR INPUT!!",0);
					}
                break;
			}
        break;
        default:
            return FALSE;
    }
    return TRUE;

}

BOOL _stdcall OptionsDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z)
{
	extern int Timer1Speed,Timer2Step,MaxScore;
	switch(msg)
    {
        case WM_INITDIALOG:
			{
				SendDlgItemMessage(hw,IDC_SLIDER1,TBM_SETPOS,1,100-ChangeToSlide(TIMER1_MIN,TIMER1_MAX,Timer1Speed));
				SendDlgItemMessage(hw,IDC_SLIDER2,TBM_SETPOS,1,ChangeToSlide(TIMER2_MIN,TIMER2_MAX,Timer2Step));
				SendDlgItemMessage(hw,IDC_SPIN1,UDM_SETRANGE,0,MAX_SCORE);
				SendDlgItemMessage(hw,IDC_SPIN1,UDM_SETPOS,1,MaxScore);
			}
			break;
        case WM_COMMAND:
            switch(LOWORD(y))
            {
                case IDOK:
					{
						
						Timer1Speed=SlideToValue(TIMER1_MIN,TIMER1_MAX,100-SendDlgItemMessage(hw,IDC_SLIDER1,TBM_GETPOS,0,0));
						Timer2Step=SlideToValue(TIMER2_MIN,TIMER2_MAX,SendDlgItemMessage(hw,IDC_SLIDER2,TBM_GETPOS,0,0));
						if(SendDlgItemMessage(hw,IDC_SPIN1,UDM_GETPOS,0,0)<MIN_SCORE || SendDlgItemMessage(hw,IDC_SPIN1,UDM_GETPOS,0,0)>MAX_SCORE)
						{
							SendDlgItemMessage(hw,IDC_SPIN1,UDM_SETPOS,0,MIN_SCORE);
						}
						MaxScore=SendDlgItemMessage(hw,IDC_SPIN1,UDM_GETPOS,0,0);

						EndDialog(hw,IDOK);
					}
					break;

				case IDCANCEL:
					EndDialog(hw,IDCANCEL);
					break;

			}
        break;
		case WM_NOTIFY:
			switch(LOWORD(y))
			{
				case IDC_SPIN1:
					break;
			}
			break;
		default:
            return FALSE;
    }
    return TRUE;

}

BOOL _stdcall CreditsDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z)
{
	switch(msg)
    {
        case WM_INITDIALOG:
			
			break;
        case WM_COMMAND:
            switch(LOWORD(y))
            {
                case IDOK:
					EndDialog(hw, IDOK);
                break;
			}
        break;
        default:
            return FALSE;
    }
    return TRUE;

}

BOOL _stdcall ScoreDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z)
{
	extern int MaxScore;
	extern PLAYERS player[NO_OF_PLAYERS];

	char str1[50];

	switch(msg)
    {
        case WM_INITDIALOG:

			SetDlgItemInt(hw,IDC_MAXDISPLAY,MaxScore,FALSE);
			wsprintf(str1,"\n	%s	=	%d\n\n\n\n\n	%s	=	%d",player[0].name,player[0].score,player[1].name,player[1].score);
			SetDlgItemText(hw,IDC_SCORES,str1);
			
			SendDlgItemMessage(hw,IDC_PROGRESS1,PBM_DELTAPOS,ChangeToSlide(0,MaxScore,abs(player[0].score)),0);

			break;
        
		case WM_COMMAND:
            switch(LOWORD(y))
            {
                case IDOK:
					{
					EndDialog(hw, IDOK);
					}
                break;
			
				case IDCANCEL:
					EndDialog(hw,IDCANCEL);
				break;
			}
        break;

        default:
            return FALSE;
    }
    return TRUE;

}

BOOL _stdcall StatsDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z)
{
	extern PLAYERS player[NO_OF_PLAYERS];
	extern int WhoseQuote;
	extern BOOL gamestarted;
	extern const char *CardType[4];
	
	switch(msg)
    {
        case WM_INITDIALOG:
			{
				char str1[100];
				if(gamestarted)
					wsprintf(str1,"\n\n%s has quoted for %d\n\n	AND \n\n The Trump card is -%s -",player[WhoseQuote].name,player[WhoseQuote].quote,CardType[player[WhoseQuote].TrumpCard]);
				else
					wsprintf(str1,"\n\nYou can view the Stats only when you are in the middle of a game");
				SetDlgItemText(hw,IDC_STATS,str1);
			}
			break;
        case WM_COMMAND:
            switch(LOWORD(y))
            {
                case IDOK:
					EndDialog(hw, IDOK);
                break;
				 case IDCANCEL:
					EndDialog(hw, IDOK);
                break;
			}
        break;
        default:
            return FALSE;
    }
    return TRUE;
}

BOOL _stdcall PointsMapDlgfn(HWND hw,UINT msg,WPARAM y,LPARAM z)
{
	extern HWND hwpm;
	switch(msg)
    {
        case WM_INITDIALOG:
			
			break;
        case WM_COMMAND:
            switch(LOWORD(y))
            {
                case IDOK:
					hwpm=NULL;
					EndDialog(hw, IDOK);
                break;
				case IDCANCEL:
					hwpm=NULL;
					EndDialog(hw, IDOK);
                break;
			}
        break;
        default:
            return FALSE;
    }
    return TRUE;

}