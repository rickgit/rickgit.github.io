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
	public void testPlusPlus(){
		int a=0;
		a++;
		++a;
		while (++a<4){
			System.out.println("a"+a);
		}
		while (a++<6){
			System.out.println("a"+a);
		}
		while (a++<8){
		}
	}
	public int simpleSwitch(int intOne) {
	    switch (intOne) {
	        case 0:
	            return 3;
	        case 1:
	            return 2;
	        case 4:
	            return 1;
	        default:
	            return -1;
	    }
	}
	public int simpleSwitchString(String intOne) {
	    switch (intOne) {
	        case "0":
	            return 3;
	        case "wrwre1":
	            return 2;
	        case "123444":
	            return 2;
	        case "4":
	            return 1;
	        default:
	            return -1;
	    }
	}
	public void testFloatAdd() {
 		double a=0;double b=0;
 		if(a-b>0){
 			a=a+b;
 		}
	}
}