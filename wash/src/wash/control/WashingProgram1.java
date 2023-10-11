package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

import static wash.control.WashingMessage.Order.*;

public class WashingProgram1 extends ActorThread<WashingMessage> {
//TODO Hela klassen
    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;

    public WashingProgram1(WashingIO io,
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
        Program 1 (color wash): Lock the hatch, let water into the machine, heat to 40◦C, keep the temperature for
        30 minutes, drain, rinse 5 times 2 minutes in cold water, centrifuge for 5 minutes and unlock the hatch.
        While washing and rinsing the barrel should spin slowly, switching between left and right direction
        every minute. While centrifuging, the drain pump should run to evacuate excess water.
         */
        try {
            System.out.println("washing program 1 started");
            // Lock the hatch
            io.lock(true);
            water.send(new WashingMessage(this, WATER_FILL));
            System.out.println("Filling up water...");
            WashingMessage ackFill = receive();
            System.out.println("washing program 1 got " + ackFill);


            temp.send(new WashingMessage(this, TEMP_SET_40));
            WashingMessage ackHeat = receive();
            System.out.println("washing program 1 got " + ackHeat);


            spin.send(new WashingMessage(this, SPIN_SLOW));
            System.out.println("setting SPIN_SLOW...");
            WashingMessage ackSlowSpin = receive();
            System.out.println("washing program 1 got " + ackSlowSpin);

            // Spin for five simulated minutes (one minute == 60000 milliseconds)
            Thread.sleep(5 * 60000 / Settings.SPEEDUP);

            // Instruct SpinController to stop spin barrel spin.
            // Expect an acknowledgment in response.
            System.out.println("setting SPIN_OFF...");
            spin.send(new WashingMessage(this, SPIN_OFF));
            WashingMessage ackSpinOff = receive();
            System.out.println("washing program 1 got " + ackSpinOff);

            // Now that the barrel has stopped, it is safe to open the hatch.
            io.lock(false);

            System.out.println("washing program 1 finished");
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
