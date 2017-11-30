package jp.yukimakura.monem;

import android.os.AsyncTask;
import android.util.Log;

import org.json.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.*;

/**
 * Created by ykazu on 2017/11/24.
 */

public class HttpResponsAsyncNem extends AsyncTask<Void, Void, Double> {

    private MainActivity mainActivity;
    private Double pricedata = 0.0;

    // コンストラクター
    public HttpResponsAsyncNem(MainActivity activity){
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



    @Override
    protected Double doInBackground(Void... params) {

        HttpURLConnection con = null;
        URL url = null;
        String urlSt = "https://api.zaif.jp/api/1/last_price/xem_jpy";

        String readSt = "";

            try {

                    // URLの作成
                    url = new URL(urlSt);
                    // 接続用HttpURLConnectionオブジェクト作成
                    con = (HttpURLConnection) url.openConnection();

                //    con.setChunkedStreamingMode(0);
                    // リクエストメソッドの設定
                    con.setRequestMethod("GET");
                    // リダイレクトを自動で許可しない設定
              //      con.setInstanceFollowRedirects(false);
                    // URL接続からデータを読み取る場合はtrue
             //       con.setDoInput(true);
                    // URL接続にデータを書き込む場合はtrue
           //         con.setDoOutput(false); //今回の場合はリクエストメソッドがGETにしたいためfalse


                    // 接続
                    con.connect(); // ①
                    InputStream in = con.getInputStream();

                    readSt = readInputStream(in);

                    JSONObject jsonData = new JSONObject(readSt);

                    pricedata = jsonData.getDouble("last_price");

                Log.e("HTTP REQ NEM", Double.toString(pricedata));




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return pricedata;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate();
        mainActivity.prjson.printJsondataNem(pricedata);
        //doInBackgroundの実行中にUIスレッドで実行される。
        //引数のvaluesを使ってプログレスバーの更新などをする際は、ここに記述する。
    }



    @Override
    protected void onPostExecute(Double result) {
        super.onPostExecute(result);
        mainActivity.prjson.printJsondataNem(result);
        // doInBackground後処理
    }

}