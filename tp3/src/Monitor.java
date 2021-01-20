//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Monitor {

    static Lock lock=new ReentrantLock(); //esto es para que no intenten disparar cosas al mismo tiempo)?

    //private final static Condition noTaskAvailable = lock.newCondition(); no se que es esto

    private long wCPU1Up = 30; //ventanas de tiempo para que haga una transicion(? pero por que seria con ventana de tiempo??
    private long wCPU1Down = 15;

    public PN pn = new PN();
    CPU_buffer buffer1;

    private long timeCPU1;

    public Monitor(CPU_buffer buffer1) {
        this.buffer1=buffer1;
        timeCPU1 = 0;
    }


    public int shoot(int index){  //Dispara una transicion (index)
        lock.lock();

        switch (index) {

            //PRODUCTOR
            case 0:
               // pn.isPos(index);

                System.out.println("Hice disparo 0");

                lock.unlock();
                return 0;
        }
        lock.unlock();
        return 0;
    }









    public boolean windowTimer(){ //devuelve true si se puede disparar la transicion o false si no se puede

        //Esta primera parte solo es para cuando se sensibiliza por primera vez y empieza a contar.

            if(timeCPU1==0) { //si se acaba de sensibilizar
                timeCPU1 = System.currentTimeMillis(); //tomo el tiempo actual
                return false;
            }

        //A partir de acá es cuando ya empieza a intentar disparar la transicion después de sensibilizarla.

        long auxtime = System.currentTimeMillis();	//Tomo el tiempo acutal del sistema.
        long time = 0;

        time = auxtime - timeCPU1;	//Calculo el tiempo transcurrid con respecto al que tomo cuando se sensibilizo.

        if(wCPU1Down<time && time<wCPU1Up){	//Si esta dentro del rango ...
            timeCPU1 = 0;
            return  true;
        }else{	//Si no ...
            timeCPU1 = auxtime;
            return false;
        }
    }
}