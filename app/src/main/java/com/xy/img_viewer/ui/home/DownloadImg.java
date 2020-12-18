package com.xy.img_viewer.ui.home;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class DownloadImg extends AsyncTask<DownloadImg.Params, String, String> {

    private final WeakReference<Context> ctx;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(ctx.get(), s, Toast.LENGTH_LONG).show();
    }

    public DownloadImg(Context ctx) {
        super();
        this.ctx = new WeakReference<>(ctx);
    }

    @Override
    protected String doInBackground(Params... params) {
        String isSuccess = "下载完毕";
        for (int i = 0; i < params.length; i++) {
            Params p = params[i];
            File file = new File(p.getPath(), i + 1 + ".png");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file); InputStream inputStream = new URL(p.getUrl()).openStream()) {
                byte[] buffer = new byte[10240];
                int byteCount;
                while ((byteCount = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, byteCount);
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
                isSuccess = "下载失败!!!";
            }
        }
        return isSuccess;
    }

    public static class Params {
        public String getUrl() {
            return url;
        }

        public File getPath() {
            return path;
        }

        private final String url;
        private final File path;

        public Params(String url, File path) {
            this.url = url;
            this.path = path;
        }
    }
}
