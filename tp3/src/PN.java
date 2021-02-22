public class PN {

    int[] M;
    int[] B;
    int[][] I;
    int[][] H;
    //int[] Q;
    int[] E;
    int estados; //N
    int transiciones; //M
    static boolean print = true;

    /*
    // N = cant de estados , M = cant de transiciones
    public PN(int[] m, int[] b, int[] q, int[][] i, int[][] h, int[] E) {
        this.M = m; // Vector de marcado inicial // (N x 1)
        this.B = b; // Si B[i] = 1, la transicion esta desensibilizada (M x 1)
        this.Q = q; // Si M[i] = 0 -> Q[i] = 1 ; Caso contrario Q[i] = 0 (N x 1)
        this.I = i; // Matriz de incidencia (N x M)
        this.H = h; // Matriz de inhibicion (M x N)
    }
     */

    public PN() {
        init();
    }

    public void init() {

        this.M = new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1}; //Vector de marcado inicial

        //                    / Ar P  P2 S  S2 1  12 13 14 2  5  6  7  8  9
        this.I = new int[][]{ { 0, 0, 0,-1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},        // m0=Active
                              { 0, 0, 0, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},        // m1=Active_2
                              { 0, 0, 0, 0, 0, 1, 0, 0, 0,-1, 0, 0, 0, 0, 0},        // m2=CPU_buffer
                              { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,-1},        // m3=CPU_buffer2
                              { 0,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},        // m4=CPU_ON
                              { 0, 0,-1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},        // m5=CPU_ON_2
                              { 0, 0, 0, 1, 0, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0},        // m6=Idle
                              { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1},        // m7=Idle_2
                              {-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},        // m8=P0
                              { 1, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0, 0, 0,-1, 0},        //m9=P1
                              { 0, 0, 0, 0, 0, 0,-1, 0,-1, 0, 0, 0, 0, 1, 0},        //m10=P13
                              { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,-1, 0,-1, 0, 0},        //m11=P6
                              { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,-1, 0, 0},        //m12=Power_up
                              { 0, 0, 0, 0, 0, 0, 0, 1,-1, 0, 0, 0, 0, 0, 0},        //m13=Power_up_2
                              { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0, 0, 0},        //m14=Stand_by
                              { 0, 0, 1, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0, 0, 0},        //m15=Stand_by_2
          };

          this.H = new int[][]{   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // arrival_rate
                                  {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // power_down_threshold
                                  {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // power_down_2
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // service_rate
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // service_rate_2
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T1
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T12
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T13
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T14
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T2
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T5
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T6
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T7
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T8
                                  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T9
          };


        this.B = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //vector de transiciones desensibilizadas por arco inhibidor
        this.E = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //vector de sensibilizado
        this.estados = 16;
        this.transiciones = 15;

    }


    public boolean isPos(int[] index) {

        String M_name[] = new String[]{"Active","Active_2","CPU_buffer","CPU_buffer 2","CPU_ON","CPU_ON_2","Idle","Idle_2","P0","P1","P13","P6","Power_up","Power_up_2","Stand_by","Stand_by_2"};
       // System.out.println("Marca: \n");
       // printArray(M);


        //this.Q  = new int[9];

        /*for (int i = 0; i < 9; i++) { //M(pi) = 0 -> Q[i] = 1, M(pi) != 0 -> Q[i] = 0
            if (m[i] != 0) Q[i] = 0;
            else Q[i] = 1;
        }
        System.out.println("Q:\n");
        printArray(Q);*/

        //calculo E
        for (int m = 0; m < transiciones; m++) {
            this.E[m] = 1;

            for (int n = 0; n < estados; n++) {
                if (I[n][m] + M[n] < 0) {
                    E[m] = 0;
                    break;
                }
            }
        }
        //System.out.println("E: \n");
        //printArray(E);



        int temp;
        int[] aux = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ,0 };

        //Calculo B
        for (int m = 0; m <transiciones ; m++) {
            B[m] = 0;
            for (int n = 0; n < estados; n++) {   //Si algun numero del nuevo vector de marcado es = 1, no puedo dispararla
                //temp = H[m][i] * Q[i];    //Sumo para obtener el nuevo vector de desensibilizado
                temp = H[m][n] * M[n];
                B[m] = B[m] + temp; // B = 0 -> no se puede :(
            }
            if(B[m] == 0) {
                B[m] = 1;
            }
            else {
                B[m] = 0;
            }
        }

        //System.out.println("H x m: \n");
        //printArray(B);


        for(int m = 0; m < transiciones; m++){
            if (B[m] * E[m] > 0) aux[m] = 1; // B and E
            if (aux[m] * index[m] > 0) aux[m] = 1; // sigma and Ex
            else aux[m] = 0; //si no pongo el else, quedan los unos de la operacion anterior
        }

        //System.out.println("sigma and Ex: \n");
        //printArray(aux);



        int zeroCounter = 0; //esto es para ver que lo que quiero y puedo disparar sea diferente de 0
        for (int m = 0; m < transiciones; m++){
            if (aux[m] != 0) zeroCounter++;
        }
        if(zeroCounter == 0)
            return false;

        // I * aux  (n x m * m x 1 = n x 1)

        int[] aux2 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int n = 0; n < estados; n++) {
            for (int m = 0; m < transiciones; m++) {
                temp = I[n][m] * aux[m];
                aux2[n] = aux2[n] + temp;
            }
        }

        int[] mPrima = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        if(print)
            System.out.println("Nuevo marcado: \n");

        for (int n = 0; n < estados; n++) {   //Si algun numero del nuevo vector de marcado es negativo, no puedo dispararla
            mPrima[n] = M[n] + aux2[n];    //Sumo para obtener el nuevo vector de marcado
            if(print)
                System.out.println(mPrima[n]+" "+M_name[n]+ "\n");
            if (mPrima[n] < 0) {
                return false;
            }
        }

        this.M = mPrima;
        return true;
    }


    /*
    public void printArray(int [] array){
        for(int i = 0; i < array.length; i++)
            System.out.println(array[i] + " ");
    }

    public void setPrint(boolean val){
        print = val;
        return;
    }
    */

    public boolean isMarked(int index){
        return ( (this.M[index] != 0) ); //devuelve false si no hay nada en esa plaza y viceversa
    }

}

