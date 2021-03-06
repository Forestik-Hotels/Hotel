package com.hotels.controller;

import com.hotels.constant.HttpStatuses;
import com.hotels.dto.OwnSignInDto;
import com.hotels.dto.OwnSignUpDto;
import com.hotels.dto.SuccessSignInDto;
import com.hotels.dto.SuccessSignUpDto;
import com.hotels.dto.UpdatePasswordDto;
import com.hotels.service.OwnSecurityService;
import com.hotels.service.VerifyEmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.hotels.constant.ErrorMessage.NO_ANY_EMAIL_TO_VERIFY_BY_THIS_TOKEN;
import static com.hotels.constant.ErrorMessage.PASSWORD_DOES_NOT_MATCH;
import static com.hotels.constant.ErrorMessage.REFRESH_TOKEN_NOT_VALID;
import static com.hotels.constant.ErrorMessage.USER_ALREADY_REGISTERED_WITH_THIS_EMAIL;
import static com.hotels.constant.ErrorMessage.USER_CREATED;

/**
 * Controller that provides our sign-up and sign-in logic.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/ownSecurity")
@Validated
@Slf4j
public class OwnSecurityController {
    private final OwnSecurityService ownSecurityService;
    private final VerifyEmailService verifyEmailService;

    /**
     * Constructor.
     *
     * @param ownSecurityService - {@link OwnSecurityService} - service for security
     *                           logic.
     * @param verifyEmailService {@link VerifyEmailService} - service for email
     *                           verification.
     */
    @Autowired
    public OwnSecurityController(OwnSecurityService ownSecurityService,
        VerifyEmailService verifyEmailService) {
        this.ownSecurityService = ownSecurityService;
        this.verifyEmailService = verifyEmailService;
    }

    /**
     * Method for sign-up by our security logic.
     *
     * @param dto - {@link OwnSignUpDto} that have sign-up information.
     * @return {@link ResponseEntity} of {@link SuccessSignUpDto}
     */
    @ApiOperation("Sign-up by own security logic")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = USER_CREATED, response = SuccessSignUpDto.class),
        @ApiResponse(code = 400, message = USER_ALREADY_REGISTERED_WITH_THIS_EMAIL)
    })
    @PostMapping("/signUp")
    public ResponseEntity<SuccessSignUpDto> singUp(@Valid @RequestBody OwnSignUpDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ownSecurityService.signUp(dto));
    }

    /**
     * Method for sign-in by our security logic.
     *
     * @param dto - {@link OwnSignInDto} that have sign-in information.
     * @return {@link ResponseEntity} of {@link SuccessSignUpDto}
     */
    @ApiOperation("Sign-in by own security logic")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = SuccessSignInDto.class),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/signIn")
    public ResponseEntity<SuccessSignInDto> singIn(@Valid @RequestBody OwnSignInDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(ownSecurityService.signIn(dto));
    }

    /**
     * Method for verifying users email.
     *
     * @param token - {@link String} this is token (hash) to verify user.
     * @return {@link ResponseEntity} of {@link Boolean}
     */
    @ApiOperation("Verify email by email token (hash that contains link for verification)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = NO_ANY_EMAIL_TO_VERIFY_BY_THIS_TOKEN)
    })
    @GetMapping("/verifyEmail")
    public ResponseEntity<Boolean> verify(@RequestParam @NotBlank String token,
        @RequestParam("user_id") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(verifyEmailService.verifyByToken(userId, token));
    }

    /**
     * Method for refresh access token.
     *
     * @param refreshToken - {@link String} this is refresh token.
     * @return {@link ResponseEntity} of {@link Object} - with new access token.
     */
    @ApiOperation("Updating access token by refresh token")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = REFRESH_TOKEN_NOT_VALID)
    })
    @GetMapping("/updateAccessToken")
    public ResponseEntity<Object> updateAccessToken(@RequestParam @NotBlank String refreshToken) {
        return ResponseEntity.ok().body(ownSecurityService.updateAccessTokens(refreshToken));
    }

    /**
     * Method for updating current password.
     *
     * @param updateDto - {@link UpdatePasswordDto}
     * @return {@link ResponseEntity} of {@link Object}
     */
    @ApiOperation("Updating current password.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = PASSWORD_DOES_NOT_MATCH)
    })
    @PutMapping
    public ResponseEntity<Object> updatePassword(@Valid @RequestBody UpdatePasswordDto updateDto,
        @ApiIgnore @AuthenticationPrincipal Principal principal) {
        String email = principal.getName();
        ownSecurityService.updateCurrentPassword(updateDto, email);
        return ResponseEntity.ok().build();
    }
}
