package com.example.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AnnouncementsDialog extends DialogFragment {

    public interface OnInputSelected{
        void sendInput(String headLine, String content);
    }

    public OnInputSelected mOnInputSelected;

    private EditText headLine, content;

    private Button saveBtn, cancelBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.announcements_dialog, container, false);

        headLine = rootView.findViewById(R.id.headline);
        content = rootView.findViewById(R.id.content);

        saveBtn = rootView.findViewById(R.id.save);
        cancelBtn = rootView.findViewById(R.id.cancel);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String headline = headLine.getText().toString();
                String cont = content.getText().toString();

                if(!headline.equals("") && !cont.equals("")){
                    mOnInputSelected.sendInput(headline, cont);
                }

                getDialog().dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString());
        }
    }
}
