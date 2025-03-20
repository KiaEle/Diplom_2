package base;

import client.UserClient;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.Before;

public class SetUpParamsTest {

    protected Faker faker;
    protected UserClient userClient;

@Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        userClient = new UserClient();
        faker = new Faker();
    }
}
