import java.util.concurrent.locks.Lock;

public class CPU1 extends Thread {

    private Monitor monitor;
    private CPU_buffer buffer;
    private int serviceRate;
    private Lock lock;


    public CPU1(Monitor monitor, CPU_buffer cpuBuffer, int serviceRate, Lock lock)
    {
        this.monitor = monitor;
        this.buffer = cpuBuffer;
        this.serviceRate = serviceRate;
        this.lock = lock;
    }

    @Override
    public  void run() {
        super.run();
        int flag;

        int tasks = 0;

        try {

            while (true) {
                //lock.lock();
                flag = monitor.shoot(3);
                if (flag == -1)
                    break;
                if(flag == 1) { //Disparo T2
                    buffer.remove(); //Saco un elemento del buffer
                    //lock.unlock();


                    Thread.sleep(serviceRate);

                    //lock.lock();
                    monitor.shoot(4); //Disparo T3 (service_rate);
                    System.out.println("Termine la tarea nro: "+tasks);
                    tasks++;

                    //lock.unlock();

                }
                else {
                    //lock.unlock();
                    Thread.sleep(1000);
                }

            }

        }catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
