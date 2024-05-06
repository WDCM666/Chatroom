import java.io.*;

public class Client_recieve implements Runnable{
    public BufferedReader is;
    public DataInputStream fis;
    public String name;
    public ClientWindows ClientGUI;
    Client_recieve(BufferedReader is,String name,DataInputStream fis,ClientWindows ClientGUI){
        try {
            this.is=is;
            this.name=name;
            this.fis=fis;
            this.ClientGUI=ClientGUI;
        }
        catch (Exception e){
            System.out.println("can't listen");
        }
    }
    public void run() {
        String readline;
        try{
            readline=is.readLine();
                while(readline!=null){

                    if(readline.equals("bye"))break;
                    if(readline.equals("文件传输")){//准备接受文件
                        String filename= is.readLine();
                        File file=new File("D:\\chatroom\\file",filename);
                        file.createNewFile();

                        long fileLength=fis.readLong();
                        FileOutputStream out=new FileOutputStream(file);
                        //文件开始传输部分
                        int n=-1;
                        byte []str=new byte[1024];//接受文件
                        long SumLength=0;
                        while((n=fis.read(str,0,str.length))!=-1){
                            SumLength+=n;
                            out.write(str,0,n);
                            out.flush();
                            if(SumLength>=fileLength)break;
                        }

                        out.close();
                        ClientGUI.showMessage("文件传输完成");
                        readline=is.readLine();
                        continue;
                    }

                    ClientGUI.showMessage(name+":"+readline);
                    if(Thread.currentThread().isInterrupted())break;//如果进程处于中断状态，则立即停止
                    readline=is.readLine();

                }

        }
        catch (Exception e){
            ClientGUI.showMessage("再见");
        }
        try{
            is.close(); //关闭Socket输入流
            fis.close();
        }
        catch (Exception e){
                e.printStackTrace();
        }
    }
}
