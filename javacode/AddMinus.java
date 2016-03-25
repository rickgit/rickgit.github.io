public class AddMinus{
	private volatile int money;

	// public void Add(){
 //        long l = System.currentTimeMillis();
 //        for (long index = 0; index < 100_000_000_000l; index++) {

 //        }
 //        System.out.println("===>" + (System.currentTimeMillis() - l));
	// }
	public void minus() {
		money=++money;
        // long l = System.currentTimeMillis();
        // for (long index =  100_000_000_000l; index >0; index--) {

        // }
        // System.out.println("===>" + (System.currentTimeMillis() - l));
    }
}