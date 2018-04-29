package edu.illinois.cs.cs125.chemHelper;

import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;


import java.util.HashMap;
import java.util.Map;


public final class MainActivity extends AppCompatActivity {

    public boolean checked = false;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView shortResult = (TextView) findViewById(R.id.shortResult);
        final EditText textbar = (EditText) findViewById(R.id.textbar);
        Button search = (Button) findViewById(R.id.search);
        final Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch1.isChecked()) {
                    checked = true;
                } else {
                    checked = false;
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(MainActivity.this, searchResult.class);
                intentSearch.putExtra("content", textbar.getText().toString());
                intentSearch.putExtra("option", checked);
                startActivity(intentSearch);
            }
        });

        Button molarMass = (Button) findViewById(R.id.molarMass);
        molarMass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMolarMass(textbar.getText().toString()) == -1.0) {
                    shortResult.setText("ERROR: Please enter a valid chemical formula. Case sensitive.");
                } else {
                    shortResult.setText("The molar mass of the compound is:\n" + Double.toString(getMolarMass(textbar.getText().toString())));
                }

            }
        });
    }


    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

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
            int j = i + 1;
            String element = "";
            double parentMass = 0.0;
            int amount = 1;
            if (j < chemical.length()) {
                if (elementsMap.containsKey(chemical.substring(i, j)) && !elementsMap.containsKey(chemical.substring(i, j + 1))) {
                    element = chemical.substring(i, j);
                } else if (elementsMap.containsKey(chemical.substring(i, j + 1))) {
                    element = chemical.substring(i, ++j);
                    i++;
                } else if (chemical.charAt(i) == '(') {
                    String newFormula = "";
                    i++;
                    while (chemical.charAt(i) != ')' && i < chemical.length()) {
                        newFormula += chemical.charAt(i);
                        i++;
                    }
                    if (chemical.charAt(i) != ')') {
                        return -1.0;
                    }
                    j = i + 1;
                    parentMass = getMolarMass(newFormula);
                } else if (chemical.charAt(i) == '·') {
                    String newFormula = "";
                    String amountStr = "";
                    int number = 1;
                    i++;
                    for (int k = i; k < chemical.length(); k++) {
                        if (chemical.charAt(k) >= '0' && chemical.charAt(k) <= '9') {
                            amountStr += chemical.charAt(k);
                            i++;
                        } else {
                            break;
                        }
                    }
                    number = Integer.parseInt(amountStr);
                    while (i < chemical.length()) {
                        newFormula += chemical.charAt(i);
                        i++;
                    }
                    j = i + 1;
                    mass += getMolarMass(newFormula) * number;
                }
                else {
                    return -1.0;
                }
            } else {
                element = chemical.substring(i);
            }
            String amountStr = "";
            for (int k = j; k < chemical.length(); k++) {
                if (chemical.charAt(k) >= '0' && chemical.charAt(k) <= '9') {
                    amountStr += chemical.charAt(k);
                    i++;
                } else {
                    break;
                }
            }
            if (amountStr != "") {
                amount = Integer.parseInt(amountStr);
            }
            if (element == "") {
                mass += amount * parentMass;
            } else {
                mass += amount * elementsMap.get(element);
            }
        }
        return mass;
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


}



