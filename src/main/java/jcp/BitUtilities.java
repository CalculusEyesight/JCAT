package jcp;

public class BitUtilities {
    public static int ToUByte(byte value){
        return  value &0xFF;
    }

    public static int ToUShort(short value){
        return  value & 0xFFFF;
    }
    
    public static long ToUint32(int value ){
        return value & 0xFFFFFFFFL;
    }
}
