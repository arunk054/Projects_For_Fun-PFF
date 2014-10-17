
#ifndef DEFINS_H
#define DEFINS_H

//Run time Controls
#define IDC_RUN_STATUS 50000
#define IDC_RUN_BUTTON1 50001
#define IDC_RUN_BUTTON2 50002


#define MAX_NAME_LEN 15 //This does not include null terminator
#define NO_OF_PLAYERS 2
#define NO_OF_CARDS 8
#define CARD_STARTING_ID 115 //This is where the bmp ID of A spade starts
#define MAX_STATUS_LEN 80+MAX_NAME_LEN

//Timer id
#define NO_OF_TIMERS 3
#define ID_TIMER1 0
#define ID_TIMER2 1
#define ID_TIMER3 2

//Timer ranges
#define TIMER1_MIN 50
#define TIMER1_MAX 750
#define TIMER2_MIN 5
#define TIMER2_MAX 120


//scores
#define MIN_SCORE 3
#define MAX_SCORE 100

//The Uflags to LoadMyBmp() and DeleteMyBmp()
#define WITH_BMP 0x1	//0001
#define WITH_DC 0x2		//0010
#define WITHOUT_BMP 0x4 //0100
#define WITHOUT_DC 0x8  //1000

#endif