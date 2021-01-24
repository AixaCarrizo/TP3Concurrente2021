import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static Lock lock=new ReentrantLock();
    private final static Condition notEmptyBuffer1 = lock.newCondition();
    private final static Condition notFullBuffer1 = lock.newCondition();
    private final static Condition powerDownCpu1 = lock.newCondition();
    public static CPU_buffer buffer1= new CPU_buffer();
    private static Monitor monitor = new Monitor(buffer1, lock, notEmptyBuffer1, notFullBuffer1, powerDownCpu1);

    public static void main(String[] args) {

        GenData gd = new GenData(monitor,buffer1,1000);
        gd.run();
    }
}