package org.freezing.naive.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.freezing.naive.security.User;

import java.util.Optional;

@Mapper
public interface UserRepository {

    Optional<User> findByName(@Param("name") String name);
    Optional<User> findById(@Param("userId") Integer userId);
    void save(User user);
}
