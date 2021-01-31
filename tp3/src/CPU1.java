public class CPU1 extends Thread {

    private Monitor monitor;
    private CPU_buffer buffer;
    private int serviceRate;
    private int cpunumber;


    public CPU1(Monitor monitor, CPU_buffer cpuBuffer, int serviceRate, int cpuNumber)
    {
        this.monitor = monitor;
        this.buffer = cpuBuffer;
        this.serviceRate = serviceRate;
        this.cpunumber = cpuNumber - 1;
    }

    @Override
    public  void run() {
        super.run();
        int flag;

        int tasks = 0;

        try {

            while (true) {
                flag = monitor.shoot(9 + cpunumber * 5); // T2 y T9 (Index: 9 y 14)
                if (flag == -1)
                    break;
                if(flag == 1) { // Disparo T2/T9
                    buffer.remove(); // Saco un elemento del buffer

                    Thread.sleep(serviceRate);

                    //lock.lock();
                    monitor.shoot(3 + cpunumber); // Disparo service_rate1-2 (Index:  3 y 4);
                    System.out.println("Termine la tarea nro: "+tasks);
                    tasks++;


                }
                else {
                    Thread.sleep(1000);
                }

            }

        }catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
