package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.healthcoco.healthcocopad.enums.ColorType;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.views.TouchImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * it will create the signature from the gestureOverlayView
 *
 * @author ashish_sharma
 */
public class SignatureView extends FrameLayout {
    private final String TAG = SignatureView.class.getSimpleName();
    private byte[] mSignatureArray;
    private Bitmap mSignatureBitMap;
    private TextView mTextView;
    private SignatureLayout mSignature;
    private TouchImageView imageView;

    private int selectedColorId = ColorType.GREEN.getColorId();
    private float STROKE_WIDTH = 5f;
    private Path path = new Path();
    private RectF dirtyRect = new RectF();
    private Paint selectedPaint = new Paint();
    private float lastTouchX;
    private float lastTouchY;

    /**
     * it will create get the signature from gestureOverlayView
     *
     * @param context
     */
    public SignatureView(Context context) {
        this(context, null);

    }

    /**
     * it will create get the signature from gestureOverlayView
     *
     * @param context
     * @param attributeSet
     */
    public SignatureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /**
     * it will create get the signature from gestureOverlayView
     *
     * @param context
     * @param attributeSet
     * @param id
     */
    public SignatureView(Context context, AttributeSet attributeSet, int id) {
        super(context, attributeSet, id);
        setBackgroundColor(Color.WHITE);
        imageView = new TouchImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(imageView);
        addView(getSignatureView(context));

    }

    private View getSignatureView(Context context) {
        mSignature = new SignatureLayout(context, null);
        mSignature.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mSignature.setBackgroundColor(Color.TRANSPARENT);
        return mSignature;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mSignature.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void setName(String name) {
        mTextView.setText(name);
    }

    /**
     * it captures the image of the drawn signature from screen.
     */
    public byte[] captureSignature() {
        setDrawingCacheEnabled(true);
        mSignatureBitMap = Bitmap.createBitmap(getDrawingCache());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mSignatureBitMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mSignatureArray = stream.toByteArray();
        setDrawingCacheEnabled(false);
        return mSignatureArray;
    }

    /**
     * this will save image to specified location.
     *
     * @param path
     * @throws IOException
     */
    public void writeImageToFile(String path) throws IOException {
        FileOutputStream mFileOutStream = new FileOutputStream(path);
        mSignatureBitMap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
        mFileOutStream.flush();
        mFileOutStream.close();
    }

    /**
     * call captureSignature() before getting signature array
     *
     * @return signatureByteArray
     */
    public byte[] getSignatureArray() {
        return mSignatureArray;

    }

    /**
     * call captureSignature() before getting signature Bitmap
     *
     * @return signatureByteArray
     */
    public Bitmap getSignatureBitmap() {
        return mSignatureBitMap;
    }

    /**
     * clears screen.
     */
    public void clear() {
        mSignature.clear();
    }

    public void setBitmapToImageView(Bitmap bitmap) {
        if (imageView != null)
            imageView.setImageBitmap(bitmap);
    }

    public void setSelectedColor(int colorId) {
        this.selectedColorId = colorId;
        initPathAndRectangle();
    }

    private void initPathAndRectangle() {
        lastTouchX = 0;
        lastTouchY = 0;
//        path = new Path();
//        dirtyRect = new RectF();
        selectedPaint = new Paint();
        selectedPaint.setAntiAlias(true);
        selectedPaint.setColor(getContext().getResources().getColor(selectedColorId));
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeJoin(Paint.Join.ROUND);
        selectedPaint.setStrokeWidth(STROKE_WIDTH);
    }

    public class SignatureLayout extends LinearLayout {
        private float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        public SignatureLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            initPathAndRectangle();
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, selectedPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    LogUtils.LOGD(TAG, "Event MotionEvent.ACTION_DOWN");
                    path.moveTo(eventX, eventY);
                    path.lineTo(eventX + 2, eventY + 2);
                    resetDirtyRect(eventX, eventY);
                    expandDirtyRect(eventX + 2, eventY + 2);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    LogUtils.LOGD(TAG, "Event MotionEvent.ACTION_MOVE");
                case MotionEvent.ACTION_UP:
                    LogUtils.LOGD(TAG, "Event MotionEvent.ACTION_UP");
                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return true;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH), (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH), (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

}
