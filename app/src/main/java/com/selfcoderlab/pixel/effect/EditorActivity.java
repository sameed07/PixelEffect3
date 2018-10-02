package com.selfcoderlab.pixel.effect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.FlagView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.StickerModel;
import utils.Consts;
import utils.StickerBtn;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    float alpha = 240;
    TextView txtHidden;
    public static StickerBtn sticker_view;
    private ImageView mainImag, styleImag;
    private ImageView icStyle, icBkground, icEmoji, icText, icSave, icBack;

    private RecyclerView recyclerSticker;
    private RecyclerView recyclerStyles;
    private RecycleStyleAdapter styleAdapter;
    private RecycleStickerAdapter stickerAdapter;
    private GrideAdapter grideAdapter;
    private FrameLayout mainFrame;
    int stylePositoion;
    private GridView gridView;
    private AdView mAdView;
    private EditText et_view;
    private ImageButton mIbtn_color_text;
    int color, tcolor = 0xFF000000;
    String textstyle[] = {"font1.ttf", "font2.ttf", "font3.ttf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf", "font8.ttf"};
    private static final String TAG = "EditorActivity";

    private static final String KEY_DEFAULT_COLOR = "default_color";
    // Give your color picker dialog unique IDs if you have multiple dialogs.
    private static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
//        addPreferencesFromResource(R.xml.main);
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        init();
    }

    private void init() {
        icStyle = (ImageView) findViewById(R.id.icStyle);
        icBkground = (ImageView) findViewById(R.id.icBkground);
        icEmoji = (ImageView) findViewById(R.id.icEmoji);
        icText = (ImageView) findViewById(R.id.icText);
        icSave = (ImageView) findViewById(R.id.icSave);
        icBack = (ImageView) findViewById(R.id.icBack);


        sticker_view = (StickerBtn) findViewById(R.id.sticker_view);
        mainImag = (ImageView) findViewById(R.id.mainImag);
        styleImag = (ImageView) findViewById(R.id.styleImag);
        recyclerStyles = (RecyclerView) findViewById(R.id.recyclerStyles);
        recyclerSticker = (RecyclerView) findViewById(R.id.recyclerSticker);
        gridView = (GridView) findViewById(R.id.gridView);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);

        styleAdapter = new RecycleStyleAdapter();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(EditorActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerStyles.setLayoutManager(layoutManager);
        recyclerStyles.setAdapter(styleAdapter);

        ArrayList<StickerModel> stickerArrayList = new ArrayList<>();
        stickerArrayList.add(new StickerModel(R.drawable.c1, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c2, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c3, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c4, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c5, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c6, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c7, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c8, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c9, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c10, 65));
        stickerArrayList.add(new StickerModel(R.drawable.c11, 65));

        stickerAdapter = new RecycleStickerAdapter(stickerArrayList);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(EditorActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerSticker.setLayoutManager(layoutManager2);
        recyclerSticker.setAdapter(stickerAdapter);

        mainImag.setImageBitmap(Consts.MAIN_BITMAP);

        icStyle.setOnClickListener(this);
        icBkground.setOnClickListener(this);
        icEmoji.setOnClickListener(this);
        icText.setOnClickListener(this);
        icSave.setOnClickListener(this);
        icBack.setOnClickListener(this);
        styleImag.setOnTouchListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icStyle:

                recyclerStyles.setVisibility(View.VISIBLE);
                recyclerSticker.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);

                break;
            case R.id.icBkground:
                gridView.setVisibility(View.GONE);

                showColorDialog(1);
                break;
            case R.id.icEmoji:
                recyclerStyles.setVisibility(View.GONE);
                recyclerSticker.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);

                break;
            case R.id.icText:
                gridView.setVisibility(View.GONE);
                showTextDialog();
                break;
            case R.id.icSave:
                gridView.setVisibility(View.GONE);
                savePhoto();
                break;
            case R.id.icBack:
                onBackPressed();
                break;
        }
    }

    private void savePhoto() {

        mainFrame.setDrawingCacheEnabled(true);

        Bitmap bitmap;
        bitmap = mainFrame.getDrawingCache();
        if (sticker_view.getTotalSize() > 0) {
            bitmap = sticker_view.saveBitmap(mainFrame.getDrawingCache());
        } else {
            bitmap = mainFrame.getDrawingCache();
        }

        //  Log.e("sticker_view.getTotalSize()", sticker_view.getTotalSize() + "");

        try {
            File file, f = null;
            file = new File(Environment.getExternalStorageDirectory() + "/Pixel");

            if (!file.exists()) {
                file.mkdirs();
            }

            String str = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            str = str + "_pixel.jpg";
            f = new File(file.getAbsolutePath() + "/" + str);
            Log.e("path...", f.getAbsolutePath() + "");

            FileOutputStream ostream = new FileOutputStream(f);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

            ostream.close();

            MediaScannerConnection.scanFile(EditorActivity.this,
                    new String[]{f.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            //Log.i("ExternalStorage", "Scanned " + path + ":");
                            //Log.i("ExternalStorage", "-> uri=" + uri);

                        }
                    });

            mainFrame.setDrawingCacheEnabled(false);
            Intent intent = new Intent(EditorActivity.this, SavedActivity.class);
            intent.putExtra("path", file.getAbsolutePath() + "/" + str);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTextDialog() {

        Spinner mSpinner_text_style;

        final Dialog dialog = new Dialog(EditorActivity.this);
        // Include dialog.xml file

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.text_custom_dialog);
        dialog.setCancelable(false);

        et_view = (EditText) dialog.findViewById(R.id.et_view);

        dialog.setTitle("Text Appearance");
        dialog.show();
        mSpinner_text_style = (Spinner) dialog
                .findViewById(R.id.spinner_text_style);
        mIbtn_color_text = (ImageButton) dialog
                .findViewById(R.id.ibtn_color_text);

        TextAdapter adapter = new TextAdapter(EditorActivity.this, R.layout.spinner_row, textstyle);
        mSpinner_text_style.setAdapter(adapter);
        mIbtn_color_text.setBackgroundColor(tcolor);
        mSpinner_text_style.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int i, long arg3) {
                stylePositoion = i;
                Typeface face = Typeface.createFromAsset(getAssets(), textstyle[stylePositoion]);
                et_view.setTypeface(face);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        mIbtn_color_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showColorDialog(2);
                ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(EditorActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("ColorPicker Dialog");
                builder.setPreferenceName("MyColorPickerDialog");
                builder.setFlagView(new CustomFlag(EditorActivity.this, R.layout.layout_flag));
                builder.setPositiveButton(getString(R.string.confirm), new ColorListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope colorEnvelope) {
                        tcolor = colorEnvelope.getColor();
                        et_view.setTextColor(colorEnvelope.getColor());
                        Log.e("====COLOR=", String.valueOf(colorEnvelope.getColor()));
                        mIbtn_color_text.setBackgroundColor(tcolor);

                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();


            }
        });


        Button declineButton = (Button) dialog
                .findViewById(R.id.btn_cancel);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        Button Ok = (Button) dialog.findViewById(R.id.btn_ok);
        // if decline button is clicked, close the custom dialog
        Ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Close dialog
                txtHidden = null;
                txtHidden = new TextView(EditorActivity.this);

                final FrameLayout.LayoutParams params =
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                txtHidden.setLayoutParams(params);
                txtHidden.setTextColor(tcolor);
                Typeface face = Typeface.createFromAsset(getAssets(),
                        textstyle[stylePositoion]);

                txtHidden.setTypeface(face);
                txtHidden.setTextSize(50);

                String s = et_view.getText().toString().trim();
                Log.e("====Text====", s);
                txtHidden.setText(" " + s + " ");

                if (txtHidden.getText().toString().trim().length() == 0) {
                    Toast.makeText(EditorActivity.this, "Please enter Text", Toast.LENGTH_SHORT).show();
                } else {


                    // Toast.makeText(MainActivity.this,txtHidden.getText()+" niravvvv",Toast.LENGTH_LONG).show();
                    txtHidden.setDrawingCacheEnabled(false);
                    txtHidden.setVisibility(View.INVISIBLE);
                    mainFrame.addView(txtHidden);


                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            txtHidden.setDrawingCacheEnabled(true);
                            txtHidden.buildDrawingCache();
                            sticker_view.setVisibility(View.VISIBLE);
                            sticker_view.setWaterMark(txtHidden.getDrawingCache(), null);

                            dialog.dismiss();
                        }
                    }.start();
                }

            }
        });

    }

    private void showColorDialog(final int i) {
        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("ColorPicker Dialog");
        builder.setPreferenceName("MyColorPickerDialog");
        builder.setFlagView(new CustomFlag(this, R.layout.layout_flag));
        builder.setPositiveButton(getString(R.string.confirm), new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
//                TextView textView = findViewById(R.id.textView);
//                textView.setText("#" + colorEnvelope.getHtmlCode());

                if (i == 1) {
                    Log.e("=====11111===", String.valueOf(i));
                    styleImag.setColorFilter(colorEnvelope.getColor());
                    Drawable dPage_header = styleImag.getDrawable();
                    dPage_header.setAlpha((int) alpha);
                    styleImag.setImageDrawable(dPage_header);
                } else {

                    Log.e("=====2222===", String.valueOf(i));
                    et_view.setTextColor(colorEnvelope.getColor());
                    Log.e("====COLOR=", String.valueOf(colorEnvelope.getColor()));
                    mIbtn_color_text.setBackgroundColor(colorEnvelope.getColor());
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();


    }


    private class RecycleStyleAdapter extends RecyclerView.Adapter<RecycleStyleAdapter.MyStyleAdapter> {
        @Override
        public MyStyleAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_recycler_item, null);
            return new MyStyleAdapter(view);
        }

        @Override
        public int getItemCount() {
            return Consts.STYLE_ID.length;
        }

        @Override
        public void onBindViewHolder(MyStyleAdapter holder, final int position) {
            Glide.with(EditorActivity.this).load(Consts.STYLE_ID[position]).into(holder.imageStyle);
            holder.imageStyle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Consts.STYLE_NO = position;
                    styleImag.setImageResource(Consts.STYLE_ID[position]);
                    Drawable dPage_header = styleImag.getDrawable();
                    dPage_header.setAlpha((int) alpha);
                    styleImag.setImageDrawable(dPage_header);

                    //styleImag.setColorFilter(Color.parseColor("#000000"));
                    // styleImag.setBackgroundColor(Color.parseColor("#000000"));
                }
            });
        }

        public class MyStyleAdapter extends RecyclerView.ViewHolder {
            public ImageView imageStyle;

            public MyStyleAdapter(View itemView) {
                super(itemView);
                imageStyle = (ImageView) itemView.findViewById(R.id.img_frame_1);

            }
        }
    }


    private class RecycleStickerAdapter extends RecyclerView.Adapter<RecycleStickerAdapter.MyStickerHolder> {
        ArrayList<StickerModel> stickerList;

        public RecycleStickerAdapter(ArrayList<StickerModel> stickerArrayList) {
            this.stickerList = stickerArrayList;
        }

        @Override
        public MyStickerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_recycler_item, null);
            return new MyStickerHolder(view);
        }

        @Override
        public void onBindViewHolder(MyStickerHolder holder, final int position) {
            StickerModel Sticker = stickerList.get(position);

            Glide.with(EditorActivity.this).load(Sticker.stickerId).into(holder.imageSticker);

            holder.imageSticker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridView.setVisibility(View.VISIBLE);
                    int newPos = position;
                    grideAdapter = new GrideAdapter(EditorActivity.this, stickerList.get(position).size, ++newPos);
                    gridView.setAdapter(grideAdapter);
                }
            });
        }

        @Override
        public int getItemCount() {
            return stickerList.size();
        }

        public class MyStickerHolder extends RecyclerView.ViewHolder {
            public ImageView imageSticker;

            public MyStickerHolder(View itemView) {
                super(itemView);
                imageSticker = (ImageView) itemView.findViewById(R.id.img_frame_1);

            }
        }
    }


    private class GrideAdapter extends BaseAdapter {
        Context context;
        int size;
        int stId;
        LayoutInflater inflater;
        int correctPosition;

        public GrideAdapter(Context context, int size, int stId) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.size = size;
            this.stId = stId;
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.sticker_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imgSticker);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(EditorActivity.this).load("file:///android_asset/" + stId + "/" + (position) + ".png").into(viewHolder.imageView);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO main logic
                    gridView.setVisibility(View.GONE);
                    correctPosition = position;
                    sticker_view.setVisibility(View.VISIBLE);
                    sticker_view.setWaterMark(getBitmapFromAsset(EditorActivity.this, "" + stId + "/" + (position) + ".png"), null);
                }
            });
            return convertView;
        }

        private class ViewHolder {

            ImageView imageView;

        }
    }

    private Bitmap getBitmapFromAsset(Context context, String s) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(s);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
        }

        return bitmap;
    }

    private class TextAdapter extends ArrayAdapter<String> {

        public TextAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_row, parent, false);

            TextView label = (TextView) row.findViewById(R.id.textView1);
            label.setText("Square Emoji");

            Typeface face = Typeface.createFromAsset(getAssets(),
                    textstyle[position]);

            label.setTypeface(face);

            return row;

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        if (x > 80 && x < 510) {
            alpha = (float) (x * 0.5);

            Drawable dPage_header = styleImag.getDrawable();
            dPage_header.setAlpha((int) alpha);
            styleImag.setImageDrawable(dPage_header);

        }
        return true;
    }

    private class CustomFlag extends FlagView {

        private TextView textView;
        private View view;

        public CustomFlag(Context context, int layout) {
            super(context, layout);
            textView = findViewById(R.id.flag_color_code);
            view = findViewById(R.id.flag_color_layout);
        }

        @Override
        public void onRefresh(ColorEnvelope colorEnvelope) {
            textView.setText("#" + String.format("%06X", (0xFFFFFF & colorEnvelope.getColor())));
            view.setBackgroundColor(colorEnvelope.getColor());
        }


//        @Override
//        public void onRefresh(int color) {
//
//        }
    }
}
