//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





public class Monitor {

    private Lock lock; //esto es para que no intenten disparar cosas al mismo tiempo)?

    int maxBufferSize = 10;

    private Condition notEmptyBuffer1; //si esta vacio el buffer
    private Condition notEmptyBuffer2; //si esta vacio el buffer
    private Condition notFullBuffer;//si esta lleno el buffer
    //private final static Condition busyCpu1 = lock.newCondition(); //si el cpu esta ocupado, esta mepa que esta al pedo
    private Condition powerDownCpu1; //si el cpu esta ocupado
    private Condition powerDownCpu2; //si el cpu esta ocupado
    private static int packetCounter;


    private PN pn = new PN();

    CPU_buffer buffer1;
    CPU_buffer buffer2;

    public Monitor(CPU_buffer buffer1, CPU_buffer buffer2, Lock lock,Condition notEmptyBuffer1,Condition notEmptyBuffer2
            ,Condition notFullBuffer, Condition powerDownCpu1, Condition powerDownCpu2) {
        this.buffer1 = buffer1;
        this.buffer1 = buffer2;
        this.lock = lock;
        this.powerDownCpu1 = powerDownCpu1;
        this.powerDownCpu2 = powerDownCpu2;
        this.notEmptyBuffer1 = notEmptyBuffer1;
        this.notEmptyBuffer2 = notEmptyBuffer2;
        this.notFullBuffer = notFullBuffer;
        this.packetCounter = 0;
    }


    /**
     * T0: Arrival_rate
     * T1: Power_down_threshold
     * T2: T1
     * T3: T2
     * T4: T3
     * T5: T5
     * T6: T6
     * T7: T7
     */


