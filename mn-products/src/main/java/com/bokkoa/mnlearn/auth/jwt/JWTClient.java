package com.bokkoa.mnlearn.auth.jwt;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;

@Client("/")
public interface JWTClient {

    @Post("/login") BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);
}
