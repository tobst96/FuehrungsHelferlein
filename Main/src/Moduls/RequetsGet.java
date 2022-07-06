package Moduls;

import com.sun.tools.javac.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class RequetsGet {
    private static HttpURLConnection conn;
    static Logger log = Logger.getLogger(Main.class.getName());

    public RequetsGet() {
    }

    public static String requetsData(String urlLink) {
        StringBuilder responseContent = new StringBuilder();

        try {
            URL url = new URL(urlLink);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int status = conn.getResponseCode();
            BufferedReader reader;
            String line;
            if (status >= 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }

                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }

                reader.close();
            }

            log.finest("response code: " + status);
            log.finest(responseContent.toString());
        } catch (MalformedURLException var10) {
            var10.printStackTrace();
        } catch (IOException var11) {
            var11.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return responseContent.toString();
    }
}
