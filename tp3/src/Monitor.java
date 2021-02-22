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
    private Politica politic;
    private static String transitions = new String("");
    private static String[] numTransitions = {"TO", "T4", "T11", "T3", "T10", "T1", "T12", "T13", "T14", "T2", "T5", "T6", "T7", "T8", "T9"};
    private static int dataNumber;


    private PN pn = new PN();

    CPU_buffer buffer1;
    CPU_buffer buffer2;

    public Monitor(CPU_buffer buffer1, CPU_buffer buffer2, Lock lock,Condition notEmptyBuffer1,Condition notEmptyBuffer2
            ,Condition notFullBuffer, Condition powerDownCpu1, Condition powerDownCpu2, int dataNumber) {
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
        this.lock = lock;
        this.powerDownCpu1 = powerDownCpu1;
        this.powerDownCpu2 = powerDownCpu2;
        this.notEmptyBuffer1 = notEmptyBuffer1;
        this.notEmptyBuffer2 = notEmptyBuffer2;
        this.notFullBuffer = notFullBuffer;
        this.packetCounter = 0;
        this.politic = new Politica(buffer1, buffer2);
        this.dataNumber = dataNumber;
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

        public String getstring() {
            return transitions;
        }

        public int shoot(int index) {  //Dispara una transicion (index) devuelve 1 si pudo hacerla y 0 si no


        int valueToReturn = 0;
        lock.lock();

        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;

        switch (index) {

            //PRODUCTOR
            case 0:
                if (pn.isPos(shoot)) {
                    try {
                        if (buffer1.size() == maxBufferSize && buffer2.size() == maxBufferSize)
                            notFullBuffer.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } finally {
                        valueToReturn = politic.bufferPolitic();
                    }
                }
                break;

            /*-----------------------------------------------------------------------*/

            case 1: //CPU1 intenta apagarse
                if (pn.isPos(shoot)) {
                    System.out.println("Hice disparo Power_down");
                    valueToReturn = 1;
                }
                try {
                    powerDownCpu1.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                break;
            case 2: //CPU1 intenta apagarse

                if (pn.isPos(shoot)) {
                    valueToReturn = 1;
                }
                try {
                    powerDownCpu2.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                break;

            /*-----------------------------------------------------------------------*/

            case 5: // Entra tarea al buffer 1 (T1)
                if (pn.isPos(shoot)) {
                    powerDownCpu1.signal();
                    notEmptyBuffer1.signal();
                    valueToReturn = 1;
                }
                break;
            case 13: // Entra tarea al buffer 2 (T8)
                if (pn.isPos(shoot)) {
                    powerDownCpu2.signal();
                    notEmptyBuffer2.signal();
                    valueToReturn = 1;
                }
                break;
            /*-----------------------------------------------------------------------*/

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
                    }
                }
                System.out.println("Cantidad de paquetes: " + packetCounter);
                break;
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
                            packetCounter++;
                            notFullBuffer.signal();
                            valueToReturn = 1;
                        }
                    }
                }
                System.out.println("Cantidad de paquetes: " + packetCounter);
                break;

            /*-----------------------------------------------------------------------*/

            case 3: // termina atender una tarea (service_rate)
                if (pn.isPos(shoot)) {
                    powerDownCpu1.signal();
                    valueToReturn = 1;
                }
                break;
            case 4: // termina atender una tarea (service_rate_2)
                if (pn.isPos(shoot)) {
                    powerDownCpu2.signal();
                    valueToReturn = 1;
                }
                break;

                /*-----------------------------------------------------------------------*/

            case 10: // Para funar los tokens en P6 cuando el cpu ya esta prendido (T5)
                if(pn.isMarked(4)){ //Veo si hay tokens en CPU_ON
                    if (pn.isPos(shoot)) {
                        try {
                            powerDownCpu1.await();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        valueToReturn = 1;
                    }
                }
                break;
            case 6: // Para funar los tokens en P13 cuando el cpu ya esta prendido (T5)
                if(pn.isMarked(5)){ //Veo si hay tokens en CPU_ON_2
                    if (pn.isPos(shoot)) {
                        try {
                            powerDownCpu2.await();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        valueToReturn = 1;
                    }
                }
                break;

            /*-----------------------------------------------------------------------*/

            case 11: //arranca el encendido del cpu (T6)
                if ((pn.isMarked(11))) {        // P6
                    if (pn.isPos(shoot)) {
                        valueToReturn = 1;
                    }
                }
                break;
            case 7: //arranca el encendido del cpu (T13)
                if ((pn.isMarked(10))) {         // P13
                    if (pn.isPos(shoot)) {
                        valueToReturn = 1;
                    }
                }
                break;

            /*-----------------------------------------------------------------------*/

            case 12: //enciende el cpu (T7)
                if (pn.isPos(shoot)) {
                    try {
                        powerDownCpu1.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    valueToReturn = 1;
                }
                break;
            case 8: //enciende el cpu (T14)
                if (pn.isPos(shoot)) {
                    try {
                        powerDownCpu2.await();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    valueToReturn = 1;
                }
                break;
        }

        /*-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/

        if(valueToReturn > 0){
            System.out.println("Hice disparo " + numTransitions[index]);
        } else{
            System.out.println("No se puedo realizar el disparo " + numTransitions[index]);
        }

        transitions +=  numTransitions[index];


        if(valueToReturn == 0 && packetCounter == dataNumber){
                //notify();
                powerDownCpu1.signal();
                powerDownCpu2.signal();
                notEmptyBuffer1.signal();
                notEmptyBuffer2.signal();
                notFullBuffer.signal();
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
