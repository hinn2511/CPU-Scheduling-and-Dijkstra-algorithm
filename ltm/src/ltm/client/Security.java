package ltm.client;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Security {
//	private String privateKey;
	private String publicKey;
	private String secretKey;

//	public String getPrivateKey() {
//		return privateKey;
//	}
//
//	public void setPrivateKey(String privateKey) {
//		this.privateKey = privateKey;
//	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

//	public void generateRSAKey() throws NoSuchAlgorithmException {
//		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//		KeyPair kp = kpg.genKeyPair();
//		PublicKey publicKey = kp.getPublic();
//		PrivateKey privateKey = kp.getPrivate();
//		byte[] publicBytes = publicKey.getEncoded();
//		byte[] privateBytes = privateKey.getEncoded();
//		String publicString = Base64.getEncoder().encodeToString(publicBytes);
//		String privateString = Base64.getEncoder().encodeToString(privateBytes);
//		setPublicKey(publicString);
//		setPrivateKey(privateString);
//	}

	public void generateAESKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

		SecureRandom secureRandom = new SecureRandom();
		int keyBitSize = 256;
		keyGenerator.init(keyBitSize, secureRandom);

		SecretKey secretKey = keyGenerator.generateKey();
		setSecretKey(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
	}

	public String RSAEncrypt(String original) throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] publicBytes = Base64.getDecoder().decode(getPublicKey());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicBytes);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = factory.generatePublic(spec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte encryptedBytes[] = cipher.doFinal(original.getBytes());
		String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
		return encryptedString;
	}

//	public String RSADecrypt(String encrypted) throws NoSuchAlgorithmException, InvalidKeySpecException,
//			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//		byte[] privateBytes = Base64.getDecoder().decode(getPrivateKey());
//		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateBytes);
//		KeyFactory factory = KeyFactory.getInstance("RSA");
//		PrivateKey privateKey = factory.generatePrivate(spec);
//		Cipher c2 = Cipher.getInstance("RSA");
//		c2.init(Cipher.DECRYPT_MODE, privateKey);
//		byte decryptedBytes[] = c2.doFinal(Base64.getDecoder().decode(encrypted));
//		String decryptedString = new String(decryptedBytes);
//		return decryptedString;
//	}

	public String AESEncrypt(String original) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] decodedKey = Base64.getDecoder().decode(getSecretKey());

		try {
			Cipher cipher = Cipher.getInstance("AES");
			SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, originalKey);
			byte[] cipherText = cipher.doFinal(original.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(cipherText);
		} catch (Exception e) {
			throw new RuntimeException("Error occured while encrypting data", e);
		}
	}

	public String AESDecrypt(String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] decodedKey = Base64.getDecoder().decode(getSecretKey());

		try {
			Cipher cipher = Cipher.getInstance("AES");
			SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
			cipher.init(Cipher.DECRYPT_MODE, originalKey);
			byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			return new String(cipherText);
		} catch (Exception e) {
			throw new RuntimeException("Error occured while decrypting data", e);
		}
	}

}
