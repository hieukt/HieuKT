package com.example.qklahpita.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lenovo-PC on 2/7/2018.
 */

public class ImageUtils {
    public static String folderName = "/DrawImage";
    private static File tempFile;

    public static void saveImage(Bitmap bitmap, Context context) {
        String root = Environment.getExternalStorageDirectory().toString();

        File folder = new File(root, folderName);
        folder.mkdirs();

        String imageName = Calendar.getInstance().getTime().toString() + " .png";
        File imageFile = new File(folder.toString(), imageName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();

            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ImageModel> getListImage() {
        List<ImageModel> imageModelList = new ArrayList<>();
        File folder = new File(Environment
                .getExternalStorageDirectory().toString(), folderName);
        File[] listImage = folder.listFiles();
        if (listImage != null) {
            for (int i = 0; i < listImage.length; i++) {
                String path = listImage[i].getAbsolutePath();
                String name = listImage[i].getName();
                ImageModel imageModel = new ImageModel(name, path);
                imageModelList.add(imageModel);
            }
        }
        return imageModelList;
    }


    public static Uri getUri(Context context) {
        try {
            tempFile = File.createTempFile(
                    Calendar.getInstance().getTime().toString(),
                    ".png",
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
            tempFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = null;
        uri = FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                tempFile
        );
        return uri;
    }

    public static Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath());
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        double ratio = (double) bitmap.getWidth() / bitmap.getHeight();
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, (int) (screenWidth / ratio), false);
        return scaleBitmap;
    }
}
