package stormhack21.memotask.model;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DetailedTaskManager {
    // Debug
    static String TAG = "DetailedTaskManager";

    // Variables
    List<Task> tasks = new ArrayList<>();

    // Singleton
    private DetailedTaskManager(){
    }
    private static DetailedTaskManager instance;
    public static DetailedTaskManager getInstance(){
        if(instance == null){
            instance = new DetailedTaskManager();
            Log.e(TAG,"created new task manager");
        }
        return instance;
    }

    public void addTask(Task task){
        tasks.add(task);
    }


    // Debug
    public void injectDebug(){
        SingleTask task1 = new SingleTask("Single-1","this is a single task, it is 1 day and 2 minutes from now", LocalDate.now().plusDays(1), LocalTime.now().plusMinutes(2),"location","no-image.img");
        tasks.add(task1);
        SingleTask task2 = new SingleTask("Single-2","this is a single task, it is 2 hours and 2 minutes from now", LocalDate.now(), LocalTime.now().plusMinutes(2).plusHours(2),"location","no-image.img");
        tasks.add(task2);
        SingleTask task3 = new SingleTask("Single-3","this is a single task, its 2 minutes from now", LocalDate.now(), LocalTime.now().plusMinutes(2),"location","no-image.img");
        tasks.add(task3);
        Log.e(TAG,"added debug tasks");
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
