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
                if (monitor.paxOnFloor() && monitor.hasSpaceForMorePassengers()){
                    //Om det finns pax på våningen OCH nuvarande passagerarantal är mindre än maxantalet.
                    monitor.openDoors();
                    monitor.wait();
                }
                if (!monitor.paxOnFloor() || !monitor.hasSpaceForMorePassengers()){
                    monitor.closeDoors();
                }
                liftView.moveLift(monitor.getCurrentFloor(), monitor.getNextFloor());
                monitor.updateCurrentFloor();
                //Uppdatera currentFloor
                monitor.notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
