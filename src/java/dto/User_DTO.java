package dto;

import java.io.Serializable;

public class User_DTO implements Serializable{
    
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String re_type_password;

    public User_DTO() {
    }
    
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRe_type_password() {
        return re_type_password;
    }

    public void setRe_type_password(String re_type_password) {
        this.re_type_password = re_type_password;
    }
    
}
