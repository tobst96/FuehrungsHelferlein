import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.*;

public class Main extends Thread {
    private static final int FILE_SIZE = 1024;
    private static Logger log = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) throws IOException, ParseException {
        try {
            // Creating an instance of FileHandler with 5 logging files
            // sequences.
            FileHandler handler = new FileHandler("current.log", FILE_SIZE, 5, true);
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(Level.ALL);
            log.addHandler(handler);
            log.setUseParentHandlers(false);
            ConsoleHandler hnd = new ConsoleHandler();
            hnd.setLevel(Level.ALL);
            log.addHandler(hnd);

        } catch (IOException e) {
            log.warning("Failed to initialize logger handler.");
        }
        log.info("Starte Syncronisation");
        while(true) {

            Object ob = new JSONParser().parse(new FileReader("config.json"));
            JSONObject js = (JSONObject) ob;
            JSONObject DiveraConfig = (JSONObject) js.get("Divera");
            String responetextEinh1 = RequetsGet.requetsData("https://app.divera247.com/api/v2/pull/vehicle-status?accesskey=" + ((String) DiveraConfig.get("token")));
            String responetextEinh2 = RequetsGet.requetsData("https://app.divera247.com/api/v2/pull/vehicle-status?accesskey=" + ((String) DiveraConfig.get("tokensync")));
            //System.out.println(responetextEinh1);
            //System.out.println(responetextEinh2);
            Files.writeString(Path.of("einheit2.json"), responetextEinh1, StandardCharsets.UTF_8);
            Iterator listEinh2 = null;
            try {
                listEinh2 = diveraDataList(responetextEinh2);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Iterator listEinh1 = null;
            try {
                listEinh1 = diveraDataList(responetextEinh1);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                checkEinh2(listEinh2, listEinh1);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void checkEinh2(Iterator Einh2Input, Iterator Einh1Input) throws ParseException, IOException {
        String issiEinh2;
        String nameEinh2;
        Long statusEinh2;
        Long tsEinh2;

        while (Einh2Input.hasNext()) {
            JSONObject slide = (JSONObject) Einh2Input.next();
            issiEinh2 = (String) slide.get("issi");
            nameEinh2 = (String) slide.get("name");
            statusEinh2 = (Long) slide.get("fmsstatus");
            tsEinh2 = (Long) slide.get("fmsstatus_ts");
            log.finest("Einheit 1: " + issiEinh2 + " | " + nameEinh2 + " | " + statusEinh2 + " | " + tsEinh2);
            checkDifferncesDivera.Einh1(Einh1Input, issiEinh2, nameEinh2, statusEinh2, tsEinh2);
        }
    }

    public static Iterator diveraDataList(String responeText) throws ParseException {
        log.finest("Platter Json: " + responeText);
        JSONParser parser = new JSONParser();
        Object ob = (Object) parser.parse(responeText);

        // typecasting ob to JSONObject
        JSONObject js = (JSONObject) ob;
        log.finest("JSON Objekt: " + js);
        JSONArray val = (JSONArray) js.get("data");
        Iterator output = val.iterator();
        return output;
    }
}

