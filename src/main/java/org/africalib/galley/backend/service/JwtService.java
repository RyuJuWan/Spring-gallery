package org.africalib.galley.backend.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String getToken(String key, Object value);
    Claims getClaims(String token);

    Boolean isValid(String token);

    int getId(String token);
}