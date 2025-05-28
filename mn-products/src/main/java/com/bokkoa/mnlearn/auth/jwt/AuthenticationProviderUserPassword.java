package com.bokkoa.mnlearn.auth.jwt;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class AuthenticationProviderUserPassword<B> implements HttpRequestAuthenticationProvider<B> {


    private  final static Logger LOG = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);


    @Override
    public @NonNull AuthenticationResponse authenticate(
            @Nullable HttpRequest<B> requestContext,
            @NonNull AuthenticationRequest<String, String> authenticationRequest) {

        final Object identity = authenticationRequest.getIdentity();
        final Object secret = authenticationRequest.getSecret();

        LOG.debug("User {} tries to login....", identity);

        if ( identity.equals("my-user") && secret.equals("secret")) {
            return AuthenticationResponse.success(identity.toString());
        } else {
            return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
        }
    }
}
