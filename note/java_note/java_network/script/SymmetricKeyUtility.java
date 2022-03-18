public class SymmetricKeyUtility {

	private static SecretKey KeyGenerator() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}

	public static String encrypt(String plainText, SecretKey secretKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			byte[] plainTextBytes = plainText.getBytes();
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedBytes = cipher.doFinal(plainTextBytes);
			Base64.Encoder encoder = Base64.getEncoder();
			String encryptedText = encoder.encodeToString(encryptedBytes);
			return encryptedText;
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String decrypt(String encryptedText, SecretKey sectetKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] encryptedBytes = decoder.decode(encryptedText);
			cipher.init(Cipher.DECRYPT_MODE, sectetKey);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			String decryptedText = new String(decryptedBytes);
			return decryptedText;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}