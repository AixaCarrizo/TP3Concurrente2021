public class PN {

    int[]m;
    int[]B;
    int[][] I;
    int[][] H;
    int[] Q;
    int[] E;


    // N = cant de estados , M = cant de transiciones
    public PN(int[] m, int[] b, int[] q, int[][] i, int[][] h, int [] E) {
        this.m = m; // Vector de marcado inicial // (N x 1)
        this.B = b; // Si B[i] = 1, la transicion esta desensibilizada (M x 1)
        this.Q = q; // Si M[i] = 0 -> Q[i] = 1 ; Caso contrario Q[i] = 0 (N x 1)
        this.I = i; // Matriz de incidencia (N x M)
        this.H = h; // Matriz de inhibicion (M x N)

    }

    public  PN(){
        init();
    }

    public void init() {

        this.m = new int[]{0, 0, 0, 1, 1, 0, 0, 0, 1}; //Vector de marcado inicial

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

        this.S = new int[]{0, 0, 0, 0, 0, 0, 0, 0}; //Vector de disparo

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

        this.I = new int[][]{{0, 0, 0, 0, -1, 1, 0, 0, 0},        // arrival_rate
                            {0, 0, -1, 0, 0, 0, 0, 0, 1},        // power_down_threshold
                            {0, 1, 0, 0, 1, -1, 1, 0, 0},        // T1
                            {1, -1, 0, -1, 0, 0, 0, 0, 0},        // T2
                            {-1, 0, 0, 1, 0, 0, 0, 0, 0},        // T3
                            {0, 0, 0, 0, 0, 0, -1, 0, 0},        // T5
                            {0, 0, 0, 0, 0, 0, 0, 1, -1},        // T6
                            {0, 0, 1, 0, 0, 0, -1, -1, 0},        // T7
        };
        this.H = new int[][]{{0, 1, 0, 0, 0, 0, 0, 0, 0},        // arrival_rate
                            {0, 1, 0, 0, 0, 0, 0, 0, 0},        // power_down_threshold
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


    public boolean isPos(int [] index) {

        for (int i = 0; i < 9; i++){ //M(pi) = 0 -> Q[i] = 1, M(pi) != 0 -> Q[i] = 0
            if (m[i] != 0) Q[i] = 0;
            else Q[i] = 1;
        }


        int temp = 0;
        int [] aux = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
        for (int j = 0; j < 8; j++) {
            B[j] = 0;
            E[j] = 0;

            for (int i = 0; i < 9; i++) {   //Si algun numero del nuevo vector de marcado es = 1, no puedo dispararla
                temp = H[j][i] * Q[i];    //Sumo para obtener el nuevo vector de desensibilizado
                B[j] = B[j] + temp; // B = 0 -> no se puede :(

                temp = I[j][i] * m[i];    //Sumo para obtener el nuevo vector de desensibilizado
                E[j] = E[j] + temp;         // E = 0 -> no se puede :(
            }

            if( B[j] * E[j] > 0 ) aux[j] = 1; // B and E

            if(aux[j] * index[j] > 0) aux[j] = 1; // sigma and Ex

        }

        temp = 0;
        int [] aux2 = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};

        for(int j = 0; j < 9; j++){
            for (int i = 0; i < 8; i++){
                temp = I[j][i]
            }
        }


        int[] mPrima = new int[9];

        for (int i = 0; i < 9; i++) {   //Si algun numero del nuevo vector de marcado es negativo, no puedo dispararla
            mPrima[i] = m[i] + ();    //Sumo para obtener el nuevo vector de marcado

            if (mPrima[i] == -1) return false;
        }



    }


}
