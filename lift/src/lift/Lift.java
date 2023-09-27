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
                currentFloor = monitor.getCurrentFloor();
                nextFloor = monitor.getNextFloor();
                monitor.waitForPax();
                liftView.moveLift(currentFloor, nextFloor);
                monitor.updateCurrentFloor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
