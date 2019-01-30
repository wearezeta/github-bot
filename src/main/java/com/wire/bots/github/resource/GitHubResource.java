package com.wire.bots.github.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wire.bots.github.HmacValidator;
import com.wire.bots.github.WebHookHandler;
import com.wire.bots.github.model.GitResponse;
import com.wire.bots.sdk.ClientRepo;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.exceptions.MissingStateException;
import com.wire.bots.sdk.tools.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Path("/{botId}")
public class GitHubResource {

    private final ClientRepo repo;
    private final HmacValidator validator;
    private final WebHookHandler webHookHandler;

    public GitHubResource(ClientRepo repo, HmacValidator validator) {
        this.repo = repo;
        this.validator = validator;
        webHookHandler = new WebHookHandler();
    }

    @POST
    public Response webHook(
            @HeaderParam("X-GitHub-Event") String event,
            @HeaderParam("X-Hub-Signature") String signature,
            @HeaderParam("X-GitHub-Delivery") String delivery,
            @PathParam("botId") String botId,
            String payload) {

        try {
            WireClient client = repo.getClient(botId);

            boolean valid = validator.isValid(botId, signature, payload);
            if (!valid) {
                Logger.warning("Invalid Signature. Bot: %s", botId);
                return Response.
                        status(403).
                        build();
            }

            ObjectMapper mapper = new ObjectMapper();
            GitResponse response = mapper.readValue(payload, GitResponse.class);

            Logger.info("%s.%s Bot: %s", event, response.action, botId);

            String message = webHookHandler.handle(event, response);
            if (message != null && !message.isEmpty())
                client.sendText(message);

        } catch (MissingStateException e) {
            Logger.info("GitHubResource.webHook: Bot previously deleted. Bot: %s", botId);
            webHookHandler.unsubscribe(botId);
            return Response.
                    status(404).
                    build();
        } catch (Exception e) {
            Logger.error("GitHubResource.webHook: %s", e);
            return Response.
                    serverError().
                    build();
        }

        return Response.
                ok().
                build();
    }
}
