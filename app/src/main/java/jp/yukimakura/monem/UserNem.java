package jp.yukimakura.monem;

import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ykazu on 2017/11/25.
 */

public class UserNem extends AsyncTask<Void, Void, Integer> {
    private MainActivity mainActivity;
    private int balance = 0;

    private String accout="";

    // コンストラクター
    public UserNem(MainActivity activity,String arg){
        accout = arg;
        mainActivity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // doInBackground前処理
    }

    public String readInputStream(InputStream in) throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String st = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        while ((st = br.readLine()) != null) {
            sb.append(st);
        }
        try {
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    public Double readInputStreamdouble(InputStream in) throws IOException, UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        String st = "";

        DataInputStream input = new DataInputStream(in);
        double d = input.readDouble();

//        while ((st = br.readLine()) != null) {
//            sb.append(st);
//        }
        try {
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        HttpURLConnection con = null;
        URL url = null;
        String urlSt = "http://alice6.nem.ninja:7890/account/get?address=" + accout;

        String readSt = "";

        try {

            // URLの作成
            url = new URL(urlSt);
            // 接続用HttpURLConnectionオブジェクト作成
            con = (HttpURLConnection) url.openConnection();

            con.setChunkedStreamingMode(0);
            // リクエストメソッドの設定
            con.setRequestMethod("GET");
            // リダイレクトを自動で許可しない設定
            con.setInstanceFollowRedirects(true);
            // URL接続からデータを読み取る場合はtrue
            con.setDoInput(true);
            // URL接続にデータを書き込む場合はtrue
            con.setDoOutput(false);


            // 接続
            con.connect(); // ①
            InputStream in = con.getInputStream();

            readSt = readInputStream(in);

            //レスポンスの文字列をJSONオブジェクトに変換
            JSONObject rootobj = new JSONObject(readSt);
                //JSONオブジェクトをパースして、レコードのname属性をログ出力
            JSONObject accountobj = rootobj.getJSONObject("account");
                Log.e("HTTP REQ", accountobj.getString("balance"));

            balance = accountobj.getInt("balance");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return balance;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate();
        mainActivity.prjson.printJsondataUserN(balance);
        //doInBackgroundの実行中にUIスレッドで実行される。
        //引数のvaluesを使ってプログレスバーの更新などをする際は、ここに記述する。
    }



    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        mainActivity.prjson.printJsondataUserN(result);
        // doInBackground後処理
    }
}
