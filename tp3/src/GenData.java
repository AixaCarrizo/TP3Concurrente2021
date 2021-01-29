import java.util.concurrent.locks.Lock;

public class GenData extends Thread {

    private Monitor monitor;
    private CPU_buffer buffer;
    //private CPU_buffer buffer2;
    private int arrivalRate;
    private int dataNumber;
    private Lock lock;

    public GenData(Monitor monitor, CPU_buffer cpuBuffer, int arrivalRate, int dataNumber, Lock lock)
    {
    this.monitor = monitor;
    this.buffer = cpuBuffer;
    this.arrivalRate = arrivalRate;
    this.dataNumber = dataNumber;
    this.lock = lock;
    //this.buffer2=cpuBuffer2;
    }

  @Override
    public  void run() {
      super.run();

      try {
          int nroData = 0;

          while (dataNumber != 0) {

              //double choose = Math.random()*100+1;

              //lock.lock();
              monitor.shoot(0); //Disparo Arrival_rate
              //lock.unlock();

              Thread.sleep(arrivalRate);

              //Cambiar transiciones despues
              /*
              if(choose<50)
              {
                  monitor.shoot(2); //Disparo T1
                  buffer.add("Dato numero: " + nroData); //Agrego un elemento al buffer
                  System.out.println("Dato numero: " + nroData);
                  nroData++;
              }else
              {
                  monitor.shoot(2); //
                  buffer.add("Dato numero: " + nroData); //Agrego un elemento al buffer (Cambiar por buffer2)
                  System.out.println("Dato numero: " + nroData);
                  nroData++;
              } */
              //lock.lock();
              monitor.shoot(2); //Disparo T1
              buffer.add("Dato numero: " + nroData); //Agrego un elemento al buffer
              System.out.println("Dato numero: " + nroData);
              nroData++;
              dataNumber--;
              //lock.unlock();
          }
      }catch(InterruptedException e)
      {
          e.printStackTrace();
      }
  }
}
