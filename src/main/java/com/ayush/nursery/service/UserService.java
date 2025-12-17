package com.ayush.nursery.service;

import com.ayush.nursery.entity.User;
import com.ayush.nursery.models.ApiResponseModal;

public interface UserService {

    ApiResponseModal<User> validateUserLogin(String userId, String password);
    ApiResponseModal createUser(User user);

}
