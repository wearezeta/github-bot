package com.wire.bots.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitResponse {
    @JsonProperty
    public String action;

    @JsonProperty
    public PullRequest pull_request;

    @JsonProperty
    public Comment comment;

    @JsonProperty
    public Issue issue;

    @JsonProperty
    public List<Commit> commits;

    @JsonProperty
    public User sender;

    @JsonProperty
    public String compare;

    @JsonProperty
    public Review review;

    @JsonProperty
    public Repository repository;

    @JsonProperty
    public Boolean created;
}
