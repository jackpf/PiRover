package com.jackpf.pirover.View;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.R;
import com.jackpf.pirover.Broadcast.ConnectionException;
import com.jackpf.pirover.Broadcast.TimeoutException;
import com.jackpf.pirover.Model.UI;

public class BroadcastUI extends UI
{
    private static ProgressDialog dialog;
    private static AlertDialog alertDialog;
    
    public BroadcastUI(Context context)
    {
        super(context);
    }
    
    public void initialise()
    {

    }
    
    public void preUpdate()
    {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        
        dialog = new ProgressDialog(context);
        
        dialog.setTitle(context.getString(R.string.broadcast_loading));
        dialog.setMessage(context.getString(R.string.broadcast_detecting));
        dialog.setCancelable(false);
        
        dialog.show();
    }
    
    public void update()
    {
        dialog.dismiss();
    }
    
    public void error(Exception e)
    {
        if (e instanceof ConnectionException) {
            alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.broadcast_disconnected_title))
                .setMessage(context.getString(R.string.broadcast_disconnected_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        activity.finish();
                    }
                 })
                .setCancelable(false)
                .show();
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
                        ((MainActivity) activity).connect(input.getText().toString());
                    }
                })
                .setCancelable(false)
                .show();
        }
    }
}