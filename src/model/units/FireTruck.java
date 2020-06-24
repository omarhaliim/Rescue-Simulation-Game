package model.units;

import exceptions.CannotTreatException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() throws CannotTreatException {
		if (this.getTarget().getDisaster() instanceof Fire) {

			getTarget().getDisaster().setActive(false);

			ResidentialBuilding target = (ResidentialBuilding) getTarget();
			if (target.getStructuralIntegrity() == 0) {
				jobsDone();
				return;
			} else if (target.getFireDamage() > 0)

				target.setFireDamage(target.getFireDamage() - 10);

			if (target.getFireDamage() == 0)

				jobsDone();

		} else if (this.getTarget().getDisaster() instanceof Collapse) {
			throw new CannotTreatException(this, this.getTarget(),
					"fire unit cant handle more than fire fire truck");
		}

		else {
			throw new CannotTreatException(this, this.getTarget(),
					"fire unit cant handle more than fire");
		}

	}
}