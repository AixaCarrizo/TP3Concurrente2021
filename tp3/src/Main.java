import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static Lock lock=new ReentrantLock();
    private final static Condition notEmptyBuffer1 = lock.newCondition();
    private final static Condition notEmptyBuffer2 = lock.newCondition();
    private final static Condition notFullBuffer1 = lock.newCondition();
    private final static Condition notFullBuffer2 = lock.newCondition();
    private final static Condition powerDownCpu1 = lock.newCondition();
    private final static Condition powerDownCpu2 = lock.newCondition();
    public static CPU_buffer buffer1= new CPU_buffer();
    public static CPU_buffer buffer2= new CPU_buffer();
    private static int dataNumber = 10;

    private static Monitor monitor = new Monitor(buffer1, buffer2, lock, notEmptyBuffer1, notEmptyBuffer2, notFullBuffer1, powerDownCpu1, powerDownCpu2, dataNumber);



    public static void main(String[] args) {
        GenData gd = new GenData(monitor,buffer1, buffer2, 500, dataNumber);
        CPU1 cpu1 = new CPU1(monitor, buffer1,buffer2, 200, 1);
        CPU1 cpu2 = new CPU1(monitor,buffer1,buffer2,200,2);
        CpuController cpu1_poweronoff = new CpuController(monitor, 1);
        CpuController cpu2_poweronoff = new CpuController(monitor,2);
        Thread log = new Thread(new Log(buffer1,buffer2, cpu1_poweronoff,cpu2_poweronoff, gd, cpu1,cpu2));
        cpu1_poweronoff.start();
        cpu2_poweronoff.start();
        gd.start();
        cpu1.start();
        cpu2.start();
        log.start();

        try{
            cpu1_poweronoff.join();
            cpu2_poweronoff.join();
            gd.join();
            cpu1.join();
            cpu2.join();
            log.join();
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("El string es: " + monitor.getstring());
    }
}