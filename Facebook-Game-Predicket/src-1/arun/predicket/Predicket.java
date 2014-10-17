package arun.predicket;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Predicket {

	public final static int STATE_COMPLETED = 0;
	public final static int STATE_PENDING = 1;
	public final static int STATE_NULLIFIED = 2;
	
	public final static int MAX_PENDING_PREDICKETS = 5;
	public final static int MAX_TOTAL_PREDICKETS = 10;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String userId, team;
	
	@Persistent
	private int runs,overs;
	
	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getOvers() {
		return overs;
	}

	public void setOvers(int overs) {
		this.overs = overs;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Persistent
	private int points;
	
	@Persistent
	private int state;
	
	public int getState() {
		return state;
	}
	
	@Persistent
	private int riskOvers;
	

	public int getRiskOvers() {
		return riskOvers;
	}

	public void setRiskOvers(int riskOvers) {
		this.riskOvers = riskOvers;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Persistent
	private long timeStamp;

	@Persistent
	private long prevTimeStamp;
	
	public long getPrevTimeStamp() {
		return prevTimeStamp;
	}

	public void setPrevTimeStamp(long prevTimeStamp) {
		this.prevTimeStamp = prevTimeStamp;
	}
	
	@Persistent
	private Integer maxOv;

	public int getMaxOv() {
		return maxOv;
	}

	public void setMaxOv(int maxOv) {
		this.maxOv = maxOv;
	}

	public Predicket(String id, String team, int runs, int overs, int minPred, int maxOv) {
		this.userId = id;
		this.team = team;
		this.runs = runs;
		this.overs = overs;
		this.timeStamp = System.currentTimeMillis();
		this.prevTimeStamp = 0;
		this.points = 0;
		this.state = STATE_PENDING;
		this.riskOvers = minPred;
		this.maxOv = maxOv;
	}	
}
