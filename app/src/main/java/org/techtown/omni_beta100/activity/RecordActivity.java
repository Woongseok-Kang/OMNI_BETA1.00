package org.techtown.omni_beta100.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;

import org.techtown.omni_beta100.AsyncTask.InternetThread;
import org.techtown.omni_beta100.Interface.onBackPressedListener;
import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.fragment.Files_Fragment_Item;
import org.techtown.omni_beta100.fragment.PlayFragment;
import org.techtown.omni_beta100.fragment.RecordFragment;
import org.techtown.omni_beta100.util.AudioEditor;
import org.techtown.omni_beta100.util.Edit_item;
import org.techtown.omni_beta100.util.MusicDto;
import org.techtown.omni_beta100.util.SamplePlayer;
import org.techtown.omni_beta100.util.SoundFile;
import org.techtown.omni_beta100.util.Speech;
import org.techtown.omni_beta100.util.Utility;
import org.techtown.omni_beta100.view.MarkerView;
import org.techtown.omni_beta100.view.WaveformView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener,
        MarkerView.MarkerListener,
        WaveformView.WaveformListener {

    private TextView txtAudioCancel;
    private TextView txtAudioUpload;
    public static TextView txtStartPosition;
    public static TextView txtEndPosition;
    private LinearLayout llAudioCapture;
    private TextView txtAudioRecord;
    private TextView txtAudioRecordTime;
    private RelativeLayout rlAudioEdit;
    private MarkerView markerStart;
    private MarkerView markerEnd;
    private WaveformView audioWaveform;
    private TextView txtAudioRecordTimeUpdate;
    private TextView txtAudioReset;
    private TextView txtAudioDone;
    private TextView txtAudioPlay;
    private TextView txtAudioRecordUpdate;
    private TextView txtAudioCrop;
    private Button bt_stt_finish;
    private int mMarkerLeftInset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mMarkerBottomOffset;

    private boolean isAudioRecording = false;
    private long mRecordingLastUpdateTime;
    private double mRecordingTime;
    private boolean mRecordingKeepGoing;
    private SoundFile mLoadedSoundFile;
    private SoundFile mRecordedSoundFile;
    private SamplePlayer mPlayer;
    private float mDensity;
    private ProgressDialog mProgressDialog;
    private long mLoadingLastUpdateTime;
    private boolean mLoadingKeepGoing;
    private File mFile;
    private Handler mHandler;
    private boolean mTouchDragging;
    private float mTouchStart;
    private int mTouchInitialOffset;
    private int mTouchInitialStartPos;
    private int mTouchInitialEndPos;

    private boolean mStartVisible;
    private boolean mEndVisible;
    private int mLastDisplayedStartPos;
    private int mLastDisplayedEndPos;
    private boolean mIsPlaying = false;
    private boolean mKeyDown;

    public SoundFile soundfile;

    private int mOffset;
    private int mOffsetGoal;
    private int mFlingVelocity;
    private int mPlayEndMillSec;
    private int mWidth;
    private int mMaxPos;
    public int mStartPos;
    private int mEndPos;
    private int mTextLeftInset;
    private int mTextRightInset;
    private int mTextTopOffset;
    private int mTextBottomOffset;
    PlayFragment frg_play;
    RecordFragment rfg_record;
    private ContentResolver res;
    private int position;
    private ArrayList<MusicDto> list;
    TextView textView;
    public InputStream stream;
    private Speech speech;
    public int sttMode;
    String path;
    public TextView file_title;// 추가
    private AudioEditor audioEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //txtAudioCancel = (TextView) findViewById(R.id.txtAudioCancel1);
        //   txtAudioUpload = (TextView) findViewById(R.id.txtAudioUpload);
        txtStartPosition = (TextView) findViewById(R.id.txtStartPosition1);
        txtEndPosition = (TextView) findViewById(R.id.txtEndPosition1);
        //   llAudioCapture = (LinearLayout) findViewById(R.id.llAudioCapture);
        //   txtAudioRecord = (TextView) findViewById(R.id.txtAudioRecord);
        //   txtAudioRecordTime = (TextView) findViewById(R.id.txtAudioRecordTime);
        //   rlAudioEdit = (RelativeLayout) findViewById(R.id.rlAudioEdit);
        markerStart = (MarkerView) findViewById(R.id.markerStart1);
        markerEnd = (MarkerView) findViewById(R.id.markerEnd1);
        audioWaveform = (WaveformView) findViewById(R.id.audioWaveform1);
        txtAudioRecordTimeUpdate = (TextView) findViewById(R.id.txtAudioRecordTimeUpdate1);
        // txtAudioReset = (TextView) findViewById(R.id.txtAudioReset);
        txtAudioDone = (TextView) findViewById(R.id.txtAudioDone);    //
        txtAudioPlay = (TextView) findViewById(R.id.txtAudioPlay);    //
        // txtAudioRecordUpdate = (TextView) findViewById(R.id.txtAudioRecordUpdate);
        // txtAudioCrop = (TextView) findViewById(R.id.txtAudioCrop);
        bt_stt_finish = (Button) findViewById(R.id.bt_stt_finish);       //
        file_title = (TextView) findViewById(R.id.file_title1);//추가


        /**
         * Change this for marker handle as per your view
         */
        mMarkerLeftInset = (int) (17.5 * mDensity);
        mMarkerRightInset = (int) (19.5 * mDensity);
        mMarkerTopOffset = (int) (6 * mDensity);
        mMarkerBottomOffset = (int) (6 * mDensity);

        /**
         * Change this for duration text as per your view
         */

        mTextLeftInset = (int) (20 * mDensity);
        mTextTopOffset = (int) (-1 * mDensity);
        mTextRightInset = (int) (19 * mDensity);
        mTextBottomOffset = (int) (-40 * mDensity);

        mHandler = new Handler();

        mRecordedSoundFile = null;
        mKeyDown = false;
        audioWaveform.setListener(this);

        markerStart.setListener(this);
        markerStart.setAlpha(1f);
        markerStart.setFocusable(true);
        markerStart.setFocusableInTouchMode(true);
        mStartVisible = true;


        markerEnd.setListener(this);
        markerEnd.setAlpha(1f);
        markerEnd.setFocusable(true);
        markerEnd.setFocusableInTouchMode(true);
        mEndVisible = true;


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_voice, PlayFragment.newInstance()).commit();

        Intent intent = getIntent();
