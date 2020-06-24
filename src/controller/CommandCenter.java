package controller;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulatable;
import simulation.Simulator;
import view.GridButton;
import view.Mywindow;

public class CommandCenter extends Mywindow implements SOSListener,
		ActionListener {

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	Mywindow x;
	@SuppressWarnings("unused")
	private ArrayList<Unit> emergencyUnits;

	Unit selectedUnit;

	public CommandCenter() throws Exception {

		engine = new Simulator(this);
		engine.setEmergencyService(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		x = new Mywindow();
		x.setVisible(true);
		engine.setWindow(x);
		x.getNextButton().addActionListener(this);
		x.getUnitsPanel().setLayout(new GridLayout(emergencyUnits.size(), 1));
		for (int i = 0; i < emergencyUnits.size(); i++) {
			JButton button = new JButton(emergencyUnits.get(i).getClass()
					.getSimpleName()
					+ " " + emergencyUnits.get(i).getUnitID());
			button.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
			x.getUnitsPanel().add(button);
			x.getUnits().add(emergencyUnits.get(i));
			button.addActionListener(this);
		}
		for (int i = 0; i < x.getGridButtons().size(); i++) {
			x.getGridButtons().get(i).addActionListener(this);
		}
		x.validate();
	}

	@Override
	public void receiveSOSCall(Rescuable r) {

		if (r instanceof ResidentialBuilding) {

			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);

		} else {

			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}
		x.addObjectOnGrid((Simulatable) r);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();
		if (button.getText().equals("Next Cycle")) {
			if (!engine.checkGameOver()) {
				try {
					engine.nextCycle();
				} catch (IncompatibleTargetException | CannotTreatException
						| CitizenAlreadyDeadException
						| BuildingAlreadyCollapsedException e) {
					String message = e.getMessage();
					JOptionPane.showMessageDialog(x, message);
				}
			} else {
				JOptionPane.showMessageDialog(x, "Game over!");
			}
		} else if (button instanceof GridButton) {
			if (selectedUnit != null) {
				Rescuable target = ((GridButton) button).getTarget();
				if (target != null) {
					try {
						selectedUnit.respond(target);
						selectedUnit = null;
					} catch (IncompatibleTargetException | CannotTreatException
							e) {
						JOptionPane.showMessageDialog(x, e.getMessage());
					}
				if(target instanceof Citizen&&((Citizen)target).getHp()==0){
					JOptionPane.showMessageDialog(x,"this citizen is dead");
				}
				if(target instanceof ResidentialBuilding&&((ResidentialBuilding)target).getStructuralIntegrity()==0){
					JOptionPane.showMessageDialog(x,"this building has been destroyed");
				}
				}
			}
		} else {
			selectedUnit = x.getUnits().get(
					Integer.parseInt(button.getText().split(" ")[1]) - 1);
			x.getInfo().setText(selectedUnit.toString());

		}
	}

	public static void main(String[] args) throws Exception {
		CommandCenter commandCenter = new CommandCenter();

	}

}
