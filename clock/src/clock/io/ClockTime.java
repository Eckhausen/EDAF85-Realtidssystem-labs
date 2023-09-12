package clock.io;

public class ClockTime extends Thread {
    private long t0, t;
    private ClockOutput out;
    private Monitor monitor;
    private int alarmCounter = 0;
    private boolean alarmSounding = false;


    public ClockTime(ClockOutput out, Monitor monitor){
        this.out = out;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        t0 = System.currentTimeMillis();
        while(true){
            t = System.currentTimeMillis();
            if(t-t0 >= 1000){
                monitor.tickTime();
                int[] currentTime = monitor.getTime();
                out.displayTime(currentTime[0], currentTime[1], currentTime[2]);

                if (monitor.checkAlarm() || (alarmSounding && alarmCounter < 20)){
                    //Kollar om larmet är triggat, sen fortsätter den tills den räknat till 20.
                    alarmSounding = true;
                    out.alarm();
                    alarmCounter++;
                } else {
                    //efter 20 signaler så resetar vi allt.
                    alarmSounding = false;
                    alarmCounter = 0;
                }
                t0 = t;
                }

            }
        }
    }
