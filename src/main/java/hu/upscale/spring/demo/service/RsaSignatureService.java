package hu.upscale.spring.demo.service;

import static hu.upscale.spring.demo.util.ResourceUtil.readResourceFile;

import hu.upscale.spring.demo.exception.CryptographyException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.springframework.stereotype.Service;

/**
 * @author László Zoltán
 */
@Service
public class RsaSignatureService {

    private static final String SIGNATURE_ALGORITHM = "SHA512withRSA";
    private static final String RSA_KEY_FACTORY_ALGORITHM = "RSA";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RsaSignatureService() {
        privateKey = getRsaPrivateKeyFromPkcs8EncodedKey(readResourceFile("rsa/privateKey.der"));
        publicKey = getRsaPublicKeyFromX509Certificate(readResourceFile("rsa/publicKey.der"));
    }

    public byte[] signData(byte[] dataToSign) {
        try {
            Signature privateSignature = Signature.getInstance(SIGNATURE_ALGORITHM);
            privateSignature.initSign(privateKey);
            privateSignature.update(dataToSign);

            return privateSignature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new CryptographyException("Failed to sign data", e);
        }
    }

    private static PrivateKey getRsaPrivateKeyFromPkcs8EncodedKey(byte[] certificateBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_FACTORY_ALGORITHM);
            PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(certificateBytes);

            return keyFactory.generatePrivate(keySpecPv);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptographyException("Failed to load RSA private key", e);
        }
    }

    private static PublicKey getRsaPublicKeyFromX509Certificate(byte[] certificateBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(certificateBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptographyException("Failed to load RSA public key", e);
        }
    }
}
