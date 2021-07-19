package com.hotels.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Class represents Facebook user data.
 *
 */
@Data
@Getter
@Setter
@Builder
public class FacebookDataDto {
    private String email;
    private String phoneNumber;
    private Long id;
    private String firstName;
    private String lastName;

}