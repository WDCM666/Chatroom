import java.io.*;
import java.net.*;

public class Client {

    public static Socket socket;
    public static PrintWriter os;//字符输出流
    public static BufferedReader is;//字符输入流
    public static DataOutputStream fos;//字节输出流
    public static DataInputStream fis;//字节输入流
    public static ClientWindows ClientGUI;//GUI窗口编程

    public static void main(String []args) {
        try{
            //向本机的4700端口发出客户请求
            socket=new Socket("127.0.0.1",5100);

            //由Socket对象得到字符输出流，并构造PrintWriter对象
            os=new PrintWriter(socket.getOutputStream());
            //由Socket对象得到字符输入流，并构造相应的BufferedReader对象
            is=new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));
            //由Socket对象得到字节输出流,以便传输文件
             fos=new DataOutputStream(socket.getOutputStream());
            //由Socket对象得到字节输入流,以便接收文件
            fis=new DataInputStream(socket.getInputStream());

            //创建客户端对话窗口
            ClientGUI=new ClientWindows();

            String Client_name= ClientGUI.GetMessage();
            ClientGUI.showMessage(Client_name);

            os.println(Client_name);
            os.flush();//刷新输出流，使Server马上收到该字符串

            ClientGUI.showMessage("请选择群聊还是私聊");
            String choose=ClientGUI.GetMessage();
            ClientGUI.showMessage(choose);

            if(choose.equals("群聊")){
                //群聊功能
                os.println("群聊");
                os.flush();//刷新输出流，使Server马上收到该字符串

                ClientGUI.showMessage(Client_name+"欢迎进入群聊聊天室");
                os.println("欢迎"+Client_name+"进入群聊聊天室");
                os.flush();//刷新输出流，使Server马上收到该字符串

                //开始说话和听话
                Thread speak=new Thread(new Client_send(os,Client_name,fos,ClientGUI));
                Thread listen=new Thread(new Client_recieve(is,"",fis,ClientGUI));
                speak.start();
                listen.start();
                while(speak.isAlive()&&listen.isAlive()){//等待speak或listen进程结束
                }
                if(speak.isAlive())speak.interrupt();
                if(listen.isAlive())listen.interrupt();

                ClientGUI.showMessage(Client_name+"退出聊天室");
                System.out.println(Client_name+"退出聊天室");
                socket.close();
                System.exit(-1);//结束客户端程序
            }
            else{
                //私聊功能
                os.println("私聊");
                os.flush();//刷新输出流，使Server马上收到该字符串

                //显示在线人都有哪些
                People online=new People(is,ClientGUI);
                online.show();

                ClientGUI.showMessage("你想和谁对话？");

                String talkto= ClientGUI.GetMessage();
                ClientGUI.showMessage(talkto);

                while(talkto.equals("显示在线的人")){
                    os.println(talkto);
                    os.flush();//刷新输出流，使Server马上收到该字符串
                    online.show();
                    talkto= ClientGUI.GetMessage();
                }

                os.println(talkto);
                os.flush();//刷新输出流，使Server马上收到该字符串

                String talkname= is.readLine();
                while(!talkname.equals("连接成功")){
                    ClientGUI.showMessage("此人还未上线，请等待一段时间再输入或换个人对话！");
                    talkto= ClientGUI.GetMessage();
                    os.println(talkto);
                    os.flush();//刷新输出流，使Server马上收到该字符串
                    if(talkto.equals("显示在线的人")){
                        online.show();
                        continue;
                    }
                    talkname= is.readLine();
                }

                //开始说话和听话
                Thread speak=new Thread(new Client_send(os,Client_name,fos,ClientGUI));
                Thread listen=new Thread(new Client_recieve(is,talkto,fis,ClientGUI));
                speak.start();
                listen.start();
                while(speak.isAlive()&&listen.isAlive()){ //等待speak或listen进程结束
                }
                //判断哪一方退出私聊频道
                if(speak.isAlive()){
                    speak.interrupt();
                    ClientGUI.showMessage("对方退出聊天室");
                }
                if(listen.isAlive()){
                    listen.interrupt();
                    ClientGUI.showMessage("己方退出聊天室");
                }
                socket.close();
                System.exit(-1);
            }
        }catch(Exception e) {
            System.out.println("Error"+e); //出错，则打印出错信息
        }
    }
}

