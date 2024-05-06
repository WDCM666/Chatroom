import java.io.BufferedReader;
import java.io.IOException;

public class People {
    public BufferedReader is;
    ClientWindows ClientGUI;
    People(BufferedReader is,ClientWindows ClientGUI){
        this.is =is;
        this.ClientGUI=ClientGUI;
    }
    void show() throws IOException {//显示在线的人有哪些
        ClientGUI.showMessage("在线的人有:");
        String readline=is.readLine();
        while(!readline.equals("显示完毕")){
            ClientGUI.showMessage(readline);
            readline= is.readLine();
        }
    }
}
