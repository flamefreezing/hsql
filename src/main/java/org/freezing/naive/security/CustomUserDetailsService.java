package org.freezing.naive.security;

import lombok.RequiredArgsConstructor;
import org.freezing.naive.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(Integer.valueOf(userId));
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(userId);
        }
        return new CustomUserDetails(user.get());
    }
}
