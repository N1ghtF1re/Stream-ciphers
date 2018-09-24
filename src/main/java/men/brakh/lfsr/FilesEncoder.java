package men.brakh.lfsr;

import java.io.*;

public class FilesEncoder {

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
        return filePath.substring(0, filePath.lastIndexOf('.')) + ".encoded";
    }
    private String getOutDecodePath(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf('.')) + ".decoded";
    }
    private String getOutKeyPath(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf('.')) + "-key.txt";
    }



    public String encode(String filePath, String register) throws IOException {
        byte[] plainText = readFile(filePath);
        LFSR lfsr = new LFSR(register);
        byte[] cipherText = lfsr.encrypt(plainText);
        writeFile(getOutEncodePath(filePath), cipherText);

        byte[] key = lfsr.generateKey(plainText.length);
        String strKey = LFSR.keyToStr(key);
        writeFile(getOutKeyPath(filePath), strKey.getBytes());

        return strKey;
    }

    public String decode(String filePath, String register) throws IOException {
        byte[] cipherText = readFile(filePath);
        LFSR lfsr = new LFSR(register);
        byte[] plainText = lfsr.encrypt(cipherText);
        writeFile(getOutDecodePath(filePath), plainText);

        byte[] key = lfsr.generateKey(plainText.length);
        String strKey = LFSR.keyToStr(key);
        writeFile(getOutKeyPath(filePath), strKey.getBytes());

        return strKey;
    }
}
