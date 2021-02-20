package stormhack21.memotask.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class SingleTask extends Task {

    // Common Fields with Multitask
    private LocalDate date; // can be null
    private LocalTime time; // can be null

    // Distinct Fields
    private String description;
    private String location; // can be null
    private String image; // can be null

    private boolean finished = false;

    // Constructors
    public SingleTask(String title, String description, LocalDate date, LocalTime time, String location, String image){
        super(title);
        if(description == null){
            this.description = "";
        } else {
            this.description = description;
        }
        this.date = date;
        this.time = time;
        this.location = location;
        this.image = image;
    }

    // Check null
    public boolean hasDate(){
        return date != null;
    }
    public boolean hasTime(){
        return time != null;
    }
    public boolean hasLocation(){
        return location != null;
    }
    public boolean hasImage(){
        return image != null;
    }

    public boolean hasDescription(){
        return description.isEmpty();
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
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
