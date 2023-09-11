
import lift.LiftView;
import lift.Passenger;

public class OnePersonRidesLift {

    /*
    * What shared data will you need in your monitor?
    * nbrPassengers, totalFloors, currentFloor, doorsOpen, destination, max
    *
    • Which monitor attributes are accessed/modified by which threads?
    *
    • What condition must be true for a passenger to be allowed to enter the lift?
    * maxPassengers, notMoving
    *
    • What condition must be true for a passenger to leave the lift?
    * notMoving, doorsOpen, correct floor
    *
    • What condition must be true for the lift to start moving to another floor?
    * doors closed, not moving, passengers exited.
    • What monitor operations will you need? What is to be done in each monitor operation?
    * gets... set... isMoving, doors open/closed.
    • Where should calls to wait() and notifyAll() be placed?
    * Monitor
    *
    * */

    public static void main(String[] args) {

        final int NBR_FLOORS = 7, MAX_PASSENGERS = 4;

        LiftView  view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);
        Passenger pass = view.createPassenger();
        int  fromFloor = pass.getStartFloor();
        int    toFloor = pass.getDestinationFloor();

        pass.begin();                        // walk in (from left)
        if (fromFloor != 0) {
            view.moveLift(0, fromFloor);
        }
        view.openDoors(fromFloor);
        pass.enterLift();                    // step inside

        view.closeDoors();
        view.moveLift(fromFloor, toFloor);   // ride lift
        view.openDoors(toFloor);

        pass.exitLift();                     // leave lift
        pass.end();                          // walk out (to the right)
    }
}