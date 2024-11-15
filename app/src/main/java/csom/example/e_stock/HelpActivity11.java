package csom.example.e_stock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class HelpActivity11 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout_lastpage2);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Button btnBack = findViewById(R.id.BTN_Back);
        Button btnReturn = findViewById(R.id.BTN_Return);
        Button btnNext = findViewById(R.id.BTN_Next);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity11.this, HelpActivity10.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the EstockHomePageActivity and finish the current activity
                Intent intent = new Intent(HelpActivity11.this, eStockHomePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_downwards, R.anim.slide_out_upwards);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HelpActivity11.this, "You've reached the end of the page", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
