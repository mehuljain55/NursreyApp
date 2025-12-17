package com.ayush.nursery.controller;

import com.ayush.nursery.entity.User;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.service.UserService;
import com.ayush.nursery.serviceImpl.BackupService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BackupService backupService;

    @GetMapping("/login")
    public ApiResponseModal validateLogin(@RequestParam("userId")String userId,@RequestParam("password") String password)
    {
        return userService.validateUserLogin(userId,password);
    }

    @PostMapping("/create")
    public ApiResponseModal validateLogin(@RequestBody User user)
    {
        return userService.createUser(user);
    }


    @GetMapping("/backup")
    public ApiResponseModal createBackup() throws MessagingException, IOException {
        backupService.createBackup();
        return new ApiResponseModal<>(StatusResponse.SUCCESS,null,"Backup created");
    }

}
