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
                monitor.waitForPax();
                liftView.moveLift(monitor.getCurrentFloor(), monitor.getNextFloor());
                monitor.updateCurrentFloor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
