package me.didi.api.ess.utils;

import me.didi.api.ess.exceptions.BadRequestBodyException;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class VerifyError {
    private VerifyError() {
        throw new IllegalStateException("Utility class");
    }

    public static synchronized void verifyBodyError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestBodyException(
                    bindingResult.getFieldErrors().stream()
                            .map(e ->
                                    "field:[" + e.getField() +
                                            "] message:[" + e.getDefaultMessage()+"]")
                            .collect(Collectors.joining(" || ")));
        }
    }
}
