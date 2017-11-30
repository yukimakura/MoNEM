package jp.yukimakura.monem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    TextView printtextM,printtextN,haveN,haveM,alljpy,alerttext;

    Double mona=0.0,nem=0.0,haveNem = 0.0,haveMona = 0.0,alert = 0.0;

    boolean played_nem = false,played_mona = false;

    public int alertsounddata;

    public SoundPool soundPool;

    public FileIO fileIO;

    public PrintJSONs prjson;

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // onCreate()とかで
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        fileIO = new FileIO(this);

        printtextM = (TextView)findViewById(R.id.printM);
        printtextN = (TextView)findViewById(R.id.printN);
        haveN = (TextView)findViewById(R.id.HaveN);
        haveM = (TextView)findViewById(R.id.HaveM);
        alljpy = (TextView)findViewById(R.id.allJpy);
        alerttext = (TextView)findViewById(R.id.alerttext);

        WebView nemchart = (WebView)findViewById(R.id.nemchart);
        WebView monachart = (WebView)findViewById(R.id.monachart);

        nemchart.getSettings().setDisplayZoomControls(true);//enable zoom ctrl
        monachart.getSettings().setDisplayZoomControls(true);

        nemchart.getSettings().setJavaScriptEnabled(true); //enable JS
        monachart.getSettings().setJavaScriptEnabled(true);

        monachart.loadUrl("https://zaif.jp/trade_mona_jpy");
        nemchart.loadUrl("https://zaif.jp/trade_xem_jpy");


        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        // shitei.wav をロードしておく
        alertsounddata = soundPool.load(this, R.raw.shitei, 1);


        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("debug","sampleId="+sampleId);
                Log.d("debug","status="+status);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        prjson = new PrintJSONs(this);


        UserNem httpresUser= new UserNem(MainActivity.this,fileIO.readFile("Usernemaddress.monem"));

        String readmona = fileIO.readFile("Usermona.monem");
        String readmonaalert = fileIO.readFile("Usermonaalert.monem");
        try{
            haveMona = Double.valueOf(readmona);
            alert = Double.valueOf(readmonaalert);
        }catch(NumberFormatException nfex) {
            haveMona = 0.0;
            alert = 0.0;
            Toast.makeText(MainActivity.this,
                    "エラー\nメニューから所持MONAとアラート設定を入力し直してください。",
                    Toast.LENGTH_LONG).show();
        }
        httpresUser.execute();

        alerttext.setText("alert line ￥"+alert);

        timer = new Timer();
        Log.d("Timer","Start");
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // ここに繰り返し処理を書く
                HttpResponsAsyncMona httpresMona = new HttpResponsAsyncMona(MainActivity.this);
                HttpResponsAsyncNem httpresNem = new HttpResponsAsyncNem(MainActivity.this);
                httpresNem.execute();
                httpresMona.execute();

            }
        },100, 10000);

    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("Timer","Stop");
        timer.cancel();

    }


    // メニューをActivity上に設置する
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 参照するリソースは上でリソースファイルに付けた名前と同じもの
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // メニューが選択されたときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Nemadress:

                //テキスト入力を受け付けるビューを作成します。
                final EditText editViewnem = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("NEMアドレスを入力\n(ハイフン無し)")
                        //setViewにてビューを設定します。
                        .setView(editViewnem)
                        .setPositiveButton("入力", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //入力した文字をトースト出力する
                                fileIO.filewrite("Usernemaddress.monem", editViewnem.getText().toString());
                                Toast.makeText(MainActivity.this,
                                        editViewnem.getText().toString() + "\nすぐ反映されない場合は再起動してください",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
                UserNem httpresUser= new UserNem(MainActivity.this,fileIO.readFile("Usernemaddress.monem"));

                httpresUser.execute();

                return true;

            case R.id.havemona:

                //テキスト入力を受け付けるビューを作成します。
                final EditText editViewmona = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("あなたが所持しているMONA\n（アドレスではない）")
                        //setViewにてビューを設定します。
                        .setView(editViewmona)
                        .setPositiveButton("入力", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    //入力した文字をトースト出力する
                                    fileIO.filewrite("Usermona.monem", editViewmona.getText().toString());

                                    haveMona = Double.parseDouble(editViewmona.getText().toString());
                                } catch (NumberFormatException nfex) {

                                    fileIO.filewrite("Usermona.monem", Double.toString(haveMona));
                                    Toast.makeText(MainActivity.this,
                                            "エラー\n実数値を入力してください。",
                                            Toast.LENGTH_LONG).show();
                                } finally {
                                    Toast.makeText(MainActivity.this,
                                            haveMona + "MONA\n10秒内に反映されます。",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();

                return true;
            case R.id.voicealert:

                //テキスト入力を受け付けるビューを作成します。
                final EditText editViewalert = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("下回ったらアラートを出したい\n金額を入力（MONAのみ)")
                        //setViewにてビューを設定します。
                        .setView(editViewalert)
                        .setPositiveButton("入力", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    //入力した文字をトースト出力する
                                    fileIO.filewrite("Usermonaalert.monem", editViewalert.getText().toString());

                                    alert = Double.parseDouble(editViewalert.getText().toString());
                                } catch (NumberFormatException nfex) {

                                    fileIO.filewrite("Usermonaalert.monem", Double.toString(alert));
                                    Toast.makeText(MainActivity.this,
                                            "エラー\n実数値を入力してください。",
                                            Toast.LENGTH_LONG).show();
                                } finally {
                                    Toast.makeText(MainActivity.this,
                                            alert + "円\n次回起動時に反映されます。",
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this,
                                            "アプリをkillしないでください。",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();

                played_mona = false;
                alerttext.setTextColor(Color.GRAY);

                return true;

            case R.id.about:
                Intent about_intent = new Intent(this, about.class);
                startActivity(about_intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
