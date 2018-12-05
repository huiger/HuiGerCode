package com.huige.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/9 0009 下午 04:11.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 生成二维码
 * </pre>
 */
public class QRCodeUtils {

    private static QRCodeUtils mQRCodeUtils = null;

    public static QRCodeUtils getInstance() {
        if (mQRCodeUtils == null) {
            synchronized (QRCodeUtils.class) {
                if (mQRCodeUtils == null) {
                    mQRCodeUtils = new QRCodeUtils();
                }
            }
        }
        return mQRCodeUtils;
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param width   宽
     * @param height  高
     * @return 二维码
     */
    public Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 中间添加logo
     *
     * @param ctx      上下文
     * @param qrBitmap 二维码
     * @param logoId   logoId
     * @return 二维码
     */
    public Bitmap addLogo(Bitmap qrBitmap, Context ctx, @DrawableRes int logoId) {
        return addLogo(qrBitmap, BitmapFactory.decodeResource(ctx.getResources(), logoId));
    }


    /**
     * 中间添加logo
     *
     * @param qrBitmap   二维码
     * @param logoBitmap logo
     * @return 二维码
     */
    public Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }
}
