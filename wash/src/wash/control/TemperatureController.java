package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class TemperatureController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    private double selectedTemp, currentTemp;
    private enum Setting {TEMP_SET_40, TEMP_SET_60, OFF}
    private Setting setting;
    private WashingIO io;
    private boolean heaterOn = false;

    public TemperatureController(WashingIO io) {
        // TODO
        this.io = io;
    }

    //I WashingIO:
    //getTemperature()
    //heat()
    //
    //I WashingMessage:
    //TEMP_IDLE
    //TEMP_SET_40
    //TEMP_SET_60
    //ACKNOWLEDGEMENT
    @Override
    public void run() {
        // TODO
        try {
            while(true){
                WashingMessage m = receiveWithTimeout(10000 / Settings.SPEEDUP);
                if (m != null) {
                    System.out.println("got " + m);

                    switch (m.order()) {
                        case TEMP_SET_40:
                            selectedTemp = 40;
                            break;
                        case TEMP_SET_60:
                            selectedTemp = 60;
                            break;
                        case TEMP_IDLE:
                            selectedTemp = 0;
                            break;
                    }
                }

                currentTemp = io.getTemperature();

                if (selectedTemp == 40) {
                    if (currentTemp < 38.2 && !heaterOn) {
                        io.heat(true);
                        heaterOn = true;
                    } else if (currentTemp >= 39.8 && heaterOn) {
                        io.heat(false);
                        heaterOn = false;
                    }
                } else if (selectedTemp == 60) {
                    if (currentTemp < 58.2 && !heaterOn) {
                        io.heat(true);
                        heaterOn = true;
                    } else if (currentTemp >= 59.8 && heaterOn) {
                        io.heat(false);
                        heaterOn = false;
                    }
                } else if (selectedTemp == 0){
                    io.heat(false);
                    heaterOn = false;
                    m.sender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                }
            }

        } catch (InterruptedException unexpected) {
            throw new RuntimeException(unexpected);
        }
    }
}
