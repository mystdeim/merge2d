package merge2d;

public class Helpers {
	
	public static final String NO_COUNTRY_CAPTION = "Не определена";
	public static final String HYPHEN = "-";
	public static final double EPSILON = 0.000001;
	
	public static final boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
	
	public static final int rank(long number) {
		return number > 0 ? (int) (Math.log(number) / Math.log(10)) + 1 : 0;
	}
	
	/**
	 * Сравнение чисел с заданной точностью
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static final boolean equalsWithEpsilon(double a, double b) {
		return Math.abs(a - b) < EPSILON;
	}
	
}
