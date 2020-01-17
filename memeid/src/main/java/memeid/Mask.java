package memeid;

public final class Mask {

    public static final long MASKS_56 = 0xFF00000000000000L;
    public static final long MASKS_48 = 0xFF000000000000L;

    public final static long TIME_LOW = 0xFFFFFFFFL;
    public final static long TIME_MID = 0xFFFF00000000L;
    public final static long TIME_HIGH = 0xFFF000000000000L;

    public final static long CLOCK_SEQ_LOW = 0xFFL;
    public final static long CLOCK_SEQ_HIGH = 0x3F00L;

    public final static long UB32 = 0xFFFFFFFFL;

    public final static long HASHED = 0x30000000000000L;

    public final static long V4_LSB = 0xC000000000000000L;

    public final static long VERSION = 0xF000L;
}
