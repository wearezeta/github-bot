package com.wire.bots.github.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.wire.bots.github.model.GitResponse;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

public class MessageTemplateTest {
    private static String[] locales = new String[]{"en"};

    @BeforeClass
    public static void init() {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("com.jcabi.manifests.Manifests");
        logger.setLevel(Level.SEVERE);
    }

    // ------------------- Tests -------------------
    @Test
    public void commit_comment_Test() throws IOException {
        test("commit_comment");
    }

    // ------------------- Tests -------------------

    private void test(String event) throws IOException {
        String filename = String.format("fixtures/events/%s.json", event);
        String payload = FixtureHelpers.fixture(filename);
        ObjectMapper mapper = new ObjectMapper();
        GitResponse gitResponse = mapper.readValue(payload, GitResponse.class);

        for (String locale : locales) {
            Mustache mustache = compileTemplate(locale, event);
            String message = execute(mustache, gitResponse);

            String f = String.format("fixtures/messages/%s.txt", event);
            String expected = FixtureHelpers.fixture(f);

            Assert.assertEquals("", expected, message);
        }
    }

    private Mustache compileTemplate(String language, String event) {
        MustacheFactory mf = new DefaultMustacheFactory();
        String path = String.format("templates/%s/%s.txt", language, event);
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
