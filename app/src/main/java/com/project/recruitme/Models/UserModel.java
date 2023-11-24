package com.project.recruitme.Models;

public class UserModel
{
    public UserModel(){

    }

    public UserModel(String UserName, String Role, String UserId, String Phone, String Email){
        this.UserName = UserName;
        this.UserId = UserId;
        this.Role = Role;
        this.Phone = Phone;
        this.Email = Email;
    }
    public String UserName;
    public String Role;
    public String UserId;
    public String Phone;
    public String Email;
}

