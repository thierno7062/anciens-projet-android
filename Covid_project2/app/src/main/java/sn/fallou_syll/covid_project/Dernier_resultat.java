package sn.fallou_syll.covid_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

public class Dernier_resultat extends AppCompatActivity {

    private WebView webView;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dernier_resultat);
       /* mapView = (MapView) findViewById(R.id.mapView2);
        mapView.*/

        webView = (WebView) findViewById(R.id.web_site);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://cartosantesen.maps.arcgis.com/apps/dashboards/260c7842a77a48c191bf51c8b0a1d3f6");
        Toast.makeText(this, "Merci de patienter!", Toast.LENGTH_LONG).show();
    }
}