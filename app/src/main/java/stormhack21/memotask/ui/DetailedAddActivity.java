package stormhack21.memotask.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import stormhack21.memotask.R;
import stormhack21.memotask.model.DetailedTaskManager;
import stormhack21.memotask.model.SingleTask;

public class DetailedAddActivity extends AppCompatActivity {

    // References to views
    private ImageView dropOverlay;
    private EditText addTitle;
    private EditText dateText;
    private EditText timeText;
    private EditText locationText;
    private EditText descriptionText;

    // stored fields
    private LocalDate dateChoice;
    private LocalTime timeChoice;
    private MaterialDatePicker datePicker;

    // state check
    private boolean needCountCountTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_add);
        Intent intent = getIntent();
        String description = intent.getStringExtra("Description");


        // get reference for Views
        addTitle = findViewById(R.id.detailAddTitle);
        dateText = findViewById(R.id.detailAddDateText);
        timeText = findViewById(R.id.detailAddTimeText);
        locationText = findViewById(R.id.detailAddLocationText);
        descriptionText = findViewById(R.id.detailAddDescriptionText);

        if(description != null)
            descriptionText.setText(description);
        dropOverlay = findViewById(R.id.img);
        dropOverlay.setAlpha(0.5f);
        dropOverlay.setOnDragListener(new MyDragListener());

        //debug buttons
        Button btn = findViewById(R.id.addTaskButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addTitle.getText().toString();

                if(dateChoice != null && timeChoice != null){
                    DetailedTaskManager.getInstance().addTask(new SingleTask(title,descriptionText.getText().toString(),dateChoice, timeChoice ,locationText.getText().toString(),"no-image.img"));
                } else {
                    Log.d("AddActivity","Tried to add single task, but either date or time is missing");
                }
                Intent intent = new Intent(DetailedAddActivity.this, DetailedListActivity.class);
                startActivity(intent);
            }
        });

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DetailedListActivity.class);
                startActivity(intent);
            }
        });

        dropOverlay.setVisibility(View.GONE);

        setUpLocationPickers();
        setUpDatePickers();
        setUpTimePickers();
        setUpRecyclerView();
    }

    private void setUpLocationPickers() {
        // do something with locations
    }
    private void setUpTimePickers() {

        // override the on click function for Time EditText
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
                builder.setTitleText("Time Picker Title");
                builder.setHour(12);
                builder.setMinute(0);

                final MaterialTimePicker timePicker = builder.build();

                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeChoice = LocalTime.of(timePicker.getHour(),timePicker.getMinute());
                        String selectedTimeStr = timeChoice.format(DateTimeFormatter.ofPattern("kk:mm"));
                        timeText.setText(selectedTimeStr);
                    }
                });

                timePicker.show(getSupportFragmentManager(),"TimePicker");
            }
        });

    }
    private void setUpDatePickers() {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        builder.setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build());

        datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                dateText.setText(datePicker.getHeaderText());
                dateChoice = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.of("UTC")).toLocalDate();
            }
        });

        // override the on click function for Date EditText
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(),"DatePicker");
            }
        });
    }
    private void setUpRecyclerView() {
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.ic_baseline_subject_24);
        imageList.add(R.drawable.ic_baseline_calendar_today_24);
        imageList.add(R.drawable.ic_baseline_access_time_24);
        imageList.add(R.drawable.ic_baseline_add_location_24);

        List<String> nameList = new ArrayList<>();
        nameList.add("Description");
        nameList.add("Date");
        nameList.add("Time");
        nameList.add("Location");

        RecyclerView iconRecyclerView = findViewById(R.id.iconRecyclerView);
        iconRecyclerView.setAdapter(new IconAdapter(this,imageList,nameList));
        iconRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
    }

    private void callDatePicker(){
        dateText.setVisibility(View.VISIBLE);
        needCountCountTimer = true;
        datePicker.show(getSupportFragmentManager(),"DatePicker");
    }
    private void callTimePicker(){
        timeText.setVisibility(View.VISIBLE);
        needCountCountTimer = true;

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        builder.setTitleText("Time Picker Title");
        builder.setHour(12);
        builder.setMinute(0);

        final MaterialTimePicker timePicker = builder.build();

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChoice = LocalTime.of(timePicker.getHour(),timePicker.getMinute());
                String selectedTimeStr = timeChoice.format(DateTimeFormatter.ofPattern("kk:mm"));
                timeText.setText(selectedTimeStr);
            }
        });

        timePicker.show(getSupportFragmentManager(),"TimePicker");
    }
    private void callLocationChooser(){
        locationText.setVisibility(View.VISIBLE);
    }

    // Adapters
    private class IconAdapter extends RecyclerView.Adapter<IconAdapter.mViewHolder> {
        // Error Tag
        String TAG = "IconAdapter";

        private Context context;
        List<Integer> imageList;
        List<String> nameList;

        private class mViewHolder extends RecyclerView.ViewHolder{

            // views in a single icon
            private ImageView image;
            private TextView name;
            private View parentView;

            public mViewHolder(View view) {
                super(view);
                this.image = view.findViewById(R.id.taskIcon);
                this.parentView = view;
                this.name = view.findViewById(R.id.taskIconName);
            }
        }

        public IconAdapter(Context context, List<Integer> imageList, List<String> nameList){
            this.context = context;
            this.imageList = imageList;
            this.nameList = nameList;
        }

        @NonNull
        @Override
        public IconAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new IconAdapter.mViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.add_detailed_task_icon, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull IconAdapter.mViewHolder holder, int position) {

            // set up Views variable
            final ImageView imageView = holder.image;
            TextView name = holder.name;
            name.setText(nameList.get(position));

            imageView.setImageDrawable(getDrawable(imageList.get(position)));
            imageView.setTag(nameList.get(position));


            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ClipData.Item item = new ClipData.Item((String) v.getTag());

                    ClipData dragData = new ClipData((String) v.getTag(),new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},item);

                    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(imageView);

                    v.startDragAndDrop(dragData,myShadow,null,0);

                    dropOverlay.setVisibility(View.VISIBLE);
                    return false;
                }
            });


            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }
    private final class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            ImageView imageView = (ImageView) v;
            final int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        imageView.setColorFilter(Color.LTGRAY);
                        v.invalidate();
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    imageView.setColorFilter(Color.DKGRAY);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    imageView.setColorFilter(Color.LTGRAY);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    String dragData = (String) item.getText();

                    Toast.makeText(getApplicationContext(),"Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                    // set up events for each dragged data

                    switch (dragData){
                        case "Description":
                            break;
                        case "Date":
                            callDatePicker();
                            break;
                        case "Time":
                            callTimePicker();
                            break;
                        case "Location":
                            callLocationChooser();
                            break;
                        default:
                    }

                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.invalidate();
                    if (event.getResult()) {
                        //Toast.makeText(getApplicationContext(), "The drop was handled.", Toast.LENGTH_LONG).show();

                    } else {
                        //Toast.makeText(getApplicationContext(), "The drop didn't work.", Toast.LENGTH_LONG).show();
                    }

                    dropOverlay.setVisibility(View.GONE);
                    return true;
                default:
                    throw new RuntimeException("Invalid drag and drop Event");
            }
        }
    }
    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        private static Drawable shadow;

        public MyDragShadowBuilder(View v) {
            super(v);

            shadow = new ColorDrawable(Color.LTGRAY);
        }

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {

            int width,height;

            width = getView().getWidth() / 2;
            height = getView().getHeight() / 2;

            shadow.setBounds(0,0, width, height);

            outShadowSize.set(width, height);

            outShadowTouchPoint.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        dateText.setVisibility(View.GONE);
        timeText.setVisibility(View.GONE);
        locationText.setVisibility(View.GONE);

    }
}