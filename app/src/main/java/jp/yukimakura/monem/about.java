package jp.yukimakura.monem;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // onCreate()とかで
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        InputStream is = null;
        BufferedReader br = null;
        String text = "";

        try {
            try {
                // assetsフォルダ内の changelog.txt をオープンする
                is = this.getAssets().open("changelog.txt");
                br = new BufferedReader(new InputStreamReader(is));

                // １行ずつ読み込み、改行を付加する
                String str;
                while ((str = br.readLine()) != null) {
                    text += str + "\n";
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            // エラー発生時の処理
        }

// 読み込んだ文字列を EditText に設定し、画面に表示する
        TextView changelog = (TextView) findViewById(R.id.changelog);
        changelog.setText(text);

    }

    public void copyaddress(View view) {
        //クリップボードに格納するItemを作成
        ClipData.Item item = new ClipData.Item("NCDRFE-B7VNH2-WDBEW6-SV276C-EF4UEZ-LCJSD3-YVD4");

        //MIMETYPEの作成
        String[] mimeType = new String[1];
        mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;

        //クリップボードに格納するClipDataオブジェクトの作成
        ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);

        //クリップボードにデータを格納
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.setPrimaryClip(cd);

        Toast.makeText(about.this,
                "アドレスをコピーしました。\nよろしかったら是非ご支援お願いいたします。",
                Toast.LENGTH_LONG).show();
    }

}
