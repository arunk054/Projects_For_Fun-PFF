package arun.puyo.v1_1;

public class HighScore {

	

	private int score;
	private String name;
	private static int level=0;
	
	public HighScore(int diff_level, String currentName, int currentScore) {
		// TODO Auto-generated constructor stub
		this.level=diff_level;
		this.name=currentName;
		this.score=currentScore;
	}

	public String getLine() {
		// TODO Auto-generated method stub
		String s=Integer.toString(this.level)+"|"+this.name+"|"+Integer.toString(this.score);
		return s;
	}

	public static String getheading1()
	{
		String s="    Your Difficulty Level is: "+AppConsts.DIFFICULTY_LEVEL[HighScore.level]+"    ";
		return s;
	}
	
	public static String getheading2()
	{
		String s="Name      "+"      Score";
		return s;
	}
	
	public String toString()
	{
		String s="    "+this.name+"     "+Integer.toString(this.score);
		return s;
		
	}
	
	
}
