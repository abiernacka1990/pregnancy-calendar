package biernacka.pregnancycalendar.ui.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.InputStream;

import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.model.DiaryEntry;

/**
 * Created by agnieszka on 14.04.16.
 */
public final class ImageHelper {

    @Nullable
    public static Bitmap getBitmap(DiaryEntry item, Context context) {
        Bitmap image = null;
        try {
            image = getImageBitmap(item, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private static Bitmap getImageBitmap(DiaryEntry item, Context context) throws Exception {
        int smallerImageDimension = (int) context.getResources().getDimension(R.dimen.diary_image_smaller_size);
        int width, height;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        Bitmap bitmap = BitmapFactory.decodeFile(item.getImagePath(), options);
        int originalBitmapWidth = bitmap.getWidth();
        int originalBitmapHeight = bitmap.getHeight();
        float aspectRatio =  originalBitmapWidth / (float) originalBitmapHeight;
        if (originalBitmapHeight > originalBitmapWidth) {
            width = smallerImageDimension;
            height = Math.round(width / aspectRatio);
        } else {
            height = smallerImageDimension;
            width = Math.round(height * aspectRatio);
        }
        return ImageHelper.decodeSampledBitmapFromFile(item.getImagePath(), width, height);

    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
