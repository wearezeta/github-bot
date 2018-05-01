package com.wire.bots.github.test.helpers;

import com.wire.bots.github.Validator;

public class DummyValidator extends Validator{
    @Override
    public boolean isValid(String botId, String signature, String payload) throws Exception {
        return true;
    }
}
