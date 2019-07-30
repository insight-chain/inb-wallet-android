package io.insightchain.inbwallet.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;

/**
 * Created by lijilong on 05/07.
 */

public class ZxUtils {
    public static Bitmap createQrCode(int wdp, int hdp, String msg) {
        Bitmap bitmap = null;
        int width = (int) (ScreenUtils.density * wdp);
        int height = (int) (ScreenUtils.density * hdp);
        String content = msg;
        //定义二维码参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width*height];
            for(int y =0;y<height;y++)
                for(int x=0; x<width; x++){
                    if(bitMatrix.get(x,y)){
                        pixels[y*width + x] = 0xff000000;
                    }
                }
            bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels,0,width,0,0,width,height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
