package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Infection;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location,
			int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() throws CannotTreatException {
		if (this.getTarget().getDisaster() instanceof Infection) {

			getTarget().getDisaster().setActive(false);
			Citizen target = (Citizen) getTarget();
			if (target.getHp() == 0) {
				jobsDone();
				return;
			} else if (target.getToxicity() > 0) {
				target.setToxicity(target.getToxicity() - getTreatmentAmount());
				if (target.getToxicity() == 0)
					target.setState(CitizenState.RESCUED);
			}

			else if (target.getToxicity() == 0)
				heal();

		} else {
			throw new CannotTreatException(this, this.getTarget(),
					"disease unit can't handle infiction");
		}
	}

	public void respond(Rescuable r) throws CannotTreatException,
			IncompatibleTargetException {
		if ((r instanceof ResidentialBuilding)) {
			throw new IncompatibleTargetException(this, r,
					"Disease control unit can't handle this");
		}
		if (!canTreat(r)) {
			throw new CannotTreatException(this, r,
					"Disease control unit says: this citizen has an injury not an infiction");
		}
		if (getTarget() != null && ((Citizen) getTarget()).getToxicity() > 0
				&& getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
	}
}
