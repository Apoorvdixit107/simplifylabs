package com.simplify.emailOtp.repository;

import com.simplify.emailOtp.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OtpRepository extends JpaRepository<Otp,Long> {

    @Query(nativeQuery = true,value = "select * from otp where email_id=:emailId")
    Otp findByEmailId(String emailId);
}
