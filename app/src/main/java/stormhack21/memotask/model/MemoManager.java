package stormhack21.memotask.model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MemoManager implements Iterable<MemoClass> {

    private List<MemoClass> memos = new ArrayList<>();
    private static MemoManager instance;

    private Bitmap stagingBitMap;
    private String stagingDesc;

    // Singleton
    public static MemoManager getInstance()
    {
        if(instance == null)
        {
            instance = new MemoManager();
        }
        return instance;
    }

    // Staging inputs, since the view are destroyed each time we switch tabs, we need to find a way to save intermediate state.
    public void stage(Bitmap image){
        Log.e("Memo","saved bitmap");
        this.stagingBitMap = image.copy(image.getConfig(),true);
    }
    public void stage(String description){
        Log.e("Memo","saved desc");
        this.stagingDesc = description;
    }
    public String getStageDesc(){
        Log.e("Memo","got desc");
        return stagingDesc;
    }
    public Bitmap getStagingBitMap(){
        return stagingBitMap;
    }
    public void saveStagedBitmap(String fileName) {
        Log.e("Memo","used bitmap:" + fileName);
        if (stagingBitMap == null){
            Log.e("Memo","used bitmap, but its null");
            return;
        }
        try
        {
            Bitmap tempBit = stagingBitMap;
            tempBit.compress(Bitmap.CompressFormat.PNG, 90, new FileOutputStream(fileName));
            Log.e("Memo","used bitmap, success");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    public void clearStage(){
        stagingBitMap = null;
        stagingDesc = null;
    }

    public int size()
    {
        return memos.size();
    }

    public List<MemoClass> getMemos() {
        return memos;
    }

    public void setMemos(List<MemoClass> memos) {
        this.memos = memos;
    }

    public void add(MemoClass memo)
    {
        memos.add(memo);
    }

    public void remove(int position)
    {
        memos.remove(position);
    }
    @NonNull
    @Override
    public Iterator<MemoClass> iterator() {
        return memos.iterator();
    }

}
