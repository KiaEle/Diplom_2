package base;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;

public class BaseTest extends SetUpParamsTest {

    public String accessToken;
    public ValidatableResponse deleteUser;

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