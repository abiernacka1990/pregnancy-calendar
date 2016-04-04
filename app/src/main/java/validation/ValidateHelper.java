package validation;

import android.content.Context;

import com.miquido.pregnancycalendar.R;

import validation.exception.ValidationException;

/**
 * Created by agnieszka on 04.04.16.
 */
public final class ValidateHelper {

    public static void fieldNotEmpty(String text, Context context) throws ValidationException {
        if (text == null || text.isEmpty()) {
            throw new ValidationException(context.getString(R.string.validate_empty_field));
        }
    }
}
