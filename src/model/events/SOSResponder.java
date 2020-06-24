package model.events;

import exceptions.CannotTreatException;
import simulation.Rescuable;

public interface SOSResponder {
public void respond(Rescuable r) throws Exception ;
}
