public class Shoot {

    PN pn;

    public Shoot()
    {
        pn = new PN();
    }

    public void disparo()
    {

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