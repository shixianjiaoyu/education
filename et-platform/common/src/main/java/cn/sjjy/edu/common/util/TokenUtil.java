package cn.sjjy.edu.common.util;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public class TokenUtil {
    public final static String createToken(String username, Integer accountId) throws JOSEException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                new JWTClaimsSet.Builder()
                        .issuer(username)
                        .jwtID(String.valueOf(accountId))
                        .expirationTime(cal.getTime())
                        .issueTime(new Date())
                        .build()
        );
        signedJWT.sign(new MACSigner(secretKey));
        return signedJWT.serialize();
    }

    private final static byte[] secretKey = Base64.getDecoder().decode("c8L3/dA0Z4AwEK4U+aXhUkAGbQfjCeO+P5CiYCC9V2o=");

    public static byte[] getSecretKey() {
        return secretKey;
    }
}
