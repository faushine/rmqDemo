import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuxin Fan
 * @create 2019-09-29
 */
public class RPCClientTest {

  String host = "localhost";

  @Test
  public void testRoundTripTime() throws IOException, TimeoutException {
    RPCClient client = new RPCClient(host);
    long[] times = new long[100];
    for (int i = 0; i<100; i++){
      client.run(1);
      times[i] = client.rrt;
    }
    Utils.formatResult(times, "TCP-Q8", "A1-3");
  }

  @Test
  public void testThroughput() throws IOException, TimeoutException {
    RPCClient client = new RPCClient(host);
    long[] times = new long[100];
    for (int i = 0; i<100; i++){
      client.run(1024*1024);
      times[i] = client.rrt;
    }
    Utils.formatResult(times, "TCP-Q9", "A1-3");
  }

}