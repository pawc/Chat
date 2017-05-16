package pl.pawc.chat.shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Crypto{
	
	private String key;
	
	public Crypto(String key){
		this.key = key;
	}
  
	private static final String ALGORITM = "AES/CBC/PKCS5Padding";
	private static final String CODIFICACION = "UTF-8";

	public String encrypt(String plaintext){
		try{
			byte[] raw = DatatypeConverter.parseHexBinary(key);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance(ALGORITM);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] cipherText = cipher.doFinal(plaintext.getBytes(CODIFICACION));
			byte[] iv = cipher.getIV();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(iv);
			outputStream.write(cipherText);
			byte[] finalData = outputStream.toByteArray();
			String encodedFinalData = DatatypeConverter.printBase64Binary(finalData);
			return encodedFinalData;
		}
		catch(Exception e){
			e.printStackTrace();
			return "error while encrypting";
		}
		
	}

	public String decrypt(String encodedInitialData){
		try{
			byte[] encryptedData = DatatypeConverter.parseBase64Binary(encodedInitialData);
			byte[] raw = DatatypeConverter.parseHexBinary(key);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance(ALGORITM);
			byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16);
			byte[] cipherText = Arrays.copyOfRange(encryptedData, 16, encryptedData.length);
			IvParameterSpec iv_specs = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv_specs);
			byte[] plainTextBytes = cipher.doFinal(cipherText);
			String plainText = new String(plainTextBytes);
			return plainText;
		}
		catch(Exception e){
			e.printStackTrace();
			return "error while decrypting";
		}

	}
	
}