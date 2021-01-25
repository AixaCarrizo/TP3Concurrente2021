public class cpuTask extends Thread {

    private Monitor monitor;
    private CPU_buffer buffer;
    private int serviceRate;

    public cpuTask(Monitor monitor, CPU_buffer cpuBuffer,int serviceRate)
    {
        this.monitor = monitor;
        this.buffer = cpuBuffer;
        this.serviceRate = serviceRate;
    }

    @Override
    public  void run() {
        super.run();

        int tasks = 0;

        try {

            while (true) {
                monitor.shoot(4); //Disparo T2
                buffer.remove(); //Saco un elemento del buffer
                Thread.sleep(serviceRate);
                monitor.shoot(5); //Disparo T3 (service_rate);
                System.out.println("Termine la tarea nro: "+tasks);
                tasks++;

            }
        }catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
