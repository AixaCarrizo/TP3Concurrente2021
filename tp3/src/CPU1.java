public class CPU1 extends Thread {

    private Monitor monitor;
    private CPU_buffer buffer;
    private CPU_buffer buffer2;
    private int serviceRate;
    private int cpunumber;


    public CPU1(Monitor monitor, CPU_buffer cpuBuffer,CPU_buffer cpuBuffer2, int serviceRate, int cpuNumber)
    {
        this.monitor = monitor;
        this.buffer = cpuBuffer;
        this.serviceRate = serviceRate;
        this.cpunumber = cpuNumber - 1;
        this.buffer2 = cpuBuffer2;
    }

    @Override
    public  void run() {
        super.run();
        int flag;

        int tasks = 0;

        try {

            while (true) {
                flag = monitor.shoot(9 + cpunumber * 5); // T2 y T9 (Index: 9 y 14)
                if (flag == -1){
                    System.out.println(("Trabajador CPU " + (cpunumber+1) + " a finalizado. Good Bye!"));
                    break;
                }
                if(flag == 1) { // Disparo T2/T9
                    if(cpunumber==0)
                        buffer.remove(); // Saco un elemento del buffer 1
                    else
                        buffer2.remove(); //Saco un elemento del buffer 2

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
