package com.hotels.service.impl;

import com.hotels.constant.ErrorMessage;
import com.hotels.dto.FacebookDataDto;
import com.hotels.dto.OwnSignUpDto;
import com.hotels.dto.SuccessSignInDto;
import com.hotels.entity.User;
import com.hotels.jwt.JwtTool;
import com.hotels.service.FacebookService;
import com.hotels.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

/**
 * Implementation of FacebookService.
 */
@Slf4j
@Service
public class FacebookServiceImpl extends AuthService implements FacebookService {
    private final UserService userService;

    /**
     * Constructor.
     *
     * @param jwtTool     {@link JwtTool}
     * @param userService {@link UserService}
     */
    public FacebookServiceImpl(JwtTool jwtTool, UserService userService) {
        super(jwtTool);
        this.userService = userService;
    }

    /**
     * Method to check the user or register a new if he/she doesn't exist.
     *
     * @param token {@link String} - token.
     * @return successful sign in
     */
    @Override
    public SuccessSignInDto authenticate(String token) {
        try {
            Facebook facebook = new FacebookTemplate(token);
            String[] fields = {"email", "firstName", "id", "lastName"};
            FacebookDataDto facebookData = facebook.fetchObject("me", FacebookDataDto.class, fields);
            User user = userService.findByEmail(facebookData.getEmail());
            if (user == null) {
                log.info(ErrorMessage.USER_NOT_FOUND_BY_EMAIL + facebookData.getEmail());
                var ownSignUpDto = new OwnSignUpDto(facebookData.getFirstName(), facebookData.getLastName(),
                    facebookData.getEmail(), null);
                user = createNewRegisteredUser(ownSignUpDto, null);
                userService.save(user);
                log.info("Facebook user registered: {}", user.getEmail());
                return getSuccessSignInDto(user);
            } else {
                log.info("Facebook user authenticated: {}", user.getEmail());
                return getSuccessSignInDto(user);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FACEBOOK_TOKEN);
        }
    }

}