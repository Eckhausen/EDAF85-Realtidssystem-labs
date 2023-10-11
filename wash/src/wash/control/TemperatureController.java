package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class TemperatureController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    private WashingIO io;

    public TemperatureController(WashingIO io) {
        // TODO
        this.io = io;
    }

    @Override
    public void run() {
        // TODO
    }
}
