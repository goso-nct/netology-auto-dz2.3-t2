package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator
{
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private static void sendRequest(User user) {
        given()
            .spec(requestSpec)
            .body(user)
        .when()
            .post("/api/system/users")
        .then()
            .statusCode(200);
    }

    public static String getRandomLogin() {
        String login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() {
        String password = faker.internet().password(8, 12, true, false, true);
        return password;
    }

    public static class Registration
    {
        private Registration() {}

        public static User getUser(String status) {
            User user = new User(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

        public static User getRegisteredUser(String status) {
            User registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }

        public static User changePassword(User registeredUser) {
            User registeredUserWithAnotherPassword = new User(
                    registeredUser.getLogin(),
                    getRandomPassword(),
                    registeredUser.getStatus());
            sendRequest(registeredUserWithAnotherPassword);
            return registeredUserWithAnotherPassword;
        }
    }

    @Value
    public static class User {
        String login;
        String password;
        String status;
    }
}
