package com.selfcoderlab.pixel.effect;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.File;

import utils.Consts;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int SELECT_GALLERY = 177;
    private static final int REQUEST_CAMERA = 188;
    ImageView ic_gallery, ic_camera, ic_share, ic_rate, ic_ad1, ic_ad2;
    private Uri selectedImageUri;
    File cacheDir;
    String fname;
    AdRequest native_adreq;
    NativeExpressAdView nativ_adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        native_adreq = new AdRequest.Builder().build();
        nativ_adView = findViewById(R.id.nativeadView);
        nativ_adView.loadAd(native_adreq);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            } else {
                init();
            }
        } else {
            init();
        }
        init();
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 101);
    }

    private boolean checkIfAlreadyhavePermission() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void init() {

        ic_gallery = findViewById(R.id.icGallery);
        ic_camera = findViewById(R.id.icCamera);
        ic_share = findViewById(R.id.icShare);
        ic_rate = findViewById(R.id.icRate);
        ic_ad1 = findViewById(R.id.icAd1);
        ic_ad2 = findViewById(R.id.icAd2);

        ic_gallery.setOnClickListener(this);
        ic_camera.setOnClickListener(this);
        ic_share.setOnClickListener(this);
        ic_rate.setOnClickListener(this);
        ic_ad1.setOnClickListener(this);
        ic_ad2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.icGallery:
                Intent iGalery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGalery, SELECT_GALLERY);
                break;
            case R.id.icCamera:
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
                    cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "framemages_cache");
                else
                    cacheDir = getCacheDir();
                if (!cacheDir.exists())
                    cacheDir.mkdirs();

                fname = System.currentTimeMillis() + ".jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                startActivityForResult(intent, REQUEST_CAMERA);
                break;
            case R.id.icShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "" + "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.icRate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                break;
            case R.id.icAd1:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.selfcoder.square.emoji")));
                break;
            case R.id.icAd2:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.selfcoderlab.manhairstyle")));
                break;
        }
    }

    private Uri getImageUri() {
        Uri tempURI;
        File mImageFile = new File(cacheDir, fname);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            tempURI = Uri.fromFile(mImageFile);

        } else {
            //tempURI = Uri.parse(mImageFile.toString());

            tempURI = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", mImageFile);
        }
        return tempURI;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            selectedImageUri = getImageUri();
            if (selectedImageUri != null) {

                try {
                    try {
                        Consts.MAIN_BITMAP = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(String.valueOf(selectedImageUri)));
                        Intent intent = new Intent(MainActivity.this, CropActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        if (selectedImageUri != null) {
                            File deletefile = new File(selectedImageUri.toString());
                            if (deletefile.exists()) {
                                deletefile.delete();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //handle exception
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //startActivity(intent);
            } else {
                Log.e("Error", "Error wile fetching image from gallery");
            }
        } else if (requestCode == SELECT_GALLERY && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Consts.MAIN_BITMAP = null;
                Bitmap bb = BitmapFactory.decodeFile(picturePath);

                //Log.e("PAth", ">> " + picturePath + "\n " + selectedImage.toString());

                Consts.MAIN_BITMAP = bb;

                Intent intent = new Intent(MainActivity.this, CropActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
