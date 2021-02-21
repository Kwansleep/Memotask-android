package stormhack21.memotask.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import stormhack21.memotask.R;
import stormhack21.memotask.model.DetailedTaskManager;
import stormhack21.memotask.model.SingleTask;
import stormhack21.memotask.model.Task;

public class DetailedListActivity extends AppCompatActivity {
    // Error Tag
    String TAG = "DetailedListActivity";

    RecyclerView detailTaskRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_list);

        // Debug
        //DetailedTaskManager.getInstance().injectDebug();

        // RecyclerView
        detailTaskRecyclerView = findViewById(R.id.detailTaskRecyclerView);
        TaskAdapter mAdapter = new TaskAdapter(this);
        detailTaskRecyclerView.setAdapter(mAdapter);
        detailTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // add task FAB
        /*
        FloatingActionButton button = findViewById(R.id.addDetailedTask);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailedAddActivity.class);
                startActivity(intent);
            }
        });
         */

        // set up bottom Nav bar
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()){
                    case R.id.menu_toMemoList:
                        intent = new Intent(getApplicationContext(),MemoListActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.menu_toDetailedList:
                        break;
                    case R.id.menu_addDetailedTask:
                        intent = new Intent(getApplicationContext(),DetailedAddActivity.class);
                        startActivity(intent);

                        break;
                    default:
                        throw new RuntimeException("Invalid menu choice");
                }
                return false;
            }
        });


    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.mViewHolder> {
        // Error Tag
        String TAG = "TaskAdapter";

        private Context context;
        private List<Task> tasks;

        private class mViewHolder extends RecyclerView.ViewHolder{

            // views in a single card
            private TextView title;
            private TextView date;
            private TextView time;
            private TextView countdown;
            private TextView description;
            private TextView location;

            private ImageButton delete;

            private CardView rootView;
            private ConstraintLayout fullInfoView;
            private View parentView;

            public mViewHolder(View view) {
                super(view);
                this.title = view.findViewById(R.id.detailedTaskCardTitle);
                this.date = view.findViewById(R.id.detailedTaskCardDate);
                this.time = view.findViewById(R.id.detailedTaskCardTime);
                this.countdown = view.findViewById(R.id.detailedTaskCardCountdown);
                this.description = view.findViewById(R.id.detailedTaskCardDescription);
                this.location = view.findViewById(R.id.detailedTaskCardLocation);

                this.rootView = view.findViewById(R.id.detailedTaskCardView);
                this.fullInfoView = view.findViewById(R.id.detailedTaskCardFullInfo);

                this.delete = view.findViewById(R.id.deleteTask);
                this.parentView = view;
            }
        }

        public TaskAdapter(Context context){
            this.context = context;
            this.tasks = DetailedTaskManager.getInstance().getTasks();
        }

        @NonNull
        @Override
        public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new mViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.detailed_task_item, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull mViewHolder holder, final int position) {

            // set up Views variable
            TextView titleView = holder.title;
            TextView dateView = holder.date;
            TextView timeView = holder.time;
            final TextView countdown = holder.countdown;
            TextView descriptionView = holder.description;
            TextView locationView = holder.location;

            final CardView rootView = holder.rootView;
            final ConstraintLayout fullInfoView = holder.fullInfoView;


            // Get the current task
            SingleTask thisTask = (SingleTask)tasks.get(position);

            // Title
            titleView.setText(thisTask.getTitle());
            // Date
            LocalDate localDate = thisTask.getDate();
            String date = localDate.format(DateTimeFormatter.ofPattern("dd/LL/yyyy"));
            dateView.setText(date);
            // Time
            LocalTime localTime = thisTask.getTime();
            String time = localTime.format(DateTimeFormatter.ofPattern("kk:mm"));
            timeView.setText(time);
            // Countdown
            long timeLeft = localTime.toSecondOfDay() - LocalTime.now().toSecondOfDay();
            timeLeft += Period.between(LocalDate.now(),localDate).getDays() * 86400; // there is 86400 seconds in 1 day

            new CountDownTimer(timeLeft * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(millisUntilFinished >= 86400000){
                        // there is 86400000 milliseconds in 1 day
                        countdown.setText( (int) millisUntilFinished/86400000 + " day(s) left");
                    } else if(millisUntilFinished >= 3600000) {
                        // there is 3600000 milliseconds in 1 hour
                        countdown.setText( (int) millisUntilFinished/3600000 + " hour(s) left");
                    } else {
                        long seconds = millisUntilFinished / 1000;
                        String displayTime = (int) seconds / 60 + ":" + seconds % 60;
                        countdown.setText(displayTime);
                    }
                }

                @Override
                public void onFinish() {
                    countdown.setText("00:00 Time's up!!!");
                }
            }.start();
            // Description
            descriptionView.setText(thisTask.getDescription());
            // Location
            locationView.setText(thisTask.getLocation());

            View parentView = holder.parentView;
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (fullInfoView.getVisibility() == View.GONE){
                        TransitionManager.beginDelayedTransition(detailTaskRecyclerView,new AutoTransition());
                        fullInfoView.setVisibility(View.VISIBLE);
                    } else {
                        //TransitionManager.beginDelayedTransition(rootView,new AutoTransition());
                        TransitionManager.beginDelayedTransition(detailTaskRecyclerView, new AutoTransition());
                        fullInfoView.setVisibility(View.GONE);
                    }

                }
            });

            final ImageButton delete = holder.delete;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        public void removeAt(int position) {

            tasks.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tasks.size());
        }
    }

    @Override
    protected void onResume() {
        detailTaskRecyclerView.getAdapter().notifyDataSetChanged();
        super.onResume();
    }
}