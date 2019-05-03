package dwajda.trackactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Alerts {

    static void createExercise(final Context context, View addExerciseView,
                               final ExpandableListView expandableListView, final JSONObject jsonObject) {

        final AutoCompleteTextView actvExerciseName = addExerciseView.findViewById(R.id.actvExerciseName);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(addExerciseView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nameOfEx = actvExerciseName.getText().toString();

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
                        Toast.makeText(context, "This name already exist", Toast.LENGTH_SHORT).show();
                    } else {
                        exList.put(nameOfEx);

                        JSONObject exercise = new JSONObject();
                        exercise.put("repeats", new JSONArray());
                        exercise.put("weight", new JSONArray());
                        jsonObject.put(nameOfEx, exercise);

                        GetDataExpandableList.SaveOneObjToFile(jsonObject);

                        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(context, jsonObject);
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

        actvExerciseName.requestFocus();
        if (actvExerciseName.requestFocus()) {
            alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void RemoveExpandableGroupItem(final Context context, final ViewGroup parent, final int groupPosition, final String headerTitle, final JSONObject jsonObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Delete " + headerTitle + " item?");
        builder.setTitle("Delete");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {

                    jsonObject.remove(headerTitle);
                    JSONArray exList = jsonObject.getJSONArray("exList");
                    exList.remove(groupPosition);

                    GetDataExpandableList.SaveOneObjToFile(jsonObject);

                    ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(context, jsonObject);
                    ExpandableListView expandableListView = (ExpandableListView) parent;
                    expandableListView.setAdapter(expandableListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("xxx", "onClick: NIE USUWAM");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Objects.requireNonNull(alert.getWindow()).setBackgroundDrawableResource(R.color.colorPrimaryDark);
    }

    public static void addSeries(final Context context, final ViewGroup parent, final int groupPosition, final String headerTitle, final JSONObject jsonObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View addRepWeightView = View.inflate(context, R.layout.add_weight_repeat_alert, null);
        builder.setView(addRepWeightView);

        final EditText etRepeats = addRepWeightView.findViewById(R.id.etRepeats);
        final EditText etWeight = addRepWeightView.findViewById(R.id.etWeight);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String sRepeats = etRepeats.getText().toString();
                String sWeight = etWeight.getText().toString();

                try {
                    sRepeats = sRepeats.trim();
                    sWeight = sWeight.trim();

                    JSONObject exName = jsonObject.getJSONObject(headerTitle);
                    JSONArray repeats = exName.getJSONArray("repeats");
                    JSONArray weight = exName.getJSONArray("weight");

                    repeats.put(sRepeats);
                    weight.put(sWeight);

                    GetDataExpandableList.SaveOneObjToFile(jsonObject);

                    ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(context, jsonObject);
                    ExpandableListView expandableListView = (ExpandableListView) parent;
                    expandableListView.setAdapter(expandableListAdapter);
                    expandableListView.expandGroup(groupPosition);


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

        etWeight.requestFocus();
        if (etWeight.requestFocus()) {
            alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public static void editSeries(final Context context, final int childPosition, final ViewGroup parent, final int groupPosition,
                                  final String headerTitle, final JSONObject jsonObject, TextView tvRepeats, TextView tvWeight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View addRepWeightView = View.inflate(context, R.layout.add_weight_repeat_alert, null);
        builder.setView(addRepWeightView);

        final EditText etRepeats = addRepWeightView.findViewById(R.id.etRepeats);
        final EditText etWeight = addRepWeightView.findViewById(R.id.etWeight);
        etRepeats.setText(tvRepeats.getText().toString());
        etWeight.setText(tvWeight.getText().toString());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String sRepeats = etRepeats.getText().toString();
                String sWeight = etWeight.getText().toString();

                try {
                    sRepeats = sRepeats.trim();
                    sWeight = sWeight.trim();

                    JSONObject exName = jsonObject.getJSONObject(headerTitle);
                    JSONArray repeats = exName.getJSONArray("repeats");
                    JSONArray weight = exName.getJSONArray("weight");

                    repeats.put(childPosition, sRepeats);
                    weight.put(childPosition, sWeight);

                    GetDataExpandableList.SaveOneObjToFile(jsonObject);

                    ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(context, jsonObject);
                    ExpandableListView expandableListView = (ExpandableListView) parent;
                    expandableListView.setAdapter(expandableListAdapter);
                    expandableListView.expandGroup(groupPosition);


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

        etWeight.requestFocus();
        if (etWeight.requestFocus()) {
            alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public static void removeSeries(final Context context, final int childPosition, final ViewGroup parent,
                                    final int groupPosition, final String headerTitle, final JSONObject jsonObject) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        int idItem = childPosition + 1;
        builder.setMessage("Delete " + idItem + " item?");
        builder.setTitle("Delete");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {

                    JSONObject exName = jsonObject.getJSONObject(headerTitle);
                    JSONArray repeats = exName.getJSONArray("repeats");
                    JSONArray weight = exName.getJSONArray("weight");

                    repeats.remove(childPosition);
                    weight.remove(childPosition);

                    GetDataExpandableList.SaveOneObjToFile(jsonObject);

                    ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(context, jsonObject);
                    ExpandableListView expandableListView = (ExpandableListView) parent;
                    expandableListView.setAdapter(expandableListAdapter);
                    expandableListView.expandGroup(groupPosition);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("xxx", "onClick: NIE USUWAM");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Objects.requireNonNull(alert.getWindow()).setBackgroundDrawableResource(R.color.colorPrimaryDark);

    }
}
