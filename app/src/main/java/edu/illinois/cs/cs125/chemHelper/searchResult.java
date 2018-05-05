package edu.illinois.cs.cs125.chemHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class searchResult extends AppCompatActivity {

    public static RequestQueue requestQueue;
    private static final String TAG = "Search Result";
    public String api = "jgXxAMt3w8GXg3aZaAkFJHTN5xBYaVLD";
    public String recordID;
    public String queryID = null;
    public TextView name;
    public TextView smile;
    public TextView molarMass;
    public TextView formula;
    public ImageView imageView;
    public TextView wikipedia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        name = findViewById(R.id.name);
        smile = findViewById(R.id.smile);
        molarMass = findViewById(R.id.molarMass);
        formula = findViewById(R.id.formula);
        imageView = findViewById(R.id.imageView);
        wikipedia = findViewById(R.id.wikipedia);
        final String chemical = intent.getStringExtra("content");
        Log.i(TAG, chemical);
        boolean checked = getIntent().getExtras().getBoolean("option");
        if (checked) {
            initiateByFormula(chemical);
        } else {
            initiateByName(chemical);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent1 = new Intent(this, about.class);
                this.startActivity(intent1);
                return true;
            case R.id.about:
                Intent intent2 = new Intent(this, about.class);
                this.startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void initiateByName(String chemical) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", chemical);
            json.put("orderDirection", "");
        } catch (JSONException ignored) {
            Log.e(TAG, "you screwed up the json package");
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://api.rsc.org/compounds/v1/filter/name",
                json,
                new Response.Listener<JSONObject>() {
                    public void onResponse(final JSONObject response) {
                        try {
                            queryID = response.getString("queryId");
                            Log.i(TAG, queryID);
                            Log.i(TAG, "success on query");
                            callQueryIDGetRecordID();

                        } catch (JSONException ignored) {
                            Log.e(TAG, "you screwed up the onResponse:\n" + ignored.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("apikey", api);
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void initiateByFormula(String chemical) {
        JSONObject json = new JSONObject();
        try {
            json.put("formula", chemical);
            json.put("orderDirection", "");
        } catch (JSONException ignored) {
            Log.e(TAG, "you screwed up the json package");
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://api.rsc.org/compounds/v1/filter/formula",
                json,
                new Response.Listener<JSONObject>() {
                    public void onResponse(final JSONObject response) {
                        try {
                            queryID = response.getString("queryId");
                            Log.i(TAG, queryID);
                            Log.i(TAG, "success on query");
                            callQueryIDGetRecordID();
                        } catch (JSONException ignored) {
                            Log.e(TAG, "you screwed up the onResponse:\n" + ignored.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("apikey", api);
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void callQueryIDGetRecordID() {
        try {
            JsonObjectRequest recordIDRequest = new JsonObjectRequest(
                    "https://api.rsc.org/compounds/v1/filter/"
                            + queryID + "/results?start=0&count=1",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                recordID = response.getJSONArray("results").getString(0);
                                if (recordID.equals("") || recordID == null) {
                                    name.setText("Error");
                                    molarMass.setText("Sorry, we can not find this compound.");
                                }
                                Log.i(TAG, recordID);
                                callRecordIDGetDetails();
                            } catch (JSONException e) {
                                Log.e(TAG, "Error on APICallQueryIDGetRecordID.OnResponse.");
                                name.setText("Error");
                                molarMass.setText("Sorry, we can not find this compound.");
                            } catch (NullPointerException e) {
                                name.setText("Error");
                                molarMass.setText("Sorry, we can not find this compound.");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                            name.setText("Error");
                            molarMass.setText("Sorry, we can not find this compound.");
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("apikey", api);
                    return params;
                }
            };
            requestQueue.add(recordIDRequest);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : implement
        }
    }

    public void callRecordIDGetDetails() {
        final JsonObjectRequest detailsRequest = new JsonObjectRequest(
                "https://api.rsc.org/compounds/v1/records/" + recordID + "/details?fields=SMILES,Formula,CommonName,MolecularWeight",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i(TAG, "Details: " + response.getString("commonName"));
                            name.setText(response.getString("commonName"));
                            smile.setText("SMILES: " + response.getString("smiles"));
                            String rawFormula = response.getString("formula");
                            String regx = "_{}";
                            char[] ca = regx.toCharArray();
                            for (char c : ca) {
                                rawFormula = rawFormula.replace("" + c, "");
                            }
                            formula.setText("Formula: " + rawFormula);
                            molarMass.setText("Molar Mass: " + Double.toString(response.getDouble("molecularWeight")));
                        } catch (JSONException e) {
                            Log.e(TAG, "Error on detailsRequest.onResponse.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error on detailsRequest.detailsRequest.");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("apikey", api);
                return params;
            }
        };
        JsonObjectRequest wikiRequest = new JsonObjectRequest(
                "https://api.rsc.org/compounds/v1/records/" + recordID + "/externalreferences?dataSources=wikipedia",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray wiki = response.getJSONArray("externalReferences");
                            JSONObject wikiLink = wiki.getJSONObject(0);
                            String link = wikiLink.getString("externalUrl");

                            wikipedia.setText(Html.fromHtml("Wikipedia page available:<br><font color=#0066ff>" + link + "</font><br>Click to access the page."));

                            final Uri uriUrl = Uri.parse(link);
                            wikipedia.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                    startActivity(launchBrowser);
                                }
                            });
                        } catch (JSONException ignored) {
                            Log.e(TAG, "NO SUCH JSON!!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "WIKI request send error.");
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("apikey", api);
                return params;
            }
        };
        final JsonObjectRequest imageRequest = new JsonObjectRequest(
                "https://api.rsc.org/compounds/v1/records/" + recordID + "/image",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response3) {
                        try {
                            Log.i(TAG, "Image (Base64 coded): " + response3.getString("image"));
                            byte[] decoded = Base64.decode(response3.getString("image"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                            imageView.setImageBitmap(decodedByte);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error on imageRequest.onResponse.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error on detailsRequest.");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("apikey", api);
                return params;
            }
        };
        requestQueue.add(detailsRequest);
        requestQueue.add(wikiRequest);
        requestQueue.add(imageRequest);
    }


}
