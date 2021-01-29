public class PN {

    int[] M;
    int[] B;
    int[][] I;
    int[][] H;
    int[] Q;
    int[] E;


    // N = cant de estados , M = cant de transiciones
    public PN(int[] m, int[] b, int[] q, int[][] i, int[][] h, int[] E) {
        this.M = m; // Vector de marcado inicial // (N x 1)
        this.B = b; // Si B[i] = 1, la transicion esta desensibilizada (M x 1)
        this.Q = q; // Si M[i] = 0 -> Q[i] = 1 ; Caso contrario Q[i] = 0 (N x 1)
        this.I = i; // Matriz de incidencia (N x M)
        this.H = h; // Matriz de inhibicion (M x N)

    }

    public PN() {
        init();
    }

    public void init() {

        this.M = new int[]{0, 0, 0, 1, 1, 0, 0, 0, 1}; //Vector de marcado inicial


        /**
         * m0: active
         * m1: CPU_buffer
         * m2: CPU_ON
         * m3: Idle
         * m4: P0
         * m5: P1
         * m6: P6
         * m7: Power_up
         * m8: Stand_by
         */


        /**
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


        this.I = new int[][]{{0, 0, 0, 1, -1, 0, 0, 0},        // m0
                             {0, 0, 1, -1, 0, 0, 0, 0},        // m1
                             {0, -1, 0, 0, 0, 0, 0, 1},        // m2
                             {0, 0, 0, -1, 1, 0, 0, 0},        // m3
                             {-1, 0, 1, 0, 0, 0, 0, 0},        // m4
                             {1, 0, -1, 0, 0, 0, 0, 0},        // m5
                             {0, 0, 1, 0, 0, -1, 0, -1},        // m6
                             {0, 0, 0, 0, 0, 0, 1, -1},        // m7
                              {0, 1, 0, 0, 0, 0,- 1, 0},       // m8
        };


        this.H = new int[][]{   {0, 0, 0, 0, 0, 0, 0, 0, 0},        // arrival_rate
                                {1, 1, 0, 0, 0, 0, 0, 0, 0},        // power_down_threshold
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},        // T1
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},        // T2
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},        // T3
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},        // T5
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},        // T6
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},        // T7
        };

        this.B = new int[]{0, 0, 0, 0, 0, 0, 0, 0}; //vector de transiciones desensibilizadas por arco inhibidor
        this.E = new int[]{0, 0, 0, 0, 0, 0, 0, 0}; //vector de sensibilizado


    }


    public boolean isPos(int[] index) {

        String M_name[] = new String[]{"Active","CPU_buffer","CPU_ON","Idle","P0","P1","P6","Power_up","Stand_by"};
        //System.out.println("Marca: \n");
        //printArray(M);


        //this.Q  = new int[9];



        /*for (int i = 0; i < 9; i++) { //M(pi) = 0 -> Q[i] = 1, M(pi) != 0 -> Q[i] = 0
            if (m[i] != 0) Q[i] = 0;
            else Q[i] = 1;
        }
        System.out.println("Q:\n");
        printArray(Q);*/


        //calculo E
        for (int m = 0; m < 8; m++) {
            this.E[m] = 1;
            for (int n = 0; n < 9; n++) {
                if (I[n][m] + M[n] < 0) {
                    E[m] = 0;
                    break;
                }
            }
        }
        //System.out.println("E: \n");
        //printArray(E);



        int temp = 0;
        int[] aux = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        //Calculo B
        for (int m = 0; m < 8; m++) {
            B[m] = 0;
            for (int n = 0; n < 9; n++) {   //Si algun numero del nuevo vector de marcado es = 1, no puedo dispararla
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


        for(int m = 0; m < 8; m++){
            if (B[m] * E[m] > 0) aux[m] = 1; // B and E
            if (aux[m] * index[m] > 0) aux[m] = 1; // sigma and Ex
            else aux[m] = 0; //si no pongo el else, quedan los unos de la operacion anterior
        }

        //System.out.println("sigma and Ex: \n");
        //printArray(aux);



        int zeroCounter = 0; //esto es para ver que lo que quiero y puedo disparar sea diferente de 0
        for (int m = 0; m < 8; m++){
            if (aux[m] != 0) zeroCounter++;
        }
        if(zeroCounter == 0)
            return false;



        // I * aux  (n x m * m x 1 = n x 1)

        int[] aux2 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int n = 0; n < 9; n++) {
            for (int m = 0; m < 8; m++) {
                temp = I[n][m] * aux[m];
                aux2[n] = aux2[n] + temp;
            }
        }



        int[] mPrima = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};


        System.out.println("Nuevo marcado: \n");
        for (int n = 0; n < 9; n++) {   //Si algun numero del nuevo vector de marcado es negativo, no puedo dispararla
            mPrima[n] = M[n] + aux2[n];    //Sumo para obtener el nuevo vector de marcado
            System.out.println(mPrima[n]+ " "+M_name[n]+"\n");
            if (mPrima[n] < 0)
            {return false;
            }

        }
        this.M = mPrima;
        return true;
    }



    public void printArray(int [] array){
        for(int i = 0; i < array.length; i++)
            System.out.println(array[i] + " ");
    }

    public boolean isMarked(int index){
        return ( (this.M[index] != 0) ); //devuelve false si no hay nada en esa plaza y viceversa
    }

}

/**
 *
 * this.M = new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1}; //Vector de marcado inicial
 *
 *
 *  this.I = new int[][]{      { 0, 0, 0, 1,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // m0=Active
 *                             { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,-1, 0, 0, 0, 0},        // m1=Active_2
 *                             { 0, 0, 1,-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // m2=CPU_buffer
 *                             { 0, 0, 0, 0, 0, 0, 0, 0, 1,-1, 0, 0, 0, 0, 0},        // m3=CPU_buffer2
 *                             { 0,-1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},        // m4=CPU_ON
 *                             { 0, 0, 0,-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // m5=Idle
 *                             { 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 1, 0, 0, 0, 0},        // m6=Idle_2
 *                             {-1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},        // m7=P0
 *                             { 1, 0,-1, 0, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0, 0},        // m8=P1
 *                             { 0, 0, 1, 0, 0,-1, 0,-1, 0, 0, 0, 0, 0, 0, 0},        //m9=P6
 *                             { 0, 0, 0, 0, 0, 0, 1,-1, 0, 0, 0, 0, 0, 0, 0},        //m10=Power_up
 *                             { 0, 1, 0, 0, 0, 0,-1, 0, 0, 0, 0, 0, 0, 0, 0},        //m11=Stand_by
 *                             { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1, 0, 0, 1},        //m12=CPU_ON_2
 *                             { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,-1, 0,-1},        //m13=P13
 *                             { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,-1, 0},        //m14=Stand_by_2
 *                             { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,-1},        //m15=Power_up_2
 *         };
 *
 *         this.H = new int[][]{   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // arrival_rate
 *                                 {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // power_down_threshold
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T1
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T2
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // service_rate
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T5
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T6
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T7
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T8
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T9
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // service_rate_2
 *                                 {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // Power_threshold_2
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T12
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T13
 *                                 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T14
 *         };
 */

