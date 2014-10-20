package com.jackpf.pirover.View;

import android.app.ProgressDialog;
import android.content.Context;

import com.jackpf.pirover.R;
import com.jackpf.pirover.Model.UI;

public class BroadcastUI extends UI
{
    ProgressDialog dialog;
    
    public BroadcastUI(Context context)
    {
        super(context);
    }
    
    public void initialise()
    {

    }
    
    public void preUpdate()
    {
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
        e.printStackTrace();
    }
}