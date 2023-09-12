package clock.io;

import java.util.concurrent.Semaphore;

public class Monitor {
    private int hrs, min, sec;
    private int alarmHrs, alarmMin, alarmSec;
    private Semaphore mutex = new Semaphore(1);
    private boolean alarmEnabled = false;

    public Monitor(){

    }

    public int[] getTime(){
        try{
            mutex.acquire();
            int[] time = new int[3];
            time[0] = hrs;
            time[1] = min;
            time[2] = sec;
            mutex.release();
            return time;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setTime(int hrs, int min, int sec) {
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

    public void setAlarmTime(int hrs, int min, int sec){
        try{
            mutex.acquire();
            this.alarmHrs = hrs;
            this.alarmMin = min;
            this.alarmSec = sec;
            mutex.release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isAlarmEnabled(){
        try{
            mutex.acquire();
            return alarmEnabled;

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            mutex.release();
        }
        return alarmEnabled;
    }
    public void toggleAlarm(){
        try{
            mutex.acquire();
            alarmEnabled = !alarmEnabled; //Toggle mellan ON/OFF.
            mutex.release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkAlarm(){
        try{
            mutex.acquire();
            if(alarmHrs == hrs && alarmMin == min && alarmSec == sec){ //Stämmer tiden överens
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally { //Alltid släppa semaforen
          mutex.release();
        }
        return false;
    }
}