//stt start
        speech = new Speech();
        //textView = (TextView)findViewById(R.id.sttResult);
        stream = getResources().openRawResource(R.raw.credential);
        sttMode = 0;///


        if(intent.getSerializableExtra("playlist")!=null){
            position = intent.getIntExtra("position",-1);
            list = (ArrayList<MusicDto>) intent.getSerializableExtra("playlist");
        }
        else{
            position = intent.getIntExtra("pl_position", -1);
            list = (ArrayList<MusicDto>) intent.getSerializableExtra("pl_playlist");
        }

        res = getContentResolver();
        System.out.println("포지션 = " + position);
        Uri musicURI = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + list.get(position).getId());

        Cursor cursor = getContentResolver().query(musicURI, null, null, null, null);
        cursor.moveToNext();
        //String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        String URI = "gs://ssussuk_omni_bucket/" + list.get(position).getTitle();
        path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        //InternetThread internetThread = new InternetThread(URI,textView,stream, sttMode);
        //internetThread.start();

        file_title.setText(list.get(position).getTitle());  //편집파일 제목


        loadFromFile(path);
        mHandler.postDelayed(mTimerRunnable, 100);
        // markerStart.setVisibility(View.GONE);
        // markerEnd.setVisibility(View.GONE);

    }

    private Runnable mTimerRunnable = new Runnable() {
        public void run() {
            // Updating Text is slow on Android.  Make sure
            // we only do the update if the text has actually changed.
            if (mStartPos != mLastDisplayedStartPos) {
                txtStartPosition.setText(myTimeformat(formatTime(mStartPos)));

                mLastDisplayedStartPos = mStartPos;
            }


            if (mEndPos != mLastDisplayedEndPos) {
                txtEndPosition.setText(myTimeformat(formatTime(mEndPos)));

                mLastDisplayedEndPos = mEndPos;
            }

            mHandler.postDelayed(mTimerRunnable, 100);
        }
    };


    public int getPosition() {
        return this.position;
    }

    public ArrayList<MusicDto> getList() {
        return this.list;
    }


    public void replaceFragment(Fragment fragment) {


        FragmentManager frgm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = frgm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_voice, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
    }
    // }

    /**
     * Start recording
     */
    //private void startRecording()

    /**
     * After recording finish do necessary steps
     *
     * @param mSoundFile sound file
     * @param isReset    isReset
     */
    private void finishOpeningSoundFile(SoundFile mSoundFile, int isReset) {
        audioWaveform.setVisibility(View.VISIBLE);
        audioWaveform.setSoundFile(mSoundFile);
        audioWaveform.recomputeHeights(mDensity);
        Log.v("ㅏ", "ㅈ");
        mMaxPos = audioWaveform.maxPos();   // 얘 값이 null 이 뜬다..
        mLastDisplayedStartPos = -1;
        mLastDisplayedEndPos = -1;
        Log.d("mMaxPos", mMaxPos + " ");
        mTouchDragging = false;
        Log.v("ㅡ", "ㄴ");
        mOffset = 0;
        mOffsetGoal = 0;
        mFlingVelocity = 0;
        resetPositions();
        if (mEndPos > mMaxPos)
            mEndPos = mMaxPos;
        Log.v("a", "a");
        Log.v("mStartPos", mStartPos + " ");
        Log.v("mEndPos", mEndPos + " ");
        if (isReset == 1) {
            mStartPos = audioWaveform.secondsToPixels(0);
            mEndPos = audioWaveform.secondsToPixels(audioWaveform.pixelsToSeconds(mMaxPos));
        }
        Log.v("mStartPos", "b" + mStartPos);
        Log.v("mEndPos", "b" + mEndPos);
        if (audioWaveform != null && audioWaveform.isInitialized()) {
            double seconds = audioWaveform.pixelsToSeconds(mMaxPos);
            int i = Integer.parseInt(String.valueOf(Math.round(seconds)));

            /*int min = (int) (seconds / 60);
            float sec = (float) (seconds - 60 * min);*/

            int hour = i / 3600;
            int min = i % 3600 / 60;
            int sec = i % 3600 % 60;

            if (hour == 0) {
                txtAudioRecordTimeUpdate.setText(((min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec)));
            } else {
                txtAudioRecordTimeUpdate.setText(((hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec)));
            }

            //txtAudioRecordTimeUpdate.setText(Double.toString(seconds));
            //txtAudioRecordTimeUpdate.setText(String.format(Locale.US, "%02d:%05d", min, (int)sec));
            Log.v("seconds", seconds + " ");
            Log.v("min", min + " ");
        }
        Log.v("seconds", " ");


        updateDisplay();
    }

    /**
     * Update views
     */

    private synchronized void updateDisplay() {
        Log.v("d", "d");
        if (mIsPlaying) {
            int now = mPlayer.getCurrentPosition();
            int frames = audioWaveform.millisecsToPixels(now);
            audioWaveform.setPlayback(frames);
            Log.e("mWidth >> ", "" + mWidth);
            setOffsetGoalNoUpdate(frames - mWidth / 2);
            if (now >= mPlayEndMillSec) {
                handlePause();
            }
        }
        Log.v("e", "e");
        if (!mTouchDragging) {
            int offsetDelta;

            if (mFlingVelocity != 0) {
                offsetDelta = mFlingVelocity / 30;
                if (mFlingVelocity > 80) {
                    mFlingVelocity -= 80;
                } else if (mFlingVelocity < -80) {
                    mFlingVelocity += 80;
                } else {
                    mFlingVelocity = 0;
                }

                mOffset += offsetDelta;

                if (mOffset + mWidth / 2 > mMaxPos) {
                    mOffset = mMaxPos - mWidth / 2;
                    mFlingVelocity = 0;
                }
                if (mOffset < 0) {
                    mOffset = 0;
                    mFlingVelocity = 0;
                }
                mOffsetGoal = mOffset;
            } else {
                offsetDelta = mOffsetGoal - mOffset;

                if (offsetDelta > 10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta > 0)
                    offsetDelta = 1;
                else if (offsetDelta < -10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta < 0)
                    offsetDelta = -1;
                else
                    offsetDelta = 0;

                mOffset += offsetDelta;
            }
        }
        Log.v("f", "f");
        audioWaveform.setParameters(mStartPos, mEndPos, mOffset);
        audioWaveform.invalidate();
        Log.v("g", "g");
        markerStart.setContentDescription(
                " Start Marker" +
                        formatTime(mStartPos));
        markerEnd.setContentDescription(
                " End Marker" +
                        formatTime(mEndPos));

        int startX = mStartPos - mOffset - mMarkerLeftInset;
        if (startX + markerStart.getWidth() >= 0) {
            if (!mStartVisible) {
                // Delay this to avoid flicker
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        mStartVisible = true;
                        markerStart.setAlpha(1f);
                        txtStartPosition.setAlpha(1f);
                    }
                }, 0);
            }
        } else {
            if (mStartVisible) {
                markerStart.setAlpha(0f);
                txtStartPosition.setAlpha(0f);
                mStartVisible = false;
            }
            startX = 0;
        }


        int startTextX = mStartPos - mOffset - mTextLeftInset;
        if (startTextX + markerStart.getWidth() < 0) {
            startTextX = 0;
        }


        int endX = mEndPos - mOffset - markerEnd.getWidth() + mMarkerRightInset;
        if (endX + markerEnd.getWidth() >= 0) {
            if (!mEndVisible) {
                // Delay this to avoid flicker
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        mEndVisible = true;
                        markerEnd.setAlpha(1f);
                    }
                }, 0);
            }
        } else {
            if (mEndVisible) {
                markerEnd.setAlpha(0f);
                mEndVisible = false;
            }
            endX = 0;
        }

        int endTextX = mEndPos - mOffset - txtEndPosition.getWidth() + mTextRightInset;
        if (endTextX + markerEnd.getWidth() < 0) {
            endTextX = 0;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(
//                startX,
//                mMarkerTopOffset,
//                -markerStart.getWidth(),
//                -markerStart.getHeight());
        params.setMargins(
                startX,
                audioWaveform.getMeasuredHeight() / 2 + mMarkerTopOffset,
                -markerStart.getWidth(),
                -markerStart.getHeight());
        markerStart.setLayoutParams(params);


        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                startTextX,
                mTextTopOffset,
                -txtStartPosition.getWidth(),
                -txtStartPosition.getHeight());
        txtStartPosition.setLayoutParams(params);


        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                endX,
                audioWaveform.getMeasuredHeight() / 2 + mMarkerBottomOffset,
                -markerEnd.getWidth(),
                -markerEnd.getHeight());
