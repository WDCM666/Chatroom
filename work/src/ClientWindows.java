import javax.swing.*;
import java.awt.*;
public class ClientWindows {
     public JTextArea showArea;
     public JTextField msgText;
     public JFrame mainJframe;
     public JButton sentBtn;
     public JScrollPane JSPane;
     public JPanel pane;
     public Container con;
     public String message=null;
     public ClientWindows(){
         mainJframe=new JFrame("聊天——客户端");
         con=mainJframe.getContentPane();
         showArea=new JTextArea();
         showArea.setEditable(false);
         showArea.setLineWrap(true);
         JSPane=new JScrollPane(showArea);
         msgText=new JTextField();
         msgText.setColumns(30);

         //msgText.addActionListener((ActionListener) this);

         sentBtn=new JButton("发送");

         sentBtn.addActionListener(e -> {
             try {
                     message =msgText.getText();
                     // 清空文本框内容
                     msgText.setText(null);
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
         });

         pane=new JPanel();
         pane.setLayout(new FlowLayout());
         pane.add(msgText);
         pane.add(sentBtn);

         con.add(JSPane, BorderLayout.CENTER);
         con.add(pane, BorderLayout.SOUTH);
         mainJframe.setSize (500 ,400);
         mainJframe.setVisible (true);
         mainJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         showArea.append("连接成功，欢迎来到聊天室\n");
         showArea.append("功能介绍：\n"+"本聊天室功能有群聊和私聊\n"+"在群聊中可以和群聊中的人聊天\n"+
                 "在私聊中可以和在线的人聊天并传输文件\n"+"传输文件时需要输入关键词：文件传输\n"+
                 "最后可通过输入关键词：bye来离开聊天室\n");
         showArea.append("请输入用户姓名\n");
     }
     //获取文本框的消息
     String GetMessage(){
         String ss;
         while(true){
             showArea.append("");
             if(message!=null){
                 ss=message;
                 message=null;
                 return ss;
             }
         }
     }
     //在界面上显示信息
     void showMessage(String s){
         showArea.append(s+"\n");
     }
}
