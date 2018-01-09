package edu.fudan.ringbell.media;

/**
 * Created by YI on 2017/12/28.
 */

public class MusicConverter {

    static{
        System.loadLibrary("ffmpeg");
        System.loadLibrary("ffmpeginvoke");
    }

    public native static int run(String[] commands);

    public static int convert(String srcFile, String dstFile) {
        String[] commands = new String[4];
        commands[0] = "ffmpeg";
        commands[1] = "-i";
        commands[2] = "\"" + srcFile + "\"";
        commands[3] = "\"" + dstFile + "\"";

        int ret = MusicConverter.run(commands);
        if (ret == 0)
            return 0;
        else
            return -1;
    }
    public static int cut(String srcFile, String dstFile, int startMsec, int endMsec) {
        String[] commands = new String[10];
        commands[0] = "ffmpeg";
        commands[1] = "-i";
        commands[2] = "\"" + srcFile + "\"";
        commands[3] = "-ss";
        commands[4] = Double.toString((double)startMsec / 1000);
        commands[5] = "-t";
        commands[6] = Double.toString((double)endMsec / 1000);
        commands[7] = "-acodec";
        commands[8] = "copy";
        commands[9] = "\"" + dstFile + "\"";
        int ret = MusicConverter.run(commands);
        if (ret == 0)
            return 0;
        else
            return -1;
    }
    public static int join(String fileOne, String fileTwo, String dstFile) {
        String[] commands = new String[6];
        commands[0] = "ffmpeg";
        commands[1] = "-i";
        commands[2] = "\"concat:" + fileOne + "|" + fileTwo + "\"";
        commands[3] = "-acodec";
        commands[4] = "copy";
        commands[5] = "\"" + dstFile + "\"";
        int ret = MusicConverter.run(commands);
        if (ret == 0)
            return 0;
        else
            return -1;
    }
}
