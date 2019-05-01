package dwajda.trackactivity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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
                String nameOfEx = null;

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseListActivity.this);
                    View addExerciseView = View.inflate(ExerciseListActivity.this, R.layout.add_exercise_alert, null);

                    final EditText etExerciseName = addExerciseView.findViewById(R.id.etExerciseName);

                    builder.setView(addExerciseView);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            nameOfEx = etExerciseName.getText().toString();

                            try {
                                nameOfEx = nameOfEx.trim();
                                JSONArray exList = jsonObject.getJSONArray("exList");

                                boolean canAdd = true;

                                for (int i = 0; i < exList.length(); i++) {
                                    if (exList.getString(i).equals(nameOfEx) || nameOfEx.equals("")) {
                                        canAdd = false;
                                        break;
                                    }
                                }

                                if (!canAdd) {
                                    Toast.makeText(ExerciseListActivity.this, "This name already exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    exList.put(nameOfEx);

                                    JSONObject exercise = new JSONObject();
                                    exercise.put("repeats", new JSONArray());
                                    exercise.put("weight", new JSONArray());
                                    jsonObject.put(nameOfEx, exercise);

                                    GetDataExpandableList.SaveOneObjToFile(jsonObject);

                                    ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(ExerciseListActivity.this, jsonObject);
                                    expandableListView.setAdapter(expandableListAdapter);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("xxx", "onClick: NIE DODAJE");
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    Objects.requireNonNull(alert.getWindow()).setBackgroundDrawableResource(R.color.colorBackground);

                    etExerciseName.requestFocus();
                    if (etExerciseName.requestFocus()) {
                        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
