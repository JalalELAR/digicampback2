package com.capgemini.test1.web;

import com.capgemini.test1.dtos.LoginUserDto;
import com.capgemini.test1.dtos.RegisterUserDto;
import com.capgemini.test1.entities.User;
import com.capgemini.test1.service.serviceSecurity.AuthenticationService;
import com.capgemini.test1.service.serviceSecurity.JwtService;
import com.capgemini.test1.service.serviceSecurity.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Optionnel : Si tu veux faire quelque chose pour invalider le token côté serveur, fais-le ici
        return ResponseEntity.ok("Logout successful");
    }





}
