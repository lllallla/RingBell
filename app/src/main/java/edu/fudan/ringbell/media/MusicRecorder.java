package edu.fudan.ringbell.media;

import android.annotation.TargetApi;
import android.media.MediaRecorder;
import android.os.Build;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by YI on 2017/12/27.
 */

public class MusicRecorder {
    private static MusicRecorder instance;

    private MusicRecorder() {
    }

    public static synchronized MusicRecorder getInstance() {
        if (instance == null) {
            instance = new MusicRecorder();
        }
        return instance;
    }

    private MediaRecorder mediaRecorder = null;
    private String lastFile = null;
    private String outputFile = null;

	public String init(String parentPath) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyMMddhhmmss", Locale.US);
        outputFile = sDateFormat.format(parentPath + "/" + new java.util.Date() + ".amr");
        mediaRecorder.setOutputFile(outputFile);
		try {
            mediaRecorder.prepare();
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return outputFile;
	}

	public void record() {
	    if (mediaRecorder != null) {
            mediaRecorder.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void pause() {
        if (mediaRecorder != null) {
            mediaRecorder.pause();
        }
    }

    public void stop() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (lastFile != null) {
            //TODO
        }
    }

    public void continueRecord(int timeOfLastFile) {
	    //TODO
    }
}
