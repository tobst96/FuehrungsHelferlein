package Moduls;

import com.sun.tools.javac.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DiveraStatus {
    public static Logger log = Logger.getLogger(Main.class.getName());



    public DiveraStatus() throws ParseException, IOException, AWTException {
        Object ob = new JSONParser().parse(new FileReader("config.json"));
        JSONObject js = (JSONObject) ob;
        JSONObject DiveraConfig = (JSONObject) js.get("Divera");
        String url = "https://app.divera247.com/api/v2/pull/vehicle-status?accesskey=" + ((String) DiveraConfig.get("token"));
        String responetext = RequetsGet.requetsData(url);
        Iterator responetextlist = diveraDataList(responetext);

        //Prüfe auf gleichheit
        Path of = Path.of("lastStatus.json");
        String responetextALT = null;
        JSONArray slogarray = null;
        JSONObject slogjson = null;
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
                    if (Lagemeldung.getTimestamp() - 2000 <= tsEinh1 * 1000) {
                        //System.out.println("Timestamp gleich");
                        System.out.println(statusEinh1 + " | " + nameEinh1 + " | " + issiEinh1);
                        //System.out.println(statusEinh1);
                        Object slog = new JSONParser().parse(new FileReader("statuslog.json"));
                        slogjson = (JSONObject) slog;
                        slogarray = (JSONArray) ((JSONObject) slog).get("History");
                        JSONObject statusJSON = new JSONObject();
                        statusJSON.put("Name", nameEinh1);
                        statusJSON.put("Status", statusEinh1);
                        statusJSON.put("ISSI", issiEinh1);
                        statusJSON.put("TS", tsEinh1);

                        slogarray.add(statusJSON);
                        slogjson.put("History", slogarray);
                        try (PrintWriter outStatus = new PrintWriter(new FileWriter("statuslog.json"))) {
                            outStatus.write(slogjson.toString());
                        } catch (Exception f) {
                            f.printStackTrace();
                        }

                        if (statusEinh1 == 3) {
                            if (Objects.equals((String) DiveraConfig.get("status3"), "True")) {
                                WindowsNotification.displayWarning("Stärke Abfragen", nameEinh1);
                                log.info(("Stärke Abfragen: " + nameEinh1));
                            }
                        }
                        if (statusEinh1 == 6) {
                            if (Objects.equals((String) DiveraConfig.get("status6"), "True")) {
                                WindowsNotification.displayError("Nicht Einsatzbereit", nameEinh1);
                                ;
                                log.info(("Nicht Einsatzbereit: " + nameEinh1));
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warning("lastStatus.json nicht anngelegt. Wird nun Angelegt!");
            Files.writeString(of, (CharSequence) responetext, StandardCharsets.UTF_8);
            if (slogjson == null) {
                Files.writeString(Path.of("statuslog.json"), "", StandardCharsets.UTF_8);
            } else {
                Files.writeString(Path.of("statuslog.json"), slogarray.toString(), StandardCharsets.UTF_8);
            }
        }

        Files.writeString(of, (CharSequence) responetext, StandardCharsets.UTF_8);
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


