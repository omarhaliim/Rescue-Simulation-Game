package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;

import javafx.scene.layout.Border;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.sun.javafx.tk.Toolkit;
import com.sun.prism.Image;

import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulatable;
import simulation.Simulator;

@SuppressWarnings("serial")
public class Mywindow extends JFrame {
	JPanel panel2;
	JPanel panel3;
	JPanel panel4;
	JPanel panel5;
	JButton Ambulance;
	JButton Evaculator;
	JButton FireTruck;
	JButton nextCycle;
	JLabel Cycle;
	JLabel casualty;
	JLabel a;
	JLabel b;
	JLabel c;
	JLabel d;
	JLabel e;
	JLabel f;
	JLabel k;
	JLabel q;
	JTextArea DisastersTextArea;
	JTextArea gameLog;
	private JTextArea info;
	
	JPanel unitsPanel;
	ArrayList<Unit> units;
	
	ArrayList<GridButton> gridButtons;

	public Mywindow() {
		super();
		
		setTitle("Rescue Simulation");
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout(8, 6));
		setBackground(Color.PINK);
		this.getRootPane().setBorder(
				BorderFactory.createMatteBorder(4, 4, 4, 4, Color.yellow));

		JPanel panel2 = new JPanel(new GridLayout(3, 1));
		
		units = new ArrayList<>();

		this.DisastersTextArea = new JTextArea(); 
		DisastersTextArea.setEditable(false);
		this.gameLog = new JTextArea(); 
		gameLog.setEditable(false);
		this.setInfo(new JTextArea()); 
		getInfo().setEditable(false);
		
		JScrollPane scrollPane1 = new JScrollPane(DisastersTextArea);
		JScrollPane scrollPane2 = new JScrollPane(gameLog);
		JScrollPane scrollPane3 = new JScrollPane(getInfo());
		scrollPane1.setPreferredSize(new Dimension(400, getHeight()/3));
		scrollPane2.setPreferredSize(new Dimension(400, getHeight()/3));
		scrollPane3.setPreferredSize(new Dimension(400, getHeight()/3));
		
		panel2.add(scrollPane1);
		panel2.add(scrollPane2);
		panel2.add(scrollPane3);
		
		
		
		///////////////////////////
		panel5 = new JPanel();
		int row = 10;
		int col = 10;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		panel5.setLayout(new GridLayout(row, col));
		gridButtons = new ArrayList<>();
		GridButton[][] grid = new GridButton[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				grid[i][j] = new GridButton(getInfo());
				grid[i][j].setBorder(new LineBorder(Color.BLACK));
				grid[i][j].setOpaque(true);
				grid[i][j].setBackground(Color.white);
				
				panel5.add(grid[i][j]);
				gridButtons.add(grid[i][j]);
			}
		}
		add(panel5);
		add(panel2, BorderLayout.WEST);

		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(4,1));
		
		nextCycle = new JButton("Next Cycle");
		nextCycle.setFont(new Font(Font.DIALOG, Font.ITALIC, 25));
		panel3.add(nextCycle);

		Cycle = new JLabel("Cycle: 0");
		Cycle.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		panel3.add(Cycle);
		
		casualty = new JLabel("Casualties: 0");
		casualty.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
		panel3.add(casualty);
		
		unitsPanel = new JPanel();
		panel3.add(unitsPanel);
		
		
		
		
		panel3.setBackground(Color.white);
		panel3.setBorder(new LineBorder(Color.black, 3));
		add(panel3, BorderLayout.EAST);
	}

	

	public void updateActiveDisasters(String DisastersOnTextArea){
		DisastersTextArea.setText(DisastersOnTextArea);
	}
	
	public void updateGameLog(String GameLogTextArea){
		gameLog.setText(gameLog.getText()+"\n"+GameLogTextArea);
	}
	
	public void updateInfo(String infoOnTextArea){
		getInfo().setText(infoOnTextArea);
	}
	
	public void setCycle(int cycle) {
		Cycle.setText("Cycle: " + cycle);
	}
	
	public void setCasualties(int casualties) {
		casualty.setText("Casualties: " + casualties);
	}
	
	public void addObjectOnGrid(Simulatable objectOnGrid){
		if (objectOnGrid instanceof Unit) {
			Unit unit = (Unit) objectOnGrid;
			((GridButton)panel5.getComponent(unit.getLocation().getY() * 10 + unit.getLocation().getX())).addSimulatable(objectOnGrid);
		} else {
			Rescuable target = (Rescuable) objectOnGrid;
			((GridButton)panel5.getComponent(target.getLocation().getY() * 10 + target.getLocation().getX())).addSimulatable(objectOnGrid);
		}
	}

	public JButton getNextButton() {
		return nextCycle;
	}
	
	public JPanel getUnitsPanel() {
		return unitsPanel;
	}
	
	public ArrayList<GridButton> getGridButtons() {
		return gridButtons;
	}
	
	public ArrayList<Unit> getUnits() {
		return units;
	}



	public JTextArea getInfo() {
		return info;
	}



	public void setInfo(JTextArea info) {
		this.info = info;
	}
	
}
