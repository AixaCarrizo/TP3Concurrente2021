import java.util.concurrent.locks.Lock;

public class CpuController extends Thread {

    private Monitor monitor;
    private Lock lock;

    public CpuController(Monitor monitor, Lock lock)
    {
        this.monitor = monitor;
        this.lock = lock;
    }

    @Override
    public  void run() {
        super.run();

        while (true) {
            //lock.lock();
            if (monitor.shoot(5) != 1) { //intenta disparar t5, si no puede, intenta prender el cpu
                if (monitor.shoot(6) == 1) { //si pudo disparar t6
                    monitor.shoot(7); //dispara t7 tambien
                } else
                    monitor.shoot(1); //si no pudo disparar ni t5 ni t6, entonces lo intenta apagar
            }
            //lock.unlock();

        }
    }


}
