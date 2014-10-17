package arun.predicket;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import arun.predicket.appengine.PMFactory;

public class RetrieveMatches extends HttpServlet{
	private static final Logger log = Logger.getLogger(RetrieveMatches.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PersistenceManager pm=null;
		try{
			pm = PMFactory.get().getPersistenceManager();
			Query q;
			
			//Has been commented, add it later if read conflict occurs
			//Check if the data is being written
			/*q = pm.newQuery("select from "+DBState.class.getName());
			List<DBState> states = (List<DBState>)q.execute();
			if (states != null && states.size() > 0) {
				JSONObject jb=new JSONObject(); 
				jb.put("error", "pending");
				resp.getWriter().write(jb.toString());
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}*/
			JSONArray retVal = new JSONArray();
			JSONObject mi;
			q = pm.newQuery("select from "+Match.class.getName());
			List<Match> matches = (List<Match>)q.execute();
			for (Iterator iterator = matches.iterator(); iterator.hasNext();) {
				Match match = (Match) iterator.next();
				mi = new JSONObject();
				mi.put("op1", match.getOp1());
				mi.put("op2", match.getOp2());
				mi.put("status", match.getMatStatus());
				mi.put("players", match.getMatPlayers());
				mi.put("location", match.getLocation());
				
				mi.put("op1Score",match.getOp1Score());
				mi.put("op1Wickets",match.getOp1Wickets());
				mi.put("op1Overs",match.getOp1Overs());
				mi.put("op1MaxOv",match.getOp1MaxOv());
				
				mi.put("op2Score",match.getOp2Score());
				mi.put("op2Wickets",match.getOp2Wickets());
				mi.put("op2Overs",match.getOp2Overs());
				mi.put("op2MaxOv",match.getOp2MaxOv());
				
				retVal.put(mi);
			}
			resp.getWriter().write(retVal.toString());
			resp.setStatus(HttpServletResponse.SC_OK);
		}catch (Exception e) {
			log.severe("Unable to retrieve from DB "+e);
		}finally{
			pm.close();
		}
		
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
		doGet(req, resp);
		
	}
}
