package stormhack21.memotask.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import stormhack21.memotask.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button memo = findViewById(R.id.buttonToMemo);
        Button detail = findViewById(R.id.buttonToDetail);

        memo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                startActivity(intent);
            }
        });


        detail.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailedListActivity.class);
                startActivity(intent);
            }
        });


    }
}