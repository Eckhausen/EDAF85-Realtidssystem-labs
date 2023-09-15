package lift;

public class Lift {
    private int currentFloor;
    private int nextFloor;
    private LiftMonitor monitor;
    private LiftView liftView;
    public Lift(LiftView view, LiftMonitor monitor){
        this.liftView = view;
        this.monitor = monitor;
    }

}
