package arun.predicket;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Match {
	public static final int TYPE_ODI = 0;
	public static final int TYPE_T20 = 1;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
	//For future use
	@NotPersistent
	private int odiOrT20;
	public int getOdiOrT20() {
		return odiOrT20;
	}
	
	//Series heading just a good to have thing
	@NotPersistent
	private String seriesHeading;
	

	public String getSeriesHeading() {
		return seriesHeading;
	}

	public void setSeriesHeading(String seriesHeading) {
		this.seriesHeading = seriesHeading;
	}

	public void setOdiOrT20(int odiOrT20) {
		this.odiOrT20 = odiOrT20;
	}

	public String getPotMatchHeading() {
		return potMatchHeading;
	}

	public void setPotMatchHeading(String potMatchHeading) {
		this.potMatchHeading = potMatchHeading;
	}

	public String getOp1() {
		return op1;
	}

	public void setOp1(String op1) {
		this.op1 = op1;
	}

	public String getOp2() {
		return op2;
	}

	public void setOp2(String op2) {
		this.op2 = op2;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getOp1Score() {
		return op1Score;
	}

	public void setOp1Score(int op1Score) {
		this.op1Score = op1Score;
	}

	public int getOp1Wickets() {
		return op1Wickets;
	}

	public void setOp1Wickets(int op1Wickets) {
		this.op1Wickets = op1Wickets;
	}

	public String getOp1Overs() {
		return op1Overs;
	}
	
	public void setOp1Overs(String op1Overs) {
		this.op1Overs = op1Overs;
	}

	public String getOp1MaxOv() {
		return op1MaxOv;
	}

	public void setOp1MaxOv(String op1MaxOv) {
		this.op1MaxOv = op1MaxOv;
	}

	public int getOp2Score() {
		return op2Score;
	}

	public void setOp2Score(int op2Score) {
		this.op2Score = op2Score;
	}

	public int getOp2Wickets() {
		return op2Wickets;
	}

	public void setOp2Wickets(int op2Wickets) {
		this.op2Wickets = op2Wickets;
	}

	public String getOp2Overs() {
		return op2Overs;
	}

	public void setOp2Overs(String op2Overs) {
		this.op2Overs = op2Overs;
	}

	public String getOp2MaxOv() {
		return op2MaxOv;
	}

	public void setOp2MaxOv(String op2MaxOv) {
		this.op2MaxOv = op2MaxOv;
	}

	public String getMatPlayers() {
		return matPlayers;
	}

	public void setMatPlayers(String matPlayers) {
		this.matPlayers = matPlayers;
	}

	public String getMatStatus() {
		return matStatus;
	}

	public void setMatStatus(String matStatus) {
		this.matStatus = matStatus;
	}

    @Persistent
	private String potMatchHeading;
	
    @Persistent
	private String op1;
	
	@Persistent
	private String op2;
    
    @Persistent
	private String location;
	
    @Persistent
	private String date;
    
    @Persistent
	private int op1Score, op1Wickets;
    
    @Persistent
	private String op1Overs, op1MaxOv;
    @Persistent
	private int op2Score, op2Wickets;

    //Maxov is 0 implies not started
    @Persistent
	private String op2Overs, op2MaxOv;
	
    @Persistent
	private String matPlayers;
    
    @Persistent
	private String matStatus;
	
	
	@Override
	public String toString() {
		String s="";
		s+=potMatchHeading+"\n";
		s+=op1 + " v " + op2 + " at "+location+"\n";
		s+=op1+": "+op1Score + "/"+op1Wickets+" ("+op1Overs+"/"+op1MaxOv+" ov)\n";
		s+=op2+": "+op2Score + "/"+op2Wickets+" ("+op2Overs+"/"+op2MaxOv+" ov)\n";
		s+=matPlayers+"\n";
		s+=matStatus;
		return s;
	}
	
	public Match() {
		op1Overs = "0";
		op1MaxOv = "50";
		op2Overs = "0";
		op2MaxOv = "50";
		
	}
}