//        params.setMargins(
//                endX,
//                audioWaveform.getMeasuredHeight() - markerEnd.getHeight() - mMarkerBottomOffset,
//                -markerEnd.getWidth(),
//                -markerEnd.getHeight());
        markerEnd.setLayoutParams(params);


        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                endTextX,
                audioWaveform.getMeasuredHeight() - txtEndPosition.getHeight() - mTextBottomOffset,
                -txtEndPosition.getWidth(),
                -txtEndPosition.getHeight());

        txtEndPosition.setLayoutParams(params);
        audioWaveform.setIsDrawBorder(false);
        // audioWaveform.setBackgroundColor(getResources().getColor(R.color.colorWaveformBg));
        markerStart.setVisibility(View.INVISIBLE);
        markerEnd.setVisibility(View.INVISIBLE);
        txtStartPosition.setVisibility(View.INVISIBLE);
        txtEndPosition.setVisibility(View.INVISIBLE);
    }

    /**
     * Reset all positions
     */

    private void resetPositions() {
        mStartPos = audioWaveform.secondsToPixels(0.0);
        mEndPos = audioWaveform.secondsToPixels(15.0);
    }

    private void setOffsetGoalNoUpdate(int offset) {
        if (mTouchDragging) {
            return;
        }

        mOffsetGoal = offset;
        if (mOffsetGoal + mWidth / 2 > mMaxPos)
            mOffsetGoal = mMaxPos - mWidth / 2;
        if (mOffsetGoal < 0)
            mOffsetGoal = 0;
    }

    private String myTimeformat(String time) {

        int real_time;
        int h, m, s;
        String sub;

        switch (time.length()) {


            case 5:
                sub = time.substring(0, 2);
                real_time = Integer.parseInt(sub);

                h = 0;
                m = real_time % 3600 / 60;
                s = real_time % 3600 % 60;

                return ((m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s));

            case 6:
                sub = time.substring(0, 3);
                real_time = Integer.parseInt(sub);

                h = 0;
                m = real_time % 3600 / 60;
                s = real_time % 3600 % 60;

                return ((m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s));

            case 7:
                sub = time.substring(0, 4);
                real_time = Integer.parseInt(sub);
                h = real_time / 3600;
                m = real_time % 3600 / 60;
                s = real_time % 3600 % 60;
                if (h == 0) {
                    return ((m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s));
                } else {
                    return ((h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s));
                }

            default:
                return "";
        }

    }

    private String formatTime(int pixels) {
        if (audioWaveform != null && audioWaveform.isInitialized()) {
            return formatDecimal(audioWaveform.pixelsToSeconds(pixels));
        } else {
            return "";
        }
    }

    private String formatDecimal(double x) {
        int xWhole = (int) x;
        int xFrac = (int) (100 * (x - xWhole) + 0.5);

        if (xFrac >= 100) {
            xWhole++; //Round up
            xFrac -= 100; //Now we need the remainder after the round up
            if (xFrac < 10) {
                xFrac *= 10; //we need a fraction that is 2 digits long
            }
        }

        if (xFrac < 10) {
            if (xWhole < 10)
                return "0" + xWhole + ".0" + xFrac;
            else
                return xWhole + ".0" + xFrac;
        } else {
            if (xWhole < 10)
                return "0" + xWhole + "." + xFrac;
            else
                return xWhole + "." + xFrac;

        }
    }

    private int trap(int pos) {
        if (pos < 0)
            return 0;
        if (pos > mMaxPos)
            return mMaxPos;
        return pos;
    }

    private void setOffsetGoalStart() {
        setOffsetGoal(mStartPos - mWidth / 2);
    }

    private void setOffsetGoalStartNoUpdate() {
        setOffsetGoalNoUpdate(mStartPos - mWidth / 2);
    }

    private void setOffsetGoalEnd() {
        setOffsetGoal(mEndPos - mWidth / 2);
    }

    private void setOffsetGoalEndNoUpdate() {
        setOffsetGoalNoUpdate(mEndPos - mWidth / 2);
    }

    private void setOffsetGoal(int offset) {
        setOffsetGoalNoUpdate(offset);
        updateDisplay();
    }

    public void markerDraw() {
    }

    public void markerTouchStart(MarkerView marker, float x) {
        mTouchDragging = true;
        mTouchStart = x;
        mTouchInitialStartPos = mStartPos;
        mTouchInitialEndPos = mEndPos;
        handlePause();
    }

    public void markerTouchMove(MarkerView marker, float x) {
        float delta = x - mTouchStart;

        if (marker == markerStart) {
            mStartPos = trap((int) (mTouchInitialStartPos + delta));
            mEndPos = trap((int) (mTouchInitialEndPos + delta));
        } else {
            mEndPos = trap((int) (mTouchInitialEndPos + delta));
            if (mEndPos < mStartPos)
                mEndPos = mStartPos;
        }

        updateDisplay();
    }

    public void markerTouchEnd(MarkerView marker) {
        mTouchDragging = false;
        if (marker == markerStart) {
            setOffsetGoalStart();
        } else {
            setOffsetGoalEnd();
        }
    }

    public void markerLeft(MarkerView marker, int velocity) {
        mKeyDown = true;

        if (marker == markerStart) {
            int saveStart = mStartPos;
            mStartPos = trap(mStartPos - velocity);
            mEndPos = trap(mEndPos - (saveStart - mStartPos));
            setOffsetGoalStart();
        }

        if (marker == markerEnd) {
            if (mEndPos == mStartPos) {
                mStartPos = trap(mStartPos - velocity);
                mEndPos = mStartPos;
            } else {
                mEndPos = trap(mEndPos - velocity);
            }

            setOffsetGoalEnd();
        }

        updateDisplay();
    }

    public void markerRight(MarkerView marker, int velocity) {
        mKeyDown = true;

        if (marker == markerStart) {
            int saveStart = mStartPos;
            mStartPos += velocity;
            if (mStartPos > mMaxPos)
                mStartPos = mMaxPos;
            mEndPos += (mStartPos - saveStart);
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            setOffsetGoalStart();
        }

        if (marker == markerEnd) {
            mEndPos += velocity;
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            setOffsetGoalEnd();
        }

        updateDisplay();
    }

    public void markerEnter(MarkerView marker) {
    }

    public void markerKeyUp() {
        mKeyDown = false;
        updateDisplay();
    }

    public void markerFocus(MarkerView marker) {
        mKeyDown = false;
        if (marker == markerStart) {
            setOffsetGoalStartNoUpdate();
        } else {
            setOffsetGoalEndNoUpdate();
        }

        // Delay updaing the display because if this focus was in
        // response to a touch event, we want to receive the touch
        // event too before updating the display.
        mHandler.postDelayed(new Runnable() {
            public void run() {
                updateDisplay();
            }
        }, 100);
    }

    //
    // WaveformListener
    //

    /**
     * Every time we get a message that our waveform drew, see if we need to
     * animate and trigger another redraw.
     */
    public void waveformDraw() {
        mWidth = audioWaveform.getMeasuredWidth();
        if (mOffsetGoal != mOffset && !mKeyDown)
            updateDisplay();
        else if (mIsPlaying) {
            updateDisplay();
        } else if (mFlingVelocity != 0) {
            updateDisplay();
        }
    }

    public void waveformTouchStart(float x) {
        mTouchDragging = true;
        mTouchStart = x;
        mTouchInitialOffset = mOffset;
        mFlingVelocity = 0;
//        long mWaveformTouchStartMsec = Utility.getCurrentTime();
    }

    public void waveformTouchMove(float x) {
        mOffset = trap((int) (mTouchInitialOffset + (mTouchStart - x)));
        updateDisplay();
    }

    public void waveformTouchEnd() {
        /*mTouchDragging = false;
        mOffsetGoal = mOffset;

        long elapsedMsec = Utility.Utility.getCurrentTime() - mWaveformTouchStartMsec;
        if (elapsedMsec < 300) {
            if (mIsPlaying) {
                int seekMsec = audioWaveform.pixelsToMillisecs(
                        (int) (mTouchStart + mOffset));
                if (seekMsec >= mPlayStartMsec &&
                        seekMsec < mPlayEndMillSec) {
                    mPlayer.seekTo(seekMsec);
                } else {
//                    handlePause();
                }
            } else {
                onPlay((int) (mTouchStart + mOffset));
            }
        }*/
    }

    private synchronized void handlePause() {
        txtAudioPlay.setBackgroundResource(R.drawable.icon_devide_play);
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        audioWaveform.setPlayback(-1);
        mIsPlaying = false;
    }

    private synchronized void onPlay(int startPosition) {
        if (mIsPlaying) {
            handlePause();
            return;
        }

        if (mPlayer == null) {
            // Not initialized yet
            return;
        }

        try {
            int mPlayStartMsec = audioWaveform.pixelsToMillisecs(startPosition);
            if (startPosition < mStartPos) {
                mPlayEndMillSec = audioWaveform.pixelsToMillisecs(mStartPos);
            } else if (startPosition > mEndPos) {
                mPlayEndMillSec = audioWaveform.pixelsToMillisecs(mMaxPos);
            } else {
                mPlayEndMillSec = audioWaveform.pixelsToMillisecs(mEndPos);
            }
            mPlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener() {
                @Override
                public void onCompletion() {
                    handlePause();
                }
            });
            mIsPlaying = true;

            mPlayer.seekTo(mPlayStartMsec);
            mPlayer.start();
            updateDisplay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waveformFling(float vx) {
        mTouchDragging = false;
        mOffsetGoal = mOffset;
        mFlingVelocity = (int) (-vx);
        updateDisplay();
    }

    public void waveformZoomIn() {
        audioWaveform.zoomIn();
        mStartPos = audioWaveform.getStart();
        mEndPos = audioWaveform.getEnd();
        mMaxPos = audioWaveform.maxPos();
        mOffset = audioWaveform.getOffset();
        mOffsetGoal = mOffset;
        updateDisplay();
    }

    public void waveformZoomOut() {
        audioWaveform.zoomOut();
        mStartPos = audioWaveform.getStart();
        mEndPos = audioWaveform.getEnd();
        mMaxPos = audioWaveform.maxPos();
        mOffset = audioWaveform.getOffset();
        mOffsetGoal = mOffset;
        updateDisplay();
    }

    /**
     * Save sound file as ringtone
     *
     * @param finish flag for finish
     */

    private void saveRingtone(final int finish) {
        // stt_filepath = null;
        double startTime = audioWaveform.pixelsToSeconds(mStartPos);
        double endTime = audioWaveform.pixelsToSeconds(mEndPos);
        final int startFrame = audioWaveform.secondsToFrames(startTime);
        final int endFrame = audioWaveform.secondsToFrames(endTime - 0.04);
        final int duration = (int) (endTime - startTime + 0.5);

        // Create an indeterminate progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Saving....");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        final String outPath = makeRingtoneFilename("AUDIO_TEMP", Utility.AUDIO_FORMAT_1);
        Log.v("outPathqqqqqqqqq", outPath);
        // stt_filepath = outPath;
        Log.v("outPathqqqqqqqqq", outPath);
        // Save the sound file in a background thread

        Thread mSaveSoundFileThread = new Thread() {
            public void run() {
                // Try AAC first.
                // String outPath = makeRingtoneFilename("AUDIO_TEMP", Utility.AUDIO_FORMAT_1);
                // Log.v("outPathqqqqqqqqq", outPath);
                //  stt_filepath = outPath;  // stt를 위한 filepath 넣어주는 코드
                if (outPath == null) {
                    Log.e(" >> ", "Unable to find unique filename");
                    return;
                }
                File outFile = new File(outPath);
                try {
                    // Write the new file
                    mRecordedSoundFile.WriteFile(outFile, startFrame, endFrame - startFrame);

                } catch (Exception e) {
                    // log the error and try to create a .wav file instead
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();
                final String finalOutPath = outPath;
                Runnable runnable = new Runnable() {
                    public void run() {
                        afterSavingRingtone("AUDIO_TEMP",
                                finalOutPath,
                                duration, finish);
                    }
                };
                mHandler.post(runnable);
            }
        };
        mSaveSoundFileThread.start();
    }

    /**
     * After saving as ringtone set its content values
     *
     * @param title    title
     * @param outPath  output path
     * @param duration duration of file
     * @param finish   flag for finish
     */
    private void afterSavingRingtone(CharSequence title,
                                     String outPath,
                                     int duration, int finish) {
        File outFile = new File(outPath);
        long fileSize = outFile.length();




        /*mp3 파일 목록 저장...
            String[] projection = {MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST};

            Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);

            while(cursor.moveToNext()){
                MusicDto musicDto = new MusicDto();
                musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                devide_mp3_list.add(musicDto);
            }
            cursor.close();

        */


        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, outPath);
        values.put(MediaStore.MediaColumns.TITLE, title.toString());
        values.put(MediaStore.MediaColumns.SIZE, fileSize);
        values.put(MediaStore.MediaColumns.MIME_TYPE, Utility.AUDIO_MIME_TYPE);

        values.put(MediaStore.Audio.Media.ARTIST, getApplicationInfo().name);
        values.put(MediaStore.Audio.Media.DURATION, duration);

        values.put(MediaStore.Audio.Media.IS_MUSIC, true);


        Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
        final Uri newUri = getContentResolver().insert(uri, values);
        Log.e("final URI >> ", newUri + " >> " + outPath);
        //editAdapter.modifyUri(newUri.toString());
        //devide_mp3_list.add(0, newUri);
        //editAdapter.add
        Log.e("AudioEditor", "넘겨주는 리스트에 포함이 된다.");

        if (finish == 0) {
            stream = getResources().openRawResource(R.raw.credential);
            //   final InternetThread internetThread = new InternetThread(stt_filepath,stream,sttMode);
            Log.v("f", "yyyyy");
            //   internetThread.start();
            Log.v("q", "yyyyy");
              /* Handler handler = new Handler(){

                    @Override
                    public void handleMessage(Message msg)
                    {
                        Log.v("kkkkkk", "yyyyy");
                        editAdapter.addItem("test",internetThread.getSttText());
                        sttlist.setAdapter(editAdapter);
                        Log.v("nnnnnn", "mmmmm");
                       // Log.v("이거들어옴", internetThread.getSttText());
                    }
                };*/
            // Message msg = handler.obtainMessage();
            // Log.v("q", internetThread.getSttText());
            // handler.sendMessage(msg);
            //loadFromFile(outPath);
        } else if (finish == 1) {
            /*Bundle conData = new Bundle();
            conData.putString("INTENT_AUDIO_FILE", outPath);
            Intent intent = getIntent();
            intent.putExtras(conData);
            setResult(RESULT_OK, intent);
            finish();*/
        }
    }

    /**
     * Generating name for ringtone
     *
     * @param title     title of file
     * @param extension extension for file
     * @return filename
     */

    private String makeRingtoneFilename(CharSequence title, String extension) {
        String subDir;
        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
        if (!externalRootDir.endsWith("/")) {
            externalRootDir += "/";
        }
        subDir = "media/audio/music/";
        String parentDir = externalRootDir + subDir;

        // Create the parent directory
        File parentDirFile = new File(parentDir);
        parentDirFile.mkdirs();

        // If we can't write to that special path, try just writing
        // directly to the sdcard
        if (!parentDirFile.isDirectory()) {
            parentDir = externalRootDir;
        }

        // Turn the title into a filename
        String filename = "";
        for (int i = 0; i < title.length(); i++) {
            if (Character.isLetterOrDigit(title.charAt(i))) {
                filename += title.charAt(i);
            }
        }

        // Try to make the filename unique
        String path = null;
        for (int i = 0; i < 100; i++) {
            String testPath;
            if (i > 0)
                testPath = parentDir + filename + i + extension;
            else
                testPath = parentDir + filename + extension;

            try {
                RandomAccessFile f = new RandomAccessFile(new File(testPath), "r");
                f.close();
            } catch (Exception e) {
                // Good, the file didn't exist
                path = testPath;
                break;
            }
        }

        return path;
    }

    /**
     * Load file from path
     *
     * @param mFilename file name
     */

    public void loadFromFile(String mFilename) {
        mFile = new File(mFilename);
//        SongMetadataReader metadataReader = new SongMetadataReader(this, mFilename);
        mLoadingLastUpdateTime = Utility.getCurrentTime();
        mLoadingKeepGoing = true;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("Loading ...");
        mProgressDialog.show();

        final SoundFile.ProgressListener listener =
                new SoundFile.ProgressListener() {
                    public boolean reportProgress(double fractionComplete) {

                        long now = Utility.getCurrentTime();
                        if (now - mLoadingLastUpdateTime > 100) {
                            mProgressDialog.setProgress(
                                    (int) (mProgressDialog.getMax() * fractionComplete));
                            mLoadingLastUpdateTime = now;
                        }
                        return mLoadingKeepGoing;
                    }
                };

        // Load the sound file in a background thread
        Thread mLoadSoundFileThread = new Thread() {
            public void run() {
                try {
                    mLoadedSoundFile = SoundFile.create(mFile.getAbsolutePath(), listener);
                    mRecordedSoundFile = mLoadedSoundFile;
                    if (mLoadedSoundFile == null) {
                        mProgressDialog.dismiss();
                        String name = mFile.getName().toLowerCase();
                        String[] components = name.split("\\.");
                        String err;
                        if (components.length < 2) {
                            err = "No Extension";
                        } else {
                            err = "Bad Extension";
                        }
                        final String finalErr = err;
                        Log.e(" >> ", "" + finalErr);
                        return;
                    }
                    mPlayer = new SamplePlayer(mLoadedSoundFile);
                } catch (final Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                    return;
                }
                mProgressDialog.dismiss();
                if (mLoadingKeepGoing) {
                    Runnable runnable = new Runnable() {
                        public void run() {
                            audioWaveform.setVisibility(View.INVISIBLE);
                            audioWaveform.setBackgroundColor(getResources().getColor(R.color.waveformUnselectedBackground));
                            audioWaveform.setIsDrawBorder(false);
                            finishOpeningSoundFile(mLoadedSoundFile, 0);
                        }
                    };
                    mHandler.post(runnable);
                }
            }
        };
        mLoadSoundFileThread.start();
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "Close" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                    0xCE)));
            // set item width
            openItem.setWidth(200);
            // set item title
            openItem.setTitle("스크립트 편집");
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(200);
            // set a icon
            deleteItem.setTitle("삭제");
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                System.out.println("첫번째 인자");
                Bundle bundle = data.getExtras();
                // Bundle bundle2 = data.getExtras();
                //Toast.makeText(getActivity(), "분할완료", Toast.LENGTH_SHORT).show();

                ArrayList<Edit_item> devide_com_script = (ArrayList<Edit_item>) bundle.getSerializable("devide_com_script");
                //ArrayList<Uri> devide_com_uri = (ArrayList<Uri>)bundle2.getSerializable("devide_com_uri");

                Log.e("AudioEditor", "Script" + Integer.toString(devide_com_script.size()));
                //Log.e("AudioEditor", "Uri" + Integer.toString(devide_com_uri.size()));
                //System.out.println(devide_com_uri.size());
                //System.out.println(sel_list.get(0).isDevide_complete());
                //System.out.println(sel_list.get(1).isDevide_complete());
                //listView.setAdapter(sel_adapter);

                for (int i = 0; i < devide_com_script.size(); i++) {
                    System.out.println("AudioEdior->file_fragment로 갈때" + devide_com_script.get(i));
                }


                Intent intent2 = new Intent();
                Bundle bundle_put = new Bundle();
                Bundle bundle_put2 = new Bundle();
                //Bundle bundle_put3 = new Bundle();

                bundle_put.putSerializable("devide_com_list", list);
                bundle_put2.putSerializable("devide_com_script", devide_com_script);
                //bundle_put3.putSerializable("devide_com_uri", devide_com_uri);

                intent2.putExtras(bundle_put);
                intent2.putExtras(bundle_put2);
                //intent2.putExtras(bundle_put3);
                intent2.putExtra("position", position);

                setResult(RESULT_OK, intent2);


            } else {   // RESULT_CANCEL
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }

//
        }

        finish();

    }


    @Override
    public void onBackPressed() {

        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof onBackPressedListener) {
                ((onBackPressedListener) fragment).onBackPressed();


                Log.e("RecordActivity", "onActivity로 전달중인데?????");
                String result = "이거 나와야해. RecordActivity로 부터 받은 값이야....";


                Intent intent_re = new Intent();
                intent_re.putExtra("position_re", result);


                setResult(RESULT_OK, intent_re);
                //Files_Fragment_Item.getCustom_loading().dismiss();
                finish();
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AudioEditor", "onDestroy()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AudioEditor", "onStop()");
    }
}








