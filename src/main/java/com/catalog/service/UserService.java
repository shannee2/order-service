package com.catalog.service;

import com.catalog.dto.user.UserRequest;
import com.catalog.dto.user.UserResponse;
import com.catalog.exceptions.users.UserNotFoundException;
//import com.catalog.exceptions.wallets.WalletNotFoundException;
import com.catalog.model.user.User;
import com.catalog.model.user.UserPrincipal;
import com.catalog.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;


@Service
@Primary
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;


    @Autowired
    public UserService(
            UserRepository userRepository,
            @Lazy AuthenticationManager authManager,
            PasswordEncoder encoder,
            JWTService jwtService) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.passwordEncoder = encoder;
        this.jwtService = jwtService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new UserPrincipal(user);
    }

    @Transactional
    public UserResponse registerUser(UserRequest userRequest) {
        User user = new User(userRequest.getUsername(), passwordEncoder.encode(userRequest.getPassword()));
        try{
            if (getUserByUsername(user.getUsername()) != null) {
                ResponseEntity.badRequest().body("Username already taken");
                return null;
            }
        }catch (UserNotFoundException _){}
        userRepository.save(user);
        String token = jwtService.generateToken(user.getId());
        return new UserResponse(true, 201, "User Registered Successfully", token, user.getId());
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserResponse verify(UserRequest userRequest) {
        String token = null;
        User user = null;
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword()
                    )
            );
            user = getUserByUsername(userRequest.getUsername());
            if (authentication.isAuthenticated()) {
                token = jwtService.generateToken(user.getId());
            } else {
                throw new AccessDeniedException("Authentication Failed");
            }
        } catch (Exception _) {}
        assert user != null;
        return new UserResponse(true, 200, "Login Success", token, user.getId());
    }
}