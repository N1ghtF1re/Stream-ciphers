package men.brakh.lfsr;

public class Geffe implements StreamCipher {
    private static int[] defaultPolinom1 = {24, 4,3,1};
    private static int[] defaultPolinom2 = {32, 28, 27, 1};
    private static int[] defaultPolinom3 = {40, 21, 19, 2};
    private int[] polinom1;
    private int[] polinom2;
    private int[] polinom3;
    long register1;
    long register2;
    long register3;


    public Geffe(String initRegister1, String initRegister2, String initRegister3, int[] polinom1, int polinom2[], int polinom3[]) {
        this.polinom1 = polinom1;
        this.polinom2 = polinom2;
        this.polinom3 = polinom3;
        if(initRegister1.length() > polinom1[0]) {
            throw new InvalidRegisterException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister1.length(), polinom1[0])
            );
        }
        if(initRegister2.length() > polinom2[0]) {
            throw new InvalidRegisterException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister2.length(), polinom2[0])
            );
        }
        if(initRegister3.length() > polinom3[0]) {
            throw new InvalidRegisterException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister3.length(), polinom3[0])
            );
        }
        register1 = Long.parseLong(initRegister1, 2);
        register2 = Long.parseLong(initRegister1, 2);
        register3 = Long.parseLong(initRegister1, 2);
    }

    public Geffe(String initRegister1, String initRegister2, String initRegister3) {
        this(initRegister1, initRegister2, initRegister3, defaultPolinom1, defaultPolinom2, defaultPolinom3);
    }

    @Override
    public byte[] encrypt(byte[] plainBytes) {
        byte[] key = generateKey(plainBytes.length);
        byte[] cipherBytes = new byte[plainBytes.length];
        for(int i = 0; i < plainBytes.length; i++) {
            cipherBytes[i] = (byte) (plainBytes[i] ^ key[i]);
        }
        return cipherBytes;
    }

    @Override
    public byte[] decrypt(byte[] cipherBytes) {
        return encrypt(cipherBytes);
    }

    @Override
    public byte[] generateKey(int len) {
        LFSR lfsr = new LFSR(register1, polinom1);
        byte[] key1 = lfsr.generateKey(len);
        lfsr = new LFSR(register2, polinom2);
        byte[] key2 = lfsr.generateKey(len);
        lfsr = new LFSR(register3, polinom3);
        byte[] key3 = lfsr.generateKey(len);

        byte[] key = new byte[len];
        for(int i = 0; i < len; i++) {
            key[i] = (byte) ((byte) (key1[i] & key2[i]) | (~key1[i] & key3[i]));
        }
        return key;
    }


}
