package com.example.myfirstandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ProfileImageView extends ImageView
{
    private Path mPath = new Path();
    
    public interface LockScreenLayoutListener
    {
            public void onUnLock();
    }
    
public ProfileImageView(Context context) {
    this(context, null);
}

public ProfileImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
}

public ProfileImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
}

@Override
protected void onDraw(Canvas canvas)
{
        float cx = getMeasuredWidth() / 2;
        float cy = getMeasuredHeight() / 2;
        float cr = cx < cy ? cx : cy;

    mPath.reset();
    mPath.addCircle(cx, cy, cr, Path.Direction.CCW);        
    canvas.clipPath(mPath);
        super.onDraw(canvas);
        
        Paint paint = new Paint();
        paint.setStrokeWidth(cr/5);
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.WHITE);
        
        int r = getWidth()/2;
        canvas.drawCircle(cx, cy, r, paint);
        paint.setAlpha((int) (255*0.71));
        canvas.drawCircle(cx, cy, r+1, paint);
        canvas.drawCircle(cx, cy, cr, paint);
        
        paint.setStrokeWidth(15);
        paint.setColor(Color.GRAY);
        paint.setAlpha((int) (255*0.51));
        canvas.drawCircle(cx, cy, r+3, paint);
        
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
        paint.setColor(Color.RED);
        canvas.drawCircle(cx, cy, r+4, paint);
            
}
}
