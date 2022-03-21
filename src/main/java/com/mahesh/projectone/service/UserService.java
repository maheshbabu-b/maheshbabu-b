package com.mahesh.projectone.service;

import com.mahesh.projectone.model.User;

public interface UserService {

    public boolean deleteUser(String u);

    public boolean addUser(User u);

    public boolean validate(String username, String password);

    public boolean updateUser(String username, String oldpassword, String newpassword);

    public String hashPassword(String plainTextPassword);

}
