package com.ratmir.springcourse.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "revoked_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "jwt_token_digest")
    private String jwtTokenDigest;

    @Column(name = "revocation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date revokationDate;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    public Token(String jwtTokenDigest, Date expirationDate) {
        this.jwtTokenDigest = jwtTokenDigest;
        this.expirationDate = expirationDate;
    }
}
