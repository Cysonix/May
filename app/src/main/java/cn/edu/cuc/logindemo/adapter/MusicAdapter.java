package cn.edu.cuc.logindemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.cuc.logindemo.Utils.ImageLoader;
import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.fragment.MusicListFragment;

public class MusicAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; //用来下载图片的类，后面有介绍
    
    public MusicAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.music_item, null);

	        TextView title = (TextView)vi.findViewById(R.id.title);
	        TextView artist = (TextView)vi.findViewById(R.id.artist);
	        TextView duration = (TextView)vi.findViewById(R.id.duration);
	        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
        
	        HashMap<String, String> song = new HashMap<String, String>();
	        song = data.get(position);
        
	        // 设置ListView的相关值  
	        title.setText(song.get(MusicListFragment.KEY_TITLE));
	        artist.setText(song.get(MusicListFragment.KEY_ARTIST));
	        duration.setText(song.get(MusicListFragment.KEY_DURATION));
	        String url = song.get(MusicListFragment.KEY_THUMB_URL);
	        imageLoader.DisplayImage(url, thumb_image);
		
	        return vi;
    }
     
}
