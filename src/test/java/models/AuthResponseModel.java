package models;

import lombok.Data;

@Data
public class AuthResponseModel {
    String accessToken, refreshToken;
    Long expiresIn;
}
