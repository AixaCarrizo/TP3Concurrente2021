import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Log implements Runnable {
    private String contenido;
    private final CPU_buffer buff1;
    private final CPU_buffer buff2;
    private final CpuController controller1;
    private final CpuController controller2;
    //private final GenData gd;
    private final CPU cpu1;
    private final CPU cpu2;
    private final static boolean print = false;


    Log (CPU_buffer buffer1, CPU_buffer buffer2, CpuController controller1, CpuController controller2, CPU cpu1, CPU cpu2) {
        this.buff1 = buffer1;
        this.buff2 = buffer2;
        this.controller1 = controller1;
        this.controller2 = controller2;
        //this.gd = gd;
        this.cpu1 = cpu1;
        this.cpu2 = cpu2;
    }

    public void EscribirContenido () {
        String estadoBuff;
        String estadoCpu;
        String estadoController;
        estadoBuff = LocalDateTime.now () + " - El buffer 1 tiene " + buff1.size () + " elementos y el buffer 2 tiene " + buff2.size () + " elementos.";
        contenido = contenido + estadoBuff + "\r\n";
        estadoCpu = " - El estado del CPU 1" + " es " + cpu1.getState () + " y el estado del CPU 2 es " + cpu2.getState () + " \r\n";
        contenido = contenido + estadoCpu;
        estadoController = " - El estado del controlador 1" + " es " + controller1.getState () + " y el estado del controlador 2 es " + controller2.getState () + "\r\n";
        contenido = contenido + estadoController;
    }

    public void GuardarArchivo () {
        try {
            String ruta = "D:\\log.txt";
            File file = new File (ruta);
            if (!file.exists ()) {
                file.createNewFile ();
            }
            FileWriter fw = new FileWriter (file);
            BufferedWriter bw = new BufferedWriter (fw);

            bw.write (contenido);
            bw.close ();

            if (print) {
                System.out.println ("Contenido Guardado: ");
                System.out.println (contenido);
                System.out.println ("Se ha guardado el txt con exito. Enhorabuena!");
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }


    @Override
    public void run () {
        for (int j = 1; j <= 250; j++) {
            this.EscribirContenido ();
            try {
                TimeUnit.MILLISECONDS.sleep (10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
                System.out.print ("Process Failed");
            }
        }
        this.GuardarArchivo ();

    }

}