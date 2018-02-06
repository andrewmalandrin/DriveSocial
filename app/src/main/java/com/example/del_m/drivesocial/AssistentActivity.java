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

    protected static final int RESULT_SPEECH = 0;
    protected static final int CONFIRMA_ENVIO = 1;
    protected static final int ESCOLHER_DESTINO = 2;

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
        startActivityForResult(intent_wspeech, RESULT_SPEECH);



       /* Thread messageRequester = new Thread(new Runnable() {
            @Override
            public void run() {



                    shandler.post(new Runnable() {
                        @Override
                        public void run() {


                            String audio = getString(R.string.whatsapp_send);
                            falaAssistent.animateText(getString(R.string.nulo));
                            audioAssistent.speak(audio, TextToSpeech.QUEUE_FLUSH, null);




                        }
                    });





            }
        });

        final Thread getAudio = new Thread(new Runnable() {
            @Override
            public void run() {
                shandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
                        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                        startActivityForResult(intent_wspeech, RESULT_SPEECH);

                    }
                });
            }

        });

        final Thread confirmRequester = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    getAudio.join();

                }catch (InterruptedException e){

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast interrupt = Toast.makeText(context, "Recepção de áudio interrompida.", duration );
                    interrupt.show();

                }
                shandler.post(new Runnable() {
                    @Override
                    public void run() {

                        audioAssistent.speak((mensagem), TextToSpeech.QUEUE_ADD, null);
                        audioAssistent.speak((confirma), TextToSpeech.QUEUE_ADD, null);

                    }
                });


            }
        });

        Thread getConfirm = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    confirmRequester.join();

                }catch (InterruptedException e){

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast interrupt = Toast.makeText(context, "Fala da assistente interrompida .", duration );
                    interrupt.show();

                }


                shandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
                        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                        startActivityForResult(intent_wspeech, RESULT_SPEECH);

                    }
                });
            }

        });

        messageRequester.start();
        getAudio.start();
        confirmRequester.start();
        getConfirm.start();
        */

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data){

            super.onActivityResult(requestCode, resultCode, data);
            ArrayList<String> texto = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mensagem = texto.get(0);


            if (requestCode == ESCOLHER_DESTINO){



            }

            if (requestCode == RESULT_SPEECH){

                wmensagem = mensagem;

                audioAssistent.speak((mensagem), TextToSpeech.QUEUE_ADD, null);
                audioAssistent.speak((confirma), TextToSpeech.QUEUE_ADD, null);

                while (audioAssistent.isSpeaking()) {

                }

                confirmaEnvio();

            }

            if (requestCode == CONFIRMA_ENVIO){


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


    public void confirmaEnvio(){

        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
        startActivityForResult(intent_wspeech, CONFIRMA_ENVIO);


    }

    public void escolherDestino(){

        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
        startActivityForResult(intent_wspeech, ESCOLHER_DESTINO);


    }



    public void logoff (View view){

        finish();
    }

}
