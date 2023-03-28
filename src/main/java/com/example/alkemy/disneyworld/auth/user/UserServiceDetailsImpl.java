package com.example.alkemy.disneyworld.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.alkemy.disneyworld.exception.NotFoundException;

@Service
public class UserServiceDetailsImpl implements UserDetailsService {

    private static final String NOT_FOUND_MESSAGE = "User Not Found";

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.getUserByUsername(username).
                orElseThrow(() -> {
                    return new NotFoundException(NOT_FOUND_MESSAGE);
                });
        return new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getAuthorities());
    }
}
