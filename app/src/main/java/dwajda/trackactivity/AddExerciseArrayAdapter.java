package dwajda.trackactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddExerciseArrayAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<String> list;

    private ArrayList<String> listAll;
    private ListFilter filter = new ListFilter();

    public AddExerciseArrayAdapter(Context context, int resource, ArrayList<String> object) {
        super(context, resource, object);
        this.context = context;
        this.resource = resource;
        this.list = object;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, null);

        TextView tvAutoCompleteExercise = convertView.findViewById(R.id.tvAutoCompleteExercise);
        tvAutoCompleteExercise.setText(getItem(position));

//        final View finalConvertView = convertView;
//        convertView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    InputMethodManager imm = (InputMethodManager) finalConvertView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 1);
//                }
//
//                return false;
//            }
//        });


        return convertView;
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    public class ListFilter extends Filter {
        private final Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (listAll == null){
                synchronized (lock){
                    listAll = new ArrayList<>(list);
                }
            }

            if(constraint == null || constraint.length() == 0){
                synchronized (lock) {
                    results.values = listAll;
                    results.count = listAll.size();
                }
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                ArrayList<String> matchValues = new ArrayList<>();

                for(String item : listAll){
                    if(item.toLowerCase().trim().contains(pattern)){
                        matchValues.add(item);
                    }
                }
                results.values = matchValues;
                results.count = matchValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.values != null){
                list = (ArrayList<String>) results.values;
            } else {
                list = null;
            }

            if(results.count > 0){
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}

