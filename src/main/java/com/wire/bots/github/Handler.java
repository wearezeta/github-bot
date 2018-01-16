package com.wire.bots.github;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.wire.bots.github.model.GitResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Handler {
    private final static MustacheFactory mf = new DefaultMustacheFactory();

    public String handle(String event, GitResponse response) {
        Mustache mustache = compileTemplate("en", event, response.action);
        return mustache != null ? execute(mustache, response) : null;
    }

    private Mustache compileTemplate(String language, String event, String action) {
        String path = String.format("templates/%s/%s.%s.template", language, event, action);
        return mf.compile(path);
    }

    private String execute(Mustache mustache, Object model) {
        try (StringWriter sw = new StringWriter()) {
            mustache.execute(new PrintWriter(sw), model).flush();
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
