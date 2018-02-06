package com.example.del_m.drivesocial;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

/**
 * Created by del_m on 19/01/2018.
 */

public class Typewriter extends android.support.v7.widget.AppCompatTextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 70;


    public Typewriter(Context context){
        super (context);
    }

    public Typewriter(Context context, AttributeSet attrs){
        super (context, attrs);
    }

    private Handler mHandler = new Handler();

    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0,mIndex++));
            if (mIndex <= mText.length()){
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text)  {

        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis){
        mDelay = millis;
    }

}
