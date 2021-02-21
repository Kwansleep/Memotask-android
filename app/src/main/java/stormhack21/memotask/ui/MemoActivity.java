package stormhack21.memotask.ui;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import stormhack21.memotask.R;
import stormhack21.memotask.model.CustomPager;
import stormhack21.memotask.ui.ui.main.Frag1;
import stormhack21.memotask.ui.ui.main.SectionsPagerAdapter;

public class MemoActivity extends AppCompatActivity {

    private ImageButton clear;
    private ImageButton save;
    private CustomPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_activity);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = (CustomPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        clear = (ImageButton) findViewById(R.id.clearbtn);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1 = sectionsPagerAdapter.getMCurrentFragment();
                View view1 = fragment1.getView();
                EditText editText = (EditText) view1.findViewById(R.id.textView3);
                Log.d("TAG", editText.toString());
                editText.setText("");
            }
        });

    }
}