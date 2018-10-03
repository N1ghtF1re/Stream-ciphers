package men.brakh.lfsr;

import java.io.*;

public class FilesEncoder {
    StreamCipher cipher;
    public FilesEncoder(StreamCipher cipher) {
        this.cipher = cipher;
    }

    private byte[] readFile(String filePath) throws IOException {
        byte[] content;
        try{
            FileInputStream fis = new FileInputStream(new File(filePath));
            content = fis.readAllBytes();
            fis.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
        return content;

    }
    private void writeFile(String filePath, byte[] content) throws IOException {

        try{
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            fos.write(content);
            fos.close();
        }catch (Exception e) {
            throw new IOException(e);
        }
    }


    private String getOutEncodePath(String filePath) {
        return filePath + ".encoded";
    }
    private String getOutDecodePath(String filePath) {
        return filePath + ".decoded";
    }
    private String getOutKeyPath(String filePath) {
        return filePath + "-key.txt";
    }



    public String[] encode(String filePath) throws IOException {
        byte[] plainText = readFile(filePath);
        byte[] cipherText = cipher.encrypt(plainText);
        writeFile(getOutEncodePath(filePath), cipherText);

        byte[] key = cipher.generateKey(plainText.length);
        String strKey = LFSR.keyToStr(key, plainText.length);
        String strCipher = LFSR.keyToStr(cipherText, 15);
        String strPlain = LFSR.keyToStr(plainText, 15);
        writeFile(getOutKeyPath(filePath), strKey.getBytes());

        return new String[]{strKey, strCipher, strPlain};
    }

    public String[]  decode(String filePath) throws IOException {
        byte[] cipherText = readFile(filePath);
        byte[] plainText = cipher.encrypt(cipherText);
        writeFile(getOutDecodePath(filePath), plainText);

        byte[] key = cipher.generateKey(plainText.length);
        String strKey = LFSR.keyToStr(key, 15);
        String plainStr = LFSR.keyToStr(plainText, 15);
        String strCipher = LFSR.keyToStr(cipherText, 15);
        writeFile(getOutKeyPath(filePath), strKey.getBytes());

        return new String[]{strKey, plainStr, strCipher};
    }
}
