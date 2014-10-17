import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;


import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

public class URLTest {

	private static boolean isParsing = false;
	private static final Logger log = Logger.getLogger(URLTest.class.getName());

	public static void main(String[] args) {
		
		//System.setProperty("http.proxyHost", "web-proxy.ind.hp.com");
		//System.setProperty("http.proxyPort", "8080");
		initiateParsing();
	}
	
	
	public static void testParsing(){
		if (isParsing)
			return;
		isParsing = true;
		try {
			Tidy parser = new Tidy();
			Document dom = parser.parseDOM(getReaderFromURL(), (Writer)null);
			NodeList nl = dom.getElementsByTagName("p");
			for (int i = 0; i < nl.getLength(); i++) {
				try {
					Node node = nl.item(i);
					String classes  = node.getAttributes().getNamedItem("class").getNodeValue();
					System.out.println(classes);
					String[] classArr  = classes.split(" ");
					
				} catch (DOMException e) {
					//LOG Warning
				} catch (NullPointerException e) {
					//LOG Warning
				}
				
			}
		} catch (MalformedURLException e) {
			//TODO Logging in appengine
			e.printStackTrace();
		} catch (IOException e) {
			//TODO Logging in appengine
			e.printStackTrace();
		}
		isParsing = false;
		
	}
	
	private static String checkMatchNoOfDays(Node potMatchHeading){
		String dateVal= potMatchHeading.getChildNodes().item(2).getNodeValue();
		dateVal = dateVal.trim();
		//Checking if there is no hiphen after the first hiphen
		if (dateVal.indexOf('-', dateVal.indexOf('-')+1) == -1) {
			return dateVal;
		} else {
			return null;
		}
	}
	private static void extractFromPotMatchHeading(Node potMatchHeading, Match mi){
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

	private static void extractFromMatScores(Node matScores, Match mi){
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
			if (score.startsWith(mi.getOp1())) {
				len = mi.getOp1().length();
			} else {
				len = mi.getOp2().length();
			}
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
				mi.setOp1Wickets(0);
			}
			
			if (overStr.isEmpty()) {
				mi.setOp1Overs(-1);
			} else {
				index2 = overStr.indexOf(" ov");
				if (index2 == -1) {
					index2 = overStr.indexOf(')');
				}else  {
					overStr = overStr.substring(0,index2);
				}
				strArr = overStr.split("/");
				mi.setOp1Overs(Float.parseFloat(strArr[0].trim()));
				if (strArr.length > 1) {
					mi.setOp1MaxOv(Float.parseFloat(strArr[1].trim()));
				} else {
					if (mi.getOdiOrT20() == Match.TYPE_T20) {
						mi.setOp1MaxOv(20);
					} else {
						mi.setOp1MaxOv(50);	
					}
				}
			}
			
			//Find Op2 Score
			//TODO put the above in a function
			if (scoreArr.length == 1) {
				mi.setOp2Overs(-1);
				return;
			}
			score = scoreArr[1].trim();
			overStr = null;
			if (score.startsWith(mi.getOp1())) {
				len = mi.getOp1().length();
			} else {
				len = mi.getOp2().length();
			}
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
				mi.setOp2Wickets(0);
			}
			
			if (overStr.isEmpty()) {
				mi.setOp2Overs(-1);
			} else {
				index2 = overStr.indexOf(" ov");
				if (index2 == -1) {
					index2 = overStr.indexOf(')');
				}else {
					overStr = overStr.substring(0,index2);
				}
				strArr = overStr.split("/");
				mi.setOp2Overs(Float.parseFloat(strArr[0].trim()));
				if (strArr.length > 1) {
					mi.setOp2MaxOv(Float.parseFloat(strArr[1].trim()));
				} else {
					if (mi.getOdiOrT20() == Match.TYPE_T20) {
						mi.setOp2MaxOv(20);
					} else {
						mi.setOp2MaxOv(50);	
					}
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
	
	private static void extractFromMatPlayers(Node matPlayers, Match mi){
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
	
	private static void extractFromMatStatus(Node matStatus, Match mi){
		try {
			mi.setMatStatus(matStatus.getFirstChild().getNodeValue());
		} catch (NullPointerException e) {}
		
	}
	
	private static void addMatchInstance(Node matchBoxElem, int odiOrT20, ArrayList<Match> matches){
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

	
	private static BufferedReader getReaderFromURL() throws IOException{
		URL url = new URL("http://www.espncricinfo.com/ci/engine/match/scores/liveframe.html");
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		return br;
	}
	
	private static boolean isContainsClass(Node node, String classAttr){
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
	public static void initiateParsing(){
		if (isParsing)
			return;
		isParsing = true;
		ArrayList<Match> matches = new ArrayList<Match>();
		log.fine("Invoking Parser");
		try {
			Tidy parser = new Tidy();
			Document dom = parser.parseDOM(getReaderFromURL(), (Writer)null);
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
						odiOrT20 = getOdiOrT20(node.getNodeValue());
						seriesHeading = node.getNodeValue();
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
		} catch (MalformedURLException e) {
			log.severe("Unable to fetch URL "+e);
		} catch (IOException e) {
			log.severe("Unable to fetch URL "+e);
		}
		for (Iterator iterator = matches.iterator(); iterator.hasNext();) {
			Match match = (Match) iterator.next();
			System.out.println(match);
			System.out.println("===================");
		}
		isParsing = false;	
	}


	private static int getOdiOrT20(String nodeValue) {
		nodeValue = nodeValue.toLowerCase();
		if (nodeValue != null) {
			if (nodeValue.indexOf("20") != -1 || nodeValue.indexOf("twenty") != -1) {
				return Match.TYPE_T20;
			}		
		}
		return Match.TYPE_ODI;
	}

}
