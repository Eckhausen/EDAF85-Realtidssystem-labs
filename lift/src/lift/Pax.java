package lift;

public class Pax extends Thread{
    private int startFloor;
    private int destinationFloor;
    private Passenger passenger;
    private LiftMonitor monitor;

    public Pax(Passenger passenger, LiftMonitor monitor){
        this.startFloor = passenger.getStartFloor();
        this.destinationFloor = passenger.getDestinationFloor();
        this.passenger = passenger;
        this.monitor = monitor;
    }
    public void run(){
        //Starta och gå till hiss.
        passenger.begin();
        monitor.addPaxWaiting(startFloor);
        monitor.waitToEnterLift(startFloor);
        //Gå in i hiss.
        passenger.enterLift();
        monitor.addPax(destinationFloor);
        //Gå ut ur hiss.
        monitor.waitToExitLift(destinationFloor);
        passenger.exitLift();
        monitor.removePax();

        passenger.end();
    }
}
