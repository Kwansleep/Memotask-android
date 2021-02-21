package stormhack21.memotask.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import stormhack21.memotask.model.MemoManager;
import stormhack21.memotask.model.finger;

public class paintView extends View {

    public static int SIZEOFPEN = 12;
    public static final int DEFAULT_COLOR1 = Color.BLACK;
    public static final int DEFAULT_COLOR2 = Color.RED;
    public static final int DEFAULT_COLOR3 = Color.GREEN;
    public static final int DEFAULT_COLOR4 = Color.BLUE;
    public static final int DEFAULT_COLOR5 = Color.YELLOW;
    public static final int DEFAULT_BACKGROUND = Color.WHITE;

    private static final float TOUCH_TOLERANCE = 4;
    private float mX;
    private float mY;
    private Path path;
    private Paint paint;
    private int strokeWidth;
    private boolean emboss;
    private int currentColor;
    private int backgroundColor = DEFAULT_BACKGROUND;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap bitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private ArrayList<finger> paths = new ArrayList<>();


    public paintView(Context context) {
        super(context);
    }

    public paintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(DEFAULT_COLOR1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(null);
        paint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }

    public void init(DisplayMetrics metrics)
    {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);

        currentColor = DEFAULT_COLOR1;
        strokeWidth = SIZEOFPEN;
    }

    public void normal()
    {
        emboss = false;
        blur = false;
    }

    public void setEmboss()
    {
        emboss = true;
        blur = false;
    }

    public void setBlur()
    {
        emboss = false;
        blur = true;
    }

    public void clear()
    {
        backgroundColor = DEFAULT_BACKGROUND;
        paths.clear();
        normal();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);
        for(finger fg: paths)
        {
            paint.setColor(fg.getColor());
            paint.setStrokeWidth(fg.getStrokeWidth());
            paint.setMaskFilter(null);

            if(fg.isEmboss())
                paint.setMaskFilter(mEmboss);
            else if(fg.isBlur())
                paint.setMaskFilter(mBlur);

            mCanvas.drawPath(fg.getPath(), paint);
        }
        canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        canvas.restore();

    }

    private void touchStart(float x, float y)
    {
        path = new Path();
        finger fg = new finger(currentColor,  emboss, blur, strokeWidth, path);
        paths.add(fg);

        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y)
    {
        float disX = Math.abs(x - mX);
        float disY = Math.abs(y - mY);

        if(disX >= TOUCH_TOLERANCE || disY >= TOUCH_TOLERANCE)
        {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp()
    {
        path.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        MemoManager.getInstance().stage(bitmap);
        return true;
    }

    public void saveBitmap(File file)
    {
        try
        {
            Bitmap tempBit = bitmap;
            tempBit.compress(Bitmap.CompressFormat.PNG, 90, new FileOutputStream(file));
            Log.e("Memo","normal bitmap:" + file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public Boolean checkEmpty()
    {
        if(paths.size() == 0)
            return true;
        else
            return false;

    }

    public Bitmap getBitmap(){
        return bitmap;
    }


}

