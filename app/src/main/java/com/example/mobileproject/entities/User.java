package com.example.mobileproject.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String email;
    @ColumnInfo
    private String password;
    @ColumnInfo
    private String confirmPassword;
    @ColumnInfo
    private String userName;
    @ColumnInfo
    private String phoneNumber;


    public User() {
    }

    public User(String login, String pwd , String confirmPwd , String userName , String phoneNumber) {
        this.email = login;
        this.password = pwd;
        this.confirmPassword = confirmPwd;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email ;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public String getPassword() {
        return password ;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + email + '\'' +
                ", pwd='" + password + '\'' +
                ", confirmPwd='" + confirmPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
