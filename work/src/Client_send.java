import java.io.*;

//第一个版本的说话线程
public class Client_send implements Runnable{
    public PrintWriter os;
    public String name;
    public DataOutputStream fos;
    public ClientWindows ClientGUI;
    Client_send(PrintWriter os,String name,DataOutputStream fos,ClientWindows ClientGUI){
        try {
            this.os=os;
            this.name=name;
            this.fos=fos;
            this.ClientGUI=ClientGUI;
        }
        catch (Exception e){
            System.out.println(name+"can't speak");
        }
    }
    public void run() {
        String readline;
        try{
            readline=ClientGUI.GetMessage();
            while(!readline.equals("bye")) {//若从标准输入读入的字符串为 "bye"则停止循环

                if(readline.equals("文件传输")){    //开始文件传输

                    os.println(readline);//让接受端开始准备接受文件
                    os.flush();//刷新输出流，使Server马上收到该字符串

                    ClientGUI.showMessage("请输入项目中已有文件的文件名");
                    String filename=ClientGUI.GetMessage();
                    File file=new File(filename);
                    while(!file.exists()){
                        ClientGUI.showMessage("文件不存在，请重新输入");
                        filename=ClientGUI.GetMessage();
                        file=new File(filename);
                    }

                    try{
                        os.println(filename);//告知接受端文件名
                        os.flush();//刷新输出流，使Server马上收到该字符串
                    }
                    catch (Exception e){
                        System.out.println("文件名未传输");
                    }

                    try{
                        FileInputStream in=new FileInputStream(file);
                        long fileLength=file.length();
                        fos.writeLong(fileLength);//发送文件长度
                        fos.flush();

                        int n=-1;
                        byte []str=new byte[1024];
                        int len=0;
                        while((n=in.read(str,0,str.length))!=-1){
                                fos.write(str,0,n);
                                len+=n;
                                fos.flush();
                        }
                        in.close();

                        ClientGUI.showMessage("文件传输完成");
                        readline=ClientGUI.GetMessage();
                    }
                    catch (Exception e){
                        System.out.println("文件传输失败");
                    }

                }

                if(Thread.currentThread().isInterrupted())break;//如果进程处于中断状态，则立即停止

                os.println(readline);
                os.flush();//刷新输出流，使Server马上收到该字符串
                //在系统标准输出上打印读入的字符串
                ClientGUI.showMessage(name+":" + readline);

                readline=ClientGUI.GetMessage();
            }

        }
        catch(Exception e){
            System.out.println("can't speak");
        }
        os.println("bye");
        os.flush();//刷新输出流，使Server马上收到该字符串

        os.close(); //关闭Socket输出流
        try {
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
