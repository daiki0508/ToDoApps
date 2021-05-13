package com.websarva.wings.android.todoapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import static com.websarva.wings.android.todoapps.ToDoActivity.note_str;
import static com.websarva.wings.android.todoapps.ToDoActivity.title_str;

public class ToDoDiaLogFragment extends DialogFragment {
    private final ToDoActivity toDoActivity;

    ToDoDiaLogFragment(ToDoActivity toDoActivity){
        this.toDoActivity = toDoActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("削除しますか？");
        builder.setMessage("タイトル:" + title_str + "\n\n" + "メモ:" + note_str);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toDoActivity.onFragmentResult();
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
