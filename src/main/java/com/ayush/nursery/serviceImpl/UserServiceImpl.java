package com.ayush.nursery.serviceImpl;

import com.ayush.nursery.entity.User;
import com.ayush.nursery.enums.StatusResponse;
import com.ayush.nursery.models.ApiResponseModal;
import com.ayush.nursery.repository.UserRepository;
import com.ayush.nursery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public ApiResponseModal<User> validateUserLogin(String userId,String password)
    {
        Optional<User> userOptional=userRepository.findByUserIdOrEmailId(userId);

        if(userOptional.isEmpty())
        {
            return new ApiResponseModal<>(StatusResponse.FAILED,null,"User not found");
        }else {
            User user=userOptional.get();

            if(user.getPassword().equals(password))
            {
                return new ApiResponseModal<>(StatusResponse.SUCCESS,user,"User validated");
            }else {
                return new ApiResponseModal<>(StatusResponse.FAILED,null,"Invalid credentials");
            }
        }
    }

    public ApiResponseModal createUser(User user) {

        if (user == null) {
            return new ApiResponseModal(StatusResponse.FAILED, null, "User object is null");
        }

        if (user.getEmailId() == null || user.getEmailId().trim().isEmpty()) {
            return new ApiResponseModal(StatusResponse.FAILED, null, "Email is required");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return new ApiResponseModal(StatusResponse.FAILED, null, "Name is required");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return new ApiResponseModal(StatusResponse.FAILED, null, "Password is required");
        }

        if (userRepository.existsByEmailId(user.getEmailId())) {
            return new ApiResponseModal(StatusResponse.FAILED, null, "Email already exists");
        }

        if (user.getUserId() != null && userRepository.existsByUserId(user.getUserId())) {
            return new ApiResponseModal(StatusResponse.FAILED, null, "UserId already exists");
        }

        User savedUser = userRepository.save(user);
        return new ApiResponseModal(StatusResponse.SUCCESS, savedUser, "User created successfully");
    }


}
