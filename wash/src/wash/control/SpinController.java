package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;
import wash.io.WashingIO.Spin;

public class SpinController extends ActorThread<WashingMessage> {
    private WashingIO io;
    private enum Mode{LEFT, RIGHT, FAST, OFF};
    private Mode mode;

    public SpinController(WashingIO io) {
        this.io = io;
    }

    @Override
    public void run() {

        // this is to demonstrate how to control the barrel spin:
        // io.setSpinMode(Spin.IDLE);

        try {
            Mode mode = Mode.OFF;
            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Settings.SPEEDUP);

                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                    System.out.println("got " + m);

                    switch (m.order()){
                        case SPIN_OFF:
                            mode = Mode.OFF;
                            break;
                        case SPIN_SLOW:
                            mode = Mode.LEFT;
                            break;
                        case SPIN_FAST:
                            mode = Mode.FAST;
                            break;

                    }
                }
                switch (mode){
                    case LEFT:
                        io.setSpinMode(Spin.LEFT);
                        mode = Mode.RIGHT;
                        break;
                    case RIGHT:
                        io.setSpinMode(Spin.RIGHT);
                        mode = Mode.LEFT;
                        break;
                    case FAST:
                        io.setSpinMode(Spin.FAST);
                        break;
                    case OFF:
                        io.setSpinMode(Spin.IDLE);
                        break;
                }

                if(m != null){
                    m.sender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                }
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
