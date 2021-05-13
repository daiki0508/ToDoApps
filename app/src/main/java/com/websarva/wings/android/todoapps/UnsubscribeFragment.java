package com.websarva.wings.android.todoapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import static com.websarva.wings.android.todoapps.AddToDoActivity.note_str;
import static com.websarva.wings.android.todoapps.AddToDoActivity.title_str;

public class UnsubscribeFragment extends DialogFragment {
    private final UnsubscribeActivity unsubscribeActivity;

    UnsubscribeFragment(UnsubscribeActivity unsubscribeActivity){
        this.unsubscribeActivity = unsubscribeActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("本当に退会しますか？");
        builder.setMessage("退会すると2度と元のデータにはアクセス出来なくなります！");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                unsubscribeActivity.onFragmentResult();
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
