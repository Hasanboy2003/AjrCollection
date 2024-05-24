package com.yurakamri.ajrcollection.controller;

public interface BaseUrl {

    String VERSION = "/v1";
    String M = "/mobile";
    String W = "/web";

    String BASE = "/api";
    String BASE_URL = BASE + VERSION;

    String BASE_URL_MOB = BASE + VERSION + M;
    String BASE_URL_WEB = BASE + VERSION + W;
}
