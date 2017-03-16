package com.healthcoco.healthcocopad.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Created by Shreshtha on 04-02-2017.
 */
public class BitmapUtil {
    private static final boolean DEBUG = false;
    private static final String TAG = BitmapUtil.class.getSimpleName();

    private BitmapUtil() {
    }

    /**
     * Decode an image into a Bitmap, using sub-sampling if the hinted dimensions call for it.
     * Does not crop to fit the hinted dimensions.
     *
     * @param src an encoded image
     * @param w   hint width in px
     * @param h   hint height in px
     * @return a decoded Bitmap that is not exactly sized to the hinted dimensions.
     */
    public static Bitmap decodeByteArray(byte[] src, int w, int h) {
        try {
            // calculate sample size based on w/h
            final BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(src, 0, src.length, opts);
            if (opts.mCancel || opts.outWidth == -1 || opts.outHeight == -1) {
                return null;
            }
            opts.inSampleSize = Math.min(opts.outWidth / w, opts.outHeight / h);
            opts.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(src, 0, src.length, opts);
        } catch (Throwable t) {
            LogUtils.LOGI(TAG, t + "unable to decode image");
            return null;
        }
    }

    /**
     * Decode an image into a Bitmap, using sub-sampling if the desired dimensions call for it.
     * Also applies a center-crop a la {@link android.widget.ImageView.ScaleType#CENTER_CROP}.
     *
     * @param src an encoded image
     * @param w   desired width in px
     * @param h   desired height in px
     * @return an exactly-sized decoded Bitmap that is center-cropped.
     */
    public static Bitmap decodeByteArrayWithCenterCrop(byte[] src, int w, int h) {
        try {
            final Bitmap decoded = decodeByteArray(src, w, h);
            return centerCrop(decoded, w, h);
        } catch (Throwable t) {
            LogUtils.LOGI(TAG, t + "unable to crop image");
            return null;
        }
    }

    /**
     * Returns a new Bitmap copy with a center-crop effect a la
     * {@link android.widget.ImageView.ScaleType#CENTER_CROP}. May return the input bitmap if no
     * scaling is necessary.
     *
     * @param src original bitmap of any size
     * @param w   desired width in px
     * @param h   desired height in px
     * @return a copy of src conforming to the given width and height, or src itself if it already
     * matches the given width and height
     */
    public static Bitmap centerCrop(final Bitmap src, final int w, final int h) {
        return crop(src, w, h, 0.5f, 0.5f);
    }

    /**
     * Returns a new Bitmap copy with a crop effect depending on the crop anchor given. 0.5f is like
     * {@link android.widget.ImageView.ScaleType#CENTER_CROP}. The crop anchor will be be nudged
     * so the entire cropped bitmap will fit inside the src. May return the input bitmap if no
     * scaling is necessary.
     * <p/>
     * <p/>
     * Example of changing verticalCenterPercent:
     * _________            _________
     * |         |          |         |
     * |         |          |_________|
     * |         |          |         |/___0.3f
     * |---------|          |_________|\
     * |         |<---0.5f  |         |
     * |---------|          |         |
     * |         |          |         |
     * |         |          |         |
     * |_________|          |_________|
     *
     * @param src                     original bitmap of any size
     * @param w                       desired width in px
     * @param h                       desired height in px
     * @param horizontalCenterPercent determines which part of the src to crop from. Range from 0
     *                                .0f to 1.0f. The value determines which part of the src
     *                                maps to the horizontal center of the resulting bitmap.
     * @param verticalCenterPercent   determines which part of the src to crop from. Range from 0
     *                                .0f to 1.0f. The value determines which part of the src maps
     *                                to the vertical center of the resulting bitmap.
     * @return a copy of src conforming to the given width and height, or src itself if it already
     * matches the given width and height
     */
    public static Bitmap crop(final Bitmap src, final int w, final int h,
                              final float horizontalCenterPercent, final float verticalCenterPercent) {
        if (horizontalCenterPercent < 0 || horizontalCenterPercent > 1 || verticalCenterPercent < 0
                || verticalCenterPercent > 1) {
            throw new IllegalArgumentException(
                    "horizontalCenterPercent and verticalCenterPercent must be between 0.0f and "
                            + "1.0f, inclusive.");
        }
        final int srcWidth = src.getWidth();
        final int srcHeight = src.getHeight();
        // exit early if no resize/crop needed
        if (w == srcWidth && h == srcHeight) {
            return src;
        }
        final Matrix m = new Matrix();
        final float scale = Math.max(
                (float) w / srcWidth,
                (float) h / srcHeight);
        m.setScale(scale, scale);
        final int srcCroppedW, srcCroppedH;
        int srcX, srcY;
        srcCroppedW = Math.round(w / scale);
        srcCroppedH = Math.round(h / scale);
        srcX = (int) (srcWidth * horizontalCenterPercent - srcCroppedW / 2);
        srcY = (int) (srcHeight * verticalCenterPercent - srcCroppedH / 2);
        // Nudge srcX and srcY to be within the bounds of src
        srcX = Math.max(Math.min(srcX, srcWidth - srcCroppedW), 0);
        srcY = Math.max(Math.min(srcY, srcHeight - srcCroppedH), 0);
        final Bitmap cropped = Bitmap.createBitmap(src, srcX, srcY, srcCroppedW, srcCroppedH, m,
                true /* filter */);
        if (DEBUG) LogUtils.LOGD(TAG,
                "IN centerCrop, srcW/H=%s/%s desiredW/H=%s/%s srcX/Y=%s/%s" +
                        " innerW/H=%s/%s scale=%s resultW/H=%s/%s",
                srcWidth, srcHeight, w, h, srcX, srcY, srcCroppedW, srcCroppedH, scale,
                cropped.getWidth(), cropped.getHeight());
        if (DEBUG && (w != cropped.getWidth() || h != cropped.getHeight())) {
            LogUtils.LOGE(TAG, new Error(), "last center crop violated assumptions.");
        }
        return cropped;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        try {
            int sourceWidth = Util.getValidatedWidth(source.getWidth());
            int sourceHeight = Util.getValidatedHeight(source.getHeight());

            newWidth = Util.getValidatedWidth(newWidth);
            newHeight = Util.getValidatedHeight(newHeight);
            // Compute the scaling factors to fit the new height and width, respectively.
            // To cover the final image, the final scaling will be the bigger
            // of these two.
            float xScale = (float) newWidth / sourceWidth;
            float yScale = (float) newHeight / sourceHeight;
            float scale = Math.max(xScale, yScale);

            // Now get the size of the source bitmap when scaled
            float scaledWidth = scale * sourceWidth;
            float scaledHeight = scale * sourceHeight;

            // Let's find out the upper left coordinates if the scaled bitmap
            // should be centered in the new size give by the parameters
            float left = (newWidth - scaledWidth) / 2;
            float top = (newHeight - scaledHeight) / 2;

            // The target rectangle for the new, scaled version of the source bitmap will now
            // be
            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

            // Finally, we create a new bitmap of the specified size and draw our new,
            // scaled bitmap onto it.
            Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
            Canvas canvas = new Canvas(dest);
            canvas.drawBitmap(source, null, targetRect, null);

            return dest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }
}
