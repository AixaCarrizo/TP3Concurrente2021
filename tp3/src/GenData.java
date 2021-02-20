public class GenData extends Thread {

    private Monitor monitor;
    private CPU_buffer buffer1;
    private CPU_buffer buffer2;
    private int arrivalRate;
    private int dataNumber;

    public GenData(Monitor monitor, CPU_buffer buffer1, CPU_buffer buffer2, int arrivalRate, int dataNumber)
    {
    this.monitor = monitor;
    this.buffer1 = buffer1;
    this.arrivalRate = arrivalRate;
    this.dataNumber = dataNumber;
    this.buffer2= buffer2;
    }

  @Override
    public  void run() {
      super.run();

      try {
          int nroData = 0;

          while (dataNumber != 0) {

              int cpuId;

              cpuId = monitor.shoot(0); //Disparo Arrival_rate

              Thread.sleep(arrivalRate);

              //Cambiar transiciones despues

              if(cpuId == 11)
              {
                  monitor.shoot(5); //Disparo T1
                  buffer1.add("Dato numero: " + nroData); //Agrego un elemento al buffer
                  System.out.println("Dato numero: " + nroData);
                  nroData++;

              } else if(cpuId == 12)
              {
                  monitor.shoot(13); // CAMBIE INDICE POR TRANSICION T8
                  buffer2.add("Dato numero: " + nroData); //Agrego un elemento al buffer (Cambiar por buffer2)
                  System.out.println("Dato numero: " + nroData);
                  nroData++;
              }

              dataNumber--;
          }
      }catch(InterruptedException e)
      {
          e.printStackTrace();
      }

      System.out.println(("El generador de datos ha finalizado. Good Bye!"));
  }
}
