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
        passenger.begin();
        monitor.incPax(startFloor);
        monitor.waitForLift(startFloor);
        passenger.enterLift();
        monitor.decPax(startFloor);
        monitor.waitToExit(destinationFloor);
        passenger.exitLift();
        passenger.end();
    }
}
