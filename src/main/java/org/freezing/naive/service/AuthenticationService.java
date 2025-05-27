package org.freezing.naive.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.freezing.naive.dto.LoginRequest;
import org.freezing.naive.dto.RegisterRequest;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.UserRepository;
import org.freezing.naive.security.User;
import org.freezing.naive.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest registerRequest) {
        Optional<User> existedUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existedUser.isPresent()) {
            throw new BusinessException("Email already in use", 400);
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
    }

    public String login(LoginRequest loginRequest) {
        Optional<User> existedUser = userRepository.findByEmail(loginRequest.getEmail());
        if (existedUser.isEmpty()) {
            throw new BusinessException("User not found", 400);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), existedUser.get().getPassword())) {
            throw new BusinessException("Wrong password", 400);
        }

        return jwtUtil.generateToken(existedUser.get());
    }

}
