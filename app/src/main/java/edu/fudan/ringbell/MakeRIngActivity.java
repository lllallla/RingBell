package edu.fudan.ringbell;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import edu.fudan.ringbell.media.MusicConverter;
import edu.fudan.ringbell.media.MusicPlayer;

/**
 * Created by niuzhenghao on 2018/1/7.
 */

public class MakeRIngActivity extends AppCompatActivity {
    private boolean readyToPlay = false;
    private boolean readyToEdit = false;
    private MusicPlayer player;
    private Drawable play;
    private Drawable pause;
    private SeekBar seekBar;
    private String ringPath = null;
    private String postfix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makering);

        String path = getIntent().getStringExtra("path");
        player = MusicPlayer.getInstance();
        if (path != null) {
            File f = new File(path);
            String fileName = f.getName();
            postfix = fileName.substring(fileName.lastIndexOf("."));
            ringPath = generateNewPath();

            Thread thread = new CopyThread(path);
            thread.start();
        }

        play = this.getResources().getDrawable(R.drawable.play_rdi_btn_play);
        pause = this.getResources().getDrawable(R.drawable.play_rdi_btn_pause);

        ImageView goBack = findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.mrTryplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readyToPlay) {
                    if (player.isPlaying()) {
                        player.pause();
                        ((ImageView) view).setImageDrawable(play);
                    } else {
                        player.play();
                        ((ImageView) view).setImageDrawable(pause);
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (player.isPlaying()) {
                                    player.pause();
                                    player.seekTo(seekBar.getProgress());
                                }
                            }
                        },40000);
                    }
                }
            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readyToEdit) {
                    readyToEdit = false;
                    readyToPlay = false;
                    player.destory();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //intent.setType(“image/*”);//选择图片
                    intent.setType("audio/*"); //选择音频
                    //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                    //intent.setType(“video/*;image/*”);//同时选择视频和图片
                    //intent.setType("*//*//");//无类型限制
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);
                }
            }
        });

        findViewById(R.id.saveRing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (readyToEdit) {
                    readyToEdit = false;
                    readyToPlay = false;
                    player.destory();
                    String path = Environment.getDataDirectory().getPath() + "/" + ((TextView) findViewById(R.id.mrMusicName)).getText() + postfix;
                    Thread thread = new CutThread(path, seekBar.getProgress(), seekBar.getProgress() + 40000);
                    thread.start();
                }
            }
        });

        //监听滚动条事件
        seekBar = findViewById(R.id.play_seek);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = null;
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    path = getPath(this, uri);
                } else {//4.4以下下系统调用方法
                    path = getRealPathFromURI(uri);
                }
            }
        }
        if (path != null) {
            readyToEdit = false;
            if (ringPath != null) {
                Thread thread = new JoinThread(path);
                thread.start();
            } else {
                Thread thread = new CopyThread(path);
                thread.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        player.destory();
        super.onDestroy();
    }

    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            String current = i / 60000 + ":" + (i / 1000) % 60;
            ((TextView) findViewById(R.id.music_duration_played)).setText(current);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (readyToPlay) {
                player.seekTo(seekBar.getProgress());
            }
        }
    }

    public class CopyThread extends Thread {
        private String path;
        CopyThread(String path) {
            this.path = path;
        }
        public void run() {
            copyFile(path,ringPath);
            player.init(ringPath, new MusicReadyListener());
        }
    }

    public class JoinThread extends Thread {
        private String path;
        JoinThread(String path) {
            this.path = path;
        }
        public void run() {
            String newPath = generateNewPath();
            int success = MusicConverter.join(ringPath, path, newPath);
            if (success == 0) {
                File f = new File(ringPath);
                f.delete();
                ringPath = newPath;
                readyToEdit = true;
                player.init(ringPath, new MusicReadyListener());
            }
        }
    }

    public class CutThread extends Thread {
        private String path;
        private int start;
        private int end;
        CutThread(String path, int start, int end) {
            this.path = path;
            this.start = start;
            this.end = end;
        }
        public void run() {
            File f = new File(ringPath);
            int success = MusicConverter.cut(ringPath,path,start,end);
            if (success == 0) {
                f.delete();
            }
        }
    }

    class MusicReadyListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            readyToPlay = true;
            int duration = mediaPlayer.getDuration();
            String current = duration / 60000 + ":" + (duration / 1000) % 60;
            ((TextView)findViewById(R.id.music_duration)).setText(current);
            seekBar.setProgress(0);
        }
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ( (byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String generateNewPath() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyMMddhhmmss", Locale.US);
        java.util.Date now = new java.util.Date();
        return Environment.getDataDirectory().getPath() + "/ring" + sDateFormat.format(now) + postfix;
    }
}
