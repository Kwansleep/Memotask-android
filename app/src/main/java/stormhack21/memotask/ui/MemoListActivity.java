package stormhack21.memotask.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.List;

import stormhack21.memotask.R;
import stormhack21.memotask.model.MemoClass;
import stormhack21.memotask.model.MemoManager;

public class MemoListActivity extends AppCompatActivity {

    private MemoManager memoManager;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        memoManager = MemoManager.getInstance();


        ImageButton imageButton = (ImageButton)findViewById(R.id.addbtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoListActivity.this, MemoActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.memoList);
        adapter = new MyAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set up bottom Nav bar
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_memo);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.menu_toMemoList:
                        break;
                    case R.id.menu_toDetailedList:
                        intent = new Intent(getApplicationContext(),DetailedListActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_addDetailedTask:
                        intent = new Intent(getApplicationContext(),DetailedAddActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        throw new RuntimeException("Invalid menu choice");
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.getAdapter().notifyDataSetChanged();

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.mViewHolder> {
        private Context context;
        List<MemoClass> memos;

        private class mViewHolder extends RecyclerView.ViewHolder
        {
            private TextView description;
            private ImageView handWritten;
            private View parentView;
            private ImageButton delete;

            public mViewHolder(View view) {
                super(view);
                this.description = view.findViewById(R.id.description);
                this.handWritten = view.findViewById(R.id.handwriting);
                this.delete = view.findViewById(R.id.deletememobtn);
                this.parentView = view;
            }
        }


        public MyAdapter(Context context)
        {
            this.context = context;
            this.memos = MemoManager.getInstance().getMemos();
        }

        @NonNull
        @Override
        public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new mViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.single_memo, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.mViewHolder holder, final int position) {
            final TextView descriptionView = holder.description;
            ImageView handWrittingView = holder.handWritten;
            ImageButton delete = holder.delete;


            final MemoClass singleMemo = (MemoClass)memos.get(position);
            if(singleMemo.getDescription() != null)
                descriptionView.setText(singleMemo.getDescription());
            if(singleMemo.getPath() != null)
            {
                File image = new File(singleMemo.getPath());
                handWrittingView.setImageURI(Uri.fromFile(image));
            }




            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MemoListActivity.this, MemoEditActivity.class);
                    intent.putExtra("Position", position);
                    startActivity(intent);
                }
            });


            if( singleMemo.getPath() != null) {
                holder.handWritten.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File image = new File(singleMemo.getPath());
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.handwritting, null);
                        ImageView imageView = view.findViewById(R.id.openDrawing);
                        imageView.setImageURI(Uri.fromFile(image));
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setView(view)
                                .create();
                        dialog.show();
                    }
                });
            }


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   removeAt(position);
                }
            });

        }
        public void removeAt(int position) {
            memos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, memos.size());
        }



        @Override
        public int getItemCount() {
            return memos.size();
        }

    }


}