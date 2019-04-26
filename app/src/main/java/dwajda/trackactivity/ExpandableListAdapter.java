package dwajda.trackactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private JSONObject jsonObject;

    public ExpandableListAdapter(Context _context, JSONObject _jsonObject) {
        context = _context;
        jsonObject = _jsonObject;
    }

    @Override
    public int getGroupCount() {
        int size = 0;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("exList");
            size = jsonArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return size;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        try {
            JSONArray exNameArray = jsonObject.getJSONArray("exList");
            String exName= exNameArray.getString(groupPosition);
            JSONObject numbAndWeig = jsonObject.getJSONObject(exName);
            JSONArray weight = numbAndWeig.getJSONArray("weight");
            size = weight.length();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        Object name = null;

        try {
            JSONArray exNameArray = jsonObject.getJSONArray("exList");
            name = exNameArray.get(groupPosition);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return name;
    }

    @Override
    public ArrayList<String> getChild(int groupPosition, int childPosition) {
        ArrayList <String> oneRepWeight = new ArrayList<>();
        try {
            JSONArray exNameArray = jsonObject.getJSONArray("exList");
            String exName= exNameArray.getString(groupPosition);

            JSONObject numbAndWeig = jsonObject.getJSONObject(exName);
            JSONArray numRepeat = numbAndWeig.getJSONArray("repeats");
            JSONArray weight = numbAndWeig.getJSONArray("weight");

            oneRepWeight.add(numRepeat.getString(childPosition));
            oneRepWeight.add(weight.getString(childPosition));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return oneRepWeight;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.group_list, null);

        TextView tvGroupItem = convertView.findViewById(R.id.tvGroupItem);

        tvGroupItem.setText(headerTitle);
        tvGroupItem.setTypeface(null, Typeface.BOLD);

        tvGroupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandableListView expandableListView = (ExpandableListView) parent;
                if(!isExpanded){
                    expandableListView.expandGroup(groupPosition);
                } else{
                    expandableListView.collapseGroup(groupPosition);
                }

            }
        });

        tvGroupItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete " + headerTitle +" item?");
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




                return false;
            }
        });





        ImageButton ibAddWeight = convertView.findViewById(R.id.ibAddWeight);
        ibAddWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cos dodam" + groupPosition, Toast.LENGTH_SHORT).show();

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
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        ArrayList<String> repWeight = getChild(groupPosition, childPosition);
        final String headerTitle = (String) getGroup(groupPosition);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_list, null);

        final TextView tvWeight = convertView.findViewById(R.id.tvWeight);
        tvWeight.setText(repWeight.get(1));

        final TextView tvRepeats = convertView.findViewById(R.id.tvNumRepeat);
        tvRepeats.setText(repWeight.get(0));

        TextView tvNumOfExercise = convertView.findViewById(R.id.tvNumOfExercise);
        tvNumOfExercise.setText(String.valueOf(childPosition + 1));

        tvWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final InputMethodManager imm = (InputMethodManager) finalConvertView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etWeight, InputMethodManager.SHOW_IMPLICIT);


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




            }
        });

        tvRepeats.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                int idItem = childPosition + 1;
                builder.setMessage("Delete " + idItem +" item?");
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


                return false;
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
