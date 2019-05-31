package cn.edu.cuc.logindemo.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.cuc.logindemo.R;
import cn.edu.cuc.logindemo.Utils.BackHandlerHelper;
import cn.edu.cuc.logindemo.Utils.FragmentBackHandler;
import cn.edu.cuc.logindemo.Utils.XMLParser;
import cn.edu.cuc.logindemo.adapter.MusicAdapter;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 音乐列表Fragment
 */
public class MusicListFragment extends Fragment{

    // 所有的静态变量
    static final String MUSIC_URL = "http://api.androidhive.info/music/music.xml";// xml目的地址,打开地址看一下
    // XML 节点
    public static final String KEY_SONG = "song"; // parent node
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_THUMB_URL = "thumb_url";

    private static final int MSG_GET_SUCCESS = 1;
    private static final String TAG = "MusicListFragment";

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;

    LocalHandler handler;
    ListView list;
    MusicAdapter adapter;
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.music, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    /**
     * 捕捉back,没有处理back键需求的Fragment不用实现
     */
    public abstract class BackHandledFragment extends Fragment implements FragmentBackHandler {
        @Override
        public boolean onBackPressed() {
            return BackHandlerHelper.handleBackPress(this);
        }
    }

    /**
     * 初始化
     */
    private void initialize(){
        handler = new LocalHandler();
        setUpViews();

        NetworkThread thread = new NetworkThread();
        thread.start();
        pDialog.show();
    }

    /**
     * 初始化页面控件
     */
    private void setUpViews(){

        list = (ListView) getView().findViewById(R.id.music_list);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle("提示信息");
        pDialog.setMessage("音乐列表加载中，请稍后...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);

    }

    /**
     * 获取音乐列表
     * @return
     */
    private ArrayList<HashMap<String, String>> getMusicList(){
        return null;

    }

    /**
     * 私有Handler消息处理类
     * @author SongQing
     *
     */
    private class LocalHandler extends Handler {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_GET_SUCCESS:
                    Toast.makeText(getActivity(), "成功获取音乐列表", Toast.LENGTH_SHORT).show();
                    if(msg.obj!=null){
                        String musicString = (String)(msg.obj);

                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            //申请WRITE_EXTERNAL_STORAGE权限
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                        }

                        try {
                            //TODO gson 解析，需要在domain创建对应article类，还需要dao创建ArticleDao，
                            //Adapter上绑定的list改为从数据库中获取
                            ArrayList<HashMap<String, String>> songsList  = resolutionXML(musicString);
                            adapter = new MusicAdapter(getActivity(), songsList);
                            list.setAdapter(adapter);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "获取音乐列表转换出错", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.toString());

                        }finally{
                            pDialog.dismiss();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 给Handler发送任务消息
     *
     * @param what
     * @param obj
     */
    private void sendMessage(int what, Object obj) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessageDelayed(msg, 1000);
    }

    /**
     * WorkerThread
     * 网络访问线程
     * @author SongQing
     *
     */
    class NetworkThread extends Thread{

        @Override
        public void run() {

            //2、基于OKhttp网络访问框架
//            OkHttpClient okHttpClient = new OkHttpClient();
//            final Request request = new Request.Builder()
//                    .url(MUSIC_URL)
//                    .build();
//            final Call call = okHttpClient.newCall(request);
//            try{
//                Response response = call.execute();
//                String temp = response.body().string();
//                sendMessage(MSG_GET_SUCCESS,temp );
//                Log.i("HttpClient", "从服务器请求到的数据为：" + temp);
//            }catch(Exception ex){
//                ex.printStackTrace();
//                Log.e("HttpClient", ex.toString());
//            }

            //1、创建httpclient
            HttpClient httpclient = new DefaultHttpClient();
            //2、创建代表请求的对象，参数是访问服务器的地址，eg：国家气象局提供的天气预报接口

            HttpGet httpget = new HttpGet(MUSIC_URL);
            Log.i("HttpClient", "开始获取音乐信息....");
            //3、执行请求，获取服务器发还的HttpResponse响应对象
            try {

                HttpResponse response = httpclient.execute(httpget);
                //4、检查相应的状态是否正常，检查状态码的值是否等于200
                int statusCode = response.getStatusLine().getStatusCode();
                if(statusCode==200){

                    //5、从响应对象中取出数据
                    HttpEntity entity = response.getEntity();
                    InputStream inStream = entity.getContent();

                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new  BufferedReader(new InputStreamReader(inStream));
                    for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                        builder.append(s);
                    }

                    sendMessage(MSG_GET_SUCCESS, builder.toString());
                    Log.i("HttpClient", "从服务器请求到的数据为：" + builder.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("HttpClient", ex.toString());
            }
        }
    }

    /**
     * 解析xml文件
     * @param xml
     */
    private ArrayList<HashMap<String, String>> resolutionXML(String xml){

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
//
        XMLParser parser = new XMLParser();
        Document doc = parser.getDomElement(xml); // 获取 DOM 节点

        NodeList nl = doc.getElementsByTagName(KEY_SONG);
        // 循环遍历所有的歌节点 <song>
        for (int i = 0; i < nl.getLength(); i++) {
            // 新建一个 HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // 每个子节点添加到HashMap关键= >值
            map.put(KEY_ID, parser.getValue(e, KEY_ID));
            map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
            map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
            map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
            map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

            // HashList添加到数组列表
            songsList.add(map);
        }

        if(songsList.isEmpty()){		//如果互联网上无法取得音乐列表
            songsList = getDefaultMusicForTest();
        }

        return songsList;
    }

    /**
     * 获取测试用音乐列表
     */
    private ArrayList<HashMap<String, String>> getDefaultMusicForTest(){

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put(KEY_ID, "001");
        map1.put(KEY_TITLE, "趁早");
        map1.put(KEY_ARTIST, "李琦");
        map1.put(KEY_DURATION,"4:40");
        map1.put(KEY_THUMB_URL, "https://api.androidhive.info/music/images/adele.png");
        songsList.add(map1);

        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put(KEY_ID, "002");
        map2.put(KEY_TITLE, "千千阙歌");
        map2.put(KEY_ARTIST, "陈慧娴");
        map2.put(KEY_DURATION,"5:45");
        map2.put(KEY_THUMB_URL, "https://api.androidhive.info/music/images/eminem.png");
        songsList.add(map2);


        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put(KEY_ID, "003");
        map3.put(KEY_TITLE, "我只在乎你");
        map3.put(KEY_ARTIST, "邓丽君");
        map3.put(KEY_DURATION,"3:25");
        map3.put(KEY_THUMB_URL, "https://api.androidhive.info/music/images/mj.png");
        songsList.add(map3);

        return songsList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
            }
        }
    }
}
