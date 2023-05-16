package com.simplify.emailOtp.dao;

import com.simplify.emailOtp.entity.UserInfo;
import com.simplify.emailOtp.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserAuthenticationService implements UserDetailsService {


    @Autowired
    UserDataRepository userDataRepository;
    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = this.userDataRepository.findByEmailId(emailId);
        userInfo.orElseThrow(
                ()->new UsernameNotFoundException("User Not Found")
        );
        UserInfo user = userInfo.get();
        return user;
    }
}
