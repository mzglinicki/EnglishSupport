package com.project.mzglinicki.yourowndictionary;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;

import java.io.File;
import java.util.Locale;

/**
 * Created by marcin on 07.06.16.
 */
public class MediaManager {

    private static MediaManager mediaManager = null;
    private TextToSpeech nativeSpeaker;
    private TextToSpeech englishSpeaker;

    private MediaManager(final Context appContext) {
        initPolishSpeaker(appContext);
        initEnglishSpeaker(appContext);
    }

    public static MediaManager getInstance(final Context context) {
        if (mediaManager == null) {
            mediaManager = new MediaManager(context);
        }
        return mediaManager;
    }

    public void tellText(final Context context, final String textToRead, final TextToSpeech toSpeech) {

        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + textToRead + Constants.TTS_FILE_FORMAT;
        final File file = new File(path);

        if (file.exists()) {
            final MediaPlayer player = MediaPlayer.create(context, Uri.parse(path));
            player.start();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toSpeech.speak(textToRead, TextToSpeech.QUEUE_ADD, null, null);
                toSpeech.synthesizeToFile(textToRead, null, file, Constants.UTTERANCE_ID);
            }
        }
    }

    private void initPolishSpeaker(final Context context) {
        nativeSpeaker = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    nativeSpeaker.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    private void initEnglishSpeaker(final Context context) {
        englishSpeaker = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    englishSpeaker.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    public TextToSpeech getPolishSpeaker() {
        return nativeSpeaker;
    }

    public TextToSpeech getEnglishSpeaker() {
        return englishSpeaker;
    }

}