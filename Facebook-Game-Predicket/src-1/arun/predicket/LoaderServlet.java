package arun.predicket;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import arun.predicket.appengine.PMFactory;

import com.google.appengine.api.capabilities.CapabilityServicePb.IsEnabledResponseOrBuilder;
import com.google.apphosting.api.ApiProxy.LogRecord.Level;

public class LoaderServlet extends HttpServlet{

	private static final Logger log = Logger.getLogger(LoaderServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//TODO: Make the security constraint in web.xml
		initiateParsing();
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
		doGet(req, resp);
		
	}
	
	private String checkMatchNoOfDays(Node potMatchHeading){
		String dateVal= potMatchHeading.getChildNodes().item(2).getNodeValue();
		dateVal = dateVal.trim();
		//log.warning(dateVal);
		//Checking if there is no hiphen after the first hiphen
		if (dateVal.indexOf('-', dateVal.indexOf('-')+1) == -1) {
			return dateVal;
		} else {
			return null;
		}
	}
	private void extractFromPotMatchHeading(Node potMatchHeading, Match mi){
		//It shld have 3 children
		NodeList headChildren = potMatchHeading.getChildNodes();
		String heading = headChildren.item(0).getNodeValue();
		heading = (heading != null)?heading.trim():"";
		mi.setPotMatchHeading(heading);
		//Get the Opponentes and Location
		String opAndLoc = headChildren.item(1).getFirstChild().getNextSibling().getFirstChild().getNodeValue();
		opAndLoc = opAndLoc.trim();
		String opDelim = " v ";
		String locDelim = " at ";
		int op2Index = opAndLoc.indexOf(opDelim);
		int atIndex = opAndLoc.indexOf(locDelim, op2Index);
		mi.setOp1(opAndLoc.substring(0,op2Index).trim());
		mi.setOp2(opAndLoc.substring(op2Index+opDelim.length(),atIndex).trim());
		mi.setLocation(opAndLoc.substring(atIndex+locDelim.length()).trim());
		//Already extracted the Date
	}

	private void extractFromMatScores(Node matScores, Match mi){
		try {
			String score = matScores.getFirstChild().getNodeValue();
			log.info(score);
			//First check for semicolon
			String[] scoreArr = score.split(";");
			//Find op1 Score
			score = scoreArr[0].trim();
			String overStr = null;
			int index1, index2;
			int len;
			if (!score.startsWith(mi.getOp1())) {
				//need to swap op1 and op2
				String s = mi.getOp1();
				mi.setOp1(mi.getOp2());
				mi.setOp2(s);
			}
			len = mi.getOp1().length();
			index1 = len+1;
			index2 = score.indexOf('(');
			log.info ("Score Part1: index1 = "+index1+" index2 = "+index2);
			if (index2 == -1) {
				overStr = "";
				score = score.substring(index1).trim();
			} else {
				overStr = score.substring(index2 + 1).trim();
				score = score.substring(index1, index2).trim();
			}
			String[] strArr =  score.split("/");
			
			mi.setOp1Score(Integer.parseInt(strArr[0].trim()));
			if (strArr.length > 1) {
				mi.setOp1Wickets(Integer.parseInt(strArr[1].trim()));
			} else {
				mi.setOp1Wickets(10);
			}
			if (overStr.isEmpty()) {
				mi.setOp1Overs("0");
			} else {
				index2 = overStr.indexOf(" ov");
				if (index2 == -1) {
					index2 = overStr.indexOf(')');
				}else  {
					overStr = overStr.substring(0,index2);
				}
				strArr = overStr.split("/");
				mi.setOp1Overs(strArr[0].trim());
				if (strArr.length > 1) {
					mi.setOp1MaxOv(strArr[1].trim());
				}
			}
			
			//Find Op2 Score
			if (scoreArr.length == 1) {
				mi.setOp2MaxOv(mi.getOp1MaxOv());
				return;
			}
			score = scoreArr[1].trim();
			overStr = null;
			len = mi.getOp2().length();
			index1 = len+1;
			index2 = score.indexOf('(');
			log.info ("Score Part2: index1 = "+index1+" index2 = "+index2);
			if (index2 == -1) {
				overStr = "";
				score = score.substring(index1).trim();
			} else {
				overStr = score.substring(index2 + 1).trim();
				score = score.substring(index1, index2).trim();
			}
			strArr =  score.split("/");
			
			mi.setOp2Score(Integer.parseInt(strArr[0].trim()));
			if (strArr.length > 1) {
				mi.setOp2Wickets(Integer.parseInt(strArr[1].trim()));
			} else {
				mi.setOp2Wickets(10);
			}
			
			if (overStr.isEmpty()) {
				mi.setOp2Overs("0");
			} else {
				index2 = overStr.indexOf(" ov");
				if (index2 == -1) {
					index2 = overStr.indexOf(')');
				}else {
					overStr = overStr.substring(0,index2);
				}
				strArr = overStr.split("/");
				mi.setOp2Overs(strArr[0].trim());
				if (strArr.length > 1) {
					mi.setOp2MaxOv(strArr[1].trim());
				} else {
					mi.setOp2MaxOv(mi.getOp1MaxOv());
				}
			}
		} catch (Exception e) {
			log.severe("Error Parsing Score - "+e);
			StackTraceElement[] se = e.getStackTrace();
			for (int i = 0; i < se.length; i++) {
				log.info(se[i].toString());
			}
		}
		
		
	}
	
