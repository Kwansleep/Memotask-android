package stormhack21.memotask.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MemoManager implements Iterable<MemoClass> {
    private List<MemoClass> memos = new ArrayList<>();
    private static MemoManager instance;
    public static MemoManager getInstance()
    {
        if(instance == null)
        {
            instance = new MemoManager();
        }
        return instance;
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
