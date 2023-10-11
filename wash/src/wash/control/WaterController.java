package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;
import static wash.control.WashingMessage.Order.*;

public class WaterController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    //WATER_IDLE
    //WATER_FILl
    //WATER_DRAIN
    //ACKNOWLEDGEMENT
    //double getWaterLevel();
    //void fill(boolean on);
    //void drain(boolean on);
    private WashingIO io;
    private double currentWaterLevel = 0;
    private enum Mode{FILL, DRAIN, OFF};
    private Mode mode;
    private ActorThread<WashingMessage> sender;


    public WaterController(WashingIO io) {
        // TODO
        this.io = io;
        sender = null;
        mode = Mode.OFF;
    }

    @Override
    public void run() {
        // TODO
            try {
                while(true) {
                    WashingMessage m = receiveWithTimeout(5000 / Settings.SPEEDUP); //5 sekund
                    if (m != null) {
                        System.out.println("got " + m);
                        switch (m.order()) {
                            case WATER_FILL:
                                mode = Mode.FILL;
                                sender = m.sender();
                                break;
                            case WATER_DRAIN:
                                mode = Mode.DRAIN;
                                sender = m.sender();
                                break;
                            case WATER_IDLE:
                                mode = Mode.OFF;
                                break;
                        }
                    }
                    currentWaterLevel = io.getWaterLevel();

                    if(mode == Mode.FILL){
                        if(currentWaterLevel < 10.0){
                            io.fill(true);
                        } else if(currentWaterLevel >= 10.0){
                            io.fill(false);
                            sender.send(new WashingMessage(this,ACKNOWLEDGMENT));
                            mode = Mode.OFF;
                        }
                    } else if (mode == Mode.DRAIN){
                        if(currentWaterLevel > 0) io.drain(true);
                    } else if (mode == Mode.OFF){
                        io.fill(false);
                        io.drain(false);
                        if(m != null){
                            m.sender().send(new WashingMessage(this, ACKNOWLEDGMENT));
                        }
                    }
                }
            } catch (InterruptedException unexpected) {
                // we don't expect this thread to be interrupted,
                // so throw an error if it happens anyway
                throw new Error(unexpected);
            }
        }

}
