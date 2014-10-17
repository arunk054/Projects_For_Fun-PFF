package arun.predicket;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import arun.predicket.appengine.PMFactory;

public class LoginServlet extends HttpServlet{
	private static final Logger log = Logger.getLogger(LoginServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String fbId = req.getParameter("fbId");
		PersistenceManager pm=null;
		try {
			//Check if the id is present
			pm = PMFactory.get().getPersistenceManager();
			log.info("Login Servlet Invoked");
			if (fbId == null || fbId.isEmpty()){
				JSONObject jb=new JSONObject(); 
				jb.put("error","no FB id");
				resp.getWriter().write(jb.toString());
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			//+" where userId == userIdParam parameters String userId"
			Query q = pm.newQuery("select from "+User.class.getName()+" where userId == userIdParam parameters String userIdParam");
			List<User> users = (List<User>)q.execute(fbId);
			log.info("No of user with the given id = "+users.size());
			JSONObject retOb = new JSONObject();
			if (users.size() == 0) {
				//create new User
				pm.makePersistent(new User(fbId, User.DEFAULT_POINTS));
				retOb.put("points",User.DEFAULT_POINTS);
			} else {
				retOb.put("points",users.get(0).getPoints());
				
			}
			
			q = pm.newQuery("select from "+Predicket.class.getName()+" where userId == userIdParam && state == stateParam parameters String userIdParam, int stateParam");
			List<Predicket> predsInDB = (List<Predicket>)q.execute(fbId,Predicket.STATE_PENDING);
			int len = predsInDB.size();
			retOb.put("available",Predicket.MAX_PENDING_PREDICKETS-(len));
			
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(retOb.toString());
		} catch (Exception e) {
			log.severe("Error in login operation "+e);
			
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			pm.close();
		}
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
		
	}

}
