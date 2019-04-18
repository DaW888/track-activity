package dwajda.trackactivity;

import android.content.Context;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

public class GetDataExpandableList {
    public static JSONArray getAllData() throws JSONException {
        JSONArray ja = new JSONArray();

        JSONObject oneDate = new JSONObject();
        oneDate.put("date", "25-01-2000");

        JSONArray ex_list = new JSONArray();
        ex_list.put("sztanga");
        ex_list.put("hantle");

        oneDate.put("exList", ex_list);

        JSONObject sztanga = new JSONObject();
        JSONArray powt = new JSONArray();
        powt.put("10");
        powt.put("20");
        powt.put("30");
        sztanga.put("repeat", powt);

        JSONArray weight = new JSONArray();
        weight.put("10kg").put("20kg").put("30kg");

        sztanga.put("weight", weight);

        oneDate.put((String) ex_list.get(0), sztanga);


        ja.put(oneDate);
        oneDate.remove("date");
        oneDate.put("date", "26-01-2000");
        ja.put(oneDate);
        oneDate.remove("date");
        oneDate.put("date", "27-01-2000");
        ja.put(oneDate);
        oneDate.remove("date");
        oneDate.put("date", "28-01-2000");
        ja.put(oneDate);

        Log.d("xxx", String.valueOf(ja));


        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(dir, "workoutData.json");
        FileWriter writer;

        try {
            writer = new FileWriter(file);
            writer.write(ja.toString(4));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("xxx", "getAllData: NIE UDALO SIE DODAC");
        }


        String getFile = readFromFile();
        JSONArray jar = new JSONArray(getFile);
        Log.d("xxx", "Z PLIKU "+String.valueOf(jar));

        return ja;

    }

    public static String readFromFile() {

        String jsonStr = null;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "workoutData.json");

        try (FileInputStream stream = new FileInputStream(file)) {

            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            jsonStr = Charset.defaultCharset().decode(bb).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return jsonStr;
    }

    public static void SaveOneObjToFile(JSONObject jsonObject) {

        JSONArray jar;
        try {
            // READ FILE
            String jsonStrFromFile = readFromFile();
            jar = new JSONArray(jsonStrFromFile);
            Log.d("xxx", "Z PLIKU " + String.valueOf(jar));

            for(int i=0; i < jar.length(); i++){
                JSONObject oneOb = jar.getJSONObject(i);

                if(oneOb.getString("date").equals(jsonObject.getString("date"))){
                    jar.put(i, jsonObject);
                }
            }


            // SAVE FILE
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dir.getPath(), "workoutData.json");
            file.delete();
            FileWriter writer;

            try {
                writer = new FileWriter(file);
                writer.write(jar.toString(4));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("xxx", "getAllData: NIE UDALO SIE DODAC" + e);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}