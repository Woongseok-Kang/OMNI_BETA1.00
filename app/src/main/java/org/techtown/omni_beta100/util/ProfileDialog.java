package org.techtown.omni_beta100.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.techtown.omni_beta100.R;

import java.util.Objects;

public class ProfileDialog extends Dialog {


    private Context mContext;
    public static ImageButton imbt_ch_profile;
    public static EditText et_name;
    public static Button bt_okay, bt_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_profile);


        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.

       imbt_ch_profile = (ImageButton)findViewById(R.id.bt_ch_profile);
       et_name = (EditText)findViewById(R.id.et_name);
       bt_okay = (Button)findViewById(R.id.bt_profile_okay);
       bt_cancel = (Button)findViewById(R.id.bt_profile_cancel);

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

    public ProfileDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

}
