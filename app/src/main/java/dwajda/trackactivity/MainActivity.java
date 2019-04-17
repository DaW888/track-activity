package dwajda.trackactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
//            JSONArray ja = GetDataExpandableList.getAllData();
//            Log.d("xxx", String.valueOf(ja));
            String getFile = GetDataExpandableList.readFromFile();
            JSONArray jar = new JSONArray(getFile);
            Log.d("xxx", "Z PLIKU "+String.valueOf(jar));

            ArrayList<String> arrayList = new ArrayList<>();
            for(int i = 0; i < jar.length(); i++){
                JSONObject ob = (JSONObject) jar.get(i);
                arrayList.add((String) ob.get("date"));
            }
            Log.d("xxx", "array lista:  "+ arrayList);

            ListView lvDateList = findViewById(R.id.lvDateList);

            dateArrayAdapter adapter = new dateArrayAdapter(
                    MainActivity.this,
                    R.layout.date_list_item,
                    arrayList
            );
            lvDateList.setAdapter(adapter);



        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("xxx", "Nie udalo sie odczytac");
        }
    }
}
