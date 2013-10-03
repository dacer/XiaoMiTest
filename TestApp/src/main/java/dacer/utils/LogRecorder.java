package dacer.utils;

import android.os.Environment;

import com.dacer.testapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dacer on 10/2/13.
 */
public class LogRecorder {

    public static void record(String s){
        Date date=new Date();
        SimpleDateFormat SDF=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dString=SDF.format(date);
        String needWriteMessage = "["+dString + "]  " + s;
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Dacer.log");
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
//            Log.e("Service", "Logout finished"+dString);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int numberStringInLog(String s){
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Dacer.log");
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        String str = text.toString();
        String findStr = s;
        int lastIndex = 0;
        int count =0;

        while(lastIndex != -1){

            lastIndex = str.indexOf(findStr,lastIndex);

            if( lastIndex != -1){
                count ++;
                lastIndex+=findStr.length();
            }
        }
        return count;
    }

}