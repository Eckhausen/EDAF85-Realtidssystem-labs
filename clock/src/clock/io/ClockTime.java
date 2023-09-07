package clock.io;

public class ClockTime extends Thread {
    private long t0, t;
    private ClockOutput out;
    private Monitor monitor;


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
                t0 = t;
            }
        }

    }
}
