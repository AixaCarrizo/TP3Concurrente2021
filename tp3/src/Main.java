import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static Lock lock = new ReentrantLock ();
    private final static Condition notEmptyBuffer1 = lock.newCondition ();
    private final static Condition notEmptyBuffer2 = lock.newCondition ();
    private final static Condition powerDownCpu1 = lock.newCondition ();
    private final static Condition powerDownCpu2 = lock.newCondition ();
    public static CPU_buffer buffer1 = new CPU_buffer ();
    public static CPU_buffer buffer2 = new CPU_buffer ();
    private final static int dataNumber = 1000;

    private final static Monitor monitor = new Monitor (buffer1, buffer2, lock, notEmptyBuffer1, notEmptyBuffer2, powerDownCpu1, powerDownCpu2, dataNumber);


    public static void main (String[] args) {
        GenData gd = new GenData (monitor, buffer1, buffer2, 25, dataNumber);
        CPU cpu1 = new CPU (monitor, buffer1, buffer2, 50, 1);
        CPU cpu2 = new CPU (monitor, buffer1, buffer2, 50, 2);
        CpuController cpu1_poweronoff = new CpuController (monitor, 1);
        CpuController cpu2_poweronoff = new CpuController (monitor, 2);
        Thread log = new Thread (new Log (buffer1, buffer2, cpu1_poweronoff, cpu2_poweronoff, cpu1, cpu2));
        cpu1_poweronoff.start ();
        cpu2_poweronoff.start ();
        gd.start ();
        cpu1.start ();
        cpu2.start ();
        log.start ();

        try {
            cpu1_poweronoff.join ();
            cpu2_poweronoff.join ();
            gd.join ();
            cpu1.join ();
            cpu2.join ();
            log.join ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        try {
            File file = new File ("prueba.txt");
            // Si el archivo no existe es creado
            if (!file.exists ()) {
                file.createNewFile ();
            }
            FileWriter fw = new FileWriter (file);
            BufferedWriter bw = new BufferedWriter (fw);
            bw.write (monitor.getTransitions ());
            bw.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}