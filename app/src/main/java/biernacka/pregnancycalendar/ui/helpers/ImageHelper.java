package biernacka.pregnancycalendar.ui.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by agnieszka on 14.04.16.
 */
public final class ImageHelper {

    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
