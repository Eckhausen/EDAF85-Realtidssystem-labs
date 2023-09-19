package lift;

public class LiftMonitor {
    private int maxFloors, maxPassengers, currentPassengerCount, currentFloor;
    private boolean doorsOpen = false;
    private int[] toEnter;
    private int[] toExit;
    private boolean isMoving;
    private enum Direction{UP, DOWN}
    private Direction currentDirection;
    private LiftView liftView;

    public LiftMonitor(int maxFloors, int maxPassengers, LiftView liftView){
        this.maxFloors = maxFloors;
        this.maxPassengers = maxPassengers;
        this.liftView = liftView;

        currentFloor = 0;
        currentPassengerCount = 0;
        doorsOpen = false;
        toEnter = new int[maxFloors];
        toExit = new int[maxFloors];
        currentDirection = Direction.UP;
        isMoving = false;
    }
    synchronized int getCurrentFloor(){
        return currentFloor;
    }

    synchronized int getFromFloor(){
        return toEnter[0];
    }
    synchronized int getNextFloor(){
        return toExit[0];
    }

    synchronized void changeDirection(){
        if(currentFloor == maxFloors - 1){
            currentDirection = Direction.DOWN;
        } else if(currentFloor == 0){
            currentDirection = Direction.UP;
        }
    }
    synchronized void standby(){
        try{
            wait();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
