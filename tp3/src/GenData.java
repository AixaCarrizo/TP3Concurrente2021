public class GenData extends Thread {

    private Monitor monitor;
    private CPU_buffer buffer;
    //private CPU_buffer buffer2;
    private int arrivalRate;

    public GenData(Monitor monitor, CPU_buffer cpuBuffer, int arrivalRate)
    {
    this.monitor = monitor;
    this.buffer = cpuBuffer;
    this.arrivalRate = arrivalRate;
    //this.buffer2=cpuBuffer2;
    }

  @Override
    public  void run() {
      super.run();

      try {
          int nroData = 0;

          while (true) {

              //double choose = Math.random()*100+1;

              monitor.shoot(0); //Disparo Arrival_rate

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
