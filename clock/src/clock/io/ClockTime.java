package clock.io;

public class ClockTime extends Thread {
    private int hrs = 0;
    private int min = 0;
    private int sec = 0;
    private long t0, t;
    private ClockOutput out;


    public ClockTime(ClockOutput out, int hrs, int min, int sec){
        this.out = out;
        this.hrs = hrs;
        this.min = min;
        this.sec = sec;
    }

    @Override
    public void run() {
        t0 = System.currentTimeMillis();

        while(true){
            t = System.currentTimeMillis();
            if(t-t0 >= 1000){
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
                t0 = t;
                out.displayTime(hrs, min, sec);
                //t0 = t;
            }
        }

    }
}
