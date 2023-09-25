package lift;

public class LiftMonitor {
    private int maxFloors, maxPassengers, currentPassengerCount, currentFloor, nextFloor;
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

    synchronized boolean paxOnFloor() {
        if (toEnter[currentFloor] > 0 || toExit[currentFloor] > 0) {
            return true;
        }
        return false;
    }

    synchronized int getNextFloor(){
        changeDirection();
        if(currentDirection == Direction.UP && currentFloor != maxFloors - 1){
            nextFloor = currentFloor + 1;
        } else {
            nextFloor = currentFloor - 1;
        }
        return nextFloor;
    }



    synchronized void changeDirection(){
        if(currentFloor == maxFloors - 1){
            currentDirection = Direction.DOWN;
        } else if(currentFloor == 0){
            currentDirection = Direction.UP;
        }
    }

    synchronized boolean isDoorsOpen(){
        return doorsOpen;
    }

    synchronized void incPax(int startFloor){
        toEnter[startFloor]++;
        liftView.showDebugInfo(toEnter, toExit);
    }
    synchronized void decPax(int startFloor){
        toEnter[startFloor]--;
        liftView.showDebugInfo(toEnter, toExit);
        currentPassengerCount++;
        notifyAll();
    }

    synchronized void waitForLift(int startFloor) {
        while(currentFloor != startFloor || !doorsOpen){
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    synchronized void waitForPax(){
        while(toEnter[currentFloor] > 0 && hasSpaceForMorePassengers()){
            try{
                openDoors();
                wait();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        closeDoors();
    }

    synchronized void waitToExit(int destinationFloor){
        try{
            while (currentFloor != destinationFloor || !doorsOpen){
                wait();
            }
            liftView.closeDoors();
        } catch (Exception e){
            e.printStackTrace();
        }
    currentPassengerCount--;
    }

    synchronized void openDoors() {
        if(!doorsOpen){
            doorsOpen = true;
            liftView.openDoors(currentFloor);
            notifyAll(); // Informera alla väntande passagerare att dörrarna är öppna
        }

    }

    synchronized void closeDoors() {
        if (doorsOpen) {  // Endast stäng dörrarna om de är öppna
            doorsOpen = false;
            liftView.closeDoors();
        }
    }


    synchronized boolean hasSpaceForMorePassengers() {
        return currentPassengerCount < maxPassengers;
    }

    synchronized void updateCurrentFloor(){
        currentFloor = nextFloor;
        notifyAll();
    }


}
