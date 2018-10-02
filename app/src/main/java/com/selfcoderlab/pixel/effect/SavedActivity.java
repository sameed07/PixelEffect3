package com.selfcoderlab.pixel.effect;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.File;

public class SavedActivity extends Activity implements View.OnClickListener {
    ImageView btnFb, btnInsta, btnWatsap, btnOthr, ad1, ad2, ad3, icBack;
    ImageView imgSaved;
    String path;
    File filepath;
    Uri tempUri;
    AdRequest native_adreq;
    NativeExpressAdView nativ_adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        path = getIntent().getStringExtra("path");

        native_adreq = new AdRequest.Builder().build();
        nativ_adView = (NativeExpressAdView) findViewById(R.id.nativeadView);
        nativ_adView.loadAd(native_adreq);

        init();
    }

    private void init() {
        icBack = (ImageView) findViewById(R.id.icBack);
        imgSaved = (ImageView) findViewById(R.id.imgSaved);
        btnFb = (ImageView) findViewById(R.id.btnFb);
        btnInsta = (ImageView) findViewById(R.id.btnInsta);
        btnWatsap = (ImageView) findViewById(R.id.btnWatsap);
        btnOthr = (ImageView) findViewById(R.id.btnMore);
        ad1 = (ImageView) findViewById(R.id.icAd1);
        ad2 = (ImageView) findViewById(R.id.icAd2);
        ad3 = (ImageView) findViewById(R.id.icAd3);

        filepath = new File(path);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 24) {
            tempUri = Uri.parse(filepath.toString());
        } else {
            tempUri = Uri.fromFile(filepath);
        }
        imgSaved.setImageURI(tempUri);

        icBack.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnInsta.setOnClickListener(this);
        btnWatsap.setOnClickListener(this);
        btnOthr.setOnClickListener(this);
        ad1.setOnClickListener(this);
        ad2.setOnClickListener(this);
        ad3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFb:
                try {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    File media = new File(path);
                    Uri screenshotUri = Uri.fromFile(media);
                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    sharingIntent.setPackage("com.facebook.katana");
                    startActivity(sharingIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SavedActivity.this, "Facebook app not found", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.btnInsta:
                try {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    File media = new File(path);
                    Uri screenshotUri = Uri.fromFile(media);
                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    sharingIntent.setPackage("com.instagram.android");
                    startActivity(sharingIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SavedActivity.this, "Instagram app not found", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.btnWatsap:
                try {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    File media = new File(path);
                    Uri screenshotUri = Uri.fromFile(media);
                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    sharingIntent.setPackage("com.whatsapp");
                    startActivity(sharingIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SavedActivity.this, "Whatsapp not found", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.btnMore:
                try {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    File media = new File(path);
                    Uri screenshotUri = Uri.fromFile(media);
                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    startActivity(Intent.createChooser(sharingIntent, "Share with"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SavedActivity.this, "Instagram app not found", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.icBack:
                onBackPressed();
                break;
            case R.id.icAd1:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.selfcoder.square.emoji")));
                break;
            case R.id.icAd2:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.selfcoderlab.birthday.photoframe")));
                break;
            case R.id.icAd3:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.selfcoderlab.love.photoframe")));
                break;
        }
    }
}
