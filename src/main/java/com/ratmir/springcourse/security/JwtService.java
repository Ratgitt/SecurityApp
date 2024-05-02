package com.ratmir.springcourse.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private int expirationPeriod;

    @Value("${application.security.jwt.issuer}")
    private String issuer;

    public String generateToken(HttpServletResponse response, String username) throws NoSuchAlgorithmException {

        SecureRandom secureRandom = new SecureRandom();

        //Generate a random string that will constitute the fingerprint for this user
//        byte[] randomFgp = new byte[50];
//        secureRandom.nextBytes(randomFgp);
//        String userFingerprint = DatatypeConverter.printHexBinary(randomFgp);

        //Add the fingerprint in a hardened cookie - Add cookie manually because
        //SameSite attribute is not supported by javax.servlet.http.Cookie class
//        String fingerprintCookie = "__Secure-Fgp=" + userFingerprint
//                + "; Path=/; SameSite=Strict; HttpOnly; Secure";
//        response.addHeader("Set-Cookie", fingerprintCookie);

        //Compute a SHA256 hash of the fingerprint in order to store the
        //fingerprint hash (instead of the raw value) in the token
        //to prevent an XSS to be able to read the fingerprint and
        //set the expected cookie itself
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] userFingerprintDigest = digest.digest(userFingerprint.getBytes(StandardCharsets.UTF_8));
//        String userFingerprintHash = DatatypeConverter.printHexBinary(userFingerprintDigest);

        //Create the token with a validity of 15 minutes and client context (fingerprint) information
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.add(Calendar.HOUR, this.expirationPeriod);
        Date expirationDate = c.getTime();
        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("typ", "JWT");

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(expirationDate)
                .withIssuer(this.issuer)
                .withIssuedAt(now)
                .withNotBefore(now)
//                .withClaim("userFingerprint", userFingerprintHash)
                .withHeader(headerClaims)
                .sign(Algorithm.HMAC256(this.secretKey));
    }

    public String validateTokenAndGetSubject(HttpServletRequest request, String token) throws Exception {

        DecodedJWT decodedToken = validateToken(request, token);

        return decodedToken.getSubject();
    }

    public Date validateTokenAndGetExpiresAt(HttpServletRequest request, String token) throws Exception {
        DecodedJWT decodedToken = validateToken(request, token);

        return decodedToken.getExpiresAt();
    }

    private DecodedJWT validateToken(HttpServletRequest request, String token) throws Exception {
        //Retrieve the user fingerprint from the dedicated cookie
//        String userFingerprint = null;
//        if (request.getCookies() != null && request.getCookies().length > 0) {
//            List<Cookie> cookies = Arrays.stream(request.getCookies()).toList();
//            Optional<Cookie> cookie = cookies.stream().filter(c -> "__Secure-Fgp"
//                    .equals(c.getName())).findFirst();
//            if (cookie.isPresent()) {
//                userFingerprint = cookie.get().getValue();
//            }
//        }
//
//        if(userFingerprint == null) {
//            throw new Exception();
//        }

        //Compute a SHA256 hash of the received fingerprint in cookie in order to compare
        //it to the fingerprint hash stored in the token
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] userFingerprintDigest = digest.digest(userFingerprint.getBytes(StandardCharsets.UTF_8));
//        String userFingerprintHash = DatatypeConverter.printHexBinary(userFingerprintDigest);

        //Create a verification context for the token
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.secretKey))
                .withIssuer(this.issuer)
//                .withClaim("userFingerprint", userFingerprintHash)
                .build();

        //Verify the token, if the verification fail then an exception is thrown
        return verifier.verify(token);
    }
}