	private void extractFromMatPlayers(Node matPlayers, Match mi){
		NodeList nl  = matPlayers.getChildNodes();
		String retStr = "";
		for (int i = 0; i < nl.getLength(); i++) {
			try {
				String s = nl.item(i).getFirstChild().getNodeValue().trim();
				if (!retStr.isEmpty() && s != null && !s.isEmpty())
					retStr+="|";
				retStr += s;
			} catch (NullPointerException e) {}
		}
		mi.setMatPlayers(retStr);
	}
	
	private void extractFromMatStatus(Node matStatus, Match mi){
			NodeList nl  = matStatus.getChildNodes();
			String retStr = "";
			for (int i = 0; i < nl.getLength(); i++) {
				try {
					String s = nl.item(i).getNodeValue();
					if (s == null || s.isEmpty())
						s = nl.item(i).getFirstChild().getNodeValue().trim();
					retStr += s;
				} catch (NullPointerException e) {}
			}
			mi.setMatStatus(retStr);
	}
	
/*	private void addMatchInstance(Node matchBoxElem, int odiOrT20, ArrayList<Match> matches){
		//Check if the node is valid
		if ("div".equals(matchBoxElem.getNodeName())) {
			String classes = matchBoxElem.getAttributes().getNamedItem("class").getNodeValue();
			String[] classArr  = classes.split(" ");
			for (int i = 0; i < classArr.length; i++) {
				if ("match_box".equalsIgnoreCase(classArr[i])) {
					//It is indeed a valid elem, extract all info, it shld have 5 children
					NodeList childNodes = matchBoxElem.getChildNodes();
					//Check if the match dates are more than one day (test match)
					String dateStr = checkMatchNoOfDays(childNodes.item(0));
					if (dateStr == null) {
						return;
					}
					//Create an empty Match Instance
					Match mi = new Match();
					mi.setDate(dateStr);
					//First child has the opponents name in potmatchHeading
					extractFromPotMatchHeading(childNodes.item(0),mi);
					//Second Child is mat_scores
					extractFromMatScores(childNodes.item(1),mi);
					//Third Child is mat_players
					extractFromMatPlayers(childNodes.item(2),mi);
					//Fourth Child is mat_status
					extractFromMatStatus(childNodes.item(3),mi);
					matches.add(mi);
					return;
				}
			}
		}
	}
*/
	
	private BufferedReader getReaderFromURL() throws IOException{
		URL url = new URL("http://www.espncricinfo.com/ci/engine/match/scores/liveframe.html");
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		return br;
	}
	
