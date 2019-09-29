/**
 * @author Yuxin Fan
 * @create 2019-09-28
 */
public class App {

  public static void main(String[] args) throws Exception {
    if (args[0]!=null){
      RPCServer server = new RPCServer();
      server.run(args[0]);
    }
  }
}
