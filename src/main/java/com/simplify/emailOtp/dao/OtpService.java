package com.simplify.emailOtp.dao;

import com.simplify.emailOtp.entity.Otp;
import com.simplify.emailOtp.entity.UserInfo;
import com.simplify.emailOtp.repository.OtpRepository;
import com.simplify.emailOtp.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    public boolean generateNewOtp(String emailId) {
        emailId=emailId.toLowerCase();
        Otp otp2 = this.otpRepository.findByEmailId(emailId);
        if(isValidOtp(otp2)) {
            while(true) {
            try {
                char[] oTp = this.OTP();
                StringBuilder stringBuilder = new StringBuilder();
                for (char c : oTp) {
                    stringBuilder.append(c);
                }
                long otp = Long.parseLong(stringBuilder.toString());
                if(otp2!=null){
                    this.otpRepository.delete(otp2);
                }
                Otp otp1 = new Otp(otp, emailId);
                this.otpRepository.save(otp1);
                sendMail(emailId, otp);
                break;
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
            return true;
        }
        return false;
    }

    private char[] OTP() {
        String numbers = "0123456789";
        Random rand = new Random();
        char[] otp = new char[4];

        for (int i = 0; i < 4; i++) {
            otp[i] =
                    numbers.charAt(rand.nextInt(numbers.length()));
        }
        return otp;
    }

    @Async
    private void sendMail(String emailId,long otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@apoorv.dixit.developer.com");
        message.setTo(emailId);
        message.setSubject("otp");
        message.setText(String.valueOf(otp));
        javaMailSender.send(message);
    }

     boolean isValidOtp(Otp otp){
        if(otp==null){
            return true;
        }

         if(Duration.between(otp.getGenerateAt(), LocalDateTime.now()).getSeconds()>=60){
            return true;
         }
        return false;
    }


}
