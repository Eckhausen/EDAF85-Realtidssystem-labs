import clock.AlarmClockEmulator;
import clock.io.*;
import clock.io.ClockInput.UserInput;

public class ClockMain {
    public static void main(String[] args) throws InterruptedException {
        /**
         // R1: Why is mutual exclusion needed in your program?
         It is needed so the threads doesnt overrides eachother, such as the main and clocktime
         communicates with the monitor at the same time
         // R2: How can you use a Semaphore (or Lock) for mutual exclusion?
         You use the acquire(); and release() methods to avoid ovverriding and get mutual exclusion
         // R3: How can you use a Semaphore for signaling between threads?
         Through letting a thread acquire the semaphore and the other relasing it,
         by this it can coordinate and synchronize the execution of threads
         // R4: How do you use the Monitor design pattern in your design?
         All the methods that will be run is in the Monitor, which is the buffer between
         clockTime and Main-method and encapsulate the shared data.
         // R5: What does it mean to say that a thread is blocked?
         The thread is currently running and no other thread can use it and is put on hold
         // R6: In your implementation work, tasks I5–I6, you encountered inconsistent
         // output: a clock time value that didn’t correspond to the time set.
         // How could this inconsistency arise? How can it be prevented?
         That can occur due to multiple threads access and shared data without proper
         synchronization -> semaphores and locks prevents this from happening
         // R7: The test in step I5 runs for 30 seconds. Why does it have to run for so long?
         // Can we guarantee that this time is sufficient to find the race conditions we are looking for?
         This allows good amount of time to find potential problems in the code related to
         multiple threads running concurrently. These problems are called "race conditions"
         The goal is to ensure that our program behaves correctly and reliably in a
         multi-threaded environment.
         */

        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput  in  = emulator.getInput();
        ClockOutput out = emulator.getOutput();
        Monitor monitor = new Monitor();
        ClockTime clock = new ClockTime(out, monitor);
        clock.start();

        //out.displayTime(15, 2, 37);   // arbitrary time: just an example

        while (true) {
            in.getSemaphore().acquire();
            UserInput userInput = in.getUserInput();
            Choice c = userInput.choice();
            int h = userInput.hours();
            int m = userInput.minutes();
            int s = userInput.seconds();
            if(c == Choice.SET_TIME){
                monitor.setTime(h,m,s);
            } else if (c == Choice.SET_ALARM) {
                monitor.setAlarmTime(h,m,s);
            } else if (c == Choice.TOGGLE_ALARM) {
                monitor.toggleAlarm();
                boolean isAlarmEnabled = monitor.isAlarmEnabled();
                out.setAlarmIndicator(isAlarmEnabled);
            }
            System.out.println("choice=" + c + " h=" + h + " m=" + m + " s=" + s);
        }
    }
}
