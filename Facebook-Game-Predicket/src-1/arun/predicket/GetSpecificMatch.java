package arun.predicket;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import arun.predicket.appengine.PMFactory;

public class GetSpecificMatch extends HttpServlet{
	private static final Logger log = Logger.getLogger(GetSpecificMatch.class.getName());
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp){
		//One param should be unique enuf
		String op1 = req.getParameter("op1");
		PersistenceManager pm=null;		
		try{
			pm = PMFactory.get().getPersistenceManager();
			JSONObject jb=new JSONObject();
			Query q ;
			
			//Check if the data is being written
			/*q = pm.newQuery("select from "+DBState.class.getName());
			List<DBState> states = (List<DBState>)q.execute();
			if (states != null && states.size() > 0) {
				jb.put("error", "pending");
				resp.getWriter().write(jb.toString());
				return;
			}*/
			/*q = pm.newQuery("select from "+Match.class.getName()+" where op1 == op1Param && op2 == op2Param parameters String op1Param, String op2Param");
			List<Match> matches = (List<Match>)q.execute(op1,op2);*/
			q = pm.newQuery("select from "+Match.class.getName()+" where op1 == op1Param parameters String op1Param");
			List<Match> matches = (List<Match>)q.execute(op1);
			if (matches.size() == 0) {
				q = pm.newQuery("select from "+Match.class.getName()+" where op2 == op2Param parameters String op2Param");
				matches = (List<Match>)q.execute(op1);
				if (matches.size() == 0) {
					jb.put("error", "NOT_FOUND");
					resp.getWriter().write(jb.toString());
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
			}
			Match match = (Match)matches.get(0);
			jb.put("op1", match.getOp1());
			jb.put("op2", match.getOp2());
			
			jb.put("date", match.getDate());
			jb.put("location", match.getLocation());
			jb.put("players", match.getMatPlayers());
			jb.put("op1MaxOv", match.getOp1MaxOv());
			jb.put("op1Overs", match.getOp1Overs());
			jb.put("op1Score", match.getOp1Score());
			jb.put("op1Wickets", match.getOp1Wickets());

			jb.put("op2MaxOv", match.getOp2MaxOv());
			jb.put("op2Overs", match.getOp2Overs());
			jb.put("op2Score", match.getOp2Score());
			jb.put("op2Wickets", match.getOp2Wickets());

			jb.put("heading",match.getPotMatchHeading());
			resp.getWriter().write(jb.toString());
			resp.setStatus(HttpServletResponse.SC_OK);
		}catch (Exception e) {
			log.severe("Unable to retrieve from DB "+e);
		}finally{
			pm.close();
		}
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		
		
	}
}
