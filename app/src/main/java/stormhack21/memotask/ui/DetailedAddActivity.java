package stormhack21.memotask.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import stormhack21.memotask.R;

public class DetailedAddActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageView imageView2;
    private ViewGroup root;
    private static final String IMAGEVIEW_TAG = "icon1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_add);

        root = findViewById(R.id.detailAddRoot);

        imageView = (ImageView) findViewById(R.id.img);
        imageView.setTag(IMAGEVIEW_TAG);

        imageView2 = (ImageView) findViewById(R.id.img2);
        imageView2.setTag("test2");
        imageView2.setOnDragListener(new MyDragListener());


        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ClipData.Item item = new ClipData.Item((String) v.getTag());

                ClipData dragData = new ClipData((String) v.getTag(),new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},item);

                View.DragShadowBuilder myShadow = new MyDragShadowBuilder(imageView);

                v.startDragAndDrop(dragData,myShadow,this,0);

                return false;
            }
        });

        imageView.setOnDragListener(new MyDragListener());
    }

    private final class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            ImageView imageView = (ImageView) v;
            final int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        imageView.setColorFilter(Color.BLUE);
                        v.invalidate();
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    imageView.setColorFilter(Color.GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    imageView.setColorFilter(Color.BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    String dragData = (String) item.getText();

                    Toast.makeText(getApplicationContext(),"Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                    ((ImageView)v).setColorFilter(Color.RED);


                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:

                    v.invalidate();

                    if (event.getResult()) {
                        Toast.makeText(getApplicationContext(), "The drop was handled.", Toast.LENGTH_LONG).show();

                    } else {
                        //Toast.makeText(getApplicationContext(), "The drop didn't work.", Toast.LENGTH_LONG).show();
                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    }
    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        private static Drawable shadow;

        public MyDragShadowBuilder(View v) {
            super(v);

            shadow = new ColorDrawable(Color.LTGRAY);
        }

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {

            int width,height;

            width = getView().getWidth() / 2;
            height = getView().getHeight() / 2;

            shadow.setBounds(0,0, width, height);

            outShadowSize.set(width, height);

            outShadowTouchPoint.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }

}