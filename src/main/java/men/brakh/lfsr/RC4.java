package men.brakh.lfsr;

public class RC4 implements StreamCipher{
    byte[] S = new byte[256];

    int x = 0;
    int y = 0;

    byte[] toBytes(int i)
    {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }
    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }


    public RC4(String strKey) {
        int intKey = Integer.parseInt(strKey, 2);
        byte[] key = toBytes(intKey);
        init(key);
    }

    // Key-Scheduling Algorithm
    private void init(byte[] key)
    {
        int keyLength = key.length;

        for (int i = 0; i < 256; i++)
        {
            S[i] = (byte)i;
        }

        int j = 0;
        for (int i = 0; i < 256; i++)
        {
            j = (j + unsignedToBytes(S[i]) + unsignedToBytes(key[i % keyLength])) % 256;
            byte tmp = S[i];
            S[i] = S[j];
            S[j] = tmp;

        }
    }


    private byte keyItem()
    {
        x = (x + 1) % 256;
        y = (y + unsignedToBytes(S[x])) % 256;

        byte temp = S[x];
        S[x] = S[y];
        S[y] = temp;

        return S[(unsignedToBytes(S[x]) + unsignedToBytes(S[y])) % 256];
    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        byte[] key = generateKey(plaintext.length);
        byte[] cipher = new byte[plaintext.length];

        for (int m = 0; m < plaintext.length; m++){
            cipher[m] = (byte)(plaintext[m] ^ key[m]);
        }

        return cipher;
    }

    @Override
    public byte[] decrypt(byte[] cipherBytes) {
        return encrypt(cipherBytes);
    }

    @Override
    public byte[] generateKey(int len) {
        byte[] key = new byte[len];
        for(int i = 0; i < len; i++) {
            key[i] = keyItem();
        }
        return key;
    }
}
