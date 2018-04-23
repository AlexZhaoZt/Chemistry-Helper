package edu.illinois.cs.cs125.lab12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab12:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;



    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);
        final TextView shortResult = (TextView) findViewById(R.id.shortResult);

        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button molarMass = (Button) findViewById(R.id.molarMass);
        molarMass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortResult.setText("HelloWorld");
            }
        });
        startAPICall();

    }



    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the weather API.
     */
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://api.openweathermap.org/data/2.5/weather?zip=61820,us&appid="
                            + BuildConfig.API_KEY,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                            } catch (JSONException ignored) { }
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

    // Periodic Table of Elements (Credits to ptable.com):

    // OTHER NONMETALS
    public final static double H = 1.008;
    public final static double C = 12.011;
    public final static double N = 14.007;
    public final static double O = 15.999;
    public final static double P = 30.973;
    public final static double S = 32.06;
    public final static double Se = 78.971;

    // Alkali Metals
    public final static double Li = 6.94;
    public final static double Na = 22.989;
    public final static double K = 39.0983;
    public final static double Rb = 85.4678;
    public final static double Cs = 132.90;
    public final static double Fr = 223.00;

    // Alkaline Earth Metals
    public final static double Be = 9.0121;
    public final static double Mg = 24.305;
    public final static double Ca = 40.078;
    public final static double Sr = 87.62;
    public final static double Ba = 137.327;
    public final static double Ra = 226.00;

    // Transition Metals
    public final static double Sc = 44.955;
    public final static double Ti = 47.867;
    public final static double V = 50.9415;
    public final static double Cr = 51.9961;
    public final static double Mn = 54.938;
    public final static double Fe = 55.845;
    public final static double Co = 58.933;
    public final static double Ni = 58.6934;
    public final static double Cu = 63.546;
    public final static double Zn = 65.38;
    public final static double Y = 88.90584;
    public final static double Zr = 91.224;
    public final static double Nb = 92.90637;
    public final static double Mo = 95.95;
    public final static double Tc = 98.00;
    public final static double Ru = 101.07;
    public final static double Rh = 102.90;
    public final static double Pd = 106.42;
    public final static double Ag = 107.8682;
    public final static double Cd = 112.414;
    public final static double Hf = 178.49;
    public final static double Ta = 180.94;
    public final static double W = 183.84;
    public final static double Re = 186.207;
    public final static double Os = 190.23;
    public final static double Ir = 192.217;
    public final static double Pt = 195.084;
    public final static double Au = 196.96;
    public final static double Hg = 200.59;
    public final static double Rf = 267.00;
    public final static double Db = 268.00;
    public final static double Sg = 271.00;
    public final static double Bh = 272.00;
    public final static double Hs = 270.00;
    public final static double Mt = 276.00;
    public final static double Ds = 281.00;
    public final static double Rg = 280.00;
    public final static double Cn = 285.00;

    // POST TRANSITION METALS
    public final static double Al = 26.981;
    public final static double Ga = 69.723;
    public final static double In = 114.818;
    public final static double Sn = 118.710;
    public final static double TI = 204.38;
    public final static double Pb = 207.20;
    public final static double Bi = 208.98;
    public final static double Uut = 284.00;
    public final static double Fl = 289.00;
    public final static double Uup = 288.00;
    public final static double Lv = 293.00;

    // Metalloids
    public final static double B = 10.81;
    public final static double Si = 28.085;
    public final static double Ge = 72.63;
    public final static double As = 74.921;
    public final static double Sb = 121.760;
    public final static double Te = 127.60;
    public final static double Po = 209.00;

    // Halogens
    public final static double F = 18.998;
    public final static double Cl = 35.45;
    public final static double Br = 79.904;
    public final static double I = 126.90;
    public final static double At = 210.00;
    public final static double Uus = 294.00;

    // Noble Gases
    public final static double He = 4.002602;
    public final static double Ne = 20.1797;
    public final static double Ar = 39.948;
    public final static double Kr = 83.798;
    public final static double Xe = 131.293;
    public final static double Rn = 222.00;
    public final static double Uuo = 294.00;

    // Lanthanoids
    public final static double La = 138.90;
    public final static double Ce = 140.116;
    public final static double Pr = 140.90;
    public final static double Nd = 144.242;
    public final static double Pm = 145.00;
    public final static double Sm = 150.36;
    public final static double Eu = 151.964;
    public final static double Gd = 157.25;
    public final static double Tb = 158.92;
    public final static double Dy = 162.500;
    public final static double Ho = 164.93;
    public final static double Er = 167.259;
    public final static double Tm = 168.93;
    public final static double Yb = 173.054;
    public final static double Lu = 174.9668;

    // Actinoids
    public final static double Ac = 227.00;
    public final static double Th = 232.0377;
    public final static double Pa = 231.03;
    public final static double U = 238.02;
    public final static double Np = 237.00;
    public final static double Pu = 244.00;
    public final static double Am = 243.00;
    public final static double Cm = 247.00;
    public final static double Bk = 247.00;
    public final static double Cf = 251.00;
    public final static double Es = 252.00;
    public final static double Fm = 257.00;
    public final static double Md = 258.00;
    public final static double No = 259.00;
    public final static double Lr = 262.00;

    public double getMolarMass(final String chemical) {
        double mass = 0.0;
        String totalElemData = "H,1.008;C,12.011;N,14.007;O,15.999;P,30.973;S,32.06;Se,78.971;Li,6.94;Na,22.989;K,39.0983;Rb,85.4678;Cs,132.90;Fr,223.00;Be,9.0121;Mg,24.305;Ca,40.078;Sr,87.62;Ba,132.327;Ra,226.00;Sc,44.955;Ti,47.867;V,50.9415;Cr,51.9961;Mn,54.938;Fe,55.845;Co,58.933;Ni,58.6934;Cu,63.546;Zn,65.38;Y,88.90584;Zr,91.224;Nb,92.90637;Mo,95.95;Tc,98.00;Ru,101.07;Rh,102.90;Pd,106.42;Ag,107.8682;Cd,112.414;Hf,178.49;Ta,180.94;W,183.84;Re,186.207;Os,193.23;Ir,192.217;Pt,195.084;Au,196.96;Hg,200.59;Rf,267.00;Db,268.00;Sg,271.00;Bh,272.00;Hs,270.00;Mt,276.00;Ds,281.00;Rg,280.00;Cn,285.00;Al,26.981;Ga,69.723;In,114.818;Sn,118.710;TI,204.38;Pb,207.20;Bi,208.98;Uut,284.00;Fl,289.00;Uup,288.00;Lv,293.00;B,10.81;Si,28.085;Ge,72.63;As,74.921;Sb,121.760;Te,127.60;Po,209.00;F,18.998;Cl,35.45;Br,79.904;I,126.90;At,210.00;Uus,294.00;He,4.002602;Ne,20.1797;Ar,39.948;Kr,83.798;Xe,131.293;Rn,222.00;Uuo,294.00;La,138.90;Ce,140.116;Pr,140.90;Nd,144.242;Pm,145.00;Sm,150.36;Eu,151.964;Gd,157.25;Tb,158.92;Dy,162.500;Ho,164.93;Er,167.259;Tm,168.93;Yb,173.054;Lu,174.9668;Ac,227.00;Th,232.0377;Pa,231.03;U,238.02;Np,237.00;Pu,244.00;Am,243.00;Cm,247.00;Bk,247.00;Cf,251.00;Es,252.00;Fm,257.00;Md,258.00;No,259.00;Lr,262.00";
        String[] elementsData = totalElemData.split(";");
        Map<String, Double> elementsMap = new HashMap<>();
        for (int i = 0; i < elementsData.length; i++) {
            String singleElement = elementsData[i];
            String[] elementAndMolarMass = singleElement.split(",");
            elementsMap.put(elementAndMolarMass[0], Double.parseDouble(elementAndMolarMass[1]));
        }
        for (int i = 0; i < chemical.length(); i++) {
            int j = i;
            if (j < chemical.length()) {
                if (elementsMap.containsKey(chemical.substring(i, j)) && !elementsMap.containsKey(chemical.substring(i, j + 1))) {
                    String element = chemical.substring(i, j);

                }




            }
        }



    }

    public String getFormula(final String chemical) {
        for (int i = 0; i < chemical.length(); i++) {
            for (int j = i; j < chemical.length(); j++) {
                String subStr = "";
                if (elementsMap.containsKey())
                    if (!chemical.substring(i, j).equals())
                        subStr += chemical.substring(i, j);


            }
        }
    }
}
