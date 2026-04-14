package com.project.ecommercebackend.service.impl;

import com.project.ecommercebackend.exception.DuplicateResourceException;
import com.project.ecommercebackend.exception.ResourceNotFoundException;
import com.project.ecommercebackend.model.Role;
import com.project.ecommercebackend.model.User;
import com.project.ecommercebackend.model.enums.AppRole;
import com.project.ecommercebackend.repository.RoleRepository;
import com.project.ecommercebackend.repository.UserRepository;
import com.project.ecommercebackend.security.jwt.JwtUtils;
import com.project.ecommercebackend.security.request.LoginRequest;
import com.project.ecommercebackend.security.request.SignupRequest;
import com.project.ecommercebackend.security.response.MessageResponse;
import com.project.ecommercebackend.security.response.UserInfoResponse;
import com.project.ecommercebackend.security.service.UserDetailsImpl;
import com.project.ecommercebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public UserInfoResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);

        return new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles, cookie.toString());
    }

    @Override
    public ResponseCookie generateJwtCookie(LoginRequest loginRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return jwtUtils.generateJwtCookie(userDetails);
    }

    @Override
    @Transactional
    public MessageResponse registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUserName(signupRequest.getUsername())) {
            throw new DuplicateResourceException("User", "username", signupRequest.getUsername());
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new DuplicateResourceException("User", "email", signupRequest.getEmail());
        }

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<Role> roles = resolveRoles(signupRequest.getRole());
        user.setRoles(roles);
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        return jwtUtils.getCleanJwtCookie();
    }

    @Override
    public UserInfoResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);
    }

    private Set<Role> resolveRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", AppRole.ROLE_USER));
            roles.add(userRole);
            return roles;
        }

        for (String role : strRoles) {
            switch (role.toLowerCase()) {
                case "seller" -> {
                    Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", AppRole.ROLE_SELLER));
                    roles.add(sellerRole);
                }
                default -> {
                    Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", AppRole.ROLE_USER));
                    roles.add(userRole);
                }
            }
        }
        return roles;
    }
}
