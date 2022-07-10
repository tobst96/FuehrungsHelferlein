package Moduls;

import com.sun.tools.javac.Main;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Lagemeldung {
    public static Logger log = Logger.getLogger(Main.class.getName());
    public static Long IntervallAbfrage = Long.valueOf(1);
    public static Long Erhöhen = Long.valueOf(1);
    public static Long Maximal = Long.valueOf(2);
    public static Long Abfragen = Long.valueOf(30);

    public static JSONObject obj = new JSONObject();

    public static void runInit() throws IOException {
        genJobLost();
    }

    public static void run() throws AWTException {
        long timenow = getTimestamp();
        JSONArray val = (JSONArray) obj.get("Lagemeldung");
        Iterator output = val.iterator();
        while(output.hasNext()) {
            SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
            String timeStamp = date.format(getTimestamp());
            //System.out.println("Current Time Stamp: "+date.format(output.next()));
            //System.out.println(date.format(getTimestamp()) + " - " + date.format(output.next()));
            if(date.format(output.next()).equals(date.format(getTimestamp())) ){
                log.info("Lagemeldung geben!");
                WindowsNotification.displayInfo("Lagemeldung", "Fordere eine Lagemeldung an!");
            }
        }
    }
    public static long getTimestamp(){
        Instant instant = Instant.now();
        return instant.toEpochMilli();
    }

    public static Long minToSec(Integer Minuten) {
        Long returnval = Long.valueOf(Minuten * 60000);
        return returnval;
    }

    public static void readConfig(){
        Properties prop = new Properties();
        String fileName = "config.ini";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (FileNotFoundException ex) {
            log.warning("FileNotFoundEception");
        } catch (IOException ex) {
            log.warning("IOException ex");
        }
        IntervallAbfrage = Long.valueOf(prop.getProperty("Intervall"));
        Erhöhen = Long.valueOf(prop.getProperty("Erhöhen"));
        Maximal = Long.valueOf(prop.getProperty("Maximal"));
        Abfragen = Long.valueOf(prop.getProperty("Abfragen"));
    }

    public static <retun> JSONObject genJobLost(){
        //JSON Objekte anlegen

        JSONArray list = new JSONArray();
        log.fine("Generie JSON JOB Liste");

        //Erster Timestamp ermitteln
        Long nextTS = getTimestamp() + minToSec(Math.toIntExact(IntervallAbfrage));
        list.add(nextTS);
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
        log.info("Erste Ausführung: " + date.format(nextTS));
        String ersteausfürhung = date.format(nextTS);

        //Die nächsten Timestamp berechnen
        Integer counter = 1;
        while(counter < Abfragen){
            counter = counter + 1;
            nextTS = nextTS + minToSec(Math.toIntExact(Erhöhen)) + minToSec(Math.toIntExact(IntervallAbfrage));
            list.add(nextTS);
        }

        //Data to JSON
        obj.put("Lagemeldung", list);
        log.info(obj.toString());
        return obj;
    }

}


