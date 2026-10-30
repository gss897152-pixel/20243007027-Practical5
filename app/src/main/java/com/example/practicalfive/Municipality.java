package com.example.practicalfive;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class Municipality extends AppCompatActivity {
    public static final String EXTRA_MUNICIPALITY = "com.example.practicalfive.MUNICIPALITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = new TextProcessor().processMunicipality(
                getIntent().getStringExtra(EXTRA_MUNICIPALITY));
        if (name == null) {
            Toast.makeText(this, R.string.invalid_municipality, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_municipality);
        WindowInsetsControllerCompat bars = WindowCompat.getInsetsController(
                getWindow(), getWindow().getDecorView());
        bars.setAppearanceLightStatusBars(true);
        bars.setAppearanceLightNavigationBars(true);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int summaryIndex;
        int imageResource;
        int colorResource;
        switch (name) {
            case "Beijing":
                summaryIndex = 0;
                imageResource = R.drawable.beijing;
                colorResource = R.color.municipality_beijing;
                break;
            case "Shanghai":
                summaryIndex = 1;
                imageResource = R.drawable.shanghai;
                colorResource = R.color.municipality_shanghai;
                break;
            case "Chongqing":
                summaryIndex = 2;
                imageResource = R.drawable.chongqing;
                colorResource = R.color.municipality_chongqing;
                break;
            case "Tianjin":
                summaryIndex = 3;
                imageResource = R.drawable.tianjin;
                colorResource = R.color.municipality_tianjin;
                break;
            default:
                finish();
                return;
        }

        TextView title = findViewById(R.id.municipality_name);
        ImageView image = findViewById(R.id.municipality_image);
        TextView information = findViewById(R.id.municipality_information);
        Button visitSite = findViewById(R.id.visit_site);
        title.setText(name);
        image.setImageResource(imageResource);
        image.setContentDescription(getString(R.string.city_image_description, name));
        information.setText(getResources().getStringArray(R.array.municipalities_array)[summaryIndex]);

        int cityColor = getColor(colorResource);
        boolean isChongqing = name.equals("Chongqing");
        int contrastingText = getColor(isChongqing ? R.color.color_text_dark : R.color.color_text_light);
        int informationBackground = getColor(isChongqing
                ? R.color.color_dark_information_background : R.color.color_default_background);
        title.setBackgroundColor(cityColor);
        title.setTextColor(contrastingText);
        information.setTextColor(cityColor);
        information.setBackgroundColor(informationBackground);
        visitSite.setBackgroundTintList(ColorStateList.valueOf(cityColor));
        visitSite.setTextColor(contrastingText);
    }
}
