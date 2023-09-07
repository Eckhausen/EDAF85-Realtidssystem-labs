package clock.io;

import java.util.concurrent.Semaphore;

public class Monitor {
    private int hrs, min, sec;
    private int ah, am, as;
    private int alarmDuration = 20;
    private Semaphore mutex;

    public Monitor(){

    }

    private int[] getTime(){
        //Skicka hrs, min, sec i en int vektor
        return new int[0];
    }

    private void setTime(){
        //Ställ in tiden.
    }

    private void tickTime(){

    }

    private int[] getAlarmTime(){

        return new int[0];
    }

    private void setAlarmTime(){

    }
}
