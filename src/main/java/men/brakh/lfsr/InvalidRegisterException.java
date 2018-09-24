package men.brakh.lfsr;

public class InvalidRegisterException extends  RuntimeException{
    public InvalidRegisterException() {
        super();
    }
    public InvalidRegisterException(String s) {
        super(s);
    }
}
