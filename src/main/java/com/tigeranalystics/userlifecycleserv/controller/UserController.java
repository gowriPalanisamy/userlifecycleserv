package com.tigeranalystics.userlifecycleserv.controller;

import com.tigeranalystics.userlifecycleserv.entity.AuthRequest;
import com.tigeranalystics.userlifecycleserv.entity.UserInfo;
import com.tigeranalystics.userlifecycleserv.model.UserInfoDetails;
import com.tigeranalystics.userlifecycleserv.service.JwtService;
import com.tigeranalystics.userlifecycleserv.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/users")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/users/{userAccNumber}")
    public UserInfo updateUser(@PathVariable Long userAccNumber, @RequestBody UserInfo userInfo) {
        return service.updateUser(userAccNumber,userInfo);
    }

    @PostMapping("/auth/token")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authentication);
        } else {
            throw new UsernameNotFoundException("Bad Credentials!");
        }
    }

    @GetMapping("/users/{userAccNumber}")
    public ResponseEntity<?> retrieveUserDetails(@PathVariable Long userAccNumber){
        UserInfoDetails userInfoDetails = service.loadUserByAccountNumber(userAccNumber);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoDetails);
    }
}
