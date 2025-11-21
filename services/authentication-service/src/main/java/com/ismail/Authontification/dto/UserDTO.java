package com.ismail.Authontification.dto;

public class UserDTO {

    private String email;
    private String userName;

    public UserDTO() {
    }

    public UserDTO(String email, String name) {
        this.email = email;
        this.userName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }
}
