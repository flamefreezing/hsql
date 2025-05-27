package org.freezing.naive.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.freezing.naive.security.User;

import java.util.Optional;

@Mapper
public interface UserRepository {

    Optional<User> findByEmail(@Param("email") String email);
    void save(User user);
}
