package com.example.del_m.drivesocial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by del_m on 20/01/2018.
 */

public class AssistentActivity extends Activity implements TextToSpeech.OnInitListener{

    protected static final int USER_REQUEST = 0;
    protected static final int CONFIRM_USER = 1;
    protected static final int CONFIRM_SEND = 3;
    protected static final int MESSAGE_REQUEST = 2;

    Button btnLogoff;
    ImageView btnWhatsapp;
    ImageView btnFacebook;
    ImageView btnSpeech;
    Typewriter falaAssistent;
    private TextToSpeech audioAssistent;
    String mensagem;
    String confirma;
    Handler shandler;
    String wmensagem;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistent_activity);

        audioAssistent = new TextToSpeech(this, this);
        shandler = new Handler();

        btnWhatsapp = findViewById(R.id.btnWhatsapp);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnLogoff = findViewById(R.id.btnLogoff);
        btnSpeech =  findViewById(R.id.btnFalar);
        falaAssistent = findViewById(R.id.falaAssistent);
        falaAssistent.animateText(getString(R.string.assistent_presentation));
        confirma = getString(R.string.confirma_envio);
        mensagem = getString(R.string.nulo);

    }


    public void onInit(int status){
        if (status == TextToSpeech.SUCCESS){

            String audio = getString(R.string.assistent_presentation);
            audioAssistent.speak(audio,TextToSpeech.QUEUE_FLUSH,null );

        }

    }

    public void whatsappsend(View view) {
        String audio = getString(R.string.whatsapp_send);
        falaAssistent.animateText(getString(R.string.nulo));
        audioAssistent.speak(audio, TextToSpeech.QUEUE_FLUSH, null);
        while (audioAssistent.isSpeaking()) {

        }

        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        startActivityForResult(intent_wspeech, USER_REQUEST);


    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data){

            super.onActivityResult(requestCode, resultCode, data);
            ArrayList<String> texto = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mensagem = texto.get(0);




            if (requestCode == USER_REQUEST){

                wmensagem = mensagem;

                audioAssistent.speak(("enviar mensagem para"+mensagem), TextToSpeech.QUEUE_ADD, null);
                audioAssistent.speak((confirma), TextToSpeech.QUEUE_ADD, null);

                while (audioAssistent.isSpeaking()) {

                }

                confirmUser();

            }

            if (requestCode == CONFIRM_USER){

                if (mensagem.equals("confirmar") || mensagem.equals("confirma")){

                    audioAssistent.speak(("Por favor, dite a mensagem que deve ser enviada."), TextToSpeech.QUEUE_ADD, null);

                    while (audioAssistent.isSpeaking()) {

                    }

                    messageRequest();

                }else{

                    audioAssistent.speak("Cancelando o envio.",TextToSpeech.QUEUE_ADD, null);

                }


            }

            if (requestCode == MESSAGE_REQUEST){

                audioAssistent.speak((mensagem), TextToSpeech.QUEUE_ADD, null);
                audioAssistent.speak((confirma), TextToSpeech.QUEUE_ADD, null);

                while (audioAssistent.isSpeaking()) {

                }

                confirmSend();


            }


            if (requestCode == CONFIRM_SEND){


                if (mensagem.equals("confirmar")|| mensagem.equals("confirma") || mensagem.equals("manda logo essa porra")) {

                    if (mensagem.equals("manda logo essa porra")){

                        audioAssistent.speak("Mensagem enviada. Seu pau no cu", TextToSpeech.QUEUE_ADD, null);

                    }else {

                        audioAssistent.speak("Mensagem enviada.", TextToSpeech.QUEUE_ADD, null);

                    }


                }else{



                        audioAssistent.speak("Cancelando o envio.",TextToSpeech.QUEUE_ADD, null);


                }

            }

    }


    public void confirmUser(){

        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
        startActivityForResult(intent_wspeech, CONFIRM_USER);


    }
    public void confirmSend(){

        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
        startActivityForResult(intent_wspeech, CONFIRM_SEND);


    }

    public void messageRequest(){

        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
        startActivityForResult(intent_wspeech, MESSAGE_REQUEST);


    }



    public void logoff (View view){

        finish();
    }

}
