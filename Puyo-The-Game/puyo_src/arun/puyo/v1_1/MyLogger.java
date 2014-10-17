/****************************************
 * 
 * GamePanel.java
 * 
 * @author ARUN
 *
 *Created on 01-5-2007 
 */

package arun.puyo.v1_1;

public class MyLogger
{

	public static void log(String s)
	{
		if(AppConsts.LOG_MODE==AppConsts.LOG_MODE_DEBUG)
			System.out.println(s);
	}
}