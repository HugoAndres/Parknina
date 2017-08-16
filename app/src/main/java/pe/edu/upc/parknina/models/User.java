package pe.edu.upc.parknina.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hugo_ on 23/07/2017.
 */

public class User {
    private int idUser;
    private String name;
    private String fatLastName;
    private String motLastName;
    private String DNI;
    private String phone;
    private String born;
    private String direction;
    private String email;
    private String password;
    private String state;

    public User() {
    }

    public User(int idUser, String name, String fatLastName, String motLastName, String DNI, String phone, String born, String direction, String email, String password, String state) {
        this.idUser = idUser;
        this.name = name;
        this.fatLastName = fatLastName;
        this.motLastName = motLastName;
        this.DNI = DNI;
        this.phone = phone;
        this.born = born;
        this.direction = direction;
        this.email = email;
        this.password = password;
        this.state = state;
    }

    public int getIdUser() {
        return idUser;
    }

    public User setIdUser(int idUser) {
        this.idUser = idUser;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getFatLastName() {
        return fatLastName;
    }

    public User setFatLastName(String fatLastName) {
        this.fatLastName = fatLastName;
        return this;
    }

    public String getMotLastName() {
        return motLastName;
    }

    public User setMotLastName(String motLastName) {
        this.motLastName = motLastName;
        return this;
    }

    public String getDNI() {
        return DNI;
    }

    public User setDNI(String DNI) {
        this.DNI = DNI;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getBorn() {
        return born;
    }

    public User setBorn(String born) {
        this.born = born;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public User setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getState() {
        return state;
    }

    public User setState(String state) {
        this.state = state;
        return this;
    }

    public static User build(JSONObject jsonObject){
        User user = new User();

        try {
            user.setIdUser(Integer.valueOf(jsonObject.getString("id")))
                .setName(jsonObject.getString("idUser"))
                .setFatLastName(jsonObject.getString("fatLastName"))
                .setMotLastName(jsonObject.getString("motLastName"))
                .setDNI(jsonObject.getString("DNI"))
                .setPhone(jsonObject.getString("phone"))
                .setBorn(jsonObject.getString("born"))
                .setDirection(jsonObject.getString("direction"))
                .setEmail(jsonObject.getString("email"))
                .setPassword(jsonObject.getString("password"))
                .setState(jsonObject.getString("state"));

            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
