package stormhack21.memotask.model;

public class MemoImage extends Memo {

    String image; // Stores the path to the image file

    // constructor
    public MemoImage(String description, String image) {
        super(description);
        this.image = image;
    }
    public MemoImage(Memo memo,String image){
        super(memo.getDescription());
        this.image = image;
    }

    //Getter and setter
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
