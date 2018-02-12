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
import com.wire.bots.cryptonite.client.CryptoClient;
import com.wire.bots.cryptonite.client.StorageClient;
import com.wire.bots.github.resource.GitHubResource;
import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.Server;
import com.wire.bots.sdk.factories.CryptoFactory;
import com.wire.bots.sdk.factories.StorageFactory;
import io.dropwizard.setup.Environment;

import java.net.URI;

public class Service extends Server<Config> {

    private static final String SERVICE = "github";
    private StorageClient storageClient;
    private CryptoClient cryptoClient;

    public static void main(String[] args) throws Exception {
        new Service().run(args);
    }

    @Override
    protected void initialize(Config config, Environment env) throws Exception {
        storageClient = new StorageClient(SERVICE, new URI(config.data));
        cryptoClient = new CryptoClient(SERVICE, new URI(config.data));
    }

    @Override
    protected MessageHandlerBase createHandler(Config config, Environment env) {
        return new MessageHandler(getStorageFactory(config));
    }

    @Override
    protected void onRun(Config config, Environment env) {
        StorageFactory storageFactory = getStorageFactory(config);
        Validator validator = new Validator(storageFactory);
        addResource(new GitHubResource(repo, validator), env);
    }

    @Override
    protected StorageFactory getStorageFactory(Config config) {
        return botId -> new StorageService(botId, storageClient);
    }

    @Override
    protected CryptoFactory getCryptoFactory(Config config) {
        return (botId) -> new CryptoService(botId, cryptoClient);
    }
}
