package dwajda.trackactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private JSONObject jsonObject;

    ExpandableListAdapter(Context _context, JSONObject _jsonObject) {
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
            String exName = exNameArray.getString(groupPosition);
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
        ArrayList<String> oneRepWeight = new ArrayList<>();
        try {
            JSONArray exNameArray = jsonObject.getJSONArray("exList");
            String exName = exNameArray.getString(groupPosition);

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
                if (!isExpanded) {
                    expandableListView.expandGroup(groupPosition);
                } else {
                    expandableListView.collapseGroup(groupPosition);
                }

            }
        });

        tvGroupItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Remove exercise element
                Alerts.RemoveExpandableGroupItem(context, parent, groupPosition, headerTitle, jsonObject);

                return false;
            }
        });


        ImageButton ibAddWeight = convertView.findViewById(R.id.ibAddWeight);
        ibAddWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create Alert with adding Series
                Alerts.addSeries(context, parent, groupPosition, headerTitle, jsonObject);

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

                // Create edit Series alert
                Alerts.editSeries(context, childPosition, parent, groupPosition, headerTitle, jsonObject, tvRepeats, tvWeight);


            }
        });

        tvRepeats.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Remove one sub element from parent
                Alerts.removeSeries(context, childPosition, parent, groupPosition, headerTitle, jsonObject);

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
