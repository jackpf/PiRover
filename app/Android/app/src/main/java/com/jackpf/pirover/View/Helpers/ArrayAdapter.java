package com.jackpf.pirover.View.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class ArrayAdapter<T> extends BaseAdapter
{
    private final List<T> objects;
    private final LayoutInflater inflater;
    private Callback callback;
    int rowLayout;

    public ArrayAdapter(Context context, List<T> objects, int rowLayout) {
        this.objects = objects;
        this.rowLayout = rowLayout;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public T getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return objects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        if (convertView == null) {
            row = inflater.inflate(rowLayout, parent, false);
        } else {
            row = convertView;
        }

        T item = getItem(position);

        if (callback != null) {
            callback.createView(row, item);
        }
        
        return row;
    }
    
    public abstract class Callback {
        public abstract void createView(View row, T item);
    }
}
