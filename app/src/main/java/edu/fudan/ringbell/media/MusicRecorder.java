package edu.fudan.ringbell.media;

import android.annotation.TargetApi;
import android.media.MediaRecorder;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by YI on 2017/12/27.
 */

public class MusicRecorder {
    private static MusicRecorder instance;
    private ArrayList<int[]> unavailableTime = new ArrayList<>();
    private MusicRecorder() {
    }

    public static synchronized MusicRecorder getInstance() {
        if (instance == null) {
            instance = new MusicRecorder();
        }
        return instance;
    }

    private MediaRecorder mediaRecorder = null;
    private String outputFile = null;
    private String outputFileTemplate = null;

	public String init(String parentPath) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyMMddhhmmss", Locale.US);
        java.util.Date now = new java.util.Date();
        outputFile = parentPath + "/" + sDateFormat.format(now) + ".aac";
        outputFileTemplate = parentPath + "/" + sDateFormat.format(now);
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

    @TargetApi(Build.VERSION_CODES.N)
    public void resume() {
        if (mediaRecorder != null) {
            mediaRecorder.resume();
        }
    }

    public int stop(int endTime) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (unavailableTime.size() != 0) {
            int ret;
            int lastTiming = 0;
            for (int i = 0; i < unavailableTime.size(); i++) {
                ret = MusicConverter.cut(outputFile, outputFileTemplate + i + ".aac", lastTiming, unavailableTime.get(i)[0]);
                if (ret != 0) {
                    unavailableTime.clear();
                    return -1;
                }
                lastTiming = unavailableTime.get(i)[1];
            }
            ret = MusicConverter.cut(outputFile, outputFileTemplate + unavailableTime.size() + ".aac", lastTiming, endTime);
            if (ret != 0) {
                unavailableTime.clear();
                return -1;
            }
            new File(outputFile).delete();
            for (int i = 0; i < unavailableTime.size(); i++) {
                ret = MusicConverter.join(outputFileTemplate + i + ".aac", outputFileTemplate + (i + 1) + ".aac", outputFileTemplate + "temp.aac");
                if (ret != 0) {
                    unavailableTime.clear();
                    return -1;
                }
                new File(outputFileTemplate + i + ".aac").delete();
                new File(outputFileTemplate + (i + 1) + ".aac").delete();
                File f = new File(outputFileTemplate + "temp.aac");
                f.renameTo(new File(outputFileTemplate + (i + 1) + ".aac"));
            }
            File f = new File(outputFileTemplate + unavailableTime.size() + ".aac");
            f.renameTo(new File(outputFile));
        }
        unavailableTime.clear();
        return 0;
    }

    public void continueRecord(int timeToTrunc, int realTime) {
        if (mediaRecorder != null) {
            unavailableTime.add(new int[] {timeToTrunc, realTime});
            resume();
        }
    }
}
