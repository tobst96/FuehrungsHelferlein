import Moduls.DiveraStatus;
import Moduls.Lagemeldung;
import Moduls.WindowsNotification;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.util.logging.*;

public class Main {
    private static final long FILE_SIZE = 1024;
    public static Logger log = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws AWTException, ParseException, IOException {
        try {
            FileHandler handler = new FileHandler("current.log", 1024, 5, true);
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(Level.ALL);
            log.addHandler(handler);
            log.setUseParentHandlers(false);
            ConsoleHandler hnd = new ConsoleHandler();
            hnd.setLevel(Level.ALL);
            log.addHandler(hnd);
        } catch (IOException var17) {
            log.warning("Failed to initialize logger handler.");
        }
        WindowsNotification.displayInfo("Start", "v0.0.1");
        new Lagemeldung();

        log.info("Timestamp: " + Lagemeldung.getTimestamp());
        Lagemeldung.runInit();
        while(true) {
            Lagemeldung.run();
            new DiveraStatus();
        }
    }

}