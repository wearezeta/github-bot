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
import com.wire.bots.sdk.Logger;
import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.Util;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.models.TextMessage;
import com.wire.bots.sdk.server.model.Member;
import com.wire.bots.sdk.server.model.NewBot;
import com.wire.bots.sdk.server.model.User;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MessageHandler extends MessageHandlerBase {
    private final BotConfig config;
    private final SessionIdentifierGenerator sesGen = new SessionIdentifierGenerator();

    MessageHandler(BotConfig config) {
        this.config = config;
    }

    @Override
    public boolean onNewBot(NewBot newBot) {
        Logger.info(String.format("onNewBot: bot: %s, username: %s",
                newBot.id,
                newBot.origin.handle));

        for (Member member : newBot.conversation.members) {
            if (member.service != null) {
                Logger.warning("Rejecting NewBot. Provider: %s service: %s",
                        member.service.provider,
                        member.service.id);
                return false; // we don't want to be in a conv if other bots are there.
            }
        }
        return true;
    }

    @Override
    public void onNewConversation(WireClient client) {
        try {
            String secret = sesGen.next(6);
            Util.writeLine(secret, new File(String.format("%s/%s/secret", config.getCryptoDir(), client.getId())));

            String help = formatHelp(client);
            client.sendText(help, TimeUnit.MINUTES.toMillis(15));
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }

    @Override
    public void onText(WireClient client, TextMessage msg) {
        try {
            if (msg.getText().equalsIgnoreCase("/help")) {
                String help = formatHelp(client);

                client.sendText(help, TimeUnit.SECONDS.toMillis(60));
            }
        } catch (Exception e) {
            Logger.error(e.getLocalizedMessage());
        }
    }

    private String formatHelp(WireClient client) throws IOException {
        String botId = client.getId();
        String host = getHost();
        String secret = Util.readLine(new File(String.format("%s/%s/secret", config.getCryptoDir(), botId)));
        String convName = URLEncoder.encode(client.getConversation().name, "UTF-8");
        String owner = getOwner(client, botId);

        String url = String.format("https://%s/%s#%s,owner=%s", host, botId, convName, owner);
        return formatHelp(url, secret);
    }

    private String formatHelp(String url, String secret) {
        return String.format("Hi, I'm GitHub-Bot. Here is how to set me up:\n\n"
                        + "1. Go to the repository that you want to connect to\n"
                        + "2. Go to **Settings / Webhooks / Add webhook**\n"
                        + "3. Add **Payload URL**: %s\n"
                        + "4. Set **Content-Type**: application/json\n"
                        + "5. **Disable** SSL verification\n"
                        + "6. Set **Secret**: %s",
                url,
                secret);
    }

    private String getHost() {
        String env = System.getProperty("env", "prod");
        if (env.equalsIgnoreCase("prod"))
            return "https://github.services.wire.com";
        else
            return "https://github-stage.services.wire.com";
    }

    @Nullable
    private String getOwner(WireClient client, String botId) throws IOException {
        File originFile = new File(String.format("%s/%s/origin.id", config.getCryptoDir(), botId));
        if (!originFile.exists())
            return null;
        String origin = Util.readLine(originFile);
        Collection<User> users = client.getUsers(Collections.singletonList(origin));
        for (User user : users)
            return user.handle;
        return null;
    }
}
