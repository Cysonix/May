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
import cn.edu.cuc.logindemo.Utils.ToastHelper;
import cn.edu.cuc.logindemo.Utils.XMLParser;
import cn.edu.cuc.logindemo.adapter.MusicAdapter;
import cn.edu.cuc.logindemo.okhttp.OnSuccessAndFaultListener;
import cn.edu.cuc.logindemo.okhttp.OnSuccessAndFaultSub;
import cn.edu.cuc.logindemo.okhttp.bean.WeatherResponseBean;
import cn.edu.cuc.logindemo.okhttp.service.MusicSubscribe;

/**
 * 音乐列表Fragment
 */
public class MusicListFragment0 extends Fragment{

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
        setUpViews();
        getMusicListData();
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
     * 请求音乐列表的数据
     * OnSuccessAndFaultSub 只是加了错误处理和请求的loading，可以自己根据项目的业务修改
     * new OnSuccessAndFaultSub（第一个参数:成功or失败的回调，第二个参数:上下文，可以不填，控制dialog的）
     */
    private void getMusicListData() {

        MusicSubscribe.getMusicListForQuery(new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {

            @Override
            public void onSuccess(String result) {
                //成功
                ToastHelper.showToast("成功获取音乐列表", Toast.LENGTH_SHORT);
                ArrayList<HashMap<String, String>> songsList = resolutionXML(result);

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                }

                try {
                    adapter = new MusicAdapter(getActivity(), songsList);
                    list.setAdapter(adapter);
                } catch (Exception e) {
                    ToastHelper.showToast("获取音乐列表转换出错", Toast.LENGTH_SHORT);
                    Log.e(TAG, e.toString());
                }finally {
                    pDialog.dismiss();
                }

            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                ToastHelper.showToast("请求失败："+errorMsg, Toast.LENGTH_SHORT);
            }
        },getActivity()));

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
