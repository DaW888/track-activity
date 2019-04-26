package dwajda.trackactivity;

import android.content.Context;
import android.graphics.Typeface;
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
    public Object getChild(int groupPosition, int childPosition) {
        Object name = null;
        try {
            JSONArray exNameArray = jsonObject.getJSONArray("exList");
            String exName= exNameArray.getString(groupPosition);

            JSONObject numbAndWeig = jsonObject.getJSONObject(exName);
            JSONArray numRepeat = numbAndWeig.getJSONArray("weight");

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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

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

        ImageButton ibAddWeight = convertView.findViewById(R.id.ibAddWeight);
        ibAddWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cos dodam" + groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = getChild(groupPosition, childPosition).toString();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_list, null);

        final TextView tvWeight = convertView.findViewById(R.id.tvWeight);
        tvWeight.setText(childText);

        TextView tvNumOfExercise = convertView.findViewById(R.id.tvNumOfExercise);
        tvNumOfExercise.setText(String.valueOf(childPosition + 1));

        final EditText etWeight = convertView.findViewById(R.id.etWeight);
        final ViewSwitcher switcher = convertView.findViewById(R.id.switcherWeight);
        etWeight.setImeActionLabel("SET", KeyEvent.KEYCODE_ENTER);

        final View finalConvertView = convertView;
        tvWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
                etWeight.setText(tvWeight.getText());


                final InputMethodManager imm = (InputMethodManager) finalConvertView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etWeight, InputMethodManager.SHOW_IMPLICIT);

                etWeight.requestFocus();

                etWeight.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        Log.d("xxx", "onKey: "+ keyCode);
                        if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                            Log.d("xxx", "onKey:etWeight " + etWeight.getText());
                            tvWeight.setText(etWeight.getText());


                            try {
                                JSONArray exNameArray = jsonObject.getJSONArray("exList");
                                String exName= exNameArray.getString(groupPosition);

                                JSONObject numbAndWeig = jsonObject.getJSONObject(exName);
                                JSONArray numWeight = numbAndWeig.getJSONArray("weight");
                                numWeight.put(childPosition, etWeight.getText());
                                Log.d("xxx", String.valueOf(jsonObject));
                                GetDataExpandableList.SaveOneObjToFile(jsonObject);

                                imm.hideSoftInputFromWindow(etWeight.getWindowToken(), 0);
                                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();

                                switcher.showPrevious();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "something went WRONG", Toast.LENGTH_SHORT).show();
                            }


                            return true;
                        }

                        return false;
                    }
                });


            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
