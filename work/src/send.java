import java.io.*;

public class send implements Runnable{
    public PrintWriter os;//对话方字符输出流
    public BufferedReader is;//己方字符输入流
    public DataOutputStream fos;//对话方字节输出流
    public DataInputStream fis;//己方字节输入流
    send(PrintWriter os,BufferedReader is,DataOutputStream fos,DataInputStream fis){
        try {
            this.os=os;
            this.is=is;
            this.fis=fis;
            this.fos=fos;
        }
        catch (Exception e){
            System.out.println("连接出错");
        }
    }
    public void run() {
        String readline;
        try{
            readline=is.readLine(); //从另一个人那里输入读入一字符串
            while(!readline.equals("bye")) {//若从另一个人那里读入的字符串为 "bye"则停止循环
                if(Thread.currentThread().isInterrupted())break;//如果进程处于中断状态，则立即停止

                if(readline.equals("文件传输")){

                    os.println(readline);//让接受端开始准备接受文件
                    os.flush();//刷新输出流，使Server马上收到该字符串

                    readline=is.readLine();
                    os.println(readline);//告知文件名
                    os.flush();//刷新输出流，使Server马上收到该字符串


                    long fileLength=fis.readLong();
                    fos.writeLong(fileLength);//告知接受端文件长度
                    fos.flush();

                    int n=-1;
                    long SumLength=0;
                    byte []str=new byte[1024];
                    while((n=fis.read(str,0,str.length))!=-1){
                        SumLength+=n;
                        fos.write(str,0,n);
                        fos.flush();
                        if(SumLength>=fileLength)break;
                    }

                    readline=is.readLine();
                }

                os.println(readline);
                os.flush();//刷新输出流，使Server马上收到该字符串
                readline=is.readLine();
            }
        }
        catch(Exception e){
            System.out.println("再见");
        }

        try {
            os.close(); //关闭os输出流
            is.close();//关闭talk_os输出流-is输入流
            fis.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
