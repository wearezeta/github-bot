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

import com.wire.bots.github.utils.SessionIdentifierGenerator;
import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.factories.StorageFactory;
import com.wire.bots.sdk.models.TextMessage;
import com.wire.bots.sdk.server.model.NewBot;
import com.wire.bots.sdk.server.model.User;
import com.wire.bots.sdk.tools.Logger;
import com.wire.bots.sdk.tools.Util;

import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;

public class MessageHandler extends MessageHandlerBase {
    private final SessionIdentifierGenerator sesGen = new SessionIdentifierGenerator();
    private final StorageFactory storageFactory;

    MessageHandler(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    @Override
    public boolean onNewBot(NewBot newBot) {
        Logger.info(String.format("onNewBot: bot: %s, user: %s",
                newBot.id,
                newBot.origin.id));
        return true;
    }

    @Override
    public void onNewConversation(WireClient client) {
        try {
            String secret = sesGen.next(6);
            getDatabase(client.getId()).insertSecret(secret);

            String origin = getOwner(client).id;

            String help = formatHelp(client);
            client.sendDirectText(help, origin);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(e.getMessage());
        }
    }

    @Override
    public void onText(WireClient client, TextMessage msg) {
        try {
            if (msg.getText().equalsIgnoreCase("/help")) {
                String help = formatHelp(client);

                client.sendDirectText(help, msg.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(e.getLocalizedMessage());
        }
    }

    private String formatHelp(WireClient client) throws Exception {
        String botId = client.getId();
        String host = getHost();
        String secret = getDatabase(botId).getSecret();
        String name = client.getConversation().name;
        String convName = name != null ? URLEncoder.encode(name, "UTF-8") : "";
        User owner = getOwner(client);
        String handle = owner.handle;

        String url = String.format("https://%s/%s#conv=%s,owner=@%s", host, botId, convName, handle);
        return formatHelp(url, secret);
    }

    private Database getDatabase(String botId) {
        return new Database(botId, Service.config.getPostgres());
    }

    private String formatHelp(String url, String secret) {
        return String.format("Hi, I'm GitHub bot. Here is how to set me up:\n\n"
                        + "1. Go to the repository that you would like to connect to\n"
                        + "2. Go to **Settings / Webhooks / Add webhook**\n"
                        + "3. Add **Payload URL**: %s\n"
                        + "4. Set **Content-Type**: application/json\n"
                        + "5. Set **Secret**: %s",
                url,
                secret);
    }

    private String getHost() {
        return String.format("services.%s/github", Util.getDomain());
    }

    private User getOwner(WireClient client) throws Exception {
        String botId = client.getId();
        NewBot state = storageFactory.create(botId).getState();
        Collection<User> users = client.getUsers(Collections.singletonList(state.origin.id));
        return users.stream().findFirst().get();
    }
}
