package arun.puyo.v1_1;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HighScoreDialog extends JDialog implements KeyListener{

	public HighScoreDialog(ArrayList highScoreObjects,boolean isCurrentAdded)
	{
		super();
		Container pane = this.getContentPane();
		JPanel containPanel = new JPanel();
		pane.add(containPanel);
		this.setContentPane(containPanel);	
		containPanel.setLayout(new GridLayout(0, 1));
		addKeyListener(this);
		
		JLabel heading;
		if(isCurrentAdded)
			heading=new JLabel("      Congrats! Your Score has been Added to High Scores      ");
		else
			heading=new JLabel("      Sorry! Your score does not come into HighScores      ");
		
		heading.setHorizontalAlignment(JLabel.CENTER);
		containPanel.add(heading);
		
		JLabel dummy1=new JLabel(" ");
		dummy1.setHorizontalAlignment(JLabel.CENTER);
		containPanel.add(dummy1);
		
		JLabel h1=new JLabel(HighScore.getheading1());
		h1.setHorizontalAlignment(JLabel.CENTER);
		containPanel.add(h1);

		/*JLabel h2=new JLabel(HighScore.getheading2());
		containPanel.add(h2);*/

		JLabel dummy2=new JLabel(" ");
		dummy2.setHorizontalAlignment(JLabel.CENTER);
		containPanel.add(dummy2);
		
		for (Iterator iter = highScoreObjects.iterator(); iter.hasNext();) {
			HighScore element = (HighScore) iter.next();
			JLabel l1=new JLabel(element.toString());
			l1.setHorizontalAlignment(JLabel.CENTER);
			containPanel.add(l1);
			
		}
		
		JLabel dummy3=new JLabel(" ");
		dummy3.setHorizontalAlignment(JLabel.CENTER);
		containPanel.add(dummy3);
		
		this.setResizable(true);
		this.setLocation(AppConsts.FRAME_X_POS- 20, AppConsts.FRAME_Y_POS + 100);
		this.pack();
		this.show();
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_ENTER: case KeyEvent.VK_ESCAPE:
				this.dispose();
				break;
			default:	
				//MyLogger.log("Error in detecting the key "+e.getKeyCode());
				break;
		}
		
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
