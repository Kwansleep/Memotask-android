package stormhack21.memotask.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;

import stormhack21.memotask.R;
import stormhack21.memotask.model.CustomPager;
import stormhack21.memotask.model.MemoClass;
import stormhack21.memotask.model.MemoManager;
import stormhack21.memotask.ui.ui.main.SectionsPagerAdapter;

public class MemoActivity extends AppCompatActivity {

    private ImageButton clear;
    private ImageButton save;
    private CustomPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private Boolean mMemoryPermissionGranted = false;
    private static final int MEMORY_PERMISSION_REQUEST_CODE = 1234;
    private MemoManager memoManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_activity);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = (CustomPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        memoManager = MemoManager.getInstance();
        getMemoryPermission();


        clear = (ImageButton) findViewById(R.id.clearbtn);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1 = sectionsPagerAdapter.getMCurrentFragment();
                View view1 = fragment1.getView();
                paintView paintView = view1.findViewById(R.id.paintView);
                EditText editText = (EditText) view1.findViewById(R.id.descrip);
                if (paintView == null)
                    editText.setText("");
                else if (editText == null)
                    paintView.clear();

            }
        });

        save = (ImageButton) findViewById(R.id.savebtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = null;
                String path = null;
                Intent intent = new Intent(MemoActivity.this, MemoListActivity.class);
                Fragment fragment2 = sectionsPagerAdapter.getMCurrentFragment();
                View view2 = fragment2.getView();
                paintView paintView = view2.findViewById(R.id.paintView);
                EditText editText = (EditText) view2.findViewById(R.id.descrip);
                if (editText != null) {
                    if (!editText.getText().toString().isEmpty())
                        description = editText.getText().toString();
                }
                else if (paintView.checkEmpty() == false) {
                    path = String.valueOf(getApplicationContext().getFilesDir());
                    File file = new File(path + memoManager.size() + 1 + ".png");
                    path = (path + memoManager.size() + 1 + ".png");
                    paintView.saveBitmap(file);
                }
                MemoClass memoClass;
                if(description != null || path != null) {
                    memoClass = new MemoClass(description, path);
                    memoManager.add(memoClass);
                }
                startActivity(intent);
            }
        });
    }

    private void getMemoryPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mMemoryPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, MEMORY_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, MEMORY_PERMISSION_REQUEST_CODE);
        }
    }
}

   