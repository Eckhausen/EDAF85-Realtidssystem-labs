package wash.control;

import static wash.control.WashingMessage.Order.*;

import actor.ActorThread;
import wash.io.WashingIO;

public class WaterController extends ActorThread<WashingMessage> {

    private enum waterState {IDLE, FILL, DRAIN}

    private WashingIO io;
    private waterState state;
    private ActorThread<WashingMessage> sender;
    private boolean centrifuge;

    public WaterController(WashingIO io) {
        this.io = io;
        state = waterState.IDLE;
        sender = null;
    }

    @Override
    public void run() {
        try {
            while (true) {
                WashingMessage m = receiveWithTimeout(5000 / Settings.SPEEDUP);

                if (m != null) {
                    switch (m.order()) {
                        case WATER_IDLE:
                            io.fill(false);
                            io.drain(false);
                            break;
                        case WATER_FILL:
                            state = waterState.FILL;
                            sender = m.sender();
                            io.fill(true);
                            break;
                        case WATER_DRAIN:
                            state = waterState.DRAIN;
                            sender = m.sender();
                            io.drain(true);
                            if (io.getWaterLevel() == 0) {
                                centrifuge = true;
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (io.getWaterLevel() >= 10 && state == waterState.FILL) {
                    state = waterState.IDLE;
                    io.fill(false);
                    sender.send(new WashingMessage(this, ACKNOWLEDGMENT));
                }
                if (io.getWaterLevel() == 0 && state == waterState.DRAIN && !centrifuge) {
                    state = waterState.IDLE;
                    io.drain(false);
                    sender.send(new WashingMessage(this, ACKNOWLEDGMENT));
                }
            }
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }
}
