import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Test {

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		long dl = sdf.parse("10-05-2017").getTime();
		long ct = System.currentTimeMillis();
		long diff = (dl - ct);
		System.out.println("Diff in milliseconds " + diff);

		long days = diff / (24 * 60 * 60 * 1000);
		System.out.println("days .. " + days);

		long hrs = (diff - (24 * 60 * 60 * 1000) * days) / (60 * 60 * 1000);
		System.out.println("hrs .. " + hrs);
		
		
	}

}
