public class Politica {

    private final CPU_buffer buffer1;
    private final CPU_buffer buffer2;

    static int prevBuff = 12;

    public Politica (CPU_buffer buffer1, CPU_buffer buffer2) {
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
    }

    int bufferPolitic () {
        if (prevBuff == 11) {
            if (buffer1.size () > buffer2.size ()) {
                prevBuff = 12;
                return 12;
            } else
                return 11;
        } else if (prevBuff == 12) {
            if (buffer2.size () > buffer1.size ()) {
                prevBuff = 11;
                return 11;
            } else
                return 12;
        }
        return -1;
    }
}
