public class SHAHashingExample {

	public void displayHashValue(byte[] hashValue) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < hashValue.length; i++) {
			builder.append(Integer.toString((hashValue[i] & 0xff) + 0x100, 16).substring(1));
		}
		System.out.println("Hash Value: " + builder.toString());
	}

	public byte[] getHashValue(String message) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(message.getBytes());
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException ex) {
		}
		return null;
	}

	public static void main(String...args) {
		SHAHashingExample example = new SHAHashingExample();
		String message = "This is a simple text message";
		byte hashValue[] = example.getHashValue(message);
		example.displayHashValue(hashValue);
	}
}