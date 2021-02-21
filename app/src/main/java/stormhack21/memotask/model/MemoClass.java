package stormhack21.memotask.model;

import java.io.File;

public class MemoClass
{
    private String description;
    private String path;

    public MemoClass(String description, String path) {
        this.description = description;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean empty()
    {
        if(description == null && path == null)
            return true;
        else
            return false;

    }
}
