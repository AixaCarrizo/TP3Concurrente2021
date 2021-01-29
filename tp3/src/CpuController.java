
public class CpuController extends Thread {

    private Monitor monitor;

    public CpuController(Monitor monitor)
    {
        this.monitor = monitor;
    }

    @Override
    public  void run() {
        super.run();
        int flag;

        while (true) {
            flag= monitor.shoot(5);
            if(flag != -1){
                if (flag != 1) { //intenta disparar t5, si no puede, intenta prender el cpu
                    if (monitor.shoot(6) == 1) { //si pudo disparar t6
                        monitor.shoot(7); //dispara t7 tambien
                    } else
                        monitor.shoot(1); //si no pudo disparar ni t5 ni t6, entonces lo intenta apagar
                }
            }
            else{
                break;
            }
        }
    }


}
