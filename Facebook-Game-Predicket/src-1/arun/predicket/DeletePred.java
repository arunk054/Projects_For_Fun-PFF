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

public class DeletePred extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		PersistenceManager pm=null;
		try {
			pm = PMFactory.get().getPersistenceManager();
			String userId = req.getParameter("id");
			JSONArray ja = new JSONArray();
			
			if (userId == null) {
				//Implies delete
				Query q = pm.newQuery("select from "+Predicket.class.getName());
				List<Predicket> predsInDB = (List<Predicket>)q.execute();
				JSONObject jb= new JSONObject();
				jb.put("count", predsInDB.size());
				ja.put(jb);
				pm.deletePersistentAll(predsInDB);
			} else {
				//Return
				long ts = 121312;
				Query q = pm.newQuery("select from "+Predicket.class.getName()+" where userId == userIdParam && timeStamp > timeStampParam parameters String userIdParam, long timeStampParam order by timeStamp desc");
				List<Predicket> predsInDB = (List<Predicket>)q.execute(userId,ts);
				for (Iterator iterator = predsInDB.iterator(); iterator
						.hasNext();) {
					Predicket predicket = (Predicket) iterator.next();
					JSONObject jb= new JSONObject();
					jb.put("team",predicket.getTeam());
					jb.put("overs",predicket.getOvers());
					jb.put("runs",predicket.getRuns());
					jb.put("timeStamp",predicket.getTimeStamp());
					jb.put("points",predicket.getPoints());
					jb.put("state",predicket.getState());
					ja.put(jb);
				}	
			}
			resp.getWriter().write(ja.toString());
		} catch (Exception e) {
			//e.printStackTrace();
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
