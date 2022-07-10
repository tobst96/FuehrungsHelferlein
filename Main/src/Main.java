import Moduls.DiveraStatus;
import Moduls.Lagemeldung;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.*;


public class Main {
    private static final long FILE_SIZE = 1024;
    public static Logger log = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws AWTException, ParseException, IOException {
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("current.log");
            log.addHandler(fileHandler);
            log.setLevel(Level.FINER);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            log.info("Log to test");

        } catch (SecurityException e) {
            log.info("Exception:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.info("IO Exception:" + e.getMessage());
            e.printStackTrace();
        }
        log.info("Hi In the main class");
        ConsoleHandler hnd = new ConsoleHandler();
        hnd.setLevel(Level.FINEST);
        log.addHandler(hnd);




        //WindowsNotification.displayInfo("Start", "v0.0.1");
        new Lagemeldung();

        log.info("Timestamp: " + Lagemeldung.getTimestamp());
        Lagemeldung.runInit();
        while(true) {
            try {
                Object ob = new JSONParser().parse(new FileReader("config.json"));
                JSONObject js = (JSONObject) ob;
                JSONObject LageConfig = (JSONObject) js.get("Lagemeldung");
                JSONObject DiveraConfig = (JSONObject) js.get("Divera");
                //Programm run Zyklisch
                if(Objects.equals((String) LageConfig.get("Aktivieren"), "True")){
                    Lagemeldung.run();
                }
                if(((String) DiveraConfig.get("token") != "")) {
                    new DiveraStatus();
                }


            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}