package com.celeMC.tonymodbus.app.Activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.celeMC.tonymodbus.app.R;

/**
 * Created by celestinbasura on 23/07/14.
 */
public class MyTextView extends TextView {


    private Paint marginPaint;
    private Paint linePaint;
    private int paperColor;
    private float margin;

    private void init(){
        //Get the references to our resoruce table

        Resources myResources = getResources();

        //Creating paint brushes

        marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            marginPaint.setColor(myResources.getColor(R.color.notepad_margin));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setColor(myResources.getColor(R.color.notepad_lines));

        //Get the paper backround color and margin

        paperColor = myResources.getColor(R.color.notepad_paper);
            margin = myResources.getDimension(R.dimen.notepad_margin);

    }



    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyTextView (Context context){
        super(context);


    }

    public MyTextView (Context context, AttributeSet attrs){

        super(context, attrs);
    }


    @Override
    public void onDraw (Canvas canvas){


        //Color as the paper
        canvas.drawColor(paperColor);

        //Draw lines

        canvas.drawLine(0, 0, 0 , getMeasuredHeight(), linePaint);
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);

        //Draw margin

        canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);

        //Moving the text from the margin

        canvas.save();
        canvas.translate(margin, 0);
        //Under the text
        super.onDraw(canvas);

        canvas.restore();

        //Over the text

    }


}
