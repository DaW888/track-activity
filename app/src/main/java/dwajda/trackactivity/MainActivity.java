package dwajda.trackactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            HashMap<String, List<String>> hashMap = GetDataExpandableList.getAllData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
