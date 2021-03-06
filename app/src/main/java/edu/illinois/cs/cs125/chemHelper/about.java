package edu.illinois.cs.cs125.chemHelper;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class about extends AppCompatActivity {

    private TextView title;
    private TextView text;
    private Button youTube;
    private TextView github;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        youTube = (Button) findViewById(R.id.youTube);
        github = (TextView) findViewById(R.id.github);
        github.setOnClickListener(new View.OnClickListener() {
            final Uri uriUrl = Uri.parse("https://github.com/AlexZhaoZt/Chemistry-Helper");
                @Override
                public void onClick(View v) {

                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }
        });
        text.setText(Html.fromHtml("Created by Zhenting Zhao.<br>The search functionality uses the " +
                "ChemSpider Compound Search API. The molar mass calculator " +
                "feature runs locally. <br> This application is still in its early " +
                "development phase. <br>" + "<font color=#e0e000>TODO</font>: implement menu.settings<br>"));
        youTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String video = "https://www.youtube.com/watch?v=wQXrmVKHJRA&t=210s";
                Uri webAddress = Uri.parse(video);

                Intent goToVideo = new Intent(Intent.ACTION_VIEW, webAddress);
                if (goToVideo.resolveActivity(getPackageManager()) != null) {
                    startActivity(goToVideo);
                }
            }
        });
    }
}
