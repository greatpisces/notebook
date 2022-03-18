public class SymmetricKeyStoreCreation {

	private static KeyStore createKeyStore(String filename, String password) {
		try {
			File file = new File(filename);
			final KeyStore keyStore = KeyStore.getInstance("JCEKS");
			if(file.exists()) {
				keyStore.load(new FileInputStream(file), password.toCharArray());
			} else {
				keyStore.load(null, null);
				keyStore.store(new FileOutputStream(filename), password.toCharArray());
			}
			return keyStore;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String...args) {

		try {
			final String keyStoreFile = "secretkeystore.jks";
			KeyStore keyStore = createKeyStore(keyStoreFile, "keystorepassword");
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			SecretKey secretKey = keyGenerator.generateKey();
			KeyStore.SecretKeyEntry keyStoreEntry = new KeyStore.SecretKeyEntry(secretKey);
			KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection("keypassword".toCharArray());
			keyStore.setEntry("secretKey", keyStoreEntry, keyPassword);
			keyStore.store(new FileOutputStream(keyStoreFile), "keystorepassword".toCharArray());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}