package stormhack21.memotask.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MultiTask extends Task{

    // Common fields with SingleTask
    private LocalDate date; // can be null
    private LocalTime time; // can be null

    // Distinct Fields
    private List<String> descriptions = new ArrayList<>();
    private String image; // can be null

    private double progress = 0;

    // Constructors
    public MultiTask(String title, List<String> descriptions, LocalDate date, LocalTime time, String image) {
        super(title);
        this.descriptions = descriptions;
        this.date = date;
        this.time = time;
        this.image = image;
    }

    // Check state functions
    public boolean hasDate(){
        return date != null;
    }
    public boolean hasTime(){
        return time != null;
    }
    public boolean hasImage(){
        return image != null;
    }
    public int getDescriptionsCount(){
        return descriptions.size();
    }
    public boolean isFinished(){
        return (progress >= 1);
    }

    // Getter Setters
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void addProgress(){

    }
    public void setDescriptions(List<String> descriptions){
        this.descriptions = descriptions;
    }
    public List<String> getDescriptions(){
        return descriptions;
    }


}
