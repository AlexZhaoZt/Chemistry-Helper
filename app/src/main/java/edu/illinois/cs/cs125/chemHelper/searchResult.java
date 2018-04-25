package edu.illinois.cs.cs125.chemHelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class searchResult extends AppCompatActivity {

    private static RequestQueue requestQueue;
    private static final String TAG = "Search Result";
    public String api = "jgXxAMt3w8GXg3aZaAkFJHTN5xBYaVLD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        String chemicalName = null;
        if (getIntent().hasExtra("searchContent")) {
            chemicalName = getIntent().getExtras().getString("searchContent");
        }
        if (chemicalName != null) {
            startAPICall(chemicalName);
        } else {

        }
    }
    void startAPICall(String chemical) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    ""
                            + api,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                            } catch (JSONException ignored) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
