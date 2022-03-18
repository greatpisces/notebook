public class AsymmetricKeyUtility {

	public static void savePrivateKey(PrivateKey privateKey) {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			FileOutputStream fos = new FileOutputStream("private.key");
			fos.write(pkcs8EncodedKeySpec.getEncoded());
			fos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static PrivateKey getPrivateKey() {

		try {
			File privateKeyFile = new File("private.key");
			FileInputStream fis = new FileInputStream("private.key");
			byte[] encodedPrivateKey = new byte[(int)privateKeyFile.length()];
			fis.read(encodedPrivateKey);
			fis.close();
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
			return privateKey;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void savePublicKey(PublicKey publicKey) {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			FileOutputStream fos = new FileOutputStream("public.key");
			fos.write(x509EncodedKeySpec.getEncoded());
			fos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static PublicKey getPublicKey() {
		try {
			File publicKeyFile = new File("public.key");
			FileInputStream fis = new FileInputStream("public.key");
			byte[] encodedPublicKey = new byte[(int) publicKeyFile.length()];
			fis.read(encodedPublicKey);
			fis.close();
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			return publicKey;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] encrypt(PublicKey publicKey, String message) {
		byte[] encodedData = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptedData = cipher.doFinal(message.getBytes());
			encodedData = Base64.getEncoder().withoutPadding().encode(encryptedData);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return encodedData;
	}

	public static String decrypt(PrivateKey privateKey, byte[] encodedData) {
		String message = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decodedData = Base64.getDecoder().decode(encodedData);
			byte[] decryptedBytes = cipher.doFinal(decodedData);
			message = new String(decryptedBytes);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	public static void main(String...args) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(1024);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			savePrivateKey(privateKey);
			savePublicKey(publicKey);

			String message = "The message";
			System.out.println("Message: " + message);
			byte[] encodedDate = encrypt(publicKey, message);
			System.out.println("Decrypted Message: " + decrypt(privateKey, encodedDate));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}