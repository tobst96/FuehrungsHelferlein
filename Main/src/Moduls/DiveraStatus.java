package Moduls;

import com.sun.tools.javac.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.logging.Logger;

public class DiveraStatus {
    public static Logger log = Logger.getLogger(Main.class.getName());


    public DiveraStatus() throws ParseException, IOException, AWTException {
        String responetext = RequetsGet.requetsData("https://app.divera247.com/api/v2/pull/vehicle-status?accesskey=" + "6v9joeAXCj5A8QlWVaGBxVhjUewuIQ8XYIcD4LCc2_cx2Dpco9pi35r8cw3we1ob");
        Iterator responetextlist = diveraDataList(responetext);

        //Prüfe auf gleichheit
        Path of = Path.of("lastStaus.json");
        String responetextALT = null;
        try {
            responetextALT = Files.readString(of, StandardCharsets.UTF_8);
            if (responetext.equals(responetextALT)) {
                log.finest("Status Gleich");
            } else {
                log.info("Neuer Status!");
                Iterator responetextlistAlt = diveraDataList(responetextALT);
                while (responetextlist.hasNext()) {
                    JSONObject slide2 = (JSONObject) responetextlist.next();
                    String issiEinh1 = (String) slide2.get("issi");
                    String nameEinh1 = (String) slide2.get("name");
                    Long statusEinh1 = (Long) slide2.get("fmsstatus");
                    Long tsEinh1 = (Long) slide2.get("fmsstatus_ts");
                    //System.out.println("Empfangen Daten : " + issiEinh1 + " | " + nameEinh1 + " | " + statusEinh1 + " | " + tsEinh1);
                    //chechDiff(responetextALT, issiEinh1, nameEinh1, statusEinh1, tsEinh1);
                    //System.out.println(Lagemeldung.getTimestamp());
                    //System.out.println(tsEinh1 * 1000);
                    if(Lagemeldung.getTimestamp() - 2000 <= tsEinh1 * 1000){
                        //System.out.println("Timestamp gleich");
                        System.out.println(statusEinh1 + " | " + nameEinh1 + " | " + issiEinh1);
                        //System.out.println(statusEinh1);
                        if(statusEinh1 == 3){
                            WindowsNotification.displayWarning("Stärke Abfrage", nameEinh1);
                        }
                        if(statusEinh1 == 6){
                            WindowsNotification.displayError("Nicht Einsatzbereit", nameEinh1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warning("lastStats.json nicht anngelegt. Wird nun Angelegt!");
            Files.writeString(of, (CharSequence) responetext, StandardCharsets.UTF_8);
        }

        Files.writeString(of, (CharSequence) responetext, StandardCharsets.UTF_8);
    }

    private void chechDiff(String responetextALT, String issiEinh1, String nameEinh1, Long statusEinh1, Long tsEinh1) throws ParseException, AWTException {
        Iterator responetextlistAlt = diveraDataList(responetextALT);
        while (responetextlistAlt.hasNext()) {
            JSONObject slide3 = (JSONObject) responetextlistAlt.next();
            String issiEinh2 = (String) slide3.get("issi");
            String nameEinh2 = (String) slide3.get("name");
            Long statusEinh2 = (Long) slide3.get("fmsstatus");
            Long tsEinh2 = (Long) slide3.get("fmsstatus_ts");
            //System.out.println("ISSI1: " + issiEinh1 + " ISSI2: " + issiEinh2 );
            if (issiEinh1.equals(issiEinh2)) {
                log.info("ISSI gleich: " + issiEinh1 + " - " + issiEinh2);
                if (tsEinh1 != tsEinh2) {
                    log.info("Timestamp ist unterschiedlich");
                    WindowsNotification.displayInfo(nameEinh1, "Status: " + statusEinh1);
                    //log.info("Einheit1: " + String.valueOf(statusEinh1) + "E inheit2: " + String.valueOf(statusEinh2) );
                    if (statusEinh1.equals("3")){
                        WindowsNotification.displayWarning("Stärke Abfragen", nameEinh1);
                        log.info(("Stärke Abfragen: " + nameEinh1));
                    }
                }
            }

        }
    }

    public static Iterator diveraDataList(String responeText) throws ParseException {
        log.finest("Platter Json: " + responeText);
        JSONParser parser = new JSONParser();
        Object ob = parser.parse(responeText);
        JSONObject js = (JSONObject)ob;
        log.finest("JSON Objekt: " + js);
        JSONArray val = (JSONArray)js.get("data");
        Iterator output = val.iterator();
        return output;
    }
}


