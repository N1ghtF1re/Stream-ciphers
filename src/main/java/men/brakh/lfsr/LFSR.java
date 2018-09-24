package men.brakh.lfsr;

public class LFSR {
    private static int[] defaultPolinom = {24, 4,3,1};
    private int[] polinom;
    private int register;
    private int currRegister;
    private int mask;

    public LFSR(String initRegister, int[] polinom) {
        this.polinom = polinom;
        if(initRegister.length() > polinom[0]) {
            throw new InvalidRegisterException(String.format("The length of the obtained register (%s) exceeds the" +
                    " maximum degree of the polynomial (%s)", initRegister.length(), polinom[0])
            );
        }
        register = Integer.parseInt(initRegister, 2);
        generateMask();
    }

    public LFSR(String initRegister) {
        this(initRegister, defaultPolinom);
    }

    public static String keyToStr(byte[] key) {
        StringBuilder strKey = new StringBuilder();
        for(byte keyByte : key) {
            StringBuilder binarByte = new StringBuilder(Integer.toBinaryString(keyByte & 255));
            for(int i = binarByte.length(); i < 8; i++) {
                binarByte.insert(0, "0");
            }
            strKey.append(binarByte);
        }
        return strKey.toString();
    }

    private byte getBitAtPos(int pos) {
        return (byte) ((byte) (currRegister >> pos - 1) & 1);
    }

    private void generateMask() {
        StringBuilder max = new StringBuilder();
        for(int i = 0; i < polinom[0]; i++) {
            max.append('1');
        }
        mask = Integer.parseInt(max.toString(), 2);
    }


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

    public byte[] encrypt(byte[] plainBytes) {
        byte[] key = generateKey(plainBytes.length);
        byte[] cipherBytes = new byte[plainBytes.length];
        for(int i = 0; i < plainBytes.length; i++) {
            cipherBytes[i] = (byte) (plainBytes[i] ^ key[i]);
        }
        return cipherBytes;
    }

    public byte[] decrypt(byte[] cipherBytes) {
        return encrypt(cipherBytes);
    }

}
