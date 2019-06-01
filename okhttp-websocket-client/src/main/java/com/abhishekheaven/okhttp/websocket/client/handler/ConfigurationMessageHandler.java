package com.abhishekheaven.okhttp.websocket.client.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class ConfigurationMessageHandler {

    public void handleInitialConfig(String str) {
        System.out.println("This is initial config : " + str);

        /*String string = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            try {
                System.out.println("please enter a string");
                string = br.readLine();
                System.out.println("entered character is : " + string);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!"quit".equalsIgnoreCase(string));*/

    }

    public void handleNotificationConfig(String str) {
        /*String rand = getSaltString();
        File file = new File("E:\\Abhishek\\github\\spring-websocket\\okhttp-websocket-client\\src\\main\\resources\\"+rand+".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        System.out.println("This is notification config : " + str);

//        String string = "";
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        do {
//            try {
//                System.out.println("please enter a string");
//                string = br.readLine();
//                System.out.println("entered character is : " + string);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } while (!"quit".equalsIgnoreCase(string));

    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
