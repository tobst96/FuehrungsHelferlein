import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        configCheck();
    }
    
    
    public static void configCheck() throws IOException, ParseException {
        boolean endeavor = configIsEndeavor();

        if(endeavor == false){
            configEndeavorFalse();
        } else {
            JSONObject js = configRead();

            Object ob = null;
            ob = new JSONObject();
            try {
                JSONObject LageConfig = (JSONObject) js.get("Lagemeldung");
            } catch (Exception e) {
                ob = configLagemeldung((JSONObject) ob);
            }


            configWrite(ob);
        }
    }

    private static JSONObject configRead() throws IOException, ParseException {
        Object ob = new JSONParser().parse(new FileReader("config.json"));
        JSONObject js = (JSONObject) ob;
        return js;
    }

    private static void configEndeavorFalse() {
        Object ob = new JSONObject();

        //Metalanguage
        ob = configLagemeldung((JSONObject) ob);

        //Divera
        ob = configDivera((JSONObject) ob);

        configWrite(ob);
    }

    private static void configWrite(Object ob) {
        //Schreibe Datei
        try (PrintWriter out = new PrintWriter(new FileWriter("config.json"))) {
            out.write(ob.toString());
        } catch (Exception f) {
            f.printStackTrace();
        }
    }

    private static boolean configIsEndeavor() {
        File file = new File("config.json");
        boolean endeavor;
        if (file.exists()) {
            System.out.println("config.json: Vorhanden");
            endeavor = true;
        } else {
            System.out.println("config.json: Anlegen");
            endeavor = false;
        }
        return endeavor;
    }

    private static JSONObject configDivera(JSONObject ob) {
        JSONObject divera = ob;
        divera.put("status6", "True");
        divera.put("status3", "True");
        divera.put("token", "");
        divera.put("Divera", divera);
        return ob;
    }

    private static JSONObject configLagemeldung(JSONObject ob) {
        JSONObject lagemeldung = ob;
        lagemeldung.put("Aktivieren", "True");
        lagemeldung.put("Maximal", "30");
        lagemeldung.put("Erhöhen", "2");
        lagemeldung.put("Intervall", "5");
        lagemeldung.put("Abfragen", "30");
        lagemeldung.put("Lagemeldung", lagemeldung);
        return ob;
    }
}