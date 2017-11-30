package jp.yukimakura.monem;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ykazu on 2017/11/29.
 */

public class PrintJSONs {
    MainActivity main;

    PrintJSONs(MainActivity m){
        main = m;
    }

    public void printJsondataMona(Double pricedata){
        main.printtextM.setText("MONA/JPY = " + pricedata);
        main.mona = pricedata;

        double price = main.haveNem * main.nem;
        main.haveN.setText(main.haveNem +"XEM ￥"+Math.round(Math.floor(price)));

        price = main.haveMona * main.mona;
        main.haveM.setText(main.haveMona + "MONA ￥"+Math.round(Math.floor(price)));

        price = (main.haveNem * main.nem) + (main.haveMona * main.mona);
        main.alljpy.setText("Total ￥"+Math.round(Math.floor(price)));
        playalert();

    }

    public void printJsondataNem(Double pricedata){
        main.printtextN.setText("NEM/JPY = " + pricedata);
        main.nem = pricedata;

        double price = main.haveNem * main.nem;
        main.haveN.setText(main.haveNem +"XEM ￥"+Math.round(Math.floor(price)));

        price = main.haveMona * main.mona;
        main.haveM.setText(main.haveMona + "MONA ￥"+Math.round(Math.floor(price)));

        price = (main.haveNem * main.nem) + (main.haveMona * main.mona);
        main.alljpy.setText("Total ￥"+Math.round(Math.floor(price)));

    }
  public void printJsondataUserN(Integer pricedata){
        main.haveNem = (double)pricedata/1000000;
        double price = main.haveNem * main.nem;
        main.haveN.setText(main.haveNem +"XEM ￥"+Math.round(Math.floor(price)));
        price = main.haveMona * main.mona;
        main.haveM.setText(main.haveMona + "MONA ￥"+Math.round(Math.floor(price)));
        price = price + (main.haveNem * main.nem);
        main.alljpy.setText("Total ￥"+Math.round(Math.floor(price)));

    }

    private void playalert(){
        Log.d("played",Boolean.toString(main.played_mona));
        main.alerttext.setText("alert line ￥"+main.alert);
        if(main.played_mona == false){
            if(main.mona<main.alert) {
                main.soundPool.play(main.alertsounddata, 1.0f, 1.0f, 1, 0, 1);

                main.alerttext.setTextColor(Color.RED);

                main.played_mona = true;
            }


        }

    }
}
