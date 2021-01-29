import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Log implements Runnable {
    private String ruta = "D:\\log.txt";
    private String estadoBuff;
    private String estadoCpu;
    private String estadoController;
    private String contenido;
    private CPU_buffer buff1;
    private CpuController controller1;
    private GenData gd;
    private CPU1 cpu1;


    Log(CPU_buffer buffer1,CpuController controller1, GenData gd, CPU1 cpu1 ){
        this.buff1=buffer1;
        this.controller1 = controller1;
        this.gd = gd;
        this.cpu1 = cpu1;
    }
    public void EscribirContenido() {
        estadoBuff = LocalDateTime.now()+ " - El buffer 1 tiene "+buff1.size()+" elementos";
        contenido= contenido + estadoBuff + "\r\n";
        estadoCpu = " - El estado del CPU "+" es " + cpu1.getState()+"\r\n";
        contenido = contenido + estadoCpu;
        estadoController = " - El estado del controlador "+" es " + controller1.getState()+"\r\n";
        contenido = contenido + estadoController;



    }

    public void GuardarArchivo(){
        try {

            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
            System.out.println("Contenido Guardado: ");
            System.out.println(contenido);
            System.out.println("Se ha guardado el txt con exito. Enhorabuena!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        for(int j=1; j<=500; j++) {
            this.EscribirContenido();
            try {
                TimeUnit.MILLISECONDS.sleep(25);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.print("Process Failed");
            }
        }
        this.GuardarArchivo();

    }

}