package com.tigeranalystics.userlifecycleserv.repository;

import com.tigeranalystics.userlifecycleserv.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmail(String email);// Use 'email' if that is the correct field for login

    Optional<UserInfo> findById(Long accountNumber);
}
