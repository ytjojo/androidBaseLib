package com.kerkr.edu.Image;

import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.*;

/**
 * Some effect of images
 */
public class BitmapUtils {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;

    public static  void recycleBitmap(Bitmap b){
        if(b != null && !b.isRecycled()) b.recycle();
    }
    
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }


    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap
                .createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 水印
     *
     * @param
     * @return
     */
    public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 图片合成
     *
     * @return
     */
    public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }
        if (bitmaps.length == 1) {
            return bitmaps[0];
        }
        Bitmap newBitmap = bitmaps[0];
// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
        for (int i = 1; i < bitmaps.length; i++) {
            newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
        }
        return newBitmap;
    }

    private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second, int direction) {
        if (first == null) {
            return null;
        }
        if (second == null) {
            return first;
        }
        int fw = first.getWidth();
        int fh = first.getHeight();
        int sw = second.getWidth();
        int sh = second.getHeight();
        Bitmap newBitmap = null;
        if (direction == LEFT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, sw, 0, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == RIGHT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw, 0, null);
        } else if (direction == TOP) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, sh, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == BOTTOM) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, fh, null);
        }
        return newBitmap;
    }

    // 对分辨率较大的图片进行缩放
    public static Bitmap zoomBitmap(Bitmap bitmap, float width, float height) {

        int w = bitmap.getWidth();

        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();

        float scaleWidth = ((float) width / w);

        float scaleHeight = ((float) height / h);

        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出

        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

        return newbmp;

    }

    /**
     * 保存图片为PNG
     *
     * @param bitmap
     * @param name
     */
    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param path
     */
    public static void saveJPGE_After(Bitmap bitmap, String path) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static Bitmap revitionImageSize(String path, int width) throws IOException {

        return revitionImageSize(path, width, width);
    }

    public static Bitmap revitionImageSize(String path) throws IOException {

        return revitionImageSize(path, 1000, 1000);
    }

    public static Bitmap revitionImageSize(String path, int width, int height) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= width)
                    && (options.outHeight >> i <= height)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }
    /**
     * @param view，当前想要创建截图的View
     * @param width，设置截图的宽度
     * @param height，设置截图的高度
     * @param scroll，如果为真则从View的当前滚动位置开始绘制截图
     * @param config，Bitmap的质量，比如ARGB_8888等
     *
     * @return 截图的Bitmap
     */
    public static Bitmap capture(View view, float width, float height, boolean scroll, Bitmap.Config config) {
        if (!view.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }
     
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, config);
        bitmap.eraseColor(Color.WHITE);
     
        Canvas canvas = new Canvas(bitmap);
        int left = view.getLeft();
        int top = view.getTop();
        if (scroll) {
            left = view.getScrollX();
            top = view.getScrollY();
        }
        int status = canvas.save();
        canvas.translate(-left, -top);
     
        float scale = width / view.getWidth();
        canvas.scale(scale, scale, left, top);
     
        view.draw(canvas);
        canvas.restoreToCount(status);
     
        Paint alphaPaint = new Paint();
        alphaPaint.setColor(Color.TRANSPARENT);
     
        canvas.drawRect(0f, 0f, 1f, height, alphaPaint);
        canvas.drawRect(width - 1f, 0f, width, height, alphaPaint);
        canvas.drawRect(0f, 0f, width, 1f, alphaPaint);
        canvas.drawRect(0f, height - 1f, width, height, alphaPaint);
        canvas.setBitmap(null);
     
        return bitmap;
    }
    public static Bitmap convertViewToBitmapWithCanvas(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }
    
    /**
     * 
     * ImageView添加bitmap时候如果bitmap不设置宽高，Imageview就会填满父容器，
     * 所以ImageView已经确定大小的时候，添加Bitmap一定要调用此方法
     * 
     * @param target
     * @param TARGET_WIDTH
     * @param TARGET_HEIGHT
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap target, int TARGET_WIDTH,
            int TARGET_HEIGHT) {
        try {
            int width = target.getWidth();
            int height = target.getHeight();
            Matrix matrix = new Matrix();
            matrix.reset();
            float scaleWidth = ((float) TARGET_WIDTH) / width;
            float scaleHeight = ((float) TARGET_HEIGHT) / height;
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap result = Bitmap.createBitmap(target, 0, 0, width, height,
                    matrix, true);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 质量压缩
    private Bitmap compressImageLimitMemSize(Bitmap image,int memSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > memSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    
    
    /**
     * 
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        int width = drawable.getIntrinsicWidth();

        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height,

        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, width, height);

        drawable.draw(canvas);

        return bitmap;

    }
    /**
     * 
     * @param bm
     * @param orientationDegree
     * @return
     */

    public static Bitmap adjustPhotoRotation(Bitmap bm,
            final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
                (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(),
                Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);

        return bm1;
    }
    

    // 获得带倒影的图片方法

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {

        final int reflectionGap = 4;

        int width = bitmap.getWidth();

        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();

        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap,

        0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2),

                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);

        canvas.drawBitmap(bitmap, 0, 0, null);

        Paint deafalutPaint = new Paint();

        // Generated by Foxit PDF Creator ? Foxit Software
        //
        // http://www.foxitsoftware.com For evaluation only.

        canvas.drawRect(0, height, width, height + reflectionGap,

        deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();

        LinearGradient shader = new LinearGradient(0,

        bitmap.getHeight(), 0, bitmapWithReflection.getHeight()

        + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);

        paint.setShader(shader);

        // Set the Transfer mode to be porter duff and destination in

        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

        // Draw a rectangle using the paint with our linear gradient

        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()

        + reflectionGap, paint);

        return bitmapWithReflection;

    }
}
