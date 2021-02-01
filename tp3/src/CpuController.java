
public class CpuController extends Thread {

    private Monitor monitor;
    private int cpunumber;

    public CpuController(Monitor monitor, int cpuNumber)
    {
        this.monitor = monitor;
        this.cpunumber = cpuNumber - 1;
    }

    @Override
    public  void run() {
        super.run();
        int flag;

        while (true) {
            flag= monitor.shoot(10 - cpunumber * 4); // T5 y T12 (Index: 10 y 6)
            if(flag != -1){
                if (flag != 1) { //intenta disparar t5, si no puede, intenta prender el cpu
                    if (monitor.shoot(11 - cpunumber * 4) == 1) { // T6 y T13 (Index: 11 y 7)
                        monitor.shoot(12 - cpunumber * 4); // T7 y T14 (Index: 12 y 8)
                    } else
                        monitor.shoot(1 + cpunumber ); // Si no pudo disparar las anteriores, entonces lo intenta
                                                             // apagar (power_down_threshold Y power_down_2) (Index: 1 y 2)
                }
            }
            else{
                System.out.println(("Controlador CPU " + (cpunumber+1) + " a finalizado. Good Bye!"));
                break;
            }
        }
    }


}
