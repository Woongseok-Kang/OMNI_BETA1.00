package org.techtown.omni_beta100.AsyncTask;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1p1beta1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1p1beta1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognizeRequest;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.SpeechSettings;
import com.google.protobuf.ByteString;

import org.techtown.omni_beta100.adapter.EditAdapter;
import org.techtown.omni_beta100.util.AudioEditor;
import org.techtown.omni_beta100.util.Speech;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class InternetThread extends Thread{

    public Speech speech;
    public String gcsUri;
    public TextView textView;
    public InputStream stream;
    public String result_string;
    public String result_total = " ";
    public int sttMode;
    //public Handler handler1;


    public InternetThread(String gcsUri, TextView textView, InputStream stream, int sttMode)
    {
        this.gcsUri =gcsUri;
        this.textView = textView;
        this.stream = stream;
        this.sttMode = sttMode;
        //speech = new Speech();

    }
    public InternetThread(String gcsUri, InputStream stream, int sttMode)
    {
        this.gcsUri =gcsUri;
        this.stream = stream;
        this.sttMode = sttMode;
        // this.handler1 = handler1;
        //speech = new Speech();

    }


    public void asyncRecognizeGcs() throws IOException {
        Log.v("bbbbbboo", "gggg");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
        SpeechSettings speechSettings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        try (SpeechClient speech = SpeechClient.create(speechSettings)) {

            Log.v("d", "kgg");
            // Configure request with local raw PCM audio
            RecognitionConfig config =
                    RecognitionConfig.newBuilder().setAudioChannelCount(2)
                            .setEncoding(RecognitionConfig.AudioEncoding.MP3)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(48000)
                            .build();
            Log.v("qqqq", "yqyqqy");
            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
            Log.v("WQA", "egggsg");
            // Use non-blocking call for getting file transcription
            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                    speech.longRunningRecognizeAsync(config, audio);
            Log.v("Q", "LFL");
            while (!response.isDone()) {
                //System.out.println("Waiting for response...");
                Log.d("waiting ", "Waiting for response...");
                Thread.sleep(10000);
            }
            List<SpeechRecognitionResult> results = response.get().getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                //System.out.printf("Transcription: %s%n", alternative.getTranscript());

                textView.setText(alternative.getTranscript());
            }
            throw new Exception();
        } catch (Exception e) {
            String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
            Log.e("sdcard-err2:",err);
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg)
        {
            Log.v("kkkkkk", "yyyyy");
            textView.setText(result_total);
            Log.v("nnnnnn", "mmmmm");
        }


    };

    Handler handler1 = new Handler(){

        @Override
        public void handleMessage(Message msg)
        {
            Log.v("nnnnnn", "mmmmm");
            // Log.v("이거들어옴", internetThread.getSttText());
            AudioEditor.editAdapter.modifyItem(result_total);
            AudioEditor.editAdapter.modifyTime(AudioEditor.txtStartPosition.getText().toString() + " - " + AudioEditor.txtEndPosition.getText().toString());
            AudioEditor.editAdapter.setItemList(AudioEditor.editAdapter.getmItems());
            AudioEditor.editAdapter.notifyDataSetChanged();
        }

    };

    public String getSttText()
    {
        return result_total;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        try {

            //asyncRecognizeGcs();
            if(sttMode == 0) {
                Log.v("bbbbbboo", "gggg");
                GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
                FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
                SpeechSettings speechSettings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
                try (SpeechClient speech = SpeechClient.create(speechSettings)) {

                    Log.v("d", "kgg");
                    // Configure request with local raw PCM audio
                    RecognitionConfig config =
                            RecognitionConfig.newBuilder().setAudioChannelCount(2)
                                    .setEncoding(RecognitionConfig.AudioEncoding.MP3)
                                    .setEnableAutomaticPunctuation(true)
                                    .setLanguageCode("en-US")
                                    .setSampleRateHertz(48000)
                                    .build();
                    Log.v("qqqq", "yqyqqy");
                    RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
                    Log.v("WQA", "egggsg");
                    // Use non-blocking call for getting file transcription
                    OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                            speech.longRunningRecognizeAsync(config, audio);
                    Log.v("Q", "LFL");
                    while (!response.isDone()) {
                        //System.out.println("Waiting for response...");
                        Log.d("waiting ", "Waiting for response...");
                        Thread.sleep(10000);
                    }
                    // while(response.isDone()) {
                    List<SpeechRecognitionResult> results = response.get().getResultsList();

                    for (SpeechRecognitionResult result : results) {
                        // There can be several alternative transcripts for a given chunk of speech. Just use the
                        // first (most likely) one here.

                        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                        //System.out.printf("Transcription: %s%n", alternative.getTranscript());
                        result_string = alternative.getTranscript();
                        result_total = result_total + result_string;
                    }
                    Message msg = handler.obtainMessage();
                    Log.v("q", result_string);
                    handler.sendMessage(msg);
                    //Thread.sleep(10000);
                    // }
                    throw new Exception();
                } catch (Exception e) {
                    //String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
                    //Log.e("sdcard-err2:",err);
                    Log.e("a:", e.getMessage() + "error!!"); // 여기에러인데????????

                    //Thread.currentThread().interrupt();
                }
            }
            else if(sttMode == 1)
            {
                Log.v("ll","kk");
                //  String p = "C:\\Users\\moongon\\Desktop\\OMNI1.0-master\\app\\src\\main\\res\\raw\\credential.json";
                Log.v("pp","kk");
                //  File f = new File(p);
                Log.v("g","b");
                //  FileInputStream credentialsStream = new FileInputStream("credential.json");
                Log.v("j","zz");
                GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
                FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
                SpeechSettings speechSettings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
                Log.v("h","c");
                try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {
                    Log.v("er","tt");
                    // The language of the supplied audio
                    String languageCode = "en-US";

                    // Sample rate in Hertz of the audio data sent
                    //int sampleRateHertz = 16000;
                    int sampleRateHertz = 48000;
                    // Encoding of audio data sent. This sample sets this explicitly.
                    // This field is optional for FLAC and WAV audio formats.
                    RecognitionConfig.AudioEncoding encoding = RecognitionConfig.AudioEncoding.MP3;
                    RecognitionConfig config =
                            RecognitionConfig.newBuilder().setAudioChannelCount(2)
                                    .setLanguageCode(languageCode)
                                    .setEnableAutomaticPunctuation(true)
                                    .setSampleRateHertz(sampleRateHertz)
                                    .setEncoding(encoding)
                                    .build();

                    Log.v("d","ad");
                    Path path = Paths.get(gcsUri);
                    //  Path path = (Path) Paths.get(gcsUri);
                    Log.v("g","gg");
                    byte[] data = Files.readAllBytes(path);
                    Log.v("xx","yy");
                    ByteString content = ByteString.copyFrom(data);
                    Log.v("ll","ddd");
                    RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(content).build();
                    Log.v("ttt","hh");

                    RecognizeRequest request =
                            RecognizeRequest.newBuilder().setConfig(config).setAudio(audio).build();
                    Log.v("e","kkk");
                    //RecognizeResponse response = speechClient.recognize(config,audio); //
                    RecognizeResponse response = speechClient.recognize(request);
                    Log.v("qqqqq","f");
                    for (SpeechRecognitionResult result : response.getResultsList()) {
                        // First alternative is the most probable result
                        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                        //System.out.printf("Transcript: %s\n", alternative.getTranscript());
                        //textView.setText(alternative.getTranscript());
                        result_string = alternative.getTranscript();
                        result_total = result_total + result_string;
                        Log.v("result_total",result_total);
                    }
                    Message msg = handler1.obtainMessage();
                    //  Log.v("q", result_string);
                    handler1.sendMessage(msg);
                } catch (Exception exception) {
                    //  System.err.println("Failed to create the client due to: " + exception);
                    Log.e("stt", exception.getMessage());
                }
            }
        }
        catch(Exception exception){
            exception.printStackTrace();
        }

    }

}