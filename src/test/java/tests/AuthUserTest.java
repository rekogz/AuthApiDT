package tests;

import config.AuthConfig;
import io.qameta.allure.Owner;
import models.AuthResponseModel;
import models.AuthUserBodyModel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static spec.AuthUserSpec.authRequestSpec;
import static spec.AuthUserSpec.authResponseSpec;

public class AuthUserTest extends TestBaseAPI {
    AuthConfig authConfig = ConfigFactory.create(AuthConfig.class);

    @Test
    @Owner("rekogz")
    @Tags({
            @Tag("Positive"),
            @Tag("Api")
    })
    @DisplayName("Успешная авторизация пользователя и проверка наличия токена и корректного времени истечения в ответе")
    void successfulAuthApiTest() {
        AuthUserBodyModel authBody = new AuthUserBodyModel();
        authBody.setEmail(authConfig.userEmail());
        authBody.setPassword(authConfig.userPassword());
        AuthResponseModel authUserResponse = step("Make request", () ->
                given(authRequestSpec)
                        .body(authBody)
                        .when()
                        .post("/login")
                        .then()
                        .spec(authResponseSpec)
                        .extract().as(AuthResponseModel.class));
        step("Check response", () -> {
            assertThat(authUserResponse.getAccessToken()).isNotNull();
            assertThat(authUserResponse.getRefreshToken()).isNotNull();
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime expiresInDateTime = Instant.ofEpochMilli(authUserResponse.getExpiresIn())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            System.out.println("Дата и время авторизации:" + currentDateTime.format(formatter));
            System.out.println("Дата и время истечения токена: " + expiresInDateTime.format(formatter));
            assertThat(expiresInDateTime).isAfterOrEqualTo(currentDateTime);
        });
    }
}