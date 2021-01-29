//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





public class Monitor {

    private Lock lock = new ReentrantLock(); //esto es para que no intenten disparar cosas al mismo tiempo)?

    int maxBufferSize = 10;

    private Condition notEmptyBuffer1; //si esta vacio el buffer
    private Condition notFullBuffer1;//si esta lleno el buffer
    //private final static Condition busyCpu1 = lock.newCondition(); //si el cpu esta ocupado, esta mepa que esta al pedo
    private Condition powerDownCpu1; //si el cpu esta ocupado
    private static int packetCounter;


    private PN pn = new PN();

    CPU_buffer buffer1;

    public Monitor(CPU_buffer buffer1, Lock lock,Condition notEmptyBuffer1,Condition notFullBuffer1,Condition powerDownCpu1) {
        this.buffer1 = buffer1;
        this.lock = lock;
        this.powerDownCpu1 = powerDownCpu1;
        this.notEmptyBuffer1 = notEmptyBuffer1;
        this.notFullBuffer1 = notFullBuffer1;
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



        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;

        switch (index) {

            //PRODUCTOR
            case 0:

                if (pn.isPos(shoot)) {
                    try {
                        if (buffer1.size() == maxBufferSize)
                            notFullBuffer1.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } finally {
                        System.out.println("Hice disparo Arrival_rate");
                        valueToReturn = 1;
                    }
                } else
                    System.out.println("No se puedo realizar el disparo Arrival_rate");

                break;


            //CPU1 intenta apagarse
            case 1:

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


            //Entra tarea al buffer (T1)
            case 2:

                if (pn.isPos(shoot)) {
                    powerDownCpu1.signal();
                    System.out.println("Hice disparo T1");
                    notEmptyBuffer1.signal();
                    valueToReturn = 1;
                } else
                    System.out.println("No se puedo realizar el disparo T1");
                break;


            case 3: // Intenta atender una tarea (T2)

                if(pn.isMarked(2)) //Veo si tengo en CPU_ON
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
                        notFullBuffer1.signal();
                        valueToReturn = 1;
                    }
                }else
                    System.out.println("No se puedo realizar el disparo T2");
                } else
                    System.out.println("No se puedo realizar el disparo T2");
                System.out.println("Cantidad de paquetes: "+ packetCounter);

                break;


            case 4: // termina atender una tarea (T3)

                if (pn.isPos(shoot)) {
                    powerDownCpu1.signal();
                    System.out.println("Hice disparo T3");
                    valueToReturn = 1;

                } else
                    System.out.println("No se puedo realizar el disparo T3");

                break;

            case 5: // para funar los tokens en P6 cuando el cpu ya esta prendido (T5)

                if(pn.isMarked(2)) //Veo si hay tokens en CPU_ON
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

            case 6: //arranca el encendido del cpu (T6)
                if ((pn.isMarked(6))) {

                    if (pn.isPos(shoot)) {
                        System.out.println("Hice disparo T6");
                        valueToReturn = 1;
                    }else
                        System.out.println("No se puedo realizar el disparo T6");

                } else
                    System.out.println("No se puedo realizar el disparo T6");

                break;


            case 7: //enciende el cpu (T7)

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
        }

        if(valueToReturn == 0 && packetCounter == 10){
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
