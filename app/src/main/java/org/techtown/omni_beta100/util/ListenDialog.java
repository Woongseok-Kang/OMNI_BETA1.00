package org.techtown.omni_beta100.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import org.techtown.omni_beta100.R;

import java.util.Objects;

public class ListenDialog extends Dialog {


    public static Button bt_re_listen;
    public static Button bt_close;
    public static SeekBar play_rec_bar;
    public static TextView seek_start;
    public static TextView seek_end;


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_seekbar);


        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.

        bt_re_listen = (Button)findViewById(R.id.bt_re_listen);
        bt_close = (Button)findViewById(R.id.bt_close);
        play_rec_bar = (SeekBar)findViewById(R.id.play_seek);

        seek_start = (TextView) findViewById(R.id.seek_start);
        seek_end = (TextView) findViewById(R.id.seek_end);
        // 버튼 리스너 설정
       /* saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시
                // ...코드..
                Toast.makeText(mContext,et_text.getText().toString(), Toast.LENGTH_SHORT).show();
                // Custom Dialog 종료
                dismiss();
            }
        });*/

       /*bt_close.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });*/

    }

    public ListenDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

}
