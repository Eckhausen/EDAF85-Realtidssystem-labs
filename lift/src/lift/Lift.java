package lift;

public class Lift extends Thread{
    private int currentFloor, nextFloor, fromFloor;
    private LiftMonitor monitor;
    private LiftView liftView;
    public Lift(LiftView view, LiftMonitor monitor){
        this.liftView = view;
        this.monitor = monitor;
    }
    public void run(){
        while(true){
            try {
                wait();

            } catch (Exception e) {
                e.printStackTrace();
            }
            currentFloor = monitor.getCurrentFloor();
            fromFloor = monitor.getFromFloor();
            nextFloor = monitor.getNextFloor();
            liftView.moveLift(currentFloor, nextFloor);
        }
    }

}
