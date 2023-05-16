package com.simplify.emailOtp.dao;

import com.simplify.emailOtp.entity.Otp;
import com.simplify.emailOtp.entity.UserInfo;
import com.simplify.emailOtp.model.AuthToken;
import com.simplify.emailOtp.repository.OtpRepository;
import com.simplify.emailOtp.repository.UserDataRepository;
import com.simplify.emailOtp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public AuthToken login(String emailId,long otp){
        try {
            Optional<Otp> newOtp = this.otpRepository.findById(otp);
            UserInfo userInfo=new UserInfo(emailId,0);
            UserInfo user = this.userDataRepository.save(userInfo);
            if (newOtp.isPresent() && newOtp.get().getEmailId().equalsIgnoreCase(emailId)) {

                if (isExpired(newOtp.get(),emailId,user)) {
                    return new AuthToken(null, "Otp Expired", false);
                }
                if (!isValid(user,newOtp.get())) {
                    return new AuthToken(null, "Otp Invalid", false);
                }


                return new AuthToken(jwtUtil.generateToken(emailId), "Otp verified", true);

            }
            int numberOfAttempts = user.getNumberOfAttempts();
            user.setNumberOfAttempts(numberOfAttempts+1);
            userDataRepository.save(user);
            return new AuthToken(null, "EmailId invalid", false);


        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    boolean isExpired(Otp otp,String emailId,UserInfo user){
        int minute = otp.getGenerateAt().getMinute();
        int minute1 = LocalDateTime.now().getMinute();
        if(Math.abs(minute-minute1)>=5){
            int numberOfAttempts = user.getNumberOfAttempts();
            user.setNumberOfAttempts(numberOfAttempts+1);
            userDataRepository.save(user);
            return true;
        }
        return false;
    }

    boolean isValid(UserInfo user,Otp otp){
        if(user.getNumberOfAttempts() < 5 || Math.abs(otp.getGenerateAt().getHour() - LocalDateTime.now().getHour()) >= 1){
           user.setNumberOfAttempts(0);
           this.userDataRepository.save(user);
           return true;
        }
        return false;

    }


}
