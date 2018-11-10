package com.wire.bots.github.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wire.bots.github.TLSValidator;
import com.wire.bots.github.WebHookHandler;
import com.wire.bots.github.model.Travis;
import com.wire.bots.sdk.ClientRepo;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.exceptions.MissingStateException;
import com.wire.bots.sdk.tools.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;

@Consumes(MediaType.APPLICATION_JSON)
@Path("/travis/{botId}")
public class TravisResource {

    private final ClientRepo repo;
    private final TLSValidator validator;
    private final WebHookHandler webHookHandler;

    public TravisResource(ClientRepo repo, TLSValidator validator) {
        this.repo = repo;
        this.validator = validator;
        webHookHandler = new WebHookHandler();
    }

    @POST
    public Response webHook(
            @HeaderParam("Signature") String signature,
            @PathParam("botId") String botId,
            String payload) {

        try {
            WireClient client = repo.getClient(botId);

            boolean valid = validator.isValid(Base64.getDecoder().decode(signature), payload.getBytes());
            if (!valid) {
                Logger.warning("Invalid Signature. Bot: %s", botId);
                return Response.
                        status(403).
                        build();
            }

            ObjectMapper mapper = new ObjectMapper();
            Travis response = mapper.readValue(payload, Travis.class);

            Logger.info("Bot: %s, id %s state: %s", botId, response.id, response.state);

            String text = String.format("Travis build: __%s__ **%s**\n[build](%s)\n%s\n%s [commit](%s)",
                    response.repository.name,
                    response.state,
                    response.build_url,
                    response.message,
                    response.author_name,
                    response.compare_url);

            client.sendText(text);

        } catch (MissingStateException e) {
            Logger.info("Bot previously deleted. Bot: %s", botId);
            webHookHandler.unsubscribe(botId);
            return Response.
                    status(404).
                    build();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("TravisResource.webHook: %s", e);
            return Response.
                    serverError().
                    build();
        }

        return Response.
                ok().
                build();
    }
}
