package dwajda.trackactivity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private FloatingActionButton fabAddExercise;
    private int numDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        Bundle bundle = getIntent().getExtras();
        numDate = bundle.getInt("idDate");


        String getFile = GetDataExpandableList.readFromFile();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(getFile);
            Log.d("xxx", "Z PLIKU " + jsonArray);
            final JSONObject jsonObject = jsonArray.getJSONObject(numDate);

            Log.d("xxx", "JEden json object" + jsonObject);
            setTitle(jsonObject.getString("date"));

            expandableListView = findViewById(R.id.elvExerciseList);

            ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, jsonObject);
            expandableListView.setAdapter(expandableListAdapter);

            fabAddExercise = findViewById(R.id.fabAddExercise);
            fabAddExercise.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    View addExerciseView = View.inflate(ExerciseListActivity.this, R.layout.add_exercise_alert, null);
                    final AutoCompleteTextView actvExerciseName = addExerciseView.findViewById(R.id.actvExerciseName);
                    ArrayList<String> listOfExercise = GetDataExpandableList.getAllExercisesList();

                    AddExerciseArrayAdapter addExerciseArrayAdapter = new AddExerciseArrayAdapter(ExerciseListActivity.this, R.layout.autocomplete_exercise_name_item, listOfExercise);
                    actvExerciseName.setAdapter(addExerciseArrayAdapter);

                    // Create Alert with Exercise Name
                    Alerts.createExercise(ExerciseListActivity.this, addExerciseView, expandableListView, jsonObject);

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
