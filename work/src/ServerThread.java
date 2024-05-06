import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Map;

public class ServerThread extends Thread{
    Socket socket; //保存与本线程相关的Socket对象
    int clientnum; //保存本进程的客户计数
    Socket []Client;//Socket数组保存客户端
    Map<String, Integer> nameMap;//使用map将客户端姓名和Socket序号一一对应起来
    LinkedList<Socket> groupChatPeople;//群聊中的人的Socket

    public ServerThread(Socket socket,int num,Socket []Client,Map<String, Integer> nameMap,LinkedList<Socket> groupChatPeople) { //构造函数
        this.socket=socket; //初始化socket变量
        clientnum=num; //初始化clientnum变量
        this.Client=Client;
        this.nameMap=nameMap;
        this.groupChatPeople=groupChatPeople;
    }
    public void run() { //线程主体
        try{

            //由Socket对象得到输入流，并构造相应的BufferedReader对象
            BufferedReader is=new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));
            //由Socket对象得到输出流，并构造PrintWriter对象
            PrintWriter os=new PrintWriter(socket.getOutputStream());

            //由Socket对象得到字节输入流,以便接收文件
            DataInputStream fis=new DataInputStream(socket.getInputStream());

            //在标准输出上打印从客户端读入的name
            String Client_name=is.readLine();
            System.out.println(Client_name+"进入聊天室");
            nameMap.put(Client_name,clientnum);

            String pattern=is.readLine();//选择群聊还是私聊
            if(pattern.equals("群聊")){
                groupChatPeople.add(socket);
                Thread speak1=new Thread(new groupSend(Client_name+":",socket,is,groupChatPeople));
                speak1.start();
                while(speak1.isAlive()){
                }
                System.out.println(Client_name+"退出聊天室");
                socket.close();
            }
            else{
                //私聊部分

                for(String key:nameMap.keySet()){
                    os.println(key);
                    os.flush();
                }
                os.println("显示完毕");
                os.flush();

                String talk_name=is.readLine();
                while(talk_name.equals("显示在线的人")) {
                    for (String key : nameMap.keySet()) {
                        os.println(key);
                        os.flush();
                    }
                    os.println("显示完毕");
                    os.flush();
                    talk_name=is.readLine();
                }

                while(!nameMap.containsKey(talk_name)){
                    os.println("No");//没有这个人
                    os.flush();//刷新输出流，使Server马上收到该字符串
                    talk_name= is.readLine();

                    if(talk_name.equals("显示在线的人")){
                        for(String key:nameMap.keySet()){
                            os.println(key);
                            os.flush();
                        }
                        os.println("显示完毕");
                        os.flush();
                        talk_name= is.readLine();
                    }
                }

                os.println("连接成功");//成功找到这个人
                os.flush();//刷新输出流，使客户端马上收到该字符串

                int talk_no= nameMap.get(talk_name);
                Socket talk_socket=Client[talk_no];

                //由talk_Socket对象得到输出流，并构造PrintWriter对象
                PrintWriter talk_os=new PrintWriter(talk_socket.getOutputStream());
                //由Socket对象得到字节输出流,以便传输文件
                DataOutputStream talk_fos=new DataOutputStream(talk_socket.getOutputStream());
                //由Socket对象得到字节输入流,以便接收文件
                Thread speak=new Thread(new send(talk_os,is,talk_fos,fis));
                speak.start();
                while(speak.isAlive()){
                }
                System.out.println(Client_name+"退出聊天室");
                socket.close();
            }

        }catch(Exception e){
            System.out.println("Error:"+e);//出错，打印出错信息
        }
    }
}
