package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    // спецификация запроса
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker facer = new Faker(new Locale("en"));

    private DataGenerator() {
    }

//метод отправляющий пользовтаеля на бэк.
    private static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                     .post("/api/system/users")
                .then()
                .statusCode(200);
    }

//метод генерации случайного логина
    public static String getRandomLogin() {
        String login = facer.name().username();
        return login;
    }

//метод генерации случайного пароля
    public static String getRandomPassword() {
        String password = facer.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

// метод создания пользователя
        public static RegistrationDto getUser(String status) {
            var user = new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

// метод вызова и генерации пользователя
        public static RegistrationDto getRegisteredUser(String status) {
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

// дата-класс с пользователем
    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
