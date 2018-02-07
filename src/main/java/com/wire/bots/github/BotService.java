//
// Wire
// Copyright (C) 2016 Wire Swiss GmbH
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see http://www.gnu.org/licenses/.
//

package com.wire.bots.github;

import com.wire.bots.cryptonite.CryptoService;
import com.wire.bots.cryptonite.StorageService;
import com.wire.bots.github.resource.GitHubResource;
import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.Server;
import com.wire.bots.sdk.factories.CryptoFactory;
import com.wire.bots.sdk.factories.StorageFactory;
import io.dropwizard.setup.Environment;

import java.net.URI;

public class BotService extends Server<BotConfig> {

    private static final String SERVICE = "github";

    public static void main(String[] args) throws Exception {
        new BotService().run(args);
    }

    @Override
    protected MessageHandlerBase createHandler(BotConfig config, Environment env) {
        return new MessageHandler(getStorageFactory(config));
    }

    @Override
    protected void onRun(BotConfig botConfig, Environment env) {
        Validator validator = new Validator(config.data);
        addResource(new GitHubResource(repo, validator), env);
    }

    @Override
    protected StorageFactory getStorageFactory(BotConfig config) {
        return botId -> new StorageService(SERVICE, botId, new URI(config.data));
    }

    @Override
    protected CryptoFactory getCryptoFactory(BotConfig config) {
        return (botId) -> new CryptoService(SERVICE, botId, new URI(config.data));
    }
}
