package dwajda.trackactivity;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

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
            JSONArray numRepeat = numbAndWeig.getJSONArray("powt");
            size = numRepeat.length();

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
    public Object getChild(int groupPosition, int childPosition) {
        Object name = null;
        try {
            JSONArray exNameArray = jsonObject.getJSONArray("exList");
            String exName= exNameArray.getString(groupPosition);

            JSONObject numbAndWeig = jsonObject.getJSONObject(exName);
            JSONArray numRepeat = numbAndWeig.getJSONArray("powt");

            name = numRepeat.get(childPosition);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return name;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.group_list, null);

        TextView tvGroupItem = convertView.findViewById(R.id.tvGroupItem);

        tvGroupItem.setText(headerTitle);
        tvGroupItem.setTypeface(null, Typeface.BOLD);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_list, null);

        TextView tvItemList = convertView.findViewById(R.id.tvNumOfExercise);
        tvItemList.setText(childText);


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
