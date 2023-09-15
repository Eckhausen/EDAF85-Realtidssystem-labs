package lift;

public class LiftMonitor {
    private int maxFloors, maxPassengers, currentAmountPassengers, currentFloor;
    private boolean doorsOpen = false;
    private int[] toEnter;
    private int[] toExit;
    private boolean isMoving = false;
    private enum direction{
        UP, DOWN;
    }

    public LiftMonitor(int maxFloors, int maxPassengers){
        this.maxFloors = maxFloors;
        this.maxPassengers = maxPassengers;

        currentFloor = 0;
        currentAmountPassengers = 0;
        doorsOpen = false;
        toEnter = new int[maxFloors];
        toExit = new int[maxFloors];
    }
    public int getCurrentFloor(){
        return currentFloor;
    }

    public int getFromFloor(){
        return toEnter[0];
    }
    public int getNextFloor(){
        return toExit[0];
    }




}
