package stormhack21.memotask.ui.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import stormhack21.memotask.R;
import stormhack21.memotask.model.MemoManager;


public class Frag1 extends Fragment {

    // views reference
    private EditText editText;
    private View rootView;
    private MemoManager memoManager = MemoManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag1_layout, container, false);
        rootView = view;
        editText = (EditText) rootView.findViewById(R.id.descrip);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                memoManager.stage(editText.getText().toString());
            }
        });
        return view;
    }

}
