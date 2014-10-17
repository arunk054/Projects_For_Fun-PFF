package arun.predicket;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import arun.predicket.appengine.PMFactory;

public class GetPredickets  extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PersistenceManager pm=null;
		try {
			pm = PMFactory.get().getPersistenceManager();
			String userId = req.getParameter("id");
			JSONArray ja = new JSONArray();
			JSONObject jbMain = new JSONObject();
			long ts = Long.parseLong(req.getParameter("ts"));
			
			Query q = pm.newQuery("select from "+Predicket.class.getName()+" where userId == userIdParam && timeStamp > timeStampParam parameters String userIdParam, long timeStampParam order by timeStamp desc");
			List<Predicket> predsInDB = (List<Predicket>)q.execute(userId,ts);
			int pendingCount = 0;
			for (Iterator iterator = predsInDB.iterator(); iterator
			.hasNext();) {
				Predicket predicket = (Predicket) iterator.next();
				JSONObject jb= new JSONObject();
				jb.put("team",predicket.getTeam());
				jb.put("overs",predicket.getOvers());
				jb.put("runs",predicket.getRuns());
				jb.put("timeStamp",predicket.getTimeStamp());
				jb.put("prevTimeStamp",predicket.getPrevTimeStamp());
				jb.put("points",predicket.getPoints());
				jb.put("state",predicket.getState());
				if (predicket.getState() == Predicket.STATE_PENDING)
					pendingCount++;
				ja.put(jb);
			}
			jbMain.put("available", Predicket.MAX_PENDING_PREDICKETS-pendingCount);
			jbMain.put("predArr",ja);
			//Also get the total points
			q = pm.newQuery("select from "+User.class.getName()+" where userId == userIdParam parameters String userIdParam");
			List<User> users = (List<User>)q.execute(userId);
			if(users.size() > 0) {
				jbMain.put("points", users.get(0).getPoints());
			} else {
				jbMain.put("points", 0);
				//TODO Report some unknown error
			}
			resp.getWriter().write(jbMain.toString());
		}catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		} finally{
			pm.close();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
