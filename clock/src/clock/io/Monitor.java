package clock.io;

import java.util.concurrent.Semaphore;

public class Monitor {
    private int hrs, min, sec;
    private int ah, am, as;
    private int alarmDuration = 20;
    private Semaphore mutex = new Semaphore(1);

    public Monitor(){

    }

    public int[] getTime(){
        //Skicka hrs, min, sec i en int vektor
        int[] time = new int[3];
        time[0] = hrs;
        time[1] = min;
        time[2] = sec;
        return time;
    }

    public void setTime(int hrs, int min, int sec) {
        //Ställ in tiden.
        try{
            mutex.acquire();
            this.hrs = hrs;
            this.min = min;
            this.sec = sec;
            mutex.release();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void tickTime()  {
        try {
            mutex.acquire();
            sec++;
            if(sec >= 60){
                sec = 0;
                min++;
            }
            if(min >= 60){
                min = 0;
                hrs++;
            }
            if(hrs >= 24){
                hrs = 0;
            }
            mutex.release();
            } catch(Exception e){
                e.printStackTrace();
            }
    }

    private int[] getAlarmTime(){

        return new int[0];
    }

    public void setAlarmTime(int hrs, int min, int sec){
        this.ah = hrs;
        this.am = min;
        this.as = sec;
    }

    public void armAlarm(){

    }
}
