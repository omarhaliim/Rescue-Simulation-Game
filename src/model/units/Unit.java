package model.units;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import exceptions.UnitException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws IncompatibleTargetException,
			CannotTreatException{
		if (!(this instanceof MedicalUnit) && r instanceof Citizen) {
			if (this instanceof Evacuator){
				throw new IncompatibleTargetException(this, r, "Evacuator can't treat that Citizen");
			}
			else if (this instanceof FireTruck){
				throw new IncompatibleTargetException(this, r, "Fire truck can't treat that Citizen");
			}
			else if (this instanceof GasControlUnit){
				throw new IncompatibleTargetException(this, r, "Gas Control Unit can't treat that Citizen");
			}
		}
		if (!canTreat(r)) {
			if(this instanceof Evacuator){
				throw new CannotTreatException(this, r, "Evacuator can't treat that building");
			}
			else if(this instanceof FireTruck){
				throw new CannotTreatException(this, r, "Fire truck can't treat that building");
			}
			else {
				throw new CannotTreatException(this, r, "Gas control unit can't treat that building");
			}
		}
		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}

	public abstract void treat() throws IncompatibleTargetException,
			CannotTreatException;

	public void cycleStep() throws IncompatibleTargetException,
			CannotTreatException {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}

	public boolean canTreat(Rescuable r) {
		if (r instanceof Citizen) {
			if (this instanceof Ambulance && ((Citizen) r).getBloodLoss() > 0) {
				return true;
			}
			if (this instanceof DiseaseControlUnit
					&& ((Citizen) r).getToxicity() > 0) {
				return true;
			}
		} else {
			if (this instanceof FireTruck
					&& ((ResidentialBuilding) r).getFireDamage() > 0) {
				return true;
			}
			if (this instanceof GasControlUnit
					&& ((ResidentialBuilding) r).getGasLevel() > 0) {
				return true;
			}
			if (this instanceof Evacuator
					&& ((ResidentialBuilding) r).getFoundationDamage() > 0) {
				return true;
			}

		}
		return false;

	}

	@Override
	public String toString() {
		return "Unit: " + getClass().getSimpleName() + " \nUnitID: " + unitID
				+ "\nlocation :  (" + location.getX() + "," + location.getY()
				+ ")" + "\nState: " + state + "\nlocation: " + location
				+ "\ntarget: " + target + "\nstepsPerCycle: " + stepsPerCycle;
	}

}
