package stormhack21.memotask.model;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Task {
    // Fields
    private String title;
    private int TaskID;

    static private int id = 0;

    public Task(String title){
        this.title = title;
        this.TaskID = id++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTaskID(){
        return TaskID;
    }
}
