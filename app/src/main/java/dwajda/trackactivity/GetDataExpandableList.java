package dwajda.trackactivity;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class GetDataExpandableList {
    public static HashMap<String, List<String>> getAllData() throws JSONException {
        HashMap<String, List<String>> listDetail = new HashMap<>();

        JSONObject jo = new JSONObject();


        jo.put("1", "asd");
        Log.d("xxx", String.valueOf(jo));

        return listDetail;
    }
}
