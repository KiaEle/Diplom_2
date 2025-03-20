package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;

import javax.print.attribute.standard.OrientationRequested;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserClient  {

    private static final String CREATE_USER_URI = "api/auth/register/";
    private static final String DELETE_USER_URI = "api/auth/user/";
    private static final String AUTHORIZATION_USER_URI = "api/auth/login/";
    private static final String UPDATE_USER_URI = "api/auth/user/";
    private static final String CREATE_ORDER_URI = "api/orders/";
    private static final String GET_ORDER_USER_URI = "api/orders/";


    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/ingredients")
                .then();
    }

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(CREATE_USER_URI)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Content-type", "Application/json")
                .and()
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE_USER_URI)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse authorization(User user, String token) {
        return given()
                .header("Content-type", "Application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(user)
                .when()
                .post(AUTHORIZATION_USER_URI)
                .then();
    }

    @Step("Обновление данных о пользователе с токеном")
    public ValidatableResponse update(UserCredentional userCred, String token) {
        return given()
                .header("Content-type", "Application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(userCred)
                .when()
                .patch(UPDATE_USER_URI)
                .then();
    }

    @Step("Обновление данных о пользователе без токена")
    public ValidatableResponse update(UserCredentional userCred) {
        return given()
                .header("Content-type", "Application/json")
                .and()
                .body(userCred)
                .when()
                .patch(UPDATE_USER_URI)
                .then();
    }

    @Step("Создание заказа. в параметры передаётся Order как -> Map<String, List<String>>")
    public ValidatableResponse createOrder(Map<String, List<String>> order, String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Создание заказа. в параметры передаётся Order order c токеном пользователя")
    public ValidatableResponse createOrder(Order order, String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Создание заказа. в параметры передаётся Order order")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Создание заказа без параметров")
    public ValidatableResponse createOrder() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Вернуть заказ пользователя. В параметре передается токен пользователя")
    public ValidatableResponse getOrderUser(String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .when()
                .get(GET_ORDER_USER_URI)
                .then();
    }

    @Step("Вернуть заказ пользователя, без параметров")
    public ValidatableResponse getOrderUser() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(GET_ORDER_USER_URI)
                .then();
    }

}