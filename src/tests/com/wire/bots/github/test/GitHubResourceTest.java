package com.wire.bots.github.test;

import com.wire.bots.github.Validator;
import com.wire.bots.github.resource.GitHubResource;
import com.wire.bots.github.test.helpers.DummyRepo;
import com.wire.bots.github.test.helpers.DummyValidator;
import com.wire.bots.sdk.ClientRepo;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertEquals;

public class GitHubResourceTest {
    private static Validator validator = new DummyValidator();
    private static ClientRepo repo = new DummyRepo();

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new GitHubResource(repo, validator))
            .build();

    @Test
    public void testOnCommitCommentCreated() {
        String event = "commit_comment";
        String payload = FixtureHelpers.fixture("fixtures/events/" + event + ".created.json");
        gitHubWebhookPost(event, payload);
    }

    private void gitHubWebhookPost(String event, String payload) {
        Response response = resources.target("/github/botId")
                .request()
                .header("X-GitHub-Event", event)
                .header("X-Hub-Signature", "signature")
                .header("X-GitHub-Delivery", "delivery")
                .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
        assertEquals("gitHubWebhookPost Response status", 200, response.getStatus());
    }
}

