package men.brakh.lfsr;

/**
 * Class for key generation, encryption and decryption using LFSR
 * About LFSR: https://en.wikipedia.org/wiki/Linear-feedback_shift_register
 * @author Alexandr Pankratiew
 */
public class LFSR implements StreamCipher{
    private static int[] defaultPolinom = {24, 4,3,1};
    private int[] polinom;
    protected long register;
    private long currRegister;
    private int mask;


    public LFSR(long initRegister, int[] polinom) {
        this.register = initRegister;
        this.polinom = polinom;
        generateMask();
    }

    /**
     * Encoder constructor
     * @param initRegister Default register state
     * @param polinom An array of indices of elements of a polynomial whose multiplier is 1
     */
    public LFSR(String initRegister, int[] polinom) {
        this.polinom = polinom;
        if(initRegister.length() > polinom[0]) {
            throw new InvalidRegisterException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister.length(), polinom[0])
            );
        }
        register = Long.parseLong(initRegister, 2);
        generateMask();
    }


    /**
     * Encoder constructor with default poinom (x^24 + x^4 + x^3 + x + 1)
     * @param initRegister An array of indices of elements of a polynomial whose multiplier is 1
     */
    public LFSR(String initRegister) {
        this(initRegister, defaultPolinom);
    }

    /**
     * Convert a key (byte array) to a text representation in binary form
     * @param key Key
     * @return A string with a key in a binary representation
     */
    public static String keyToStr(byte[] key, int bytesCount) {
        StringBuilder strKey = new StringBuilder();
        if (bytesCount > key.length) {
            bytesCount = key.length;
        }
        for(int i = 0; i < bytesCount; i++) {
            StringBuilder binarByte = new StringBuilder(Integer.toBinaryString(key[i] & 255));
            for(int j = binarByte.length(); j < 8; j++) {
                binarByte.insert(0, "0");
            }
            strKey.append(binarByte);
        }
        return strKey.toString();
    }

    /**
     * Get bit at position
     * @param pos index of bit
     * @return bit
     */
    private byte getBitAtPos(int pos) {
        return (byte) ((byte) (currRegister >> pos - 1) & 1);
    }

    /**
     * Generating a mask for register size
     */
    private void generateMask() {
        StringBuilder max = new StringBuilder();
        for(int i = 0; i < polinom[0]; i++) {
            max.append('1');
        }
        mask = Integer.parseInt(max.toString(), 2);
    }


    /**
     * Generating a key
     * @param len Length of key
     * @return Key
     */
    @Override
    public byte[] generateKey(int len) {
        currRegister = register;
        byte[] key = new byte[len];
        for(int i = 0; i < key.length; i++) { // Генерируем ключ по байтам
            for(int j = 0; j < 8; j++) {

                byte abortedBit = getBitAtPos(polinom[0]); // Бит, который мы
                key[i] = (byte) (key[i] | (abortedBit << (8 - j - 1))); // Записываем выкинутый бит в ключ

                byte newFirstBit = abortedBit; // Расчитываем новый первый бит как ксор битов, заданных полигоном
                for(int k = 1; k < polinom.length; k++) {
                    newFirstBit ^= getBitAtPos(polinom[k]);
                }
                currRegister = (currRegister << 1) & mask; // Сдвигаем регистр
                currRegister = currRegister | newFirstBit; // Устанавливаем первый бит согласно вычисленям
            }
        }
        return key;
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

}
