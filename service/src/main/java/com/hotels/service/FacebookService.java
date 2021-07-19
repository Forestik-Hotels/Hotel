package com.hotels.service;

import com.hotels.dto.SuccessSignInDto;

/**
 * Provides Facebook social logic.
 *
 */
public interface FacebookService {

    /**
     * Method that allow you authenticate with Facebook token.
     *
     * @param token {@link String} - token.
     * @return {@link SuccessSignInDto} if successful
     */
    SuccessSignInDto authenticate(String token);
}