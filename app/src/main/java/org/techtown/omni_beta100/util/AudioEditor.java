package org.techtown.omni_beta100.util;

import android.app.ProgressDialog;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.apache.commons.lang3.RandomStringUtils;
import org.techtown.omni_beta100.AsyncTask.InternetThread;
import org.techtown.omni_beta100.MainActivity;
import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.activity.Devide_RecordActivity;
import org.techtown.omni_beta100.activity.ShareActivity;
import org.techtown.omni_beta100.adapter.EditAdapter;
import org.techtown.omni_beta100.adapter.MySelectAdapter;
import org.techtown.omni_beta100.view.MarkerView;
import org.techtown.omni_beta100.view.WaveformView;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class AudioEditor extends AppCompatActivity implements View.OnClickListener,
        MarkerView.MarkerListener,
        WaveformView.WaveformListener{ /* Audio trimmer*/

    private TextView txtAudioCancel;
    public static TextView txtStartPosition;
    public static TextView txtEndPosition;
    private MarkerView markerStart;
    private MarkerView markerEnd;
    private WaveformView audioWaveform;
    private TextView txtAudioRecordTimeUpdate;
   
    private TextView txtAudioDone;
    private TextView txtAudioPlay;
    private TextView txtAudioRecordUpdate;
    private TextView txtAudioCrop;
    private Button bt_stt_finish;

    private boolean isAudioRecording = false;
    private long mRecordingLastUpdateTime;
    private double mRecordingTime;
    private boolean mRecordingKeepGoing;
    private SoundFile mLoadedSoundFile;
    private SoundFile mRecordedSoundFile;
    private SamplePlayer mPlayer;

    private Handler mHandler;

    private boolean mTouchDragging;
    private float mTouchStart;
    private int mTouchInitialOffset;
    private int mTouchInitialStartPos;
    private int mTouchInitialEndPos;
    private float mDensity;
    private int mMarkerLeftInset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mMarkerBottomOffset;

    private int mTextLeftInset;
    private int mTextRightInset;
    private int mTextTopOffset;
    private int mTextBottomOffset;

    private int mOffset;
    private int mOffsetGoal;
    private int mFlingVelocity;
    private int mPlayEndMillSec;
    private int mWidth;
    private int mMaxPos;
    private int mStartPos;
    private int mEndPos;

    private boolean mStartVisible;
    private boolean mEndVisible;
    private int mLastDisplayedStartPos;
    private int mLastDisplayedEndPos;
    private boolean mIsPlaying = false;
    private boolean mKeyDown;
    private ProgressDialog mProgressDialog;
    private long mLoadingLastUpdateTime;
    private boolean mLoadingKeepGoing;
    private File mFile;
    int position;
    private ArrayList<MusicDto> list;
    public SoundFile soundfile;
    String path;

    public TextView file_title;// 추가



    public static SwipeMenuListView sttlist;
    private int sttMode;
    public String stt_filepath;
    public InputStream stream;
    public static EditAdapter editAdapter;
    public AudioEditor() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_editor);
        Log.v("m", "kk");
        mHandler = new Handler();



        txtAudioCancel = (TextView) findViewById(R.id.txtAudioCancel);
        txtStartPosition = (TextView) findViewById(R.id.txtStartPosition);
        txtEndPosition = (TextView) findViewById(R.id.txtEndPosition);
        markerStart = (MarkerView) findViewById(R.id.markerStart);
        markerEnd = (MarkerView) findViewById(R.id.markerEnd);
        audioWaveform = (WaveformView) findViewById(R.id.audioWaveform);
        txtAudioRecordTimeUpdate = (TextView) findViewById(R.id.txtAudioRecordTimeUpdate);
        
        txtAudioDone = (TextView) findViewById(R.id.txtAudioDone);
        txtAudioPlay = (TextView) findViewById(R.id.txtAudioPlay);
       
        sttlist = findViewById(R.id.stt_list);
        bt_stt_finish = (Button)findViewById(R.id.bt_stt_finish);
        file_title = (TextView)findViewById(R.id.file_title);//추가


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

        txtAudioCancel.setOnClickListener(this);
        
        txtAudioDone.setOnClickListener(this);
        txtAudioPlay.setOnClickListener(this);
        stream = getResources().openRawResource(R.raw.credential);
        sttMode = 1;

        final Intent intent = getIntent();

        editAdapter = new EditAdapter(0);



        if(intent.getSerializableExtra("playlist")!= null){//file_fragment에서 넘어올때
            position = intent.getIntExtra("position",-1);
            list = (ArrayList<MusicDto>) intent.getSerializableExtra("playlist");
            editAdapter.resetItem();

        }
        else if(intent.getSerializableExtra("pl_playlist")!=null) {
            position = intent.getIntExtra("pl_position", -1);//playlist_infile에서 넘어올때
            list = (ArrayList<MusicDto>) intent.getSerializableExtra("pl_playlist");
            editAdapter.resetItem();

        }else if(intent.getSerializableExtra("orin_music")!=null)// audioEditor->Devide_RecordActivity->다시 audioEditor
        {
            list = new ArrayList<>();
            position=0;
            list.add((MusicDto)intent.getSerializableExtra("orin_music"));

            editAdapter.setItemList((ArrayList<Edit_item>)intent.getSerializableExtra("devide_list0"));
        }

        sttlist.setAdapter(editAdapter);
        sttlist.setMenuCreator(creator);



        //devide_mp3_list = new ArrayList<Uri>();
        //devide_mp3_list.clear();

        Log.i("EditAdapter", "새로생성됨");

        Log.v("mDensity", mDensity+" ");
        final SoundFile.ProgressListener listener =
                new SoundFile.ProgressListener() {
                    public boolean reportProgress(double elapsedTime) {
                        long now = Utility.getCurrentTime();
                        if (now - mRecordingLastUpdateTime > 5) {
                            mRecordingTime = elapsedTime;
                            // Only UI thread can update Views such as TextViews.
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    int min = (int) (mRecordingTime / 60);
                                    float sec = (float) (mRecordingTime - 60 * min);
                                    //txtAudioRecordTime.setText(String.format(Locale.US, "%02d:%05.2f", min, sec));
                                }
                            });
                            mRecordingLastUpdateTime = now;
                        }
                        return mRecordingKeepGoing;
                    }
                };
        Log.v("hhhhhhhhhhhhh", "aaaaaaaaaaa");
        Uri musicURI = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+ list.get(position).getId());

        Cursor cursor = getContentResolver().query(musicURI, null, null, null, null );
        cursor.moveToNext();
        path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        Log.e("id", list.get(position).getId());
        Log.e("album id", list.get(position).getAlbumId());
        Log.e("title", list.get(position).getTitle());
        Log.e("artist", list.get(position).getArtist());

        Log.d("path : ",path);

        file_title.setText(list.get(position).getTitle());  //편집파일 제목
        //sttlist.setItemsCanFocus(true);

        // 모두완료 버튼 눌렀을 때 화면 전환
        bt_stt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //다시 분할 후 완료 눌렀을 때
                if (intent.getSerializableExtra("orin_music") != null) {

                    Intent intent_re = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("new_script", editAdapter.getmItems());
                    intent_re.putExtras(bundle);


                    //intent_re.putExtra("position_re", devide_position);


                    setResult(RESULT_OK, intent_re);

                    finish();


                    // 첫 분할후 완료 눌렀을 때
                } else {
                    if (editAdapter.getmItems().size() == 0) {
                        Toast.makeText(AudioEditor.this, "분할된 파일이 없습니다!", Toast.LENGTH_SHORT).show();
                    } else {
                        list.get(position).setDevide_complete(true);

                        Intent intent2 = new Intent(AudioEditor.this, Devide_RecordActivity.class);
                        intent2.putExtra("devide_list", editAdapter.getmItems());
                        intent2.putExtra("audio_title", list.get(position).getTitle());
                        intent2.putExtra("origin_music", list.get(position));
                        startActivityForResult(intent2, 200);
                    }

                }
            }
        });

        // listview 슬라이드 이벤트

        sttlist.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                sttlist.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {

                sttlist.smoothOpenMenu(position);
            }
        });

        sttlist.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch(index){
                    case 0 :
                        Log.d("", "delete..............");
                        // 요기 삭제버턴 클릭시 코딩...

                        fileDelete(editAdapter.getmItems().get(position).getOutpath());
                        editAdapter.removeItem(position);
                        //devide_mp3_list.remove(position);
                        editAdapter.setItemList(editAdapter.getmItems());
                        editAdapter.notifyDataSetChanged();



                        break;
                }
                return false;
            }
        });


        loadFromFile(path);
        mHandler.postDelayed(mTimerRunnable, 100);


        Log.v("zzzz", "ssg");
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {

        if (view == txtAudioCancel) {
            finish();
        } /*else if (view == txtAudioRecordUpdate) {
                rlAudioEdit.setVisibility(View.GONE);
                txtAudioUpload.setVisibility(View.GONE);
                llAudioCapture.setVisibility(View.VISIBLE);
                isAudioRecording = true;
                txtAudioRecord.setBackgroundResource(R.drawable.ic_stop_btn1);
                txtAudioRecordTime.setVisibility(View.VISIBLE);
                startRecording();
                mRecordingLastUpdateTime = Utility.getCurrentTime();
                mRecordingKeepGoing = true;
                txtAudioCrop.setBackgroundResource(R.drawable.ic_crop_btn);
                txtAudioDone.setVisibility(View.GONE);       // 가위표시
                txtAudioCrop.setVisibility(View.VISIBLE);    // 연필표시
                txtAudioPlay.setBackgroundResource(R.drawable.ic_play_btn);
                markerStart.setVisibility(View.INVISIBLE);
                markerEnd.setVisibility(View.INVISIBLE);
                txtStartPosition.setVisibility(View.VISIBLE);
                txtEndPosition.setVisibility(View.VISIBLE);

            }*/ else if (view == txtAudioPlay) {

            if (!mIsPlaying) {
                txtAudioPlay.setBackgroundResource(R.drawable.icon_devide_pause);
            } else {
                txtAudioPlay.setBackgroundResource(R.drawable.icon_devide_play);
            }

            onPlay(mStartPos);
        } else if (view == txtAudioDone) {

            double startTime = audioWaveform.pixelsToSeconds(mStartPos);
            double endTime = audioWaveform.pixelsToSeconds(mEndPos);
            double difference = endTime - startTime;

            if (difference <= 0) {
                Toast.makeText(AudioEditor.this, "Trim seconds should be greater than 0 seconds", Toast.LENGTH_SHORT).show();
            } else if (difference > 60) {
                Toast.makeText(AudioEditor.this, "Trim seconds should be less than 1 minute", Toast.LENGTH_SHORT).show();
            } else {
                if (mIsPlaying) {
                    handlePause();
                }
                saveRingtone(0);

                //txtAudioDone.setVisibility(View.GONE);
                //txtAudioReset.setVisibility(View.VISIBLE);
//                txtAudioCrop.setBackgroundResource(R.drawable.ic_crop_btn_fill);
                //txtAudioCrop.setVisibility(View.VISIBLE);
                Log.v("pp","kk");
                markerStart.setVisibility(View.INVISIBLE);
                markerEnd.setVisibility(View.INVISIBLE);
                txtStartPosition.setVisibility(View.INVISIBLE);
                txtEndPosition.setVisibility(View.INVISIBLE);
                Log.v("eeexx", "yyyyy");


                editAdapter.addItem("loading...","스크립트 생성중입니다...", "", "");
                editAdapter.notifyDataSetChanged();

                Log.v("stt_filepath", stt_filepath);



                   /* final InternetThread internetThread = new InternetThread(stt_filepath,stream,sttMode);
                    Log.v("f", "yyyyy");
                    internetThread.start();
                    Log.v("q", "yyyyy");
                    Handler handler = new Handler(){

                        @Override
                        public void handleMessage(Message msg)
                        {
                            Log.v("kkkkkk", "yyyyy");
                            editAdapter.addItem("test",internetThread.getSttText());
                            sttlist.setAdapter(editAdapter);
                            Log.v("nnnnnn", "mmmmm");
                        }
                    };
                    Message msg = handler.obtainMessage();
                   // Log.v("q", internetThread.getSttText());
                    handler.sendMessage(msg);
*/

            }

        } /*else if (view == txtAudioReset) {
                audioWaveform.setIsDrawBorder(true);
                mPlayer = new SamplePlayer(mRecordedSoundFile);
                finishOpeningSoundFile(mRecordedSoundFile, 1);
            }*/ else if (view == txtAudioCrop) {

//       
           
            txtAudioDone.setVisibility(View.VISIBLE);
            audioWaveform.setIsDrawBorder(true);
            audioWaveform.setBackgroundColor(getResources().getColor(R.color.colorWaveformBg));
            markerStart.setVisibility(View.VISIBLE);
            markerEnd.setVisibility(View.VISIBLE);
            txtStartPosition.setVisibility(View.VISIBLE);
            txtEndPosition.setVisibility(View.VISIBLE);

        }  else {
            
        }
    }
    // }

    /**
     * Start recording
     */
    /**
     * After recording finish do necessary steps
     * @param mSoundFile sound file
     * @param isReset isReset
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
        Log.v("mStartPos", mStartPos+" ");
        Log.v("mEndPos", mEndPos+" ");
        if (isReset == 1) {
            mStartPos = audioWaveform.secondsToPixels(0);
            mEndPos = audioWaveform.secondsToPixels(audioWaveform.pixelsToSeconds(mMaxPos));
        }
        Log.v("mStartPos", "b"+mStartPos);
        Log.v("mEndPos", "b"+mEndPos);
        if (audioWaveform != null && audioWaveform.isInitialized()) {
            double seconds = audioWaveform.pixelsToSeconds(mMaxPos);
            int i = Integer.parseInt(String.valueOf(Math.round(seconds)));
            
            int hour = i/3600;
            int min = i%3600/60;
            int sec = i%3600%60;

            if(hour==0) {
                txtAudioRecordTimeUpdate.setText(((min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec))) ;
            }else{
                txtAudioRecordTimeUpdate.setText(((hour<10?"0" + hour : hour ) + ":" + (min<10?"0" + min : min ) + ":" + (sec  <10 ? "0" + sec : sec)))  ;
            }
            
            Log.v("seconds", seconds +" ");
            Log.v("min", min +" ");
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
        audioWaveform.setIsDrawBorder(true);
        // audioWaveform.setBackgroundColor(getResources().getColor(R.color.colorWaveformBg));
        markerStart.setVisibility(View.VISIBLE);
        markerEnd.setVisibility(View.VISIBLE);
        txtStartPosition.setVisibility(View.VISIBLE);
        txtEndPosition.setVisibility(View.VISIBLE);
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

    private String myTimeformat(String time){

        int real_time;
        int h, m, s;
        String sub;

        switch (time.length()){



            case 5 :
                sub = time.substring(0,2);
                real_time = Integer.parseInt(sub);

                h =0;
                m = real_time%3600/60;
                s = real_time%3600%60;

                return ((m<10?"0" + m : m ) + ":" + (s  <10 ? "0" + s : s));

            case 6 :
                sub = time.substring(0,3);
                real_time = Integer.parseInt(sub);

                h =0;
                m = real_time%3600/60;
                s = real_time%3600%60;

                return ((m<10?"0" + m : m ) + ":" + (s  <10 ? "0" + s : s));

            case 7 :
                sub = time.substring(0,4);
                real_time = Integer.parseInt(sub);
                h = real_time/3600;
                m = real_time%3600/60;
                s = real_time%3600%60;
                if(h==0) {
                    return ((m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s));
                }else{
                    return  ((h<10?"0" + h : h ) + ":" + (m<10?"0" + m : m ) + ":" + (s  <10 ? "0" + s : s));
                }

            default :
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
     * @param finish flag for finish
     */

    private void saveRingtone(final int finish) {
        stt_filepath = null;
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
        stt_filepath = outPath;
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
     * @param title title
     * @param outPath output path
     * @param duration duration of file
     * @param finish flag for finish
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
        editAdapter.modifyUri(newUri.toString());
        editAdapter.modifyFilePath(outPath);
        //devide_mp3_list.add(0, newUri);
        //editAdapter.add
        Log.e("AudioEditor", "넘겨주는 리스트에 포함이 된다.");

        if (finish == 0) {
            stream = getResources().openRawResource(R.raw.credential);
            final InternetThread internetThread = new InternetThread(stt_filepath,stream,sttMode);
            Log.v("f", "yyyyy");
            internetThread.start();
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
     * @param title title of file
     * @param extension extension for file
     * @return filename
     */

    private String makeRingtoneFilename(CharSequence title, String extension) {
        String subDir;
        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
        if (!externalRootDir.endsWith("/")) {
            externalRootDir += "/";
        }
        subDir = "media/audio/omni_music/";
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
        for (int i = 0; i < 1000; i++) {
            String testPath;
            if (i > 0)
                testPath = parentDir + filename + getUniqueId()+ extension;
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



       /* FileOutputStream fos;

        String subDir;

        File strFolderPath = getExternalFilesDir(Environment.DIRECTORY_MUSIC + File.separator + "omni_devide_audio");
        File folder = new File(String.valueOf(strFolderPath));
        if(!folder.exists()) {  // 해당 폴더 없으면 만들어라
            folder.mkdirs();
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
        for (int i = 0; i < 1000; i++) {
            String testPath;
            if (i > 0)

                testPath = strFolderPath + filename + i + extension;
            else
                testPath = strFolderPath + filename + extension;

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
    }*/

    /**
     * Load file from path
     * @param mFilename file name
     */

    private void loadFromFile(String mFilename) {
        mFile = new File(mFilename);
//        SongMetadataReader metadataReader = new SongMetadataReader(this, mFilename);
        mLoadingLastUpdateTime = Utility.getCurrentTime();
        mLoadingKeepGoing = true;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("Loading ...");
        mProgressDialog.setCanceledOnTouchOutside(false);
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


                    if(mProgressDialog!=null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                    e.printStackTrace();
                    Log.e("AudioEditor", e.getMessage());
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
            /*// create "Close" item
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
            menu.addMenuItem(openItem);*/

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

    public boolean fileDelete(String filePath){
        try{
            File del_file = new File(filePath);

            if(del_file.exists()){
                del_file.delete();
                System.out.println("성공");

                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("실패");
        }

        return false;

    }

    public static String getUniqueId(){
        String uniqueId = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Calendar dateTime = Calendar.getInstance();
        uniqueId = sdf.format(dateTime.getTime());

        uniqueId = uniqueId + "_" + RandomStringUtils.randomAlphanumeric(8);

        return uniqueId;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 200 ) {
            if (resultCode == RESULT_OK) {
                System.out.println("첫번째 인자");
                Bundle bundle = data.getExtras();
                ArrayList<Edit_item> devide_com_script = (ArrayList<Edit_item>)bundle.getSerializable("devide_com_script");

                for(int i=0;i<devide_com_script.size();i++)
                {
                    System.out.println("AudioEdior->file_fragment로 갈때" + devide_com_script.get(i));
                }


                Intent intent3 = new Intent();
                Bundle bundle_put = new Bundle();
                Bundle bundle_put2 = new Bundle();

                bundle_put.putSerializable("devide_com_list", list);
                bundle_put2.putSerializable("devide_com_script", devide_com_script);

                intent3.putExtras(bundle_put);
                intent3.putExtras(bundle_put2);
                intent3.putExtra("position", position);
                setResult(RESULT_OK, intent3);


            } else {   // RESULT_CANCEL
                Toast.makeText(this, "편집을 완료하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Log.i("AudioEditor", "종료???");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog!=null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        Log.i("AudioEditor", "onDestroy()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mProgressDialog!=null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        Log.i("AudioEditor", "onStop()");
    }
}
class ObjectUtilss {
    public static boolean isEmpty(Object s) {
        if (s == null) { return true; }
        if ((s instanceof String) && (((String)s).trim().length() == 0)) { return true; }
        if (s instanceof Map) { return ((Map<?, ?>)s).isEmpty(); }
        if (s instanceof List) { return ((List<?>)s).isEmpty(); }
        if (s instanceof Object[]) { return (((Object[])s).length == 0); } return false; }


}
