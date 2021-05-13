package com.websarva.wings.android.todoapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import static com.websarva.wings.android.todoapps.AddToDoActivity.note_str;
import static com.websarva.wings.android.todoapps.AddToDoActivity.title_str;
import static com.websarva.wings.android.todoapps.AddToDoActivity.todo_list;

public class AddDialogFragment extends DialogFragment {
    private final AddToDoActivity addToDoActivity;

    AddDialogFragment(AddToDoActivity addToDoActivity){
        this.addToDoActivity = addToDoActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("この内容で追加しますか？");
        builder.setMessage("タイトル:"+title_str + "\n\n" +"メモ:" + note_str);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addToDoActivity.onFragmentResult();
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