	private BufferedReader getReaderFromLocal() throws IOException{
		URL url = new URL("http://localhost:8080/test-server-1/retrieved.html");
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		return br;
	}
	
	private boolean isContainsClass(Node node, String classAttr){
		try {
			String classes  = node.getAttributes().getNamedItem("class").getNodeValue();
			String[] classArr  = classes.split(" ");
			for (int j = 0; j < classArr.length; j++) {
				if (classAttr.equals(classArr[j])) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	public void initiateParsing(){
		ArrayList<Match> matches = new ArrayList<Match>();
		log.fine("Invoking Parser");
		boolean isError = false;
		try {
			Tidy parser = new Tidy();
			Document dom = parser.parseDOM(getReaderFromURL(),(Writer)null);
			NodeList nl = dom.getElementsByTagName("p");
			int odiOrT20 = 0;
			String seriesHeading = "";
			Match mi = null;
			for (int i = 0; i < nl.getLength(); i++) {
				try {
					Node node = nl.item(i);
					if (mi != null) {
						if (isContainsClass(node, "mat_scores")) {
							extractFromMatScores(node,mi);
						} else if (isContainsClass(node, "mat_players")) {
							extractFromMatPlayers(node,mi);
						} else if (isContainsClass(node, "mat_status")) {
							extractFromMatStatus(node,mi);
						}
					}
					if (isContainsClass(node, "potMatchSeriesHeading")) {
						seriesHeading = getSeriesHeading(node);
						odiOrT20 = getOdiOrT20(seriesHeading);
					} else if (isContainsClass(node, "potMatchHeading")) {
						if (mi != null) {
							//Add to list
							matches.add(mi);
							mi = null;
						}
						String dateStr = checkMatchNoOfDays(node);
						if (dateStr != null) {
							mi = new Match();
							mi.setDate(dateStr);
							mi.setOdiOrT20(odiOrT20);
							String possibleMaxOv = "0";
							if (odiOrT20 == Match.TYPE_T20) {
								possibleMaxOv = "20";
							} else {
								possibleMaxOv = "50";	
							}
							mi.setOp1MaxOv(possibleMaxOv);
							mi.setOp2MaxOv(possibleMaxOv);

							mi.setSeriesHeading(seriesHeading);
							extractFromPotMatchHeading(node,mi);
						}
					}				
				} catch (DOMException e) {
					log.severe("Exception in parsing "+ e);
				} catch (NullPointerException e) {
					log.severe("Exception in parsing "+ e);
				}
			}
			if (mi != null) {
				matches.add(mi);
				mi = null;
			}
		} catch (MalformedURLException e) {
			log.severe("Unable to fetch URL "+e);
			isError  = true;
		} catch (IOException e) {
			log.severe("Unable to fetch URL "+e);
			isError = true;
		}
		if (!isError) {
			PersistenceManager pm=null;
			try{			
				pm = PMFactory.get().getPersistenceManager();

				
				Query q = pm.newQuery("select from "+Match.class.getName());
				List<Match> matchesInDB = (List<Match>)q.execute();
				
				//Has been commented, add it later if read conflict occurs in RetrieveMatches
				//Set the DB state to pending
				/*pm.makePersistent(new DBState());*/
				pm.deletePersistentAll(matchesInDB);
				pm.makePersistentAll(matches);
				
				//Remove all entries of DBState to indicate its not pending
				/*q = pm.newQuery("select from "+DBState.class.getName());
				pm.deletePersistentAll((List<DBState>)q.execute());	*/
				
				//Update the predickets
				updatePredickets(matches);
				
			}catch (Exception e) {
				log.severe("Unable to persist to DB "+e);
			}finally{
				pm.close();
			}
		}
	}
	private int myRound(float f){
		int iVal = (int)f;
		if ((f - iVal) > 0.5) {
			return iVal + 1;
		}
		return iVal;
	}

	private void updatePredickets(List<Match> matches){
		
		HashMap<String, Team> mMap = new HashMap<String, Team>();
		
		//Construct the match hashmap
		for (Iterator iterator = matches.iterator(); iterator.hasNext();) {
			Match mi = (Match) iterator.next();
			//create ur own round function to avoid setting 32.5 to 33
			mMap.put(mi.getOp1(),new Team(mi.getOp1Score(),myRound(Float.parseFloat(mi.getOp1Overs()))));
			mMap.put(mi.getOp2(),new Team(mi.getOp2Score(),myRound(Float.parseFloat(mi.getOp2Overs()))));
		}
		PersistenceManager pm=null;
		try {
			HashMap<String, User> userPointsUpdate = new HashMap<String, User>();
			pm = PMFactory.get().getPersistenceManager();
			//Iterate through all predickets that are pending
			Query q = pm.newQuery("select from "+Predicket.class.getName()+" where state == stateParam parameters String stateParam");
			List<Predicket> predsInDB = (List<Predicket>)q.execute(Predicket.STATE_PENDING);
			log.warning("preds in db = "+predsInDB.size());
			Team data;
			User userObj;
			int points;
			for (Iterator iterator = predsInDB.iterator(); iterator.hasNext();) {
				Predicket pr = (Predicket) iterator.next();
				//Check if the overs predicted is greater than or equal to curOv
				data = mMap.get(pr.getTeam());
				if (data == null) {
					//Nullify the prediction
					pr.setState(Predicket.STATE_NULLIFIED);
					pr.setPrevTimeStamp(pr.getTimeStamp());
					pr.setTimeStamp(System.currentTimeMillis());
				} else {
					if (data.overs >=  pr.getOvers())
					{
						pr.setState(Predicket.STATE_COMPLETED);
						pr.setPrevTimeStamp(pr.getTimeStamp());
						pr.setTimeStamp(System.currentTimeMillis());
						//Compute the predicket points
						points = getPoints(pr, data.score);
						pr.setPoints(points);
						//Create HashMap of usersToUpdate
						userObj = userPointsUpdate.get(pr.getUserId());
						if (userObj == null){
							q = pm.newQuery("select from "+User.class.getName()+" where userId == userIdParam parameters String userIdParam");
							List<User> users = (List<User>)q.execute(pr.getUserId());
							userObj=users.get(0);
							userPointsUpdate.put(pr.getUserId(),userObj );
						}
						userObj.setPoints(userObj.getPoints()+points); 
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.severe("Exception in update Predickets "+e.toString());
		} finally {
			pm.close();
		}
	}
	
	
	private int getPoints(Predicket pr, int score){
		//Find delta
		int origDelta = Math.abs(pr.getRuns() - score);
		int delta = origDelta;
		int risk = pr.getRiskOvers();
		int penalty =(pr.getMaxOv()-risk)*2;

		int points = 500;
		//after each interval of incr runs in delta the penalty is doubled
		int incr = 5;
			
		int totPenalty = 0;
		int k=1;
		while(delta > 0) {
			if (delta > incr) {
				totPenalty += penalty*k*incr;
			} else {
				totPenalty += penalty*k*delta;
			}
			//Keep a limit on max penalty
			if (totPenalty >= points<<1) {
				totPenalty=points<<1;
				totPenalty += origDelta-(risk*2);
				return points-totPenalty;
			}
			k++;
			delta = delta - incr;
		}

		return points-totPenalty+(risk*2);
	}
	
	private String getSeriesHeading(Node node){
		try {
			String nodeValue = node.getFirstChild().getNodeValue();
			return nodeValue;
		} catch (Exception e) { return "";	}
	}

	private int getOdiOrT20(String nodeValue) {
		nodeValue = nodeValue.toLowerCase();
		if (nodeValue.indexOf("20") != -1 || nodeValue.indexOf("twenty") != -1
				|| nodeValue.indexOf("indian premier league")!=-1) {
			return Match.TYPE_T20;
		}		


		return Match.TYPE_ODI;
	}	
}
