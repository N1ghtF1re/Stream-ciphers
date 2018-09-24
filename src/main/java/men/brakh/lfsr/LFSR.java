package men.brakh.lfsr;

public class LFSR {
    private int polinom[] = {24, 4,3,1};
    private int register =  Integer.parseInt("101001111111111111111011", 2);
    private int mask;

    LFSR() {
        generateMask();
    }

    private byte getBitAtPos(int pos) {
        return (byte) ((byte) (register >> pos - 1) & 1);
    }

    private void generateMask() {
        StringBuilder max = new StringBuilder();
        for(int i = 0; i < polinom[0]; i++) {
            max.append('1');
        }
        mask = Integer.parseInt(max.toString(), 2);
        System.out.println(Integer.toBinaryString(mask));
    }


    public byte[] generateKey(byte[] source) {
        byte[] key = new byte[source.length];
        //System.out.println("111111111111111111111111".length());
        System.out.println(Integer.toBinaryString(register) + " " +Integer.toBinaryString(register).length());
        for(int i = 0; i < key.length; i++) { // Генерируем ключ по байтам
            for(int j = 0; j < 8; j++) {

                byte abortedBit = getBitAtPos(polinom[0]); // Бит, который мы
                key[i] = (byte) (key[i] | (abortedBit << (8 - j - 1))); // Записываем выкинутый бит в ключ

                byte newFirstBit = abortedBit; // Расчитываем новый первый бит как ксор битов, заданных полигоном
                for(int k = 1; k < polinom.length; k++) {
                    newFirstBit ^= getBitAtPos(polinom[k]);
                }
                System.out.println("NEW BIT = " + newFirstBit + "\n");
                register = (register << 1) & mask; // Сдвигаем регистр
                register = register | newFirstBit; // Устанавливаем первый бит согласно вычисленям
                System.out.println(Integer.toBinaryString(register) + " " +Integer.toBinaryString(register).length());
            }
            System.out.println("KEY : " + (Integer.toBinaryString(key[i] & 255)));
        }

        System.out.println(register);
        return new byte[]{0};
    }

    public static void main(String args[]) {
        new LFSR().generateKey(new byte[]{1,2,3,4,5,6,7});
    }
}
