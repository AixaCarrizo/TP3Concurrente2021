public class Shoot {

    PN pn;

    public Shoot()
    {
        pn = new PN();
    }

    public void disparo()
    {

        /*
         *
         * T0: Arrival_rate
         * T1: Power_down_threshold
         * T2: T1
         * T3: T2
         * T4: T3
         * T5: T5
         * T6: T6
         * T7: T7
         *
         */

        int[] disparo = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        int numDisp = 0;
        disparo[numDisp] = 1;
            if(pn.isPos(disparo))
        {
            System.out.println("Pude disparar la transicion: "+ numDisp);
        }else
        {
            System.out.println("NO pude disparar la transicion: "+ numDisp);
        }


        disparo = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        numDisp = 2;
        disparo[numDisp] = 1;
        if(pn.isPos(disparo))
        {
            System.out.println("Pude disparar la transicion: "+numDisp);
        }else
        {
            System.out.println("NO pude disparar la transicion: "+numDisp);
        }
    }
}