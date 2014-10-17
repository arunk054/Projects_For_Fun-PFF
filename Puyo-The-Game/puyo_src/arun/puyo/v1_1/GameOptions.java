package arun.puyo.v1_1;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class GameOptions extends JDialog implements ActionListener,KeyListener {

	JTextField playerName,gameSpeed,fallSpeed,smoothness,xSize,ySize;

	private JButton save;

	private int difficultyLevel;

	private LevelRadioButton easy, med, hard;

	private ButtonGroup levGroup;

	public GameOptions(JFrame f) {
		super(f, "Set Game Options", true);
		// gtting the root pane and ading our main container
		Container pane = this.getContentPane();
		JPanel containPanel = new JPanel();
		pane.add(containPanel);
		this.setContentPane(containPanel);

		
		/* Adding the components */
		// First the panel for displaying the text
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		JLabel l1 = new JLabel("Enter Your Name: ");
		panel1.add(l1);
		playerName = new JTextField(AppConsts.DEFAULT_PLAYER_NAME, 15);
		playerName.addKeyListener(this);
		playerName.selectAll();
		panel1.add(playerName);

		// Next the radio buttons for difficulty level
		JPanel panelLev = new JPanel();
		levGroup = new ButtonGroup();
		easy = new LevelRadioButton("Easy", false,levGroup,"easy",this);
		med = new LevelRadioButton("Medium", true,levGroup,"med",this);
		hard = new LevelRadioButton("Hard", false,levGroup,"hard",this);
		difficultyLevel = 1;
		
		panelLev.add(easy);
		panelLev.add(med);
		panelLev.add(hard);

		JPanel panelSpeed = new JPanel();
		panelSpeed.add(new JLabel("Game Speed: "));
		gameSpeed=new JTextField(Integer.toString(AppConsts.MOVES_PER_SEC), 2);
		gameSpeed.addKeyListener(this);
		panelSpeed.add(gameSpeed);

		panelSpeed.add(new JLabel("Animation Speed: "));
		fallSpeed=new JTextField(Integer.toString(AppConsts.FALL_MOVES_PER_SEC), 2);
		fallSpeed.addKeyListener(this);
		panelSpeed.add(fallSpeed);

		panelSpeed.add(new JLabel("Smoothness: "));
		smoothness=new JTextField(Integer.toString(AppConsts.STEPS_PER_GRID), 2);
		smoothness.addKeyListener(this);
		panelSpeed.add(smoothness);

		JPanel panelSize = new JPanel();
		panelSize.add(new JLabel("GRID SIZE "));
		
		xSize=new JTextField(Integer.toString(AppConsts.MAX_COLS), 2);
		xSize.addKeyListener(this);
		panelSize.add(xSize);
		
		panelSize.add(new JLabel("  X  "));
			
		ySize=new JTextField(Integer.toString(AppConsts.MAX_ROWS), 2);
		ySize.addKeyListener(this);
		panelSize.add(ySize);
		
		save = new JButton("DONE");
		save.addActionListener(this);

		/* Now adding all the panels to main panel */
		containPanel.setLayout(new GridLayout(0, 1));
		containPanel.add(panel1);
		containPanel.add(panelLev);
		containPanel.add(panelSpeed);
		containPanel.add(panelSize);
		containPanel.add(save);

		this.setResizable(false);
		this.setLocation(f.getLocation().x - 20, f.getLocation().y + 100);
		this.pack();
		this.show();
	}

	public String getPlayerName() {
		return playerName.getText();
	}

	public int getDifficultyLevel() {
		return difficultyLevel;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==save)
		{
			this.dispose();
		}
		else if(levGroup.getSelection().getActionCommand()=="easy")
		{
			difficultyLevel=0;
		}
		else if(levGroup.getSelection().getActionCommand()=="med")
		{
			difficultyLevel=1;
		}	
		else if(levGroup.getSelection().getActionCommand()=="hard")
		{
			difficultyLevel=2;
		}				
	}
	

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
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
	
	class LevelRadioButton extends JRadioButton
	{
		LevelRadioButton(String text,boolean selected,ButtonGroup b,String action,ActionListener a)
		{
			super(text,selected);
			b.add(this);
			setActionCommand(action);
			addActionListener(a);
		}
	}
}
