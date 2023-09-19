import lift.*;

public class LiftMain2 extends Thread {
    public static void main(String[] args) {
        final int NBR_FLOORS = 7, MAX_PASSENGERS = 4;

        LiftView view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);
        LiftMonitor monitor = new LiftMonitor(NBR_FLOORS, MAX_PASSENGERS, view);
        Lift liftThread  = new Lift(view, monitor);

        for(int i = 0; i < 20; i++){
            Pax passengerThread = new Pax(view.createPassenger(), monitor);
            passengerThread.start();
        }
        liftThread.start();
        //int  fromFloor = pass.getStartFloor();
        //int    toFloor = pass.getDestinationFloor();
    }

}
