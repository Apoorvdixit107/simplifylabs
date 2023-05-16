package com.simplify.emailOtp.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Otp {

    @Id
    @Column(updatable = false,nullable = false)
    private Long otp;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime generateAt;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String emailId;

    public Otp(
            Long otp,
            String emailId
    ) {
        this.otp = otp;
        this.emailId = emailId;
    }
}
