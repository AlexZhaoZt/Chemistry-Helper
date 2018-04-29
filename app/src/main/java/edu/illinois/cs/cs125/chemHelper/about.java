package edu.illinois.cs.cs125.chemHelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class about extends AppCompatActivity {

    private TextView title;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        text.setText(Html.fromHtml("Created by Zhenting Zhao.<br>The search functionality uses the " +
                "API from ChemSpider Compound Search API. The molar mass calculator" +
                "functionality runs locally. <br> This application is still in its early " +
                "developing phase. <br>" + "<font color=#e0e000>TODO</font>: implement menu.settings<br>" +
                "<font color=#e0e000>TODO</font>: enable the molar mass calculator to read interpunct " +
                "(for example: CuSO4·5H2O)."));
    }
}
