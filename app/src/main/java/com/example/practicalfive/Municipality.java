package com.example.practicalfive;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int summaryIndex;
        int imageResource;
        switch (name) {
            case "Beijing":
                summaryIndex = 0;
                imageResource = R.drawable.beijing;
                break;
            case "Shanghai":
                summaryIndex = 1;
                imageResource = R.drawable.shanghai;
                break;
            case "Chongqing":
                summaryIndex = 2;
                imageResource = R.drawable.chongqing;
                break;
            case "Tianjin":
                summaryIndex = 3;
                imageResource = R.drawable.tianjin;
                break;
            default:
                finish();
                return;
        }

        TextView title = findViewById(R.id.municipality_name);
        ImageView image = findViewById(R.id.municipality_image);
        TextView information = findViewById(R.id.municipality_information);
        title.setText(name);
        image.setImageResource(imageResource);
        image.setContentDescription(getString(R.string.city_image_description, name));
        information.setText(getResources().getStringArray(R.array.municipalities_array)[summaryIndex]);
    }
}
