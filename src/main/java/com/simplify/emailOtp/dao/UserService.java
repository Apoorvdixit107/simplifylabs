package com.simplify.emailOtp.dao;

import com.simplify.emailOtp.entity.Otp;
import com.simplify.emailOtp.entity.UserInfo;
import com.simplify.emailOtp.model.AuthToken;
import com.simplify.emailOtp.repository.OtpRepository;
import com.simplify.emailOtp.repository.UserDataRepository;
import com.simplify.emailOtp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthToken login(String emailId, long otp) {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            Optional<Otp> newOtp = this.otpRepository.findById(otp);
            Optional<UserInfo> myUser = this.userDataRepository.findByEmailId(emailId);
            UserInfo user = new UserInfo();
            if (!myUser.isPresent()) {
                UserInfo userInfo = new UserInfo(emailId, 0);
                user = this.userDataRepository.save(userInfo);
            } else {
                user = myUser.get();
            }
            if (isBlocked(user)) {
                return new AuthToken(null, "EmailId blocked for one hour", false);
            }
            if (!newOtp.isPresent()) {
                Optional<UserInfo> findByEmailId = this.userDataRepository.findByEmailId(emailId);
                UserInfo currentUser = findByEmailId.get();
                int numberOfAttempts = currentUser.getNumberOfAttempts();
                currentUser.setNumberOfAttempts(numberOfAttempts + 1);
                currentUser.setLastAttempt(currentTime);
                userDataRepository.save(currentUser);
                return new AuthToken(null, "Invalid Otp", false);
            }
            if (!newOtp.get().getEmailId().equalsIgnoreCase(emailId)) {
                Optional<UserInfo> findByEmailId = this.userDataRepository.findByEmailId(emailId);
                UserInfo currentUser = findByEmailId.get();
                int numberOfAttempts = currentUser.getNumberOfAttempts();
                currentUser.setNumberOfAttempts(numberOfAttempts + 1);
                currentUser.setLastAttempt(currentTime);
                userDataRepository.save(currentUser);
                return new AuthToken(null, "Invalid EmailId", false);
            }


            if (isExpired(newOtp.get(), emailId, user)) {
                Optional<UserInfo> findByEmailId = this.userDataRepository.findByEmailId(emailId);
                UserInfo currentUser = findByEmailId.get();
                int numberOfAttempts = currentUser.getNumberOfAttempts();
                currentUser.setNumberOfAttempts(numberOfAttempts + 1);
                currentUser.setLastAttempt(currentTime);
                userDataRepository.save(currentUser);
                return new AuthToken(null, "Otp Expired", false);
            }


            return new AuthToken(jwtUtil.generateToken(emailId), "Otp verified", true);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean isExpired(Otp otp, String emailId, UserInfo user) {
        int minute = otp.getGenerateAt().getMinute();
        int minute1 = LocalDateTime.now().getMinute();
        if (Math.abs(minute - minute1) >= 5) {
            Optional<UserInfo> findByEmailId = this.userDataRepository.findByEmailId(user.getEmailId());
            UserInfo currentUser = findByEmailId.get();
            int numberOfAttempts = currentUser.getNumberOfAttempts();
            currentUser.setNumberOfAttempts(numberOfAttempts + 1);
            userDataRepository.save(currentUser);
            return true;
        }
        return false;
    }

    boolean isBlocked(UserInfo user) {

        if (user.getNumberOfAttempts() < 5 || (user.getNumberOfAttempts()>=5&& Duration.between(user.getLastAttempt()
                ,LocalDateTime.now()).getSeconds()>=3600)) {
            Optional<UserInfo> findByEmailId = this.userDataRepository.findByEmailId(user.getEmailId());
            UserInfo currentUser = findByEmailId.get();
            if(currentUser.getNumberOfAttempts()>=5){
                currentUser.setNumberOfAttempts(0);
            }
            userDataRepository.save(currentUser);
            return false;
        }
        return true;

    }


}
