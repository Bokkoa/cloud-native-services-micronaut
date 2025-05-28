package com.bokkoa.mnlearn.auth.jwt;

import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.token.render.BearerAccessRefreshToken;

@Client("/")
public class JWTClient {

    @Post("/login")
    BearerAccessRefreshToken login()

}
