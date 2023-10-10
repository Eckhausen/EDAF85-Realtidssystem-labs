package wash.control;

import wash.io.WashingIO;
import wash.simulation.WashingSimulator;

import static wash.control.WashingMessage.Order.*;

public class Wash {

    public static void main(String[] args) throws InterruptedException {
        WashingSimulator sim = new WashingSimulator(Settings.SPEEDUP);

        WashingIO io = sim.startSimulation();

        TemperatureController temp = new TemperatureController(io);
        WaterController water = new WaterController(io);
        SpinController spin = new SpinController(io);

        temp.start();
        water.start();
        spin.start();

        while (true) {
            int n = io.awaitButton();
            System.out.println("user selected program " + n);
            switch (n){
                case 0:
                    //TODO Något knas här, kopierat från prog 3 error handling
                    //Program.interrupt();

                    temp.interrupt();
                    water.interrupt();
                    spin.interrupt();

                    System.out.println("washing program terminated");
                    break;
                case 1:
                    //Prog 1
                    //Program = new WashingProgram1();
                    break;
                case 2:
                    //Prog 2
                    break;
                case 3:
                    WashingProgram3 washingProgram3 = new WashingProgram3(io, temp, water, spin);
                    washingProgram3.start();
                    break;
            }
            // TODO:
            // if the user presses buttons 1-3, start a washing program
            // if the user presses button 0, and a program has been started, stop it
        }
    }
};
