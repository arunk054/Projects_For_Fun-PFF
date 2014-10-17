/****************************************
 * 
 * 
 * @author ARUN
 *
 *Created on 30-4-2007 
 */
package arun.puyo.v1_1;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public class GroupPuyoManager {

	ArrayList groupPuyos; // ArrayList of GroupPuyo objects

	public GroupPuyoManager() {
		groupPuyos = new ArrayList();
	}

	public void add(Puyo p) {
		ArrayList tempList = new ArrayList();
		for (int i = 0; i < groupPuyos.size(); ++i) {
			GroupPuyo gPuyo = (GroupPuyo) groupPuyos.get(i);
			if (gPuyo.check(p) == true)// calling the check method on the
										// GroupPuyo Object
			{
				// Noting that how many groups can our p be part of
				tempList.add(gPuyo);
			}
		}
		// Merge all gPuyo in tempList
		GroupPuyo tempGPuyo = new GroupPuyo(p);
		for (int i = 0; i < tempList.size(); ++i) {
			tempGPuyo.add((GroupPuyo) tempList.get(i));
			// remove it from the list
			groupPuyos.remove(tempList.get(i));
		}
		// Add our newly created groupPuyo to the list
		groupPuyos.add(tempGPuyo);
	}

	public void draw(ImageHolder imH, Graphics g, ImageObserver gp) {
		for (int i = 0; i < groupPuyos.size(); ++i) {
			((GroupPuyo) groupPuyos.get(i)).draw(imH, g, gp);
		}

	}

	public boolean eliminateLargeGroups(PuyoManager pm) {
		ArrayList tempList = new ArrayList();
		boolean eliminated=false;
		for (int i = 0; i < groupPuyos.size(); ++i) {
			GroupPuyo gPuyo = (GroupPuyo) groupPuyos.get(i);
			if (gPuyo.canBeEliminated(pm) == true)// calling the check method on
												// the GroupPuyo Object
			{
				eliminated = true;
				// Noting that this gpuyo needs to be eliminated
				tempList.add(gPuyo);
			}
		}
		for (int i = 0; i < tempList.size(); ++i) {
			// remove it from the list
			groupPuyos.remove(tempList.get(i));
		}
		return eliminated;
	}

	public Puyo getPuyo(int row, int column) {
		Puyo p = null;
		for (Iterator iter = groupPuyos.iterator(); iter.hasNext();) {
			GroupPuyo gPuyo = (GroupPuyo) iter.next();
			p = gPuyo.getPuyo(row, column);
			if (p != null) {
				return p;
			}

		}
		return null;
	}

	public void remove(Puyo p) {
		int row = p.getRow(), column = p.getColumn();
		for (Iterator iter = groupPuyos.iterator(); iter.hasNext();) {
			GroupPuyo gPuyo = (GroupPuyo) iter.next();
			if (gPuyo.getPuyo(row, column) != null) {
				MyLogger.log("Removed " + row + ", " + column);
				MyLogger.log("size b4 removing=" + gPuyo.size());
				gPuyo.remove(p);// ,true);//the boolean is only dummy
				MyLogger.log("size after removing=" + gPuyo.size());
				if (gPuyo.size() == 0) {
					groupPuyos.remove(gPuyo);
				}
				break;
			}
		}

	}

	public int getNoOfGroups() {
		return groupPuyos.size();
	}
}