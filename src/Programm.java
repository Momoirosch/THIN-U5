public interface Programm {

     int run(int x, boolean details);

     int collatz(long x);

     default long collatzStep(long x) {
         if (x % 2 == 0) {
             return x / 2;
         } else {
             return 3 * x + 1;
         }
     }

     default void printIfDetails(String message, boolean details) {
         if (details) {
             System.out.println(message);
         }
     }
}