    public int shoot(int index) {  //Dispara una transicion (index) devuelve 1 si pudo hacerla y 0 si no



        int valueToReturn = 0;
        lock.lock();



        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;

        switch (index) {

            // ACTUALIZADO
            //PRODUCTOR
            case 0:

                if (pn.isPos(shoot)) {
                    try {
                        if (buffer1.size() == maxBufferSize && buffer2.size() == maxBufferSize)
                            notFullBuffer.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } finally {
                        System.out.println("Hice disparo Arrival_rate");
                        valueToReturn = 1;
                    }
                } else
                    System.out.println("No se puedo realizar el disparo Arrival_rate");

                break;

            /*-----------------------------------------------------------------------*/

            // ACTUALIZADO
            case 1: //CPU1 intenta apagarse

                if (pn.isPos(shoot)) {
                    System.out.println("Hice disparo Power_down");
                    valueToReturn = 1;
                } else
                    System.out.println("No se puedo realizar el disparo Power_down");

                try {
                    powerDownCpu1.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                break;

            // ACTUALIZADO
            case 2: //CPU1 intenta apagarse

                if (pn.isPos(shoot)) {
                    System.out.println("Hice disparo Power_down_2");
                    valueToReturn = 1;
                } else
                    System.out.println("No se puedo realizar el disparo Power_down_2");

                try {
                    powerDownCpu2.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                break;

            /*-----------------------------------------------------------------------*/

            // ACTUALIZADO
            case 5: // Entra tarea al buffer 1 (T1)

                if (pn.isPos(shoot)) {
                    powerDownCpu1.signal();
                    System.out.println("Hice disparo T1");
                    notEmptyBuffer1.signal();
                    valueToReturn = 1;
                } else
                    System.out.println("No se puedo realizar el disparo T1");
                break;

            // ACTUALIZADO
            case 13: // Entra tarea al buffer 1 (T1)

                if (pn.isPos(shoot)) {
                    powerDownCpu2.signal();
                    System.out.println("Hice disparo T8");
                    notEmptyBuffer2.signal();
                    valueToReturn = 1;
                } else
                    System.out.println("No se puedo realizar el disparo T8");
                break;

            /*-----------------------------------------------------------------------*/

            // ACTUALIZADO
            case 9: // Intenta atender una tarea (T2)

                if(pn.isMarked(4)) //Veo si tengo en CPU_ON
                {
                if (pn.isPos(shoot)) {
                    try {
                        if (buffer1.size() == 0)
                            notEmptyBuffer1.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } finally {
                        System.out.println("Hice disparo T2");
                        packetCounter++;
                        notFullBuffer.signal();
                        valueToReturn = 1;
                    }
                }else
                    System.out.println("No se puedo realizar el disparo T2");
                } else
                    System.out.println("No se puedo realizar el disparo T2");
                System.out.println("Cantidad de paquetes: "+ packetCounter);

                break;

            // ACTUALIZADO
            case 14: // Intenta atender una tarea (T9)
                if(pn.isMarked(5)) //Veo si tengo en CPU_ON_2
                {
                    if (pn.isPos(shoot)) {
                        try {
                            if (buffer2.size() == 0)
                                notEmptyBuffer2.await();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } finally {
                            System.out.println("Hice disparo T9");
                            packetCounter++;
                            notFullBuffer.signal();
                            valueToReturn = 1;
                        }
                    }else
                        System.out.println("No se puedo realizar el disparo T9");
                } else
                    System.out.println("No se puedo realizar el disparo T9");
                System.out.println("Cantidad de paquetes: "+ packetCounter);

                break;

            /*-----------------------------------------------------------------------*/

            // ACTUALIZADO
            case 3: // termina atender una tarea (service_rate)

                if (pn.isPos(shoot)) {
                    powerDownCpu1.signal();
                    System.out.println("Hice disparo service_rate");
                    valueToReturn = 1;

                } else
                    System.out.println("No se puedo realizar el disparo service_rate");

                break;

            // ACTUALIZADO
            case 4: // termina atender una tarea (service_rate_2)

                if (pn.isPos(shoot)) {
                    powerDownCpu2.signal();
                    System.out.println("Hice disparo service_rate_2");
                    valueToReturn = 1;

                } else
                    System.out.println("No se puedo realizar el disparo service_rate_2");

                break;

                /*-----------------------------------------------------------------------*/

            // ACTUALIZADO
            case 10: // Para funar los tokens en P6 cuando el cpu ya esta prendido (T5)

                if(pn.isMarked(4)) //Veo si hay tokens en CPU_ON
                {
                if (pn.isPos(shoot)) {
                    try {
                        powerDownCpu1.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Hice disparo T5");
                    valueToReturn = 1;
                }else
                    System.out.println("No se puedo realizar el disparo T5");
                } else
                    System.out.println("No se puedo realizar el disparo T5");

                break;

            // ACTUALIZADO
            case 6: // Para funar los tokens en P13 cuando el cpu ya esta prendido (T5)

                if(pn.isMarked(5)) //Veo si hay tokens en CPU_ON_2
                {
                    if (pn.isPos(shoot)) {
                        try {
                            powerDownCpu2.await();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Hice disparo T12");
                        valueToReturn = 1;
                    }else
                        System.out.println("No se puedo realizar el disparo T12");
                } else
                    System.out.println("No se puedo realizar el disparo T12");

                break;

            /*-----------------------------------------------------------------------*/

            // ACTUALIZADO
            case 11: //arranca el encendido del cpu (T6)
                if ((pn.isMarked(11))) {        // P6

                    if (pn.isPos(shoot)) {
                        System.out.println("Hice disparo T6");
                        valueToReturn = 1;
                    }else
                        System.out.println("No se puedo realizar el disparo T6");

                } else
                    System.out.println("No se puedo realizar el disparo T6");

                break;

            // ACTUALIZADO
            case 7: //arranca el encendido del cpu (T13)
                if ((pn.isMarked(13))) {         // P13

                    if (pn.isPos(shoot)) {
                        System.out.println("Hice disparo T13");
                        valueToReturn = 1;
                    }else
                        System.out.println("No se puedo realizar el disparo T13");

                } else
                    System.out.println("No se puedo realizar el disparo T13");

                break;

            /*-----------------------------------------------------------------------*/

            // ACTUALIZADO
            case 12: //enciende el cpu (T7)

                if (pn.isPos(shoot)) {
                    try {
                        powerDownCpu1.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Hice disparo T7");
                    valueToReturn = 1;
                } else
                    System.out.println("No se puedo realizar el disparo T7");
                break;

            // ACTUALIZADO
            case 8: //enciende el cpu (T14)

                if (pn.isPos(shoot)) {
                    try {
                        powerDownCpu2.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("Hice disparo T14");
                    valueToReturn = 1;
                } else
                    System.out.println("No se puedo realizar el disparo T14");
                break;
        }

        if(valueToReturn == 0 && packetCounter == 5){
                lock.unlock();
                return -1;
        }

        else{
            lock.unlock();
            return valueToReturn;
        }
        //return 0;
    }
}
