package com.miquido.pregnancycalendar.ui.fragments.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.adapters.DiaryEntriesAdapter;
import com.miquido.pregnancycalendar.adapters.ItemTouchHelper;
import com.miquido.pregnancycalendar.adapters.SimpleSectionedRecyclerViewAdapter;
import com.miquido.pregnancycalendar.model.DiaryEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DiaryFragment extends MainFragment implements ItemTouchHelper {

    private RecyclerView recyclerView;
    private EditText newEntryEditText;
    private ImageView saveImageView;
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
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        diaryEntriesAdapter = new DiaryEntriesAdapter();
        diaryEntriesAdapter.updateList(App.getInstance().getDiaryEntriesRepository().getAll());
        recyclerView.setAdapter(diaryEntriesAdapter);
        //setSections();
    }


    //FIXME: add sections?
    private void setSections() {
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0,"Section 1"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(2,"Section 2"));

        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(getContext(), R.layout.section,R.id.section_text,diaryEntriesAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        recyclerView.setAdapter(mSectionedAdapter);
    }

    private void findViews(View mainView) {
        recyclerView = (RecyclerView) mainView.findViewById(R.id.recycleview_entries);
        newEntryEditText = (EditText) mainView.findViewById(R.id.edittext_new_entry);
        saveImageView = (ImageView) mainView.findViewById(R.id.imageview_save);
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
    public void onItemDismiss(int position) {
        //do nothing?
    }

    public static MainFragment newInstance() {
        return new DiaryFragment();
    }
}
