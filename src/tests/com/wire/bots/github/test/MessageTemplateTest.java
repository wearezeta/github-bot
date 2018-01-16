package com.wire.bots.github.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.wire.bots.github.model.GitResponse;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MessageTemplateTest {
    private static String[] locales = new String[]{"en"};

    // ------------------- Tests -------------------
    @Test
    public void commit_commentCreatedTest() throws IOException {
        test("commit_comment", "created");
    }

    @Test
    public void issuesOpenedTest() throws IOException {
        test("issues", "opened");
    }

    @Test
    public void issuesReopenedTest() throws IOException {
        test("issues", "reopened");
    }

    @Test
    public void issuesClosedTest() throws IOException {
        test("issues", "closed");
    }

    @Test
    public void issueCommentCreatedTest() throws IOException {
        test("issue_comment", "created");
    }

    @Test
    public void pullRequestOpenedTest() throws IOException {
        test("pull_request", "opened");
    }

    @Test
    public void pullRequestClosedTest() throws IOException {
        test("pull_request", "closed");
    }
    // ------------------- Tests -------------------

    private void test(String event, String action) throws IOException {
        String filename = String.format("fixtures/events/%s.%s.json", event, action);
        String payload = FixtureHelpers.fixture(filename);
        ObjectMapper mapper = new ObjectMapper();
        GitResponse gitResponse = mapper.readValue(payload, GitResponse.class);

        for (String locale : locales) {
            Mustache mustache = compileTemplate(locale, event, action);
            String message = execute(mustache, gitResponse);

            String f = String.format("fixtures/messages/%s.%s.txt", event, action);
            String expected = FixtureHelpers.fixture(f);

            Assert.assertEquals("", expected, message);
        }
    }

    private Mustache compileTemplate(String language, String event, String action) {
        MustacheFactory mf = new DefaultMustacheFactory();
        String path = String.format("templates/%s/%s.%s.template", language, event, action);
        Mustache mustache = mf.compile(path);
        Assert.assertNotNull(path, mustache);
        return mustache;
    }

    private String execute(Mustache mustache, Object model) {
        try (StringWriter sw = new StringWriter()) {
            mustache.execute(new PrintWriter(sw), model).flush();
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(mustache.getName(), false);
            return null;
        }
    }
}
