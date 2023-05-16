package com.simplify.emailOtp.repository;

import com.simplify.emailOtp.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp,Long> {
}
