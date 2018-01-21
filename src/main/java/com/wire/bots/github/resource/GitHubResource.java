package com.wire.bots.github.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wire.bots.github.Validator;
import com.wire.bots.github.WebHookHandler;
import com.wire.bots.github.model.GitResponse;
import com.wire.bots.sdk.ClientRepo;
import com.wire.bots.sdk.Logger;
import com.wire.bots.sdk.WireClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Path("/{botId}")
public class GitHubResource {

    private final ClientRepo repo;
    private final Validator validator;
    private final ObjectMapper mapper = new ObjectMapper();
    private final WebHookHandler webHookHandler = new WebHookHandler();

    public GitHubResource(ClientRepo repo, Validator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    @POST
    public Response webHook(
            @HeaderParam("X-GitHub-Event") String event,
            @HeaderParam("X-Hub-Signature") String signature,
            @HeaderParam("X-GitHub-Delivery") String delivery,
            @PathParam("botId") String botId,
            String payload) throws Exception {

        boolean valid = validator.isValid(botId, signature, payload);
        if (!valid) {
            Logger.warning("Invalid Signature. Bot: %s", botId);
            return Response.
                    status(403).
                    build();
        }

        WireClient client = repo.getWireClient(botId);
        if (client == null) {
            Logger.warning("Bot previously deleted. Bot: %s", botId);
            return Response.
                    status(404).
                    build();
        }

        GitResponse response = mapper.readValue(payload, GitResponse.class);

        Logger.info("%s.%s\tBot: %s", event, response.action, botId);

        String message = webHookHandler.handle(event, response);
        if (message != null)
            client.sendText(message);

        return Response.
                ok().
                build();
    }
}
