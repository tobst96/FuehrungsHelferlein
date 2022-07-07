import Moduls.DiveraStatus;
import Moduls.Lagemeldung;
import Moduls.WindowsNotification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.*;


public class Main {
    private static final long FILE_SIZE = 1024;
    public static Logger log = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws AWTException, ParseException, IOException {
        try {
            FileHandler handler = new FileHandler("current.log", 1024, 5, true);
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(Level.FINEST);
            log.setUseParentHandlers(false);
            log.addHandler(handler);

            ConsoleHandler hnd = new ConsoleHandler();
            hnd.setLevel(Level.FINEST);
            log.addHandler(hnd);

        } catch (IOException var17) {
            log.warning("Failed to initialize logger handler.");
        }
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