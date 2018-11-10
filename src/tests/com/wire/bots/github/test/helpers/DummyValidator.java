package com.wire.bots.github.test.helpers;

import com.wire.bots.github.HmacValidator;

public class DummyValidator extends HmacValidator {
    @Override
    public boolean isValid(String botId, String signature, String payload) {
        return true;
    }
}
