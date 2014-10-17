package arun.puyo.v1_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class HighScoresManager {

	private int currentScore, diff_level;

	private String currentName;

	// Need to make this as a sorted ArrayList by implementing comparator
	private ArrayList highScoreObjects;

	private boolean isCurrentAdded = false;

	public HighScoresManager(int diff_level,String currentName, int currentScore) {

		this.currentScore = currentScore;
		this.diff_level = diff_level;
		this.currentName = currentName;
		this.highScoreObjects= new ArrayList();
		
		// readFileContents
		readFileToArrayList();
		// decrypt

		// Write To File
		writeArrayListToFile();

		// if inserted, congratulate
		// otherwise say sorry
	}

	private void writeArrayListToFile() {
		// TODO Auto-generated method stub
		File outFile,inFile=null;FileInputStream fis;BufferedReader in=null;
			try{	
				 inFile = new File(AppConsts.HIGH_SCORE_FILE_NAME);
				 fis = new FileInputStream(inFile);
				 in = new BufferedReader(new InputStreamReader(fis));
	
				 outFile = new File("$$$.tmp");
			}catch(FileNotFoundException e){
				//Instead of creating a new file create with the highscores name
				outFile = new File(AppConsts.HIGH_SCORE_FILE_NAME);}
			
			PrintWriter out=null;
			try {
				out = new PrintWriter(new FileOutputStream(outFile));
			} catch (FileNotFoundException e1) {
				System.out.println("Unable to create File to write!");
			}
			String line;
			try {
				while((line = in.readLine()) != null)
				{
					if(this.diff_level!=Integer.parseInt(line.substring(0,line.indexOf("|") )))
					{
						out.println(line);
					}
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Unable to read file");
			}catch (NullPointerException e){
			}catch (Exception e)
			{System.out.println("File corrupted!");}

			for (Iterator iter = highScoreObjects.iterator(); iter.hasNext();) {
				HighScore element = (HighScore) iter.next();
				out.println(element.getLine());
			}			

			out.flush();out.close();
			if(in!=null)
			{
				inFile.delete();
				outFile.renameTo(inFile);
			}
		} 

	private void readFileToArrayList() {
		// TODO Auto-generated method stub

		String line = null, player_name;
		int index = 0;
		int file_level, file_score;
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					AppConsts.HIGH_SCORE_FILE_NAME));
			try {
				while ((line = in.readLine()) != null) {
					index = line.indexOf("|");
					file_level = Integer.parseInt(line.substring(0, index));
					if (file_level == this.diff_level) {
						player_name = line.substring(index + 1, line.indexOf(
								"|", index + 1));
						index = line.indexOf("|", index + 1);
						file_score = Integer
								.parseInt(line.substring(index + 1));
						if (this.currentScore > file_score && !this.isCurrentAdded) {
							highScoreObjects.add(new HighScore(this.diff_level,
									this.currentName, this.currentScore));
							this.isCurrentAdded = true;
						} 
						if(highScoreObjects.size()<AppConsts.NO_OF_HIGH_SCORES)
						{
							highScoreObjects.add(new HighScore(file_level,
									player_name, file_score));
						}
						else
						{
							break;
						}
					}
				}
				in.close();
			} catch (IOException e) {
				System.out.println("Unable to read file "
						+ AppConsts.HIGH_SCORE_FILE_NAME);
			} catch (Exception e) {
				System.out.println("High Score File is corrupted!");
			}

		} catch (FileNotFoundException e) {
			System.out.println("High Score file not found, But will try to create!!");
		}
		if (!this.isCurrentAdded) {
			if (highScoreObjects.size() < AppConsts.NO_OF_HIGH_SCORES) {
				highScoreObjects.add(new HighScore(this.diff_level,
						this.currentName, this.currentScore));
				this.isCurrentAdded = true;
			}
		}

	}

	public ArrayList getHighScoreObjects()
	{
		return highScoreObjects;
	}
	
	//main added for testing
	/*public static void main(String[] args)
	{
		HighScoresManager hsm=new HighScoresManager(0,"p2",50);
		new HighScoreDialog(hsm.getHighScoreObjects(),hsm.getIsCurrentAdded());
	}*/
	public boolean getIsCurrentAdded()
	{
		return isCurrentAdded;
		
	}
}
