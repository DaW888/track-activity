package dwajda.trackactivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
//            JSONArray ja = GetDataExpandableList.getAllData();
//            Log.d("xxx", String.valueOf(ja));
                        String getFile = GetDataExpandableList.readFromFile();
                        JSONArray jar = new JSONArray(getFile);
                        Log.d("xxx", "Z PLIKU " + String.valueOf(jar));

                        ArrayList<String> arrayList = new ArrayList<>();
                        for (int i = 0; i < jar.length(); i++) {
                            JSONObject ob = (JSONObject) jar.get(i);
                            arrayList.add((String) ob.get("date"));
                        }
                        Log.d("xxx", "array lista:  " + arrayList);

                        ListView lvDateList = findViewById(R.id.lvDateList);

                        dateArrayAdapter adapter = new dateArrayAdapter(
                                MainActivity.this,
                                R.layout.date_list_item,
                                arrayList
                        );
                        lvDateList.setAdapter(adapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xxx", "Nie udalo sie odczytac");
                    }


                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Permissions");
                    alert.setMessage("We need Your permission just for save workoutData to json file in DOWNLOAD folder," +
                            " thanks to this You can Export/Save your data to other phone");
                    alert.setCancelable(false);

                    alert.setPositiveButton("OK, show alert Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    });

                    alert.setNegativeButton("NOPE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    alert.show();


                }
        }
    }
}
