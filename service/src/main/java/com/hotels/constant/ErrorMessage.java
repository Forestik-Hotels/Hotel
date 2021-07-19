package com.hotels.constant;

public final class ErrorMessage {

    public static final String USER_NOT_FOUND_BY_ID = "The user does not exist by this id: ";
    public static final String USER_NOT_FOUND_BY_EMAIL = "The user does not exist by this email: ";
    public static final String USER_DEACTIVATED = "User is deactivated";
    public static final String NO_ANY_EMAIL_TO_VERIFY_BY_THIS_TOKEN = "No any email to verify by this token";
    public static final String EMAIL_TOKEN_EXPIRED = "User late with verify. Token is invalid.";
    public static final String REFRESH_TOKEN_NOT_VALID = "Refresh token not valid!";
    public static final String BAD_PASSWORD = "Bad password";
    public static final String USER_ALREADY_REGISTERED_WITH_THIS_EMAIL = "User with this email is already registered";
    public static final String PASSWORDS_DO_NOT_MATCHES = "The passwords don't matches";
    public static final String PASSWORD_DOES_NOT_MATCH = "The password doesn't match";
    public static final String HOTEL_NOT_FOUND_BY_ID = "Hotel doesn't exist by this id: ";
    public static final String HOTEL_NOT_FOUND_BY_NAME = "Hotel doesn't exist by this name: ";
    public static final String ROOM_NOT_FOUND_BY_ID = "Room doesn't exist by this id: ";
    public static final String BOOKING_NOT_FOUND_BY_ID = "Booking doesn't exist by this id: ";
    public static final String USER_CREATED = "{greenCity.validation.user.created}";
    public static final String BAD_GOOGLE_TOKEN = "Bad google token";
    public static final String INVALID_FACEBOOK_TOKEN = "Invalid Facebook auth token";

    private ErrorMessage() {
    }
}
