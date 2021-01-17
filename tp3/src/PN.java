public class PN {

    int[]m;
    int[]B;
    int[][] I;
    int[][] H;
    int[] Q;

    public PN(int[] m, int[] b, int[] q, int[][] i, int[][] h,int[] s) {
        this.m = m;
        this.B = b; // si B[i] = 1, la transicion esta desensibilizada
        this.Q = q;
        this.I = i; //matriz de incidencia
        this.H = h; //matriz de inhibicion



        //S = s;
    }

    public  PN(){
        init();
    }

    public void init(){

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

        //this.S = new int[]{0, 0, 0, 0, 0, 0, 0, 0}; //Vector de disparo

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

        this.I = new int[][]{   {0, 0, 0, 0, -1, 1, 0, 0, 0},	    // arrival_rate
                                {0, 0, -1, 0, 0, 0, 0, 0, 1},	    // power_down_threshold
                                {0, 1, 0, 0, 1, -1, 1, 0, 0},		// T1
                                {1, -1, 0, -1, 0, 0, 0, 0, 0},	    // T2
                                {-1, 0, 0, 1, 0, 0, 0, 0, 0},		// T3
                                {0, 0, 0, 0, 0, 0, -1, 0, 0},		// T5
                                {0, 0, 0, 0, 0, 0, 0, 1, -1},		// T6
                                {0, 0, 1, 0, 0, 0, -1, -1, 0},		// T7
        };
        this.H = new int[][]{   {0, 1, 0, 0, 0, 0, 0, 0, 0},	    // arrival_rate
                                {0, 1, 0, 0, 0, 0, 0, 0, 0},	    // power_down_threshold
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},		// T1
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},	    // T2
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},		// T3
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},		// T5
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},		// T6
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},		// T7
        };



        for (int i = 0; i < 9; i++){
            if (m[i] != 0) Q[i] = 0;
            else Q[i] = 1;
        }


        int temp = 0;
        for (int i = 0; i < 8; i++) {   //Si algun numero del nuevo vector de marcado es negativo, no puedo dispararla
            B[i] = 0;
            for (int j = 0; j < 9; i++){
                    temp = H[i][j] * Q[j];    //Sumo para obtener el nuevo vector de marcado
                    B[i] = B[i] + temp;
            }
        }


    }


}
