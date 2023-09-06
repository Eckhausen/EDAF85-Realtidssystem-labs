import clock.AlarmClockEmulator;
import clock.io.Choice;
import clock.io.ClockInput;
import clock.io.ClockInput.UserInput;
import clock.io.ClockOutput;
import clock.io.ClockTime;

public class ClockMain {
    public static void main(String[] args) throws InterruptedException {
        int hrs = 23;
        int min = 59;
        int sec = 30;
        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput  in  = emulator.getInput();
        ClockOutput out = emulator.getOutput();

        ClockTime klockan = new ClockTime(out,hrs,min,sec);
        klockan.start();

        //out.displayTime(15, 2, 37);   // arbitrary time: just an example

        while (true) {
            in.getSemaphore().acquire();
            UserInput userInput = in.getUserInput();
            Choice c = userInput.choice();
            int h = userInput.hours();
            int m = userInput.minutes();
            int s = userInput.seconds();
            //in.getSemaphore().release();

            System.out.println("choice=" + c + " h=" + h + " m=" + m + " s=" + s);
        }
    }
}
