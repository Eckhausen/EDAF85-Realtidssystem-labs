package lift;

public class LiftMonitor {
    private int maxFloors, maxPassengers, currentPassengerCount, currentFloor, nextFloor, passengerEntering, passengerExiting;
    private boolean doorsOpen = false;
    private int[] toEnter, toExit;
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
        this.passengerEntering = 0;
        this.passengerExiting = 0;
    }
    synchronized void  addPaxWaiting(int startFloor){
        this.toEnter[startFloor]++;
        notifyAll();
    }
    synchronized int getCurrentFloor(){
        return currentFloor;
    }
    synchronized void updateCurrentFloor(int nextFloor){
        currentFloor = nextFloor;
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
    synchronized void addPax(int destination){
        currentPassengerCount++;
        passengerEntering--;
        toExit[destination]++;
        toEnter[currentFloor]--;
        liftView.showDebugInfo(toEnter, toExit);
        notifyAll();
    }
    synchronized void removePax(){
        currentPassengerCount--;
        passengerExiting--;
        toExit[currentFloor]--;
        liftView.showDebugInfo(toEnter, toExit);
        notifyAll();
    }

    synchronized void waitToEnterLift(int startFloor) {
        while(!doorsOpen ||
                currentFloor != startFloor ||
                (currentPassengerCount + passengerEntering - passengerExiting) == maxPassengers){
            try{
                wait();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        passengerEntering++;
    }
    synchronized void waitToExitLift(int destination){
        while(currentFloor != destination || !doorsOpen){
            try{
                wait();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        passengerExiting++;
        notifyAll();
    }

    synchronized void allowPassengersInOut(){
        while(((toEnter[currentFloor] > 0) && currentPassengerCount != maxPassengers) ||
                (toExit[currentFloor] > 0) ||
                (passengerEntering > 0)){

            notifyAll();
            try{
                if(!doorsOpen){
                    liftView.openDoors(currentFloor);
                    toggleDoors();
                }
                wait();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        if(doorsOpen){
            liftView.closeDoors();
            toggleDoors();
        }
    }

    private void toggleDoors() {
        doorsOpen = !doorsOpen;
    }

    synchronized void waitForPax(){
        while(!passengerWaiting() && currentPassengerCount == 0){
            try{
                wait();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean passengerWaiting(){
        for(int i=0; i<maxFloors; i++){
            if(toEnter[i] > 0){
                return true;
            }
        }
        return false;
    }





}
