package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

import static wash.control.WashingMessage.Order.*;

public class WashingProgram2 extends ActorThread<WashingMessage> {
//TODO Hela klassen
    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;

    public WashingProgram2(WashingIO io,
                           ActorThread<WashingMessage> temp,
                           ActorThread<WashingMessage> water,
                           ActorThread<WashingMessage> spin)
    {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }

    @Override
    public void run() {
        /*
        Program 2 (white wash): Like program 1, but with a 20 minute prewash in 40 ◦C. The main wash (30 min
        utes) is to be performed in 60◦C. Between the prewash and the main wash, the water in the barrel is
        drained and replaced with new, clean water.
         */
        try {
            System.out.println("washing program 3 started");

            // Switch off heating
            temp.send(new WashingMessage(this, TEMP_IDLE));

            // Wait for temperature controller to acknowledge
            WashingMessage ack1 = receive();
            System.out.println("got " + ack1);

            // Drain barrel, which may take some time. To ensure the barrel
            // is drained before we continue, an acknowledgment is required.
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage ack2 = receive();  // wait for acknowledgment
            System.out.println("got " + ack2);

            // Now that the barrel is drained, we can turn off water regulation.
            // For the WATER_IDLE order, we assume the water level regulator to
            // NOT send any acknowledgment. (Note: in your solution, you
            // are free to introduce an acknowledgment here if you wish.)
            water.send(new WashingMessage(this, WATER_IDLE));

            // Switch off spin. We expect an acknowledgment, to ensure
            // the hatch isn't opened while the barrel is spinning.
            spin.send(new WashingMessage(this, SPIN_OFF));
            WashingMessage ack3 = receive();  // wait for acknowledgment
            System.out.println("got " + ack3);

            // Unlock hatch
            io.lock(false);

            System.out.println("washing program 3 finished");
        } catch (InterruptedException e) {

            // If we end up here, it means the program was interrupt()'ed:
            // set all controllers to idle

            temp.send(new WashingMessage(this, TEMP_IDLE));
            water.send(new WashingMessage(this, WATER_IDLE));
            spin.send(new WashingMessage(this, SPIN_OFF));
            System.out.println("washing program terminated");
        }
    }
}
