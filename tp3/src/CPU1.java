public class CPU1 extends Thread {

        private int id;
        private Monitor monitor;
        private CPU_buffer buffer1;

        public CPU1(int id, Monitor monitor, CPU_buffer buffer1){
            this.id = id;
            this.monitor = monitor;
            this.buffer1 = buffer1;
        }

        @Override
        public void run() {
            super.run();

            int index;

            while(true) {

                //aca tendria que elegir que transicion va a intentar disparar

                switch (monitor.shoot(index)) {
                    //y aca va lo que deberia hacer en cada transicion
                }
            }
        }
    }

}
