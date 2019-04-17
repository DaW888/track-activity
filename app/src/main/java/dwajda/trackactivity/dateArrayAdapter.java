package dwajda.trackactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class dateArrayAdapter extends ArrayAdapter {
    private Context _context;
    private int _resource;
    private ArrayList<String> list;

    public dateArrayAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        _context = context;
        _resource = resource;
        list = objects;

    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);

        TextView tvDateListItem = convertView.findViewById(R.id.tvDateListItem);
        tvDateListItem.setText(list.get(position));

        tvDateListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xxx", list.get(position));

                Intent intent = new Intent(_context, ExerciseListActivity.class);
                intent.putExtra("idDate", position);
                _context.startActivity(intent);

            }
        });

        return convertView;
    }
}
