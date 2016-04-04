package validation.fields;

import android.content.Context;
import android.widget.EditText;

import validation.IValidator;
import validation.exception.ValidationException;

/**
 * Created by agnieszka on 04.04.16.
 */
public abstract class BaseEditTextValidator implements IValidator {

    private EditText editText;

    public BaseEditTextValidator(EditText editText) {

        if (editText == null) {
            throw new IllegalArgumentException("EditText object cannot be null");
        }

        this.editText = editText;
    }

    protected EditText getEditText() {
        return editText;
    }

    @Override
    public final boolean validate() {
        boolean isValid = true;

        resetOldError();

        try {
            validMethod();
        } catch (ValidationException e) {
            editText.setError(e.getMessage());
            isValid = false;
        }

        return isValid;
    }

    private void resetOldError() {
        editText.setError(null);
    }

    public abstract void validMethod() throws ValidationException;

}
