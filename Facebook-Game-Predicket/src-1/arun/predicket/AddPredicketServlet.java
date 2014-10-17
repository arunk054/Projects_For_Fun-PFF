package arun.predicket;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import arun.predicket.appengine.PMFactory;

public class AddPredicketServlet  extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PersistenceManager pm=null;
		
		try {
			pm = PMFactory.get().getPersistenceManager();
			String userId = req.getParameter("id");
			String team = req.getParameter("team");
			int runs = Integer.parseInt(req.getParameter("runs"));
			int overs = Integer.parseInt(req.getParameter("overs"));
			int riskOvers = Integer.parseInt(req.getParameter("riskOvers"));
			int maxOv = Integer.parseInt(req.getParameter("maxOv"));
			//Hopefully none of them is null
			
			//Check if limit is reached
			JSONObject retVal = new JSONObject();
			
			Query q = pm.newQuery("select from "+Predicket.class.getName()+" where userId == userIdParam && state == stateParam parameters String userIdParam, int stateParam");
			List<Predicket> predsInDB = (List<Predicket>)q.execute(userId,Predicket.STATE_PENDING);
			int len = predsInDB.size();
			if (len < Predicket.MAX_PENDING_PREDICKETS) {
				Predicket curPred = new Predicket(userId,team,runs,overs,riskOvers,maxOv);
				retVal.put("ts",curPred.getTimeStamp());
				pm.makePersistent(curPred);
				retVal.put("status", "success");
				retVal.put("available",Predicket.MAX_PENDING_PREDICKETS-(len+1));

				//Remove excess entries from predickets for this user
				//This can be a separate cron job as well
				/*q = pm.newQuery("select from "+Predicket.class.getName()+" where userId == userIdParam && state != stateParam parameters String userIdParam, int stateParam order by timeStamp");*/
				q = pm.newQuery("select from "+Predicket.class.getName()+" where userId == userIdParam parameters String userIdParam order by timeStamp");
				List<Predicket> allPreds = (List<Predicket>)q.execute(userId);
				
				int tot = allPreds.size();
				int diff = tot - Predicket.MAX_TOTAL_PREDICKETS;
				if (diff > 0){
					Predicket toDelete;
					//Select the oldest entry and delete it.
					for (int i = 0, j=0; i < tot && j<diff ; i++) {
						toDelete=allPreds.get(i);
						if (toDelete.getState() != Predicket.STATE_PENDING) {
							pm.deletePersistent(toDelete);
							j++;
						}
					}
				}
			} else {
				retVal.put("error", "You do not have any predickets left!");
				retVal.put("status", "error");
			}
			resp.getWriter().write(retVal.toString());
			
			
			//When the loaderServlet fires it check all the predickets in pending state
			//And updates ones that have changed, and also changes the time stamp
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		} finally{
			pm.close();
		}
		
		
		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
		doGet(req, resp);		
	}
	
}

