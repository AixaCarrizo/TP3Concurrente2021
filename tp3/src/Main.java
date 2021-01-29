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



    public static void main(String[] args) {
        GenData gd = new GenData(monitor,buffer1,500, 10, lock);
        CPU1 cpu1 = new CPU1(monitor, buffer1, 200, lock);
        CpuController cpu1_poweronoff = new CpuController(monitor, lock);
        Thread log = new Thread(new Log(buffer1, cpu1_poweronoff, gd, cpu1));
        cpu1_poweronoff.start();
        gd.start();
        cpu1.start();
        log.start();

        try{
            cpu1_poweronoff.join();
            gd.join();
            cpu1.join();
            log.join();
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}