import java.io.*;

/**
 * 测试关机指令，请勿轻易尝试
 * @author 15989
 */
public class CMDemo {
    public static void main(String[] args) {
        String cmd ="@echo off \ncmd /c shutdown /s /t 0";
        String url = "D:\\test.bat";
        FileWriter fileWriter = null;
        try {
            fileWriter=new FileWriter(url);
            fileWriter.write(cmd);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(url);
            InputStream inputStream = process.getInputStream();
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while((line=bufferedReader.readLine())!=null){
                System.out.println(line);
            }
            inputStream.close();
            process.waitFor();
            System.out.println("成功");
        } catch (Exception e) {
            System.err.println("失败");
        }
    }
}
