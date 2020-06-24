package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextArea;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Rescuable;
import simulation.Simulatable;

public class GridButton extends JButton implements ActionListener {
	private ArrayList<Simulatable> Simulatables;
	JTextArea info;

	public GridButton(JTextArea info) {
		Simulatables = new ArrayList<>();
		addActionListener(this);
		this.info = info;

	}
	
	public void addSimulatable(Simulatable sim) {
		Simulatables.add(sim);
		if (sim instanceof Citizen) {
			setBackground(Color.ORANGE);
			setIcon(new ImageIcon("src/view/icon0.jpg"));// change this
		} else if(sim instanceof ResidentialBuilding) {
			setBackground(Color.GREEN);
			setIcon(new ImageIcon("src/view/icona1.jpg"));
		}
		else {
			///////
		}
	}

	public void removeSimulatable(Simulatable sim) {
		Simulatables.remove(sim);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String s = "";
		for (int i = 0; i < Simulatables.size(); i++) {
			s += Simulatables.get(i).toString() + "\n";
		}
		info.setText(s);

	}

	public Rescuable getTarget() {
		for (int i = 0; i < Simulatables.size(); i++) {
			if (Simulatables.get(i) instanceof Rescuable) {
				return (Rescuable) Simulatables.get(i);
			}
		}
		return null;
	}

	public ArrayList<Simulatable> getSimulatables() {
		return Simulatables;
	}
	
}
