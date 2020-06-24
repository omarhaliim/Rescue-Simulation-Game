package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.people.Citizen;
import model.people.CitizenState;


public class Injury extends Disaster {

	public Injury(int startCycle, Citizen target) {
		super(startCycle, target);
	}
	@Override
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException 
	{ Citizen target = (Citizen)getTarget();
	if(!(target.getState().equals(CitizenState.DECEASED))){
		target.setBloodLoss(target.getBloodLoss()+30);
		super.strike();
	}
	else 
		throw new CitizenAlreadyDeadException(this," this citizen is Dead now bitch in injury");
	}
	@Override
	public void cycleStep() {
		Citizen target = (Citizen)getTarget();
		target.setBloodLoss(target.getBloodLoss()+10);
		
	}

}
