package com.jackpf.pirover.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.jackpf.pirover.Broadcast.ConnectionException;
import com.jackpf.pirover.Broadcast.TimeoutException;
import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.R;

import java.io.IOException;

public class BroadcastUI extends UI<MainActivity>
{
    private static ProgressDialog dialog;
    private static AlertDialog alertDialog;
    
    public BroadcastUI(MainActivity activity)
    {
        super(activity);
    }

    @Override
    public void preUpdate()
    {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        
        dialog = new ProgressDialog(context);
        
        dialog.setTitle(context.getString(R.string.broadcast_loading_title));
        dialog.setMessage(context.getString(R.string.broadcast_loading_text));
        dialog.setCancelable(false);
        
        dialog.show();
    }

    @Override
    public void update()
    {
        dialog.dismiss();
    }

    @Override
    public void error(Exception e)
    {
        if (e instanceof ConnectionException) {
            dialog.dismiss();
            Toast.makeText(activity, context.getString(R.string.broadcast_disconnected_message), Toast.LENGTH_SHORT).show();
        } else if (e instanceof TimeoutException || e instanceof IOException) {
            dialog.dismiss();
            
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_PHONE);
            
            alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.broadcast_timeout_title))
                .setMessage(context.getString(R.string.broadcast_timeout_message))
                .setView(input)
                .setPositiveButton(context.getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        activity.connect(input.getText().toString());
                    }
                })
                .setNegativeButton(context.getString(R.string.broadcast_rescan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        activity.connect(null);
                    }
                })
                .setCancelable(true)
                .show();
        }
    }
}