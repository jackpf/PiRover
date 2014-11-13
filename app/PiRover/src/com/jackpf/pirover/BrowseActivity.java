package com.jackpf.pirover;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jackpf.pirover.Camera.Recorder;
import com.jackpf.pirover.Camera.Video;

public class BrowseActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        
        ListView lvFiles = (ListView) findViewById(R.id.files);
        List<Video> files = new ArrayList<Video>();
        
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + Recorder.RECORD_DIR);
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.getName().substring(file.getName().lastIndexOf('.') + 1).equals(Recorder.RECORD_EXT)) {
                    try {
                        files.add(new Video(file.getPath()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        final ArrayAdapter<Video> adapter = new ArrayAdapter<Video>(this, files);
        adapter.setCallback(adapter.new Callback() {
            public void createView(View row, Video video) {
                ((TextView) row.findViewById(R.id.video_name)).setText(video.getName());
                ((ImageView) row.findViewById(R.id.video_icon)).setImageDrawable(video.getIcon().getDrawable());
            }
        });
        lvFiles.setAdapter(adapter);
        
        lvFiles.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startPlaybackActivity(adapter.getItem(position));
            }
        });
    }
    
    protected void startPlaybackActivity(Video video)
    {
        Intent intent = new Intent(this, PlaybackActivity.class);
        intent.putExtra("video", video.getName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browse, menu);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class ArrayAdapter<T> extends BaseAdapter
    {
        private final List<T> objects;
        private final LayoutInflater inflater;
        private Callback callback;

        public ArrayAdapter(Context context, List<T> objects) {
            this.objects = objects;

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
                row = inflater.inflate(R.layout._file, parent, false);
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
}
