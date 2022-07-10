package de.tobiobst.frameconfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigToJson {
    private JButton Save;
    private JPanel GibLagemeldungAus;
    public JCheckBox LageMeldungAktivieren;
    private JTextField Lagemeldung_Intervall;
    private JLabel Lagemeldung_erhoehen;
    private JTextField Lagemeldung_Erhoehen;
    private JTextField Lagemeldung_Maximal;
    private JTextField Lagemeldung_Abfragen;
    private JTextField Divera_Token_1;

    public ConfigToJson() throws IOException, ParseException {
        Object ob = new JSONParser().parse(new FileReader("config.json"));
        JSONObject js = (JSONObject) ob;
        JSONObject LageConfig = (JSONObject) js.get("Lagemeldung");
        JSONObject DiveraConfig = (JSONObject) js.get("Divera");
        if(Objects.equals((String) LageConfig.get("Aktivieren"), "True")) {
            LageMeldungAktivieren.setSelected(true);
        } else {
            LageMeldungAktivieren.setSelected(false);
        }
        Lagemeldung_Intervall.setText(String.valueOf((Long) LageConfig.get("Intervall")));
        Lagemeldung_Erhoehen.setText(String.valueOf((Long) LageConfig.get("Erhöhen")));
        Lagemeldung_Maximal.setText(String.valueOf((Long) LageConfig.get("Maximal")));
        Lagemeldung_Abfragen.setText(String.valueOf((Long) LageConfig.get("Abfragen")));
        Divera_Token_1.setText((String) DiveraConfig.get("token"));


        LageMeldungAktivieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              if(LageMeldungAktivieren.isSelected()){
                  System.out.println("True");
                  LageConfig.put("Aktivieren", "True");
              } else {
                  System.out.println("False");
                  LageConfig.put("Aktivieren", "False");
              }
            }
        });
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == Save) {
                    System.out.println("Übernehmen");
                    LageConfig.put("Intervall", Long.parseLong(Lagemeldung_Intervall.getText()));
                    LageConfig.put("Erhöhen", Long.parseLong(Lagemeldung_Erhoehen.getText()));
                    LageConfig.put("Maximal", Long.parseLong(Lagemeldung_Maximal.getText()));
                    LageConfig.put("Abfragen", Long.parseLong(Lagemeldung_Abfragen.getText()));

                    DiveraConfig.put("token", Divera_Token_1.getText());
                    js.put("Lagemeldung", LageConfig);
                    js.put("Divera", DiveraConfig);

                    try (PrintWriter out = new PrintWriter(new FileWriter("config.json"))) {
                        out.write(js.toString());
                    } catch (Exception f) {
                        f.printStackTrace();
                    }

                }
            }
        });
    }

    public static void main(String[] args) throws IOException, ParseException {
        JFrame frame = new JFrame ("Config");
        frame.setContentPane (new ConfigToJson().GibLagemeldungAus);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
