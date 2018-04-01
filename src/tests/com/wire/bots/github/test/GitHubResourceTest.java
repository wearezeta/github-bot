package com.wire.bots.github.test;

import com.wire.bots.github.Config;
import com.wire.bots.github.Service;
import com.wire.bots.github.resource.GitHubResource;
import com.wire.bots.github.test.helpers.DummyRepo;
import com.wire.bots.github.test.helpers.DummyValidator;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertEquals;

public class GitHubResourceTest {
    @ClassRule
    public static final DropwizardAppRule<Config> app
            = new DropwizardAppRule<>(Service.class, "github.yaml");

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new GitHubResource(new DummyRepo(), new DummyValidator()))
            .build();

    @Test
    public void testOnCommitCommentCreated() {
        String event = "commit_comment";
        String payload = FixtureHelpers.fixture("fixtures/events/" + event + ".created.json");
        gitHubWebhookPost("bot", event, payload);
    }

    @Test
    public void testOnPRCreated() {
        String event = "pull_request";
        String payload = FixtureHelpers.fixture("fixtures/events/" + event + ".created.json");
        gitHubWebhookPost("bot", event, payload);
    }

    private void gitHubWebhookPost(String botId, String event, String payload) {
        Response response = resources.target("/")
                .path(botId)
                .request()
                .header("X-GitHub-Event", event)
                .header("X-Hub-Signature", "signature")
                .header("X-GitHub-Delivery", "delivery")
                .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
        assertEquals("gitHubWebhookPost Response status", 200, response.getStatus());
    }
}

