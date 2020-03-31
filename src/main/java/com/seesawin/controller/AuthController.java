package com.seesawin.controller;


import com.seesawin.config.security.jwt.JwtUtils;
import com.seesawin.config.security.services.UserDetailsImpl;
import com.seesawin.models.ERole;
import com.seesawin.models.Roles;
import com.seesawin.models.Users;
import com.seesawin.payload.request.LoginRequest;
import com.seesawin.payload.request.SignupRequest;
import com.seesawin.payload.response.JwtResponse;
import com.seesawin.payload.response.MessageResponse;
import com.seesawin.repository.RolesMapper;
import com.seesawin.repository.UsersMapper;
import com.seesawin.service.AuthService;
import com.sun.tools.javac.util.StringUtils;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RolesMapper rolesMapper;
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    @ApiOperation(
            value = "登入",
            notes = "登出由前端直接刪除localStorage中的token ",
            response = JwtResponse.class)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("AuthController.login, LoginRequest : {}", loginRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    @ApiOperation("註冊")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws Exception {
        log.info("AuthController.registerUser, SignupRequest : {}", signUpRequest);

        Users users = usersMapper.selectAll().stream()
                .filter(user -> user.getUsername().equals(signUpRequest.getUsername()))
                .findAny()
                .orElse(null);
        if (users != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        users = usersMapper.selectAll().stream()
                .filter(user -> user.getEmail().equals(signUpRequest.getEmail()))
                .findAny()
                .orElse(null);
        if (users != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Set<String> strRoles = signUpRequest.getRole();
        if (Collections.isEmpty(strRoles)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Roles is empty!"));
        }

        // Create new user's account
        Users user = new Users();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<Roles> roles = new HashSet<>();

        List<Roles> roleList = rolesMapper.selectAll();
        if (strRoles == null) {
            Roles userRole = roleList.stream()
                    .filter(role -> role.getName().equals(ERole.ROLE_USER.name()))
                    .findAny().orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Roles adminRole = roleList.stream()
                                .filter(r -> r.getName().equals(ERole.ROLE_ADMIN.name()))
                                .findAny().orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Roles modRole = roleList.stream()
                                .filter(r -> r.getName().equals(ERole.ROLE_MODERATOR.name()))
                                .findAny().orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Roles userRole = roleList.stream()
                                .filter(r -> r.getName().equals(ERole.ROLE_USER.name()))
                                .findAny().orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        authService.saveUserAndRoles(user, roles);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
