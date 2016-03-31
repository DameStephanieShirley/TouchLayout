package com.example.liyang.touchlayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by liyang on 2016/3/29.
 */
public class TouchLauout extends FrameLayout {
    //  想使用 触摸的 FrameLayout 子控件必须是 MatchLayout
    private int position = -1; // init
    private float lasty;


    public TouchLauout(Context context) {
        super(context);
    }

    public TouchLauout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchLauout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //
        if (getChildCount()>0) {

        if (position == -1) {

            position = getChildCount() - 1;

            //
            for (int i = 0; i < position; i++) {
                View child = getChildAt(i);
                ViewCompat.setScaleX(child, 0);
                ViewCompat.setScaleY(child, 0);
            }

        }



        // first frameLayout should have child View
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    lasty = event.getY();

                    break;

                case MotionEvent.ACTION_MOVE:

                    // first we should check slide up or down

                    // up
                    if (lasty > event.getY()) {

                        //  The position value default is -1
                        // when it has next
                        if (position < getChildCount() - 1) {
                            View child = getChildAt(position + 1);
                            ViewCompat.setTranslationY(child, ViewCompat.getTranslationY(child) + event.getY() - lasty);
                            View bg = getChildAt(position);

                            // if scale == 1 : next
                            float scale = ViewCompat.getTranslationY(child) / child.getHeight();
                            ViewCompat.setScaleX(bg, scale);
                            ViewCompat.setScaleY(bg, scale);

                        }


                    } else {

                        // down
                        if (position > 0) {
                            View child = getChildAt(position);
                            ViewCompat.setTranslationY(child, ViewCompat.getTranslationY(child) + event.getY() - lasty);
                            View bg = getChildAt(position - 1);
                            float scale = ViewCompat.getTranslationY(child) / child.getHeight();
                            ViewCompat.setScaleX(bg, scale);
                            ViewCompat.setScaleY(bg, scale);

                        }
                    }
                    lasty = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    View child = getChildAt(position);
                    if (position > 0) {

                        if (ViewCompat.getTranslationY((child)) > child.getHeight() / 2) {

                            // turn to next Page
                            ViewCompat.animate(child).translationY(child.getHeight()).start();
                            ViewCompat.animate(getChildAt(position - 1)).scaleY(1).scaleX(1).start();

                            //  lost a page
                            position--;
                        } else {
                            // reset to start
                            ViewCompat.animate(child).translationY(0).start();
                            ViewCompat.animate(getChildAt(position - 1)).scaleY(0).scaleX(0).start();
                        }
                    }

                    if (position < getChildCount() - 1) { //  explain before has pic
                        if (ViewCompat.getScaleX(child) > 0.5f) {
                            ViewCompat.animate(child).scaleX(1).scaleY(1).start();
                            ViewCompat.animate(getChildAt(position + 1)).translationY(getChildAt(position + 1).getHeight());
                        } else {
                            ViewCompat.animate(child).scaleX(0).scaleY(0).start();
                            ViewCompat.animate(getChildAt(position + 1)).translationY(0).start();
                            position++;
                        }
                    }


                    break;


            }
            }


        return true;
    }

}
