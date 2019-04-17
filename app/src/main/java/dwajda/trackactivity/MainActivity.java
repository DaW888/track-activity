package dwajda.trackactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            JSONArray ja = GetDataExpandableList.getAllData();
            Log.d("xxx", String.valueOf(ja));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("xxx", "nie da sie zapisac");
        }
    }
}
