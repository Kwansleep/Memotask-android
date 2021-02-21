package stormhack21.memotask.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        DetailedTaskManager.getInstance().injectDebug();

        detailTaskRecyclerView = findViewById(R.id.detailTaskRecyclerView);
        TaskAdapter mAdapter = new TaskAdapter(this);
        detailTaskRecyclerView.setAdapter(mAdapter);
        detailTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.mViewHolder> {
        // Error Tag
        String TAG = "TaskAdapter";

        private Context context;
        List<Task> tasks;

        private class mViewHolder extends RecyclerView.ViewHolder{

            // views in a single card
            private TextView title;
            private TextView date;
            private TextView time;
            private TextView countdown;
            private TextView description;
            private TextView location;

            private View parentView;

            public mViewHolder(View view) {
                super(view);
                this.title = view.findViewById(R.id.detailedTaskCardTitle);
                this.date = view.findViewById(R.id.detailedTaskCardDate);
                this.time = view.findViewById(R.id.detailedTaskCardTime);
                this.countdown = view.findViewById(R.id.detailedTaskCardCountdown);
                this.description = view.findViewById(R.id.detailedTaskCardDescription);
                this.location = view.findViewById(R.id.detailedTaskCardLocation);

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
        public void onBindViewHolder(@NonNull mViewHolder holder, int position) {

            // set up Views variable
            TextView titleView = holder.title;
            TextView dateView = holder.date;
            TextView timeView = holder.time;
            final TextView countdown = holder.countdown;
            TextView descriptionView = holder.description;
            TextView locationView = holder.location;



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


            // Set On clicker listener
            //Log.e("tracking Number View:","tracking: " + trackingNumber);
            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

    }

}