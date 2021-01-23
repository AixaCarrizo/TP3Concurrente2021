public class Main {

    public static void main(String[] args) {

        //Shoot s = new Shoot();
        //s.disparo();
        CPU_buffer cpu_buffer = new CPU_buffer();
        Monitor monitor = new Monitor(cpu_buffer);
        GenData gd = new GenData(monitor,cpu_buffer,1000);
        gd.run();
    }
}