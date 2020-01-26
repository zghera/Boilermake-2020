package com.boilermake.mwen;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class AddReviewFragment extends DialogFragment{

    public static final String TAG = "ADDREVIEW";
    Bathroom bathroom = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        String obj_tostring = null;
        long count = 0;
        if (bundle != null) {
            obj_tostring = bundle.getString("obj", "");
            count        = bundle.getLong("count");
        }
        Log.v("MWEN", obj_tostring);
        try {
            JSONObject obj = new JSONObject(obj_tostring);
            bathroom = new Bathroom(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        setHasOptionsMenu(true);
        final com.google.android.material.textfield.TextInputEditText input = getView().findViewById(R.id.add_bathroom_name_textview);
        FloatingActionButton fb = getView().findViewById(R.id.fab_add_review);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final long finalCount = count;
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("DefaultLocale")
                DatabaseReference ref = database.getReference(String.format("/rating/\"%s\"/%d",bathroom.name , (int) finalCount));
                ref.setValue(new Review((int) finalCount, "anonymous", Objects.requireNonNull(input.getText()).toString()));
                assert getFragmentManager() != null;
                getDialog().dismiss();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_data, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.add_review_dialog, container, false);
    }
}