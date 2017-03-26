package com.shwingbah.sprinklerclient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class SprinklerServerManager {
    private static SprinklerServerManager sprinklerServerManager = null;

    public static SprinklerServerManager GetInstance() {
        if(sprinklerServerManager == null) {
            sprinklerServerManager = new SprinklerServerManager();
        }
        return sprinklerServerManager;
    }

    ///// Send Sprinkler Command /////
    public interface SendSprinklerCommandListener {
        void SendSprinklerCommandReceivedResponse(boolean succeeded);
    }
    public void SendSprinklerCommand(Context context, final String serverURL, final String command, final SendSprinklerCommandListener listener) {
        URL sendSprinklerCommandURL = null;
        try {
            sendSprinklerCommandURL = new URL("http://" + serverURL);
            Log.i("test", sendSprinklerCommandURL.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.SendSprinklerCommandReceivedResponse(false);
        }

        AsyncTask<URL, Double, Boolean> sendSprinklerCommandRequestTask = new AsyncTask<URL, Double, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... params) {
                try {
                    JSONObject serverCommandJsonObject = new JSONObject();
                    serverCommandJsonObject.put("command", command);
                    Log.i("test", "json - " + serverCommandJsonObject.toString());

                    String response = PostDataToURL(params[0], serverCommandJsonObject.toString());

                    Log.i("test", "finished POST - " + response);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    listener.SendSprinklerCommandReceivedResponse(false);
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean sendSprinklerCommandResponse) {
                super.onPostExecute(sendSprinklerCommandResponse);
                listener.SendSprinklerCommandReceivedResponse(true);
            }
        };

        sendSprinklerCommandRequestTask.execute(sendSprinklerCommandURL);
    }
    ///// Local Functions /////

    private String PostDataToURL(URL url, String jsonString) throws IOException
    {
        String returnDataString = null;
        HttpURLConnection connection = null;
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(jsonString);
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = connection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        returnDataString = stringBuilder.toString();
        inputStream.close();

        return returnDataString;
    }
}

