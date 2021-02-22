package org.techtown.omni_beta100.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeRequest;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//import android.graphics.Path;


public class Speech extends Service {
    public Context context;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void syncRecognizeFile(String fileName, TextView textView, InputStream stream) throws Exception {
        Log.v("aaa", "gggg");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
        SpeechSettings speechSettings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        try (SpeechClient speech = SpeechClient.create(speechSettings)) {
            Log.v("oo", "a");
            Path path = (Path) Paths.get(fileName);
            byte[] data = Files.readAllBytes((Path) path);
            ByteString audioBytes = ByteString.copyFrom(data);
            Log.v("d", "kgg");
            // Configure request with local raw PCM audio
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(AudioEncoding.LINEAR16)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(16000)
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Use blocking call to get audio transcript
            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                //System.out.printf("Transcription: %s%n", alternative.getTranscript());
                textView.setText(alternative.getTranscript());
            }
            throw new Exception();
        } catch (Exception e) {
            Log.e("stt", e.getMessage());
        }
    }



    public void sampleRecognize(String localFilePath, TextView textView, InputStream stream) throws IOException {
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
            AudioEncoding encoding = AudioEncoding.FLAC;
            RecognitionConfig config =
                    RecognitionConfig.newBuilder().setAudioChannelCount(2)
                            .setLanguageCode(languageCode)
                            .setSampleRateHertz(sampleRateHertz)
                            .setEncoding(encoding)
                            .build();

            Log.v("d","ad");
            Path path = Paths.get(localFilePath);
           // Path path = (Path) Paths.get(localFilePath);
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
                textView.setText(alternative.getTranscript());
            }
        } catch (Exception exception) {
          //  System.err.println("Failed to create the client due to: " + exception);
            Log.e("stt", exception.getMessage());
        }
    }


}
