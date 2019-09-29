import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * @author Yuxin Fan
 * @create 2019-09-28
 */
public class Utils {

  public static void writeFile(String message, String fileName){
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(fileName, true)));
      out.write(message);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }finally {
      try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static long getPercentile(long[] times, double percentile) {
    Arrays.sort(times);
    return times[Integer.valueOf((int) (percentile*times.length))];
  }

  public static void formatResult(long[] times,String taskName, String fileName){
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(taskName);
    stringBuilder.append(System.getProperty("line.separator"));
    stringBuilder.append(Utils.getPercentile(times,0.1));
    stringBuilder.append(System.getProperty("line.separator"));
    stringBuilder.append(Utils.getPercentile(times,0.5));
    stringBuilder.append(System.getProperty("line.separator"));
    stringBuilder.append(Utils.getPercentile(times,0.9));
    stringBuilder.append(System.getProperty("line.separator"));
    Utils.writeFile(stringBuilder.toString(),fileName);
  }

  public static void formatRawResult(long[] times,String taskName, String fileName){
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(taskName);
    stringBuilder.append(System.getProperty("line.separator"));
    for (int i = 0; i<times.length; i++){
      stringBuilder.append(times[i]);
      stringBuilder.append(System.getProperty("line.separator"));
    }
    Utils.writeFile(stringBuilder.toString(),fileName);
  }
}
