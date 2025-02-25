package base;

import client.UserClient;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;

public class BaseTest {
    public String accessToken;

    public ValidatableResponse deleteUser;
    public UserClient userClient;

    public Faker faker;

    @Before
    public void setUp() {
        RestAssured.baseURI= "https://stellarburgers.nomoreparties.site/";
        userClient = new UserClient();
        faker = new Faker();
    }


    @After
    public void clear() {
        if(accessToken != null) {
            deleteUser = userClient.deleteUser(accessToken);
            int statusCode = deleteUser.extract().statusCode();
            boolean success = deleteUser.extract().path("success");

            Assert.assertEquals(HTTP_ACCEPTED, statusCode);
            Assert.assertTrue(success);
        }
    }
}