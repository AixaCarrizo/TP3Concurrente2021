public class Politica {

    private final CPU_buffer buffer1;
    private final CPU_buffer buffer2;

    public Politica (CPU_buffer buffer1, CPU_buffer buffer2) {
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
    }

    int bufferPolitic () {
        if ( (buffer1.size () > buffer2.size () ) && ( buffer2.size() < buffer2.getMaxSize() ) ) {
            return 12;
        } else
            return 11;
    }
}
