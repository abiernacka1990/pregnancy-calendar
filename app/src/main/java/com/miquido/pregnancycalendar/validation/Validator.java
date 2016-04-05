package com.miquido.pregnancycalendar.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by agnieszka on 04.04.16.
 */
public class Validator implements IValidator {

    private List<IValidator> validatorList = new ArrayList<>();

    private Validator(IValidator... validators) {
        if (validators.length > 0) {
            if (validators.length == 1 && validators[0] == null) {
                return;
            }
            validatorList.addAll(Arrays.asList(validators));
        }
    }

    public boolean add(IValidator... validators) {
        if (validators.length > 0) {
            if (validators.length == 1 && validators[0] == null) {
                return false;
            }

            return validatorList.addAll(Arrays.asList(validators));
        } else {
            return false;
        }
    }

    public boolean validate() {

        boolean isValid = true;

        for (IValidator iValidator : validatorList) {
            boolean result = iValidator.validate();
            isValid = result && isValid;
        }

        return isValid;
    }

    public static Validator create(IValidator... validators) {
        return new Validator(validators);
    }
}
