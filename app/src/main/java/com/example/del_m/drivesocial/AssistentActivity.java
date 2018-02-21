package com.example.del_m.drivesocial;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by del_m on 20/01/2018.
 */

public class AssistentActivity extends Activity implements TextToSpeech.OnInitListener {

    protected static final int USER_REQUEST = 0;
    protected static final int CONFIRM_USER = 1;
    protected static final int CONFIRM_SEND = 3;
    protected static final int MESSAGE_REQUEST = 2;

    Weather CLIMA;


    protected static final int CLIMA_PERMISSION = 99;
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 100;

    RelativeLayout bg;

    TextView txtTemp;
    Button btnLogoff;
    ImageView btnWhatsapp;
    ImageView btnFacebook;
    ImageView btnSpeech;
    TextClock relogio;
    Typewriter falaAssistent;
    private TextToSpeech audioAssistent;
    String mensagem;
    String confirma;
    Handler shandler;
    String wmensagem = "";
    String contactname = "";
    String number;
    ArrayList<String> numeros_home = new ArrayList<>();
    String numero_mobile;
    ArrayList<String> numeros_work = new ArrayList<>();
    int CONTATO = 0;
    int contador_home = 0;
    int contador_mobile = 0;
    int contador_work = 0;
    Context context;

    Typeface font;

    GoogleApiClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistent_activity);

        audioAssistent = new TextToSpeech(this, this);
        shandler = new Handler();

        btnWhatsapp = findViewById(R.id.btnWhatsapp);
        //btnFacebook = findViewById(R.id.btnFacebook);
        btnLogoff = findViewById(R.id.btnLogoff);
        //btnSpeech = findViewById(R.id.btnFalar);


        txtTemp = findViewById(R.id.temperatureText);
        relogio = findViewById(R.id.textClock);
        font = Typeface.createFromAsset(getAssets(),"font/robotothin.ttf");
        relogio.setTypeface(font);
        txtTemp.setTypeface(font);



        //falaAssistent = findViewById(R.id.falaAssistent);
        //falaAssistent.animateText(getString(R.string.assistent_presentation));
        confirma = getString(R.string.confirma_envio);
        mensagem = getString(R.string.nulo);
        context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
        }
       permissaoweather();

        GoogleApiClient client = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build();
        client.connect();

    }


    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            String audio = getString(R.string.assistent_presentation);
            audioAssistent.speak(audio, TextToSpeech.QUEUE_FLUSH, null);
            clima();


        }

    }

    public void whatsappsend(View view) {
        String audio = getString(R.string.whatsapp_send);
        number = "0";
        falaAssistent.animateText(getString(R.string.nulo));
        audioAssistent.speak(audio, TextToSpeech.QUEUE_FLUSH, null);
        while (audioAssistent.isSpeaking()) {

        }

        Intent intent_wspeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pt-BR");
        intent_wspeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        startActivityForResult(intent_wspeech, USER_REQUEST);


    }

    public void permissaoweather() {

        if (ContextCompat.checkSelfPermission(
                AssistentActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    AssistentActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CLIMA_PERMISSION

            );
            return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == USER_REQUEST) {

            ArrayList<String> texto = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            contactname = texto.get(0);


            audioAssistent.speak(("enviar mensagem para. " + contactname), TextToSpeech.QUEUE_ADD, null);
            audioAssistent.speak((confirma), TextToSpeech.QUEUE_ADD, null);
            //falaAssistent.setText(contactname);
            while (audioAssistent.isSpeaking()) {

            }

            confirmUser();

        }

        if (requestCode == CONFIRM_USER) {

            ArrayList<String> texto = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mensagem = texto.get(0);

            phoneNumQuery();
            // if(!number.equals("0")){
            if (mensagem.equals("confirmar") || mensagem.equals("confirma")) {


                audioAssistent.speak(("Qual a mensagem que deve ser enviada?"), TextToSpeech.QUEUE_ADD, null);
                //falaAssistent.setText(numero_mobile);
                while (audioAssistent.isSpeaking()) {

                }
                messageRequest();
            } else {

                audioAssistent.speak("Cancelando o envio.", TextToSpeech.QUEUE_ADD, null);
                contactname = "";
            }

            //}else{

            //  audioAssistent.speak("Cancelando o envio.",TextToSpeech.QUEUE_ADD, null);
            //contactname = "";

        }


        //}

        if (requestCode == MESSAGE_REQUEST) {

            ArrayList<String> texto = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mensagem = texto.get(0);

            wmensagem = mensagem;

            audioAssistent.speak((mensagem), TextToSpeech.QUEUE_ADD, null);
            audioAssistent.speak((confirma), TextToSpeech.QUEUE_ADD, null);

            while (audioAssistent.isSpeaking()) {

            }

            confirmSend();


        }


        if (requestCode == CONFIRM_SEND) {

            ArrayList<String> texto = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mensagem = texto.get(0);


            if (mensagem.equals("confirmar") || mensagem.equals("confirma") || mensagem.equals("manda logo essa porra")) {


                //EDITAR COMANDOS PARA CRIAR A URL

                if (mensagem.equals("manda logo essa porra")) {


                    audioAssistent.speak("Abrindo o whatsapp. Otário.", TextToSpeech.QUEUE_ADD, null);

                } else {

                    audioAssistent.speak("Abrindo o whatsapp.", TextToSpeech.QUEUE_ADD, null);

                }
                sendWA();

            } else {


                audioAssistent.speak("Cancelando o envio.", TextToSpeech.QUEUE_ADD, null);
                wmensagem = "";

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

    public void phoneNumQuery(){


        //
        //  Find contact based on name.
        //
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                "DISPLAY_NAME = '" + contactname + "' COLlATE NOCASE", null, null);
       if (cursor.moveToFirst()) {

            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //
            //  Get all phone numbers.
            //
            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {
                phones.moveToFirst();
                number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                numero_mobile = number;

                if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {

                        numero_mobile = number.replace("+","");
                        numero_mobile.replace("55", "");
                        numero_mobile = "55"+numero_mobile;
                        //falaAssistent.setText(numero_mobile);
                        phones.moveToLast();
                }

            }
            phones.close();
        }
        cursor.close();

    }




    public void clima(){


        if (ContextCompat.checkSelfPermission(
                AssistentActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    AssistentActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CLIMA_PERMISSION

            );
            return;
        }

        Awareness.getSnapshotClient(this).getWeather()
                .addOnSuccessListener(new OnSuccessListener<WeatherResponse>(){
                public void onSuccess(WeatherResponse weatherResponse) {


                    Weather weather = weatherResponse.getWeather();
                    //CLIMA = weather;
                    final int[] conditions = weather.getConditions();
                    switch (conditions[0]){

                        case 1:
                            audioAssistent.speak("Está um lindo dia hoje.", TextToSpeech.QUEUE_ADD, null);
                            break;
                        case 2:
                            audioAssistent.speak("Está nublado, separe o guarda-chuva por precaução.", TextToSpeech.QUEUE_ADD, null);
                            bg = findViewById(R.id.corpo);
                            bg.setBackground(ContextCompat.getDrawable(context, R.drawable.day_cloudy_bg));
                            break;
                        case 3:
                            audioAssistent.speak("Cuidado com as neblinas.", TextToSpeech.QUEUE_ADD, null);
                            break;
                        case 4:
                            audioAssistent.speak("Está nublado, leve o guarda-chuva por precaução.", TextToSpeech.QUEUE_ADD, null);
                            break;
                        case 5:
                            audioAssistent.speak("Nossa, que frio, não esqueça sua blusa.", TextToSpeech.QUEUE_ADD, null);
                            break;
                        case 6:
                            audioAssistent.speak("Está chovendo, não esqueça o gaurda-chuva.", TextToSpeech.QUEUE_ADD, null);
                            break;
                        case 7:
                            audioAssistent.speak("Neve lá fora, não esqueça de se agasalhar.", TextToSpeech.QUEUE_ADD, null);
                            break;
                        case 8:
                            audioAssistent.speak("Vem tempestade aí, permaneça agasalhado, com guarda-chuva e preste muita atenção.", TextToSpeech.QUEUE_ADD, null);
                            break;
                        case 9:
                            audioAssistent.speak("Está ventando, separe o casaco.", TextToSpeech.QUEUE_ADD, null);
                            break;
                        default:
                            audioAssistent.speak("O clima não é monitorado em sua localização ou não foi possível obter a informação, me desculpe.", TextToSpeech.QUEUE_ADD, null);
                            break;


                    }
                    final float temperature = weather.getFeelsLikeTemperature(2);
                    audioAssistent.speak("Faz  "+Math.round(temperature)+" graus em sua região.", TextToSpeech.QUEUE_ADD, null);
                    txtTemp.setText(Math.round(temperature)+"º");
                }

                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                            Log.e(TAG, "Could not get weather.");

                    }
                });



    }




    public void sendWA(){

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+numero_mobile+"&text="+wmensagem.replace(" ","%20"))));

    }

    public void logoff (View view){

        finish();
    }

}
