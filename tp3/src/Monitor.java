import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Monitor {
    private final Lock lock; //esto es para que no intenten disparar cosas al mismo tiempo)?
    private final Condition notEmptyBuffer1; //si esta vacio el buffer
    private final Condition notEmptyBuffer2; //si esta vacio el buffer
    private final Condition powerDownCpu1; //si el cpu esta ocupado
    private final Condition powerDownCpu2; //si el cpu esta ocupado
    private int packetCounter;
    private final Politica politic;
    private String transitions = new String ("");
    private final int dataNumber;
    private final PN pn = new PN ();
    private final CPU_buffer buffer1;
    private final CPU_buffer buffer2;
    private static final String[] numTransitions = {"T0", "T4", "T11", "T3", "T10", "TA", "T12", "T13", "T14", "T2", "T5", "T6", "T7", "T8", "T9"};
    private static final boolean print = false;

    public Monitor (CPU_buffer buffer1, CPU_buffer buffer2, Lock lock, Condition notEmptyBuffer1, Condition notEmptyBuffer2
            , Condition powerDownCpu1, Condition powerDownCpu2, int dataNumber) {
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
        this.lock = lock;
        this.powerDownCpu1 = powerDownCpu1;
        this.powerDownCpu2 = powerDownCpu2;
        this.notEmptyBuffer1 = notEmptyBuffer1;
        this.notEmptyBuffer2 = notEmptyBuffer2;
        this.packetCounter = 0;
        this.politic = new Politica (buffer1, buffer2);
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

    public String getTransitions () {
        return transitions;
    }

    /*
    INVARIANTES DE PLAZA
    m1 + m7 = 1
    m4 + m12 + m14 = 1
    m5 + m13 + m15 = 1
    m0 + m6 = 1
    m8 + m9 = 1
    */

    private boolean verifyMInvariants () throws Exception {
        int mark[] = pn.getMarkVector ();

        if (((mark[1] + mark[7]) == 1) && ((mark[4] + mark[12] + mark[14]) == 1) &&
                ((mark[5] + mark[13] + mark[15]) == 1) && ((mark[0] + mark[6]) == 1) && ((mark[8] + mark[9]) == 1))
            return true;
        else {
            throw new Exception ("Fallo en invariantes de plaza");
        }
    }

    private void printSave (int index, int valueToReturn) {
        if (print) {
            if (valueToReturn > 0) {
                System.out.println ("Hice disparo " + numTransitions[index]);
            } else {
                System.out.println ("No se puedo realizar el disparo " + numTransitions[index]);
            }
        }
        if (valueToReturn > 0)
            transitions += numTransitions[index];
    }

    public int shoot (int index) {  //Dispara una transicion (index) devuelve 1 si pudo hacerla y 0 si no
        int valueToReturn = 0;
        lock.lock ();

        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;

        switch (index) {
            //PRODUCTOR
            case 0:
                if (pn.isPos (shoot)) {
                    valueToReturn = politic.bufferPolitic ();
                    printSave (index, valueToReturn);
                }
                break;

            /*-----------------------------------------------------------------------*/

            case 1: //CPU1 intenta apagarse
                if (pn.isPos (shoot)) {
                    valueToReturn = 1;
                } else {
                    if (packetCounter == dataNumber) {
                        lock.unlock ();
                        return -1;
                    }
                }
                printSave (index, valueToReturn);
                try {
                    powerDownCpu1.await ();
                } catch (InterruptedException e1) {
                    e1.printStackTrace ();
                }
                break;
            case 2: //CPU1 intenta apagarse
                if (pn.isPos (shoot)) {
                    valueToReturn = 1;
                } else {
                    if (packetCounter == dataNumber) {
                        lock.unlock ();
                        return -1;
                    }
                }
                printSave (index, valueToReturn);
                try {
                    powerDownCpu2.await ();
                } catch (InterruptedException e1) {
                    e1.printStackTrace ();
                }
                break;

            /*-----------------------------------------------------------------------*/

            case 5: // Entra tarea al buffer 1 (T1)
                if (pn.isPos (shoot)) {
                    powerDownCpu1.signal ();
                    notEmptyBuffer1.signal ();
                    valueToReturn = 1;
                }
                printSave (index, valueToReturn);
                break;
            case 13: // Entra tarea al buffer 2 (T8)
                if (pn.isPos (shoot)) {
                    powerDownCpu2.signal ();
                    notEmptyBuffer2.signal ();
                    valueToReturn = 1;
                }
                printSave (index, valueToReturn);
                break;

            /*-----------------------------------------------------------------------*/

            case 9: // Intenta atender una tarea (T2)
                if (pn.isMarked (4)) //Veo si tengo en CPU_ON
                {
                    if (pn.isPos (shoot)) {
                        valueToReturn = 1;
                        printSave (index, valueToReturn);
                        packetCounter++;
                        try {
                            //notFullBuffer.signal ();
                            if (buffer1.size () == 0)
                                notEmptyBuffer1.await ();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace ();
                        }
                    }
                } else
                    printSave (index, valueToReturn);
                break;
            case 14: // Intenta atender una tarea (T9)
                if (pn.isMarked (5)) //Veo si tengo en CPU_ON_2
                {
                    if (pn.isPos (shoot)) {
                        valueToReturn = 1;
                        packetCounter++;
                        printSave (index, valueToReturn);
                        try {
                            //notFullBuffer.signal ();
                            if (buffer2.size () == 0)
                                notEmptyBuffer2.await ();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace ();
                        }
                    }
                } else
                    printSave (index, valueToReturn);
                break;

            /*-----------------------------------------------------------------------*/

            case 3: // termina atender una tarea (service_rate)
                if (pn.isPos (shoot)) {
                    powerDownCpu1.signal ();
                    valueToReturn = 1;
                }
                printSave (index, valueToReturn);
                break;
            case 4: // termina atender una tarea (service_rate_2)
                if (pn.isPos (shoot)) {
                    powerDownCpu2.signal ();
                    valueToReturn = 1;
                }
                printSave (index, valueToReturn);
                break;

            /*-----------------------------------------------------------------------*/

            case 10: // Para funar los tokens en P6 cuando el cpu ya esta prendido (T5)
                if (pn.isMarked (4)) { //Veo si hay tokens en CPU_ON
                    if (pn.isPos (shoot)) {
                        valueToReturn = 1;
                        printSave (index, valueToReturn);
                        try {
                            powerDownCpu1.await ();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace ();
                        }
                    }
                } else
                    printSave (index, valueToReturn);
                break;
            case 6: // Para funar los tokens en P13 cuando el cpu ya esta prendido (T5)
                if (pn.isMarked (5)) { //Veo si hay tokens en CPU_ON_2
                    if (pn.isPos (shoot)) {
                        valueToReturn = 1;
                        printSave (index, valueToReturn);
                        try {
                            powerDownCpu2.await ();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace ();
                        }
                    }
                } else
                    printSave (index, valueToReturn);
                break;

            /*-----------------------------------------------------------------------*/

            case 11: //arranca el encendido del cpu (T6)
                if ((pn.isMarked (11))) {        // P6
                    if (pn.isPos (shoot)) {
                        valueToReturn = 1;
                    }
                }
                printSave (index, valueToReturn);
                break;
            case 7: //arranca el encendido del cpu (T13)
                if ((pn.isMarked (10))) {         // P13
                    if (pn.isPos (shoot)) {
                        valueToReturn = 1;
                    }
                }
                printSave (index, valueToReturn);
                break;

            /*-----------------------------------------------------------------------*/

            case 12: //enciende el cpu (T7)
                if (pn.isPos (shoot)) {
                    valueToReturn = 1;
                    printSave (index, valueToReturn);
                    try {
                        powerDownCpu1.await ();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace ();
                    }
                } else
                    printSave (index, valueToReturn);
                break;
            case 8: //enciende el cpu (T14)
                if (pn.isPos (shoot)) {
                    valueToReturn = 1;
                    printSave (index, valueToReturn);
                    try {
                        powerDownCpu2.await ();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace ();
                    }
                } else
                    printSave (index, valueToReturn);
                break;
        }

        /*-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/

        try {
            if (valueToReturn == 0 && packetCounter == dataNumber) {
                powerDownCpu1.signal ();
                powerDownCpu2.signal ();
                notEmptyBuffer1.signal ();
                notEmptyBuffer2.signal ();
                lock.unlock ();
                return -1;
            } else if (verifyMInvariants ()) {
                lock.unlock ();
                return valueToReturn;
            } else {
                lock.unlock ();
                return -1;
            }
        } catch (Exception e1) {
            e1.printStackTrace ();
            System.exit (1);
        }
        return 0;
    }
}