package biernacka.pregnancycalendar.ui.fragments.main.features;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

import biernacka.pregnancycalendar.App;
import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.adapters.DiaryEntriesAdapter;
import biernacka.pregnancycalendar.adapters.helper.ItemTouchHelper;
import biernacka.pregnancycalendar.model.DiaryEntry;
import biernacka.pregnancycalendar.ui.activities.TitleImageActivity;
import biernacka.pregnancycalendar.ui.fragments.main.MainFragment;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DiaryFragment extends MainFragment implements ItemTouchHelper {

    private static final int REQUEST_SELECT_PHOTO = 100;
    private static final int REQUEST_TITLE_IMAGE = 90;
    private static final int REQUEST_IMAGE_CAPTURE = 80;

    private RecyclerView recyclerView;
    private EditText newEntryEditText;
    private ImageView saveImageView;
    private ImageView photoImageView;
    private DiaryEntriesAdapter diaryEntriesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_diary, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        findViews(mainView);
        initAdapter();
        initListeners();
    }

    private void initListeners() {
        saveImageView.setOnClickListener(view -> addNewDiaryEntry());
        photoImageView.setOnClickListener(view -> pickPhotoSrcChoice());
        newEntryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSaveBtAvailability();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSaveBtAvailability();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setSaveBtAvailability();
            }
        });
    }

    private void pickPhotoSrcChoice() {
        new AlertDialog.Builder(getContext())
                .setSingleChoiceItems(getResources().getStringArray(R.array.diary_photo_src), 0, null)
                .setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        if (selectedPosition == 0) {
                            makePhoto();
                        } else {
                            choosePhoto();
                        }
                    }
                })
                .show();
    }

    private void makePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void choosePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == getActivity().RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    startActivityToTitleImage(imageBitmap);
                }
                break;
            case REQUEST_SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    startActivityToTitleImage(selectedImage);
                }
                break;
            case REQUEST_TITLE_IMAGE:
                if(resultCode == getActivity().RESULT_OK) {
                    refreshAdapter();
                }
                break;
        }
    }

    private void startActivityToTitleImage(Bitmap selectedImage) {
        Intent titleImageIntent = new Intent(getContext(), TitleImageActivity.class);
        titleImageIntent.putExtra(TitleImageActivity.ARG_IMAGE_BITMAP, selectedImage);
        titleImageIntent = appendImageTitleToIntent(titleImageIntent);
        startActivityForResult(titleImageIntent, REQUEST_TITLE_IMAGE);
    }

    private Intent appendImageTitleToIntent(Intent intent) {
        intent.putExtra(TitleImageActivity.ARG_IMAGE_TITLE, newEntryEditText.getText().toString());
        return intent;
    }

    private void startActivityToTitleImage(Uri selectedImage) {
        Intent titleImageIntent = new Intent(getContext(), TitleImageActivity.class);
        titleImageIntent.putExtra(TitleImageActivity.ARG_IMAGE_URI, selectedImage);
        titleImageIntent = appendImageTitleToIntent(titleImageIntent);
        startActivityForResult(titleImageIntent, REQUEST_TITLE_IMAGE);
    }

    private void setSaveBtAvailability() {
        saveImageView.setEnabled(!newEntryEditText.getText().toString().isEmpty());
    }

    private void addNewDiaryEntry() {
        String diaryEntryText = newEntryEditText.getText().toString();
        setViewsAfterSave();
        if (!diaryEntryText.isEmpty()) {
            createNewEntry(diaryEntryText);
            diaryEntriesAdapter.updateList(App.getInstance().getDiaryEntriesRepository().getAll());
        }
    }

    private void setViewsAfterSave() {
        newEntryEditText.setText(null);
        saveImageView.setEnabled(false);
        hideKeyboard();
    }

    private void createNewEntry(String diaryEntryText) {
        DiaryEntry newDiaryEntry = new DiaryEntry.Builder()
                .setDate(Calendar.getInstance().getTimeInMillis())
                .setText(diaryEntryText)
                .build();
        App.getInstance().getDiaryEntriesRepository().create(newDiaryEntry);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initAdapter() {
        setSwipe2DismissEnabled(recyclerView, this);
        diaryEntriesAdapter = new DiaryEntriesAdapter(getActivity());
        refreshAdapter();
        //setSections();
    }

    private void refreshAdapter() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(linearLayoutManager);
        diaryEntriesAdapter.updateList(App.getInstance().getDiaryEntriesRepository().getAll());
        recyclerView.setAdapter(diaryEntriesAdapter);
    }

    private void findViews(View mainView) {
        recyclerView = (RecyclerView) mainView.findViewById(R.id.recycleview_entries);
        newEntryEditText = (EditText) mainView.findViewById(R.id.edittext_new_entry);
        saveImageView = (ImageView) mainView.findViewById(R.id.imageview_save);
        photoImageView = (ImageView) mainView.findViewById(R.id.imageview_photo);
    }

    @Override
    public boolean isExpandedAppBarEnabled() {
        return false;
    }

    @Override
    public boolean isFabBottomVisible() {
        return false;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_diary;
    }

    @Override
    public void onItemDismiss(int position) {
        App.getInstance().getDiaryEntriesRepository().delete(diaryEntriesAdapter.getItem(position));
        refreshAdapter();
    }

    public static MainFragment newInstance() {
        return new DiaryFragment();
    }
}
