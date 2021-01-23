public class GenData extends Thread {

    private Monitor monitor;
    private CPU_buffer buffer;
    private int arrivalRate;

    public GenData(Monitor monitor, CPU_buffer cpuBuffer,int arrivalRate)
    {
    this.monitor = monitor;
    this.buffer = cpuBuffer;
    this.arrivalRate = arrivalRate;
    }

  @Override
    public  void run() {
      super.run();

      try {
          int nroData = 0;

          while (true) {
              monitor.shoot(0); //Disparo Arrival_rate

              Thread.sleep(arrivalRate);

              monitor.shoot(2); //Disparo T1
              buffer.add("Dato numero: " + nroData); //Agrego un elemento al buffer
              System.out.println("Dato numero: " + nroData);
              nroData++;
          }
      }catch(InterruptedException e)
      {
          e.printStackTrace();
      }
  }
}
