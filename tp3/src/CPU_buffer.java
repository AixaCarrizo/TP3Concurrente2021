import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class CPU_buffer {


    private Queue<String> cola;
    //private int maxSize;



    public CPU_buffer() {

        cola = new ConcurrentLinkedQueue<>();
    }

    /*


    public CPU_buffer(int maxSize) {
        cola = new ConcurrentLinkedQueue<>();
        this.maxSize = maxSize;
    }

    public int getMaxSize(){
        return this.maxSize;
    }*/

    public void add(String dato) {
        cola.add(dato);
    }

    public String remove() {
        return cola.remove();
    }

    public  int size() {
        return cola.size();
    }
}