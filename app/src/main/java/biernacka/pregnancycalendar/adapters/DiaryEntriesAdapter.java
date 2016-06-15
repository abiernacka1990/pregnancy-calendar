package biernacka.pregnancycalendar.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.model.DiaryEntry;
import biernacka.pregnancycalendar.utils.StringFormatter;

import org.joda.time.DateTime;

import java.io.File;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DiaryEntriesAdapter extends BaseRecycleViewAdapter<DiaryEntry, DiaryEntriesAdapter.ViewHolder> {

    private int smallerImageDimension;

    public DiaryEntriesAdapter(Context context) {
        smallerImageDimension = (int) context.getResources().getDimension(R.dimen.diary_image_smaller_size);
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
        if (item.getText() == null || item.getText().isEmpty()) {
            holder.diaryEntryTextTextView.setVisibility(View.GONE);
        } else {
            holder.diaryEntryTextTextView.setVisibility(View.VISIBLE);
            holder.diaryEntryTextTextView.setText(item.getText());
        }
        holder.diaryEntryDateTextView.setText(StringFormatter.withDayOfWeekDate(new DateTime(item.getDate())));
        if (item.getImagePath() == null) {
            holder.photoImageView.setVisibility(View.GONE);
        } else {
            holder.photoImageView.setVisibility(View.VISIBLE);
            holder.photoImageView.setImageBitmap(getImageBitmap(item));
        }
        holder.shareImageView.setOnClickListener(view -> shareEntry(item, holder.shareImageView.getContext()));
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

    private Bitmap getImageBitmap(DiaryEntry item) {
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
        return Bitmap.createScaledBitmap(bitmap, width, height, false);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView diaryEntryTextTextView;
        TextView diaryEntryDateTextView;
        ImageView photoImageView;
        ImageView shareImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            diaryEntryTextTextView = (TextView) itemView.findViewById(R.id.texview_diary_entry_text);
            diaryEntryDateTextView = (TextView) itemView.findViewById(R.id.texview_diary_entry_date);
            photoImageView = (ImageView) itemView.findViewById(R.id.imageview);
            shareImageView = (ImageView) itemView.findViewById(R.id.imageview_share);
        }
    }
}
