package com.hotels.controller;

import com.hotels.constant.ErrorMessage;
import com.hotels.constant.HttpStatuses;
import com.hotels.dto.SuccessSignInDto;
import com.hotels.service.FacebookService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * Controller that provide facebook security logic.
 *
 */
@RestController
@RequestMapping("/facebookSecurity")
@Validated
@AllArgsConstructor
public class FacebookController {

    private FacebookService facebookService;

    /**
     * Method that provide authenticate with facebook token.
     *
     * @param token {@link String} - facebook token
     * @return {@link SuccessSignInDto} if token valid
     */
    @ApiOperation("Make authentication by Facebook")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = SuccessSignInDto.class),
        @ApiResponse(code = 400, message = ErrorMessage.INVALID_FACEBOOK_TOKEN)
    })
    @GetMapping
    public SuccessSignInDto authenticate(@RequestParam @NotBlank String token) {
        return facebookService.authenticate(token);
    }
}
