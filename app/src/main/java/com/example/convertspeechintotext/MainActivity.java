package com.example.convertspeechintotext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText tec;
    private ImageView img;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //we will check for the permissions:
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            checkPermission();
        }

        tec = findViewById(R.id.txt1);
        img = findViewById(R.id.mic);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);//creating object of speech recognizer
        final Intent Sintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//ACTION_RECOGNIZE_SPEECH starts an activity that will prompt the user for speech and send it through a speech recogniz
        Sintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);//extra language model - Informs the recognizer which speech model to prefer when performing ACTION_RECOGNIZE_SPEECH.language model-free from-   Informs the recognizer which speech model to prefer when performing ACTION_RECOGNIZE_SPEECH.
        Sintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());//extra language- Informs the recognizer which speech model to prefer when performing ACTION_RECOGNIZE_SPEECH.

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
               tec.setText("Listening...............");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {//We get an ArrayList of String as a result and we use this ArrayList to update the UI (EditText here).
                img.setImageResource(R.drawable.mic);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                tec.setText(data.get(0));

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        img.setOnTouchListener(new View.OnTouchListener() {//lets set up the imageView. We will add a touchListener to the image view to know when the user has pressed the image.When the user taps the imageView the listener starts listening and the imageView source image is also changed to update the user that his voice is being listened to.
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    speechRecognizer.stopListening();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    img.setImageResource(R.drawable.mic);
                    speechRecognizer.startListening(Sintent);
                }
                return false;
            }
        });
    }

    private void checkPermission() {//If the permission is not granted then we will call the checkPermission method.
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length > 0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) Toast.makeText(this, "permisson granted",Toast.LENGTH_SHORT).show();
        }
    }
}