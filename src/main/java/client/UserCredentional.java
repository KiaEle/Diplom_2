package client;

import model.User;

public class UserCredentional {
    private  String email;
    private  String password;
    private  String name;

    public UserCredentional(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserCredentional updateEmailCred(User user, String email) {
        user.setEmail(email);
        return new UserCredentional(user.getEmail(), user.getPassword(), user.getName());
    }

    public static UserCredentional updatePasswordCred(User user, String password) {
        user.setPassword(password);
        return new UserCredentional(user.getEmail(), user.getPassword(), user.getName());
    }

    public static UserCredentional updateNameCred(User user, String name) {
        user.setName(name);
        return new UserCredentional(user.getEmail(), user.getPassword(), user.getName());
    }
}