package biernacka.pregnancycalendar.ui.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import biernacka.pregnancycalendar.App;
import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.model.DiaryEntry;
import biernacka.pregnancycalendar.ui.helpers.ImageHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class TitleImageActivity extends AppCompatActivity {

    public static final String APP_IMAGES_DIR_NAME = "/PregnancyCalendar";
    public static String ARG_IMAGE_URI = "ARG_IMAGE_URI";
    public static String ARG_IMAGE_BITMAP = "ARG_IMAGE_BITMAP";
    private ImageView photoImageView;
    private EditText titleEditText;
    private Bitmap imageBitmap;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_image);
        initView();
    }

    private void initView() {
        initToolbar();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        photoImageView = (ImageView) findViewById(R.id.imageview_photo);
        titleEditText = (EditText) findViewById(R.id.edittext_title);
        setImage();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setImage() {
        imageBitmap = getIntent().getParcelableExtra(ARG_IMAGE_BITMAP);
        if (imageBitmap == null) {
            Uri imageUri = getIntent().getParcelableExtra(ARG_IMAGE_URI);
            imageBitmap = ImageHelper.getBitmapFromUri(imageUri, this);
        }
        if (imageBitmap != null) {
            photoImageView.setImageBitmap(imageBitmap);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_title_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                saveAndCloseActivity();
                return true;
            case  android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveAndCloseActivity() {
        showProgressBar(true);
        save();
        closeActivity();
    }

    private void save() {
        File imageFile = getImageFile();
        saveImageAndGetPath(imageFile);
        DiaryEntry diaryEntry = new DiaryEntry.Builder()
                .setText(titleEditText.getText().toString())
                .setImagePath(imageFile.getAbsolutePath())
                .setDate(Calendar.getInstance().getTimeInMillis())
                .build();
        App.getInstance().getDiaryEntriesRepository().create(diaryEntry);
    }

    @NonNull
    private File getImageFile() {
        String root = Environment.getExternalStorageDirectory().toString();
        File imagesDir = new File(root + APP_IMAGES_DIR_NAME);
        imagesDir.mkdirs();
        String fileName = "Image-" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        return new File(imagesDir, fileName);
    }

    private void saveImageAndGetPath(File imageFile) {
        if (imageFile.exists()) imageFile.delete();
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeActivity() {
        setResult(RESULT_OK);
        finish();
    }

    private void showProgressBar(boolean isShown) {
        if (isShown) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}


