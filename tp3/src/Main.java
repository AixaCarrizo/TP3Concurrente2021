import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static Lock lock=new ReentrantLock();
    private final static Condition notEmptyBuffer1 = lock.newCondition();
    private final static Condition notFullBuffer1 = lock.newCondition();
    private final static Condition powerDownCpu1 = lock.newCondition();
    public static CPU_buffer buffer1= new CPU_buffer();
    //public static CPU_buffer buffer2= new CPU_buffer();

    private static Monitor monitor = new Monitor(buffer1, lock, notEmptyBuffer1, notFullBuffer1, powerDownCpu1);

    public static CPU1 cpu1 = new CPU1(monitor, buffer1, 200);
    public static CpuController cpu1_poweronoff = new CpuController(monitor);



    public static void main(String[] args) {
        GenData gd = new GenData(monitor,buffer1,1000);

        cpu1.run();
        cpu1_poweronoff.run();
        gd.run();
    }
}