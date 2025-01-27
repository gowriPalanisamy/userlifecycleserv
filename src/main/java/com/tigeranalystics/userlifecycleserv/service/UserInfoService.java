package com.tigeranalystics.userlifecycleserv.service;

import com.tigeranalystics.userlifecycleserv.entity.UserInfo;
import com.tigeranalystics.userlifecycleserv.model.UserInfoDetails;
import com.tigeranalystics.userlifecycleserv.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {


    private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);
    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserInfoDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
                                return userDetail.map(UserInfoDetails::new)
                                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        logger.info("User info got added successfully {} ",userInfo);
        return "User Added Successfully";
    }

    public UserInfoDetails loadUserByAccountNumber(Long accountNumber){
        Optional<UserInfo> userInfo = repository.findById(accountNumber);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + accountNumber));
    }

    public UserInfo updateUser(Long userAccNumber, UserInfo userInfo) {
        Optional<UserInfo> userDetails = repository.findById(userAccNumber);
        if(userDetails.isPresent()){
            repository.save(userInfo);
        } else {
            throw new UsernameNotFoundException("User not found: " + userAccNumber);
        }
        return userInfo;
    }
}