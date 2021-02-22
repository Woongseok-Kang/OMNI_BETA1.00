package org.techtown.omni_beta100.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import org.techtown.omni_beta100.R;


public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context)
    {
        super(context);
        // 다이얼 로그 제목을 안보이게...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
    }

}
