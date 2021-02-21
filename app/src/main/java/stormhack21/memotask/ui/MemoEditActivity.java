package stormhack21.memotask.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import stormhack21.memotask.R;
import stormhack21.memotask.model.MemoManager;

public class MemoEditActivity extends AppCompatActivity {
    private MemoManager memoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        memoManager = MemoManager.getInstance();

        final Intent intent = getIntent();
        final int position = intent.getIntExtra("Position", 0);
        final EditText editText = (EditText) findViewById(R.id.edit);
        editText.setText(memoManager.getMemos().get(position).getDescription());

        ImageButton save = (ImageButton) findViewById(R.id.save);
        ImageButton clear = (ImageButton) findViewById(R.id.delete);
        ImageButton update = (ImageButton) findViewById(R.id.upgradebtn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = (EditText) findViewById(R.id.edit);
                Intent intent1 = new Intent(MemoEditActivity.this, MemoListActivity.class);
                if(!editText1.getText().toString().isEmpty())
                    memoManager.getMemos().get(position).setDescription(editText1.getText().toString());
                else
                    memoManager.getMemos().get(position).setDescription("Try to write something");

                Log.d("TAG", memoManager.getMemos().get(position).getPath());
                startActivity(intent1);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = (EditText) findViewById(R.id.edit);
                Intent intent1 = new Intent(MemoEditActivity.this, DetailedListActivity.class);
                intent1.putExtra("Description", editText1.toString());
                startActivity(intent1);
            }
        });
    }
}