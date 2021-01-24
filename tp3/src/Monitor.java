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


    private PN pn = new PN();

    CPU_buffer buffer1;

    public Monitor(CPU_buffer buffer1, Lock lock,Condition notEmptyBuffer1,Condition notFullBuffer1,Condition powerDownCpu1) {
        this.buffer1 = buffer1;
        this.lock = lock;
        this.powerDownCpu1 = powerDownCpu1;
        this.notEmptyBuffer1 = notEmptyBuffer1;
        this.notFullBuffer1 = notFullBuffer1;
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


    public int shoot(int index) {  //Dispara una transicion (index)
        lock.lock();

        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;

        switch (index) {

            //PRODUCTOR
            case 0:

                if (pn.isPos(shoot)){
                    try {
                        if (buffer1.size() == maxBufferSize)
                            notFullBuffer1.await();
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    finally {
                        System.out.println("Hice disparo 0");
                    }
                }
                else
                    System.out.println("No se puedo realizar el disparo 0");

                lock.unlock();
                return 0;


            //CPU1 intenta apagarse
            case 1:

                if (pn.isPos(shoot)) {
                    try {
                        if (buffer1.size() == maxBufferSize)
                            powerDownCpu1.await();
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    finally {
                        System.out.println("Hice disparo 1");
                    }
                }
                else
                    System.out.println("No se puedo realizar el disparo 1");

                lock.unlock();
                return 0;


            //Entra tarea al buffer (T1)
            case 2:

                if (pn.isPos(shoot)) {
                    System.out.println("Hice disparo 2");
                    notEmptyBuffer1.signal();
                }
                else
                    System.out.println("No se puedo realizar el disparo 2");
                lock.unlock();
                return 0;


            case 3: // Intenta atender una tarea (T2)

                if (pn.isPos(shoot)) {
/*                    try {
                        busyCpu1.await();
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    finally {*/
                        try {
                            if (buffer1.size() == 0)
                                notEmptyBuffer1.await();
                        }
                        catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        finally {
                            System.out.println("Hice disparo 3");
                            notFullBuffer1.signal();
                        }
                    //}
                }
                else
                    System.out.println("No se puedo realizar el disparo 3");
                lock.unlock();
                return 0;


            case 4: // termina atender una tarea (T3)

                if (pn.isPos(shoot)) {
                    //busyCpu1.signal();
                    System.out.println("Hice disparo 3");

                }
                else
                    System.out.println("No se puedo realizar el disparo 3");

                lock.unlock();
                return 0;


            case 5: // para funar los tokens en P6 cuando el cpu ya esta prendido (T5)

                if (pn.isPos(shoot)) {
                    System.out.println("Hice disparo 5");
                }
                else
                    System.out.println("No se puedo realizar el disparo 5");

                lock.unlock();


            case 6: //arranca el encendido del cpu (T6)

                if (pn.isPos(shoot) && (pn.isMarked(6))) {
                    System.out.println("Hice disparo 6");
                }
                else
                    System.out.println("No se puedo realizar el disparo 6");

                lock.unlock();


            case 7: //enciende el cpu (T7)

                if (pn.isPos(shoot)) {
                    powerDownCpu1.signal();
                    System.out.println("Hice disparo 7");
                }
                else
                    System.out.println("No se puedo realizar el disparo 7");

                lock.unlock();

        }
        lock.unlock();
        return 0;
    }
}
