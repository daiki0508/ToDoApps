package com.websarva.wings.android.todoapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import static com.websarva.wings.android.todoapps.DetailContentActivity.note_str;
import static com.websarva.wings.android.todoapps.DetailContentActivity.title_str;

public class DetailContentDialogFragment extends DialogFragment {
    private final DetailContentActivity detailContentActivity;

    DetailContentDialogFragment(DetailContentActivity detailContentActivity){
        this.detailContentActivity = detailContentActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("この内容で更新しますか？");
        builder.setMessage("タイトル:"+ title_str + "\n\n" +"メモ:" + note_str);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                detailContentActivity.onFragmentResult();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }
}
