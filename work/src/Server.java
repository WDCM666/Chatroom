import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Server{
    public static int clientnum=0; //静态成员变量，记录当前客户的个数
    public static Socket []Client;
    public static Map<String, Integer>nameMap;
    public static LinkedList<Socket> groupChatPeople;
    public static ServerSocket serverSocket;
    public static void main(String []args) throws IOException {
        Client=new Socket[1000];
        nameMap=new HashMap<>();//name对应Client的socket在socket数组中的位置，以实现一对一连接
        groupChatPeople=new LinkedList<Socket>();//groupChatPeople链表来记录加入群聊的Client的Socket
        serverSocket=null;
        boolean listening=true;
        try{
            //创建一个ServerSocket在端口4700监听客户请求
            serverSocket=new ServerSocket(5100);
        }catch(IOException e) {
            System.out.println("Could not listen on port:4700.");
            //出错，打印出错信息
            System.exit(-1); //退出
        }
        while(listening){ //循环监听
            //监听到客户请求，根据得到的Socket对象和客户计数创建服务线程，并启动之
            Socket sno=serverSocket.accept();
            Client[clientnum]=sno;
            new ServerThread(sno,clientnum,Client,nameMap,groupChatPeople).start();
            clientnum++; //增加客户计数
        }
        serverSocket.close(); //关闭ServerSocket
    }
}

