package biernacka.pregnancycalendar.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.model.DiaryEntry;
import biernacka.pregnancycalendar.ui.helpers.ImageHelper;
import biernacka.pregnancycalendar.utils.StringFormatter;

import org.joda.time.DateTime;

import java.io.File;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DiaryEntriesAdapter extends BaseRecycleViewAdapter<DiaryEntry, DiaryEntriesAdapter.ViewHolder> {

    private int smallerImageDimension;
    private Activity activity;

    public DiaryEntriesAdapter(Context context, Activity activity) {
        smallerImageDimension = (int) context.getResources().getDimension(R.dimen.diary_image_smaller_size);
        this.activity = activity;
    }

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_diary_entry;
    }

    @Override
    protected void updateViewHolder(ViewHolder holder, DiaryEntry item) {

        Bitmap image = getBitmap(item);
        setText(holder, item);
        setDate(holder, item);
        setImage(holder, item, image);
        boolean emptyImage = image == null;
        setOptions(holder, emptyImage);
        setListeners(holder, item, image);
    }

    private void setOptions(ViewHolder holder, boolean emptyImage) {
        if (emptyImage) {
            holder.fbShareImageView.setVisibility(View.GONE);
        } else {
            holder.fbShareImageView.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners(ViewHolder holder, DiaryEntry item, Bitmap image) {
        holder.shareImageView.setOnClickListener(view -> shareEntry(item, holder.shareImageView.getContext()));
        holder.fbShareImageView.setOnClickListener(view -> shareToFbEntry(image, item.getText()));
    }

    private void setImage(ViewHolder holder, DiaryEntry item, Bitmap image) {
        if (item.getImagePath() == null) {
            holder.photoImageView.setVisibility(View.GONE);
        } else {
            holder.photoImageView.setVisibility(View.VISIBLE);
            holder.photoImageView.setImageBitmap(image);
        }
    }

    private void setDate(ViewHolder holder, DiaryEntry item) {
        holder.diaryEntryDateTextView.setText(StringFormatter.withDayOfWeekDate(new DateTime(item.getDate())));
    }

    private void setText(ViewHolder holder, DiaryEntry item) {
        if (item.getText() == null || item.getText().isEmpty()) {
            holder.diaryEntryTextTextView.setVisibility(View.GONE);
        } else {
            holder.diaryEntryTextTextView.setVisibility(View.VISIBLE);
            holder.diaryEntryTextTextView.setText(item.getText());
        }
    }

    @Nullable
    private Bitmap getBitmap(DiaryEntry item) {
        Bitmap image = null;
        try {
            image = getImageBitmap(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private void shareToFbEntry(Bitmap image, String text) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(text)
                .build();
        SharePhotoContent shareContent = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(shareContent, null);
        ShareDialog shareDialog = new ShareDialog(activity);
        shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
    }

    private void shareEntry(DiaryEntry diaryEntry, Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = getImageUri(diaryEntry);
        if (uri != null) {
            shareIntent.setType("text/html");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TITLE, diaryEntry.getText());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, diaryEntry.getText());
            shareIntent.putExtra(Intent.EXTRA_TEXT, diaryEntry.getText());
        } else {
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, diaryEntry.getText());
        }
        context.startActivity(shareIntent);
    }

    private Uri getImageUri(DiaryEntry diaryEntry) {
        try {
            return Uri.fromFile(new File(diaryEntry.getImagePath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getImageBitmap(DiaryEntry item) throws Exception {
        int width, height;
        Bitmap bitmap = BitmapFactory.decodeFile(item.getImagePath());
        int originalBitmapWidth = bitmap.getWidth();
        int originalBitmapHeight = bitmap.getHeight();
        float aspectRatio =  originalBitmapWidth / (float) originalBitmapHeight;
        if (originalBitmapHeight > originalBitmapWidth) {
            width = smallerImageDimension;
            height = Math.round(width / aspectRatio);
        } else {
            height = smallerImageDimension;
            width = Math.round(height * aspectRatio);
        }
        return ImageHelper.decodeSampledBitmapFromFile(item.getImagePath(), width, height);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView diaryEntryTextTextView;
        TextView diaryEntryDateTextView;
        ImageView photoImageView;
        ImageView shareImageView;
        ImageView fbShareImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            diaryEntryTextTextView = (TextView) itemView.findViewById(R.id.texview_diary_entry_text);
            diaryEntryDateTextView = (TextView) itemView.findViewById(R.id.texview_diary_entry_date);
            photoImageView = (ImageView) itemView.findViewById(R.id.imageview);
            shareImageView = (ImageView) itemView.findViewById(R.id.imageview_share);
            fbShareImageView = (ImageView) itemView.findViewById(R.id.imageview_fb_share);
        }
    }
}
