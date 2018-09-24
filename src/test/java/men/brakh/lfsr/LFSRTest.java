package men.brakh.lfsr;

import org.junit.Test;

import static org.junit.Assert.*;

public class LFSRTest {

    @Test
    public void generateKeyTest() {
        String result = "1111010110010001";
        LFSR lfsr = new LFSR("1111", new int[]{4,1});
        byte[] key = lfsr.generateKey(2);
        assertEquals(LFSR.keyToStr(key), result);
    }

    @Test
    public void generateKeyTest2() {
        String result = "101001111111111111111011";
        String register = "101001111111111111111011";
        LFSR lfsr = new LFSR(register);
        byte[] key = lfsr.generateKey(3);
        assertEquals(LFSR.keyToStr(key), result);
    }
}