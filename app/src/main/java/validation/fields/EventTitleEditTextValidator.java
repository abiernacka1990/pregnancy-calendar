package validation.fields;

import android.widget.EditText;

import validation.ValidateHelper;
import validation.exception.ValidationException;

/**
 * Created by agnieszka on 04.04.16.
 */
public class EventTitleEditTextValidator extends BaseEditTextValidator {

    public EventTitleEditTextValidator(EditText editText) {
        super(editText);
    }

    @Override
    public void validMethod() throws ValidationException {
        ValidateHelper.fieldNotEmpty(getEditText().getText().toString(), getEditText().getContext());
    }
}
