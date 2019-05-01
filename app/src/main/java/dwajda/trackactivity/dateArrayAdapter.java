package dwajda.trackactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class dateArrayAdapter extends ArrayAdapter<String> {
    private Context _context;
    private int _resource;
    private ArrayList<String> list;

    dateArrayAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        _context = context;
        _resource = resource;
        list = objects;

    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final dateArrayAdapter thisAdapter = this;

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

        tvDateListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Delete " + list.get(position) + " ?");
                builder.setTitle("Delete");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GetDataExpandableList.removeOneDate(position);

                        list.remove(position);
                        thisAdapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("xxx", "onLongClick: NIE USUWAM");
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
}
