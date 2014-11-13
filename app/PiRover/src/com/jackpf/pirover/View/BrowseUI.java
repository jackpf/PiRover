package com.jackpf.pirover.View;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jackpf.pirover.BrowseActivity;
import com.jackpf.pirover.R;
import com.jackpf.pirover.Camera.BufferedVideo;
import com.jackpf.pirover.Model.UI;
import com.jackpf.pirover.View.Helpers.ArrayAdapter;

public class BrowseUI extends UI<BrowseActivity>
{
    public BrowseUI(BrowseActivity activity)
    {
        super(activity);
    }

    @Override
    public void update()
    {
        @SuppressWarnings("unchecked")
        List<BufferedVideo> files = (List<BufferedVideo>) vars.get("videos");
        ListView lvFiles = (ListView) activity.findViewById(R.id.files);
        
        final ArrayAdapter<BufferedVideo> adapter = new ArrayAdapter<BufferedVideo>(activity, files, R.layout._file);
        adapter.setCallback(adapter.new Callback() {
            @Override
            public void createView(View row, BufferedVideo video) {
                ((TextView) row.findViewById(R.id.video_name)).setText(video.getName());
                ((ImageView) row.findViewById(R.id.video_icon)).setImageDrawable(video.getIcon().getDrawable());
            }
        });
        lvFiles.setAdapter(adapter);
        
        lvFiles.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.startPlaybackActivity(adapter.getItem(position));
            }
        });
    }
}