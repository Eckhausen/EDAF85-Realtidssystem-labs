package wash.control;

import static wash.control.WashingMessage.Order.*;

import actor.ActorThread;
import wash.io.WashingIO;

public class WashingProgram2 extends ActorThread<WashingMessage> {

    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;

    public WashingProgram2(
            WashingIO io,
            ActorThread<WashingMessage> temp,
            ActorThread<WashingMessage> water,
            ActorThread<WashingMessage> spin
    ) {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }
    /*
    Program 2 (whitewash): Like program 1, but with a 20 minute prewash in 40 ◦C. The main wash (30 min
    utes) is to be performed in 60◦C. Between the prewash and the main wash, the water in the barrel is
    drained and replaced with new, clean water.
     */
    @Override
    public void run() {
        try {
            // Lock the hatch
            io.lock(true);

            // Fill with water
            water.send(new WashingMessage(this, WATER_FILL));
            WashingMessage waterFilled = receive();
            System.out.println("got " + waterFilled);

            // Heat to 40C and start spin
            temp.send(new WashingMessage(this, TEMP_SET_40));
            WashingMessage waitForTemp = receive();
            System.out.println("got " + waitForTemp);
            spin.send(new WashingMessage(this, SPIN_SLOW));
            WashingMessage spinningSlow = receive();
            System.out.println("got " + spinningSlow);
            //20 minutes
            Thread.sleep(20 * 60000 / Settings.SPEEDUP);
            temp.send(new WashingMessage(this, TEMP_IDLE));
            WashingMessage tempIdle = receive();
            System.out.println("got " + tempIdle);

            // Drain
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage draining = receive();
            System.out.println("got " + draining);

            // Fill with clean water
            water.send(new WashingMessage(this, WATER_FILL));
            WashingMessage waterFilled2 = receive();
            System.out.println("got " + waterFilled2);

            // Heat to 60C and start spin
            temp.send(new WashingMessage(this, TEMP_SET_60));
            WashingMessage waitForTemp2 = receive();
            System.out.println("got " + waitForTemp2);
            spin.send(new WashingMessage(this, SPIN_SLOW));
            WashingMessage spinningSlow2 = receive();
            System.out.println("got " + spinningSlow2);
            //30 minutes
            Thread.sleep(30 * 60000 / Settings.SPEEDUP);
            temp.send(new WashingMessage(this, TEMP_IDLE));
            WashingMessage tempIdle2 = receive();
            System.out.println("got " + tempIdle2);

            // Drain
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage draining2 = receive();
            System.out.println("got " + draining2);

            // Rinse 5 times
            for (int i = 0; i < 5; i++) {
                // fill with cold
                water.send(new WashingMessage(this, WATER_FILL));
                WashingMessage coldWater = receive();
                System.out.println("got " + coldWater);
                // wait 2 min
                Thread.sleep(2 * 60000 / Settings.SPEEDUP);

                // drain
                water.send(new WashingMessage(this, WATER_DRAIN));
                WashingMessage draining3 = receive();
                System.out.println("got " + draining3);
            }
            // Centrifuge 5 min
            spin.send(new WashingMessage(this, SPIN_FAST));
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage spinningFast = receive();
            System.out.println("got " + spinningFast);
            Thread.sleep(5 * 60000 / Settings.SPEEDUP);

            // Stop the program
            spin.send(new WashingMessage(this, SPIN_OFF));
            WashingMessage spinningOff = receive();
            System.out.println("got " + spinningOff);
            water.send(new WashingMessage(this, WATER_IDLE));

            // Unlock the hatch
            io.lock(false);
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
