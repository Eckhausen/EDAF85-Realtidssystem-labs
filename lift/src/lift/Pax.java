package lift;

public class Pax extends Thread{
    private int startFloor;
    private int destinationFloor;
    private Passenger passengerView;

    public Pax(Passenger passenger, LiftMonitor monitor){
        this.startFloor = passenger.getStartFloor();
        this.destinationFloor = passenger.getDestinationFloor();
        this.passengerView = passenger;
    }
    public void run(){

    }
}
