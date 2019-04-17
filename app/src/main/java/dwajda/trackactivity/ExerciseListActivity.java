package dwajda.trackactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExerciseListActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        Bundle bundle = getIntent().getExtras();
        int numDate = bundle.getInt("idDate");


        String getFile = GetDataExpandableList.readFromFile();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(getFile);
            Log.d("xxx", "Z PLIKU "+String.valueOf(jsonArray));
            JSONObject jsonObject = jsonArray.getJSONObject(numDate);

            Log.d("xxx", "JEden json object"+ jsonObject);
            setTitle(jsonObject.getString("date"));

            expandableListView = findViewById(R.id.elvExerciseList);

            ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, jsonObject);
            expandableListView.setAdapter(expandableListAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
