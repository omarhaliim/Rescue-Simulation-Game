package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);

	}

	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException {
		if (!(this.getTarget().getDisaster() instanceof Collapse)) {
			ResidentialBuilding target = (ResidentialBuilding) getTarget();
			target.setFoundationDamage(target.getFoundationDamage() + 10);
			super.strike();
		} else {
			throw new BuildingAlreadyCollapsedException(this," this building is already collapsed ");
		}
	}

	public void cycleStep() throws BuildingAlreadyCollapsedException {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setFoundationDamage(target.getFoundationDamage() + 10);
	}

}
