package com.ayush.nursery.repository;

import com.ayush.nursery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT u FROM User u WHERE u.userId = :userId OR u.emailId = :userId")
    Optional<User> findByUserIdOrEmailId(@Param("userId") String userId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.emailId = :emailId")
    boolean existsByEmailId(@Param("emailId") String emailId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userId = :userId")
    boolean existsByUserId(@Param("userId") String userId);

}
