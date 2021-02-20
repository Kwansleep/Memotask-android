package stormhack21.memotask.model;

public abstract class Memo extends Task {

    private static int memoId = 1;
    private String description;

    // Constructor
    public Memo(String description) {
        super("Memo-" + memoId++ +" Created on ");
        this.description = description;
    }

    // Check empty
    public boolean hasDescription(){
        return description.isEmpty();
    }

    // Getter and setter
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
