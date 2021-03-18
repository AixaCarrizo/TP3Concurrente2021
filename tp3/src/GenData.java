public class GenData extends Thread {

    private final Monitor monitor;
    private final CPU_buffer buffer1;
    private final CPU_buffer buffer2;
    private final int arrivalRate;
    private final int dataNumber;

    public GenData (Monitor monitor, CPU_buffer buffer1, CPU_buffer buffer2, int arrivalRate, int dataNumber) {
        this.monitor = monitor;
        this.buffer1 = buffer1;
        this.arrivalRate = arrivalRate;
        this.dataNumber = dataNumber;
        this.buffer2 = buffer2;
    }

    @Override
    public void run () {
        super.run ();

        try {
            int nroData = 1;
            while (dataNumber >= nroData) {
                int cpuId;
                Thread.sleep (arrivalRate);
                cpuId = monitor.shoot (0); //Disparo Arrival_rate
                if (cpuId > 0) {
                    if (cpuId == 11) {
                        if (monitor.shoot (5) > 0) //Disparo T1
                            buffer1.add ("Dato numero: " + nroData); //Agrego un elemento al buffer

                    } else if (cpuId == 12) {
                        if (monitor.shoot (13) > 0) // CAMBIE INDICE POR TRANSICION T8
                            buffer2.add ("Dato numero: " + nroData); //Agrego un elemento al buffer (Cambiar por buffer2)
                    }
                    System.out.println ("GenData       : Genero dato numero " + nroData);
                    nroData++;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        System.out.println (("GenData       : Good Bye!"));
    }
}
