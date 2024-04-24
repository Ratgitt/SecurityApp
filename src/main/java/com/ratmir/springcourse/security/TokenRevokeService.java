package com.ratmir.springcourse.security;

import com.ratmir.springcourse.models.Token;
import com.ratmir.springcourse.repositories.TokenRepository;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenRevokeService {

    private final TokenRepository tokenRepository;

    /**
     * Verify if a digest encoded in HEX of the ciphered token is present
     * in the revocation table
     *
     * @param jwtInHex DatatypeConverter.printHexBinary(jwtToken.getBytes(StandardCharsets.UTF_8));
     * @return Presence flag
     * @throws Exception If any issue occur during communication with DB
     */
    @Transactional
    public boolean isTokenRevoked(String jwtInHex) throws Exception {

        boolean tokenIsPresent = false;
        if (jwtInHex != null && !jwtInHex.trim().isEmpty()) {
            //Decode the ciphered token
            byte[] cipheredToken = DatatypeConverter.parseHexBinary(jwtInHex);

            //Compute a SHA256 of the ciphered token
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] cipheredTokenDigest = digest.digest(cipheredToken);
            String jwtTokenDigestInHex = DatatypeConverter.printHexBinary(cipheredTokenDigest);

            //Search token digest in HEX in DB
            Optional<Token> foundToken = tokenRepository.findByJwtTokenDigest(jwtTokenDigestInHex);
            tokenIsPresent = foundToken.isPresent();
        }

        return tokenIsPresent;
    }


    /**
     * Add a digest encoded in HEX of the ciphered token to the revocation token table
     *
     * @param jwtInHex Token encoded in HEX
     * @throws Exception If any issue occur during communication with DB
     */
    @Transactional
    public void revokeToken(String jwtInHex, Date expirationDate) throws Exception {
        if (jwtInHex != null && !jwtInHex.trim().isEmpty()) {
            //Decode the ciphered token
            byte[] cipheredToken = DatatypeConverter.parseHexBinary(jwtInHex);

            //Compute a SHA256 of the ciphered token
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] cipheredTokenDigest = digest.digest(cipheredToken);
            String jwtTokenDigestInHex = DatatypeConverter.printHexBinary(cipheredTokenDigest);

            //Check if the token digest in HEX is already in the DB and add it if it is absent
            if (!isTokenRevoked(jwtInHex)) {
                tokenRepository.save(
                        Token.builder()
                                .jwtTokenDigest(jwtTokenDigestInHex)
                                .revokationDate(new Date())
                                .expirationDate(expirationDate)
                                .build()
                );
            }
        }
    }
}
