package com.selfcoderlab.pixel.effect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.theartofdev.edmodo.cropper.CropImageView;

import utils.Consts;

public class CropActivity extends Activity implements View.OnClickListener {

    CropImageView cropImageView;
    ImageView icLeft, icRight, icApply, icCancle,ic_back;
    Matrix mat;
    Bitmap flippedBitmap;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        init();
    }

    private void init() {
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        icLeft = (ImageView) findViewById(R.id.icLeft);
        icRight = (ImageView) findViewById(R.id.icRight);
        icApply = (ImageView) findViewById(R.id.icApply);
        icCancle = (ImageView) findViewById(R.id.icCancle);
        ic_back = (ImageView) findViewById(R.id.icBack);

        mat = new Matrix();
        cropImageView.setImageBitmap(Consts.MAIN_BITMAP);
        cropImageView.setAspectRatio(16, 9);
        cropImageView.setFixedAspectRatio(true);

        icLeft.setOnClickListener(this);
        icRight.setOnClickListener(this);
        icApply.setOnClickListener(this);
        icCancle.setOnClickListener(this);
        ic_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icLeft:
                mat.postRotate(-90);
                flippedBitmap = Bitmap.createBitmap(Consts.MAIN_BITMAP, 0, 0,
                        Consts.MAIN_BITMAP.getWidth(), Consts.MAIN_BITMAP.getHeight(), mat, true);
                cropImageView.setImageBitmap(flippedBitmap);
                break;
            case R.id.icRight:
                mat.postRotate(90);
                flippedBitmap = Bitmap.createBitmap(Consts.MAIN_BITMAP, 0, 0,
                        Consts.MAIN_BITMAP.getWidth(), Consts.MAIN_BITMAP.getHeight(), mat, true);
                cropImageView.setImageBitmap(flippedBitmap);
                break;
            case R.id.icApply:
                Consts.MAIN_BITMAP = cropImageView.getCroppedImage();

                Intent intent = new Intent(CropActivity.this, EditorActivity.class);
                startActivity(intent);
                break;
            case R.id.icCancle:
                onBackPressed();
                break;
            case R.id.icBack:
                onBackPressed();
                break;

        }

    }
}
