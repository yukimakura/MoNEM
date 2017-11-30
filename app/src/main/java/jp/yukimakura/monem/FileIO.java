package jp.yukimakura.monem;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
/**
 * Created by ykazu on 2017/11/29.
 */

public class FileIO {

    private MainActivity main;
    FileIO(MainActivity m){
        main = m;
    }

    public boolean filewrite(String filename,String str){
        boolean success = true;
        try{
            FileOutputStream fileOutputstream  = main.openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputstream.write(str.getBytes());


        }catch (FileNotFoundException e){
            e.printStackTrace();
            success = false;
        }catch(java.io.IOException e){
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    // ファイルを読み出し
    public String readFile(String filename) {
        String text = null;

        // try-with-resources
        try (FileInputStream fileInputStream = main.openFileInput(filename);
             BufferedReader reader= new BufferedReader(
                     new InputStreamReader(fileInputStream,"UTF-8"))
        ) {

            String lineBuffer;
            while( (lineBuffer = reader.readLine()) != null ) {
                text = lineBuffer ;
            }

        } catch (java.io.IOException e) {
            filewrite(filename,"0.0");
            e.printStackTrace();
        }

        return text;
    }
}
