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
                if (monitor.paxOnFloor()){
                    liftView.openDoors(monitor.getCurrentFloor());
                    monitor.wait();
                }
                liftView.moveLift(monitor.getCurrentFloor(), monitor.getNextFloor());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
