import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

public class groupSend implements Runnable{
    public BufferedReader is;
    public LinkedList<Socket> groupChatPeople;
    public Socket self;
    public String speaker;
    groupSend(String speaker,Socket self,BufferedReader is,LinkedList<Socket> groupChatPeople){
        try{
            this.speaker=speaker;
            this.self=self;
            this.is=is;
            this.groupChatPeople=groupChatPeople;
        }
        catch (Exception e){
            System.out.println("无法进入群聊·");
        }
    }

    public void run() {
        String line;
        try {
            line=is.readLine();
            while(!line.equals("bye")){
                Iterator<Socket>iter=groupChatPeople.iterator();
                while(iter.hasNext()){
                    Socket te=iter.next();
                    if(te==self)continue;//避免发到自己
                    PrintWriter os=new PrintWriter(te.getOutputStream());
                    os.println(speaker+line);
                    os.flush();
                }
                if(Thread.currentThread().isInterrupted())break;
                line= is.readLine();
            }
        }
        catch (Exception e){
            System.out.println("连接出错");

        }

        try {

            is.close();//关闭输入流
            groupChatPeople.remove(self);//从链表中剔除
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
