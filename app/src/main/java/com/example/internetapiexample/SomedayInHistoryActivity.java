package com.example.internetapiexample;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SomedayInHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button sendRequests_today, sendRequests_custom_day, change_bg;
    private TextView response_text, todayDate_text;
    private EditText edit_month, edit_day;
    private RecyclerView content_display;
    private LinearLayout bg_linearLayout;
    private String image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_display_layout);
        initBinding(); //初始化绑定
        sendRequestsByOkHttp(); //以Okhttp的方式请求背景图

        sendRequests_today.setOnClickListener(this);
        sendRequests_custom_day.setOnClickListener(this);
        change_bg.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        content_display.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.inquire_today) {
            month = "auto";
            sendRequestsByHttpURLConnection();
        } else if (view.getId() == R.id.inquire_custom_day) {
            month = edit_month.getText().toString();
            day = edit_day.getText().toString();

            if (month.isEmpty() || day.isEmpty()) {
                Toast.makeText(this, "请完成日期填写后再点击查询！", Toast.LENGTH_SHORT).show();
            } else if (month.length() == 1 || day.length() == 1) {
                Toast.makeText(this, "请按给定格式正确地输入！！！", Toast.LENGTH_SHORT).show();
            } else {
                if (isInteger(month) && isInteger(day)) {
                    boolean signal;
                    switch (Integer.parseInt(month)) {
                        case 2:
                            signal = Integer.parseInt(day) > 0 && Integer.parseInt(day) < 29;
                            break;
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            signal = Integer.parseInt(day) > 0 && Integer.parseInt(day) < 31;
                            break;
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            signal = Integer.parseInt(day) > 0 && Integer.parseInt(day) < 32;
                            break;
                        default:
                            signal = false;
                            break;
                    }
                    if (signal) {
                        sendRequestsByHttpURLConnection();
                    } else {
                        Toast.makeText(this, "请输入正确的数字···", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请填入整数···", Toast.LENGTH_SHORT).show();
                }

            }
        } else if (view.getId() == R.id.change_bg) {
            Log.d("test", "get there!");
            Toast.makeText(this, "点击成功", Toast.LENGTH_SHORT).show();
            sendRequestsByOkHttp();
        }

    }

    // 使用Integer.parseInt()方法来分类选定字符串是否为整数
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //绑定区域
    public void initBinding() {
        sendRequests_today = findViewById(R.id.inquire_today);
        sendRequests_custom_day = findViewById(R.id.inquire_custom_day);
        response_text = findViewById(R.id.response_text);
        todayDate_text = findViewById(R.id.today_date);
        change_bg = findViewById(R.id.change_bg);
        edit_month = findViewById(R.id.input_month);
        edit_day = findViewById(R.id.input_day);
        content_display = findViewById(R.id.content_area);
        bg_linearLayout = findViewById(R.id.main_layout_bg);
    }

    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    private String month;
    private String day;

    private void sendRequestsByHttpURLConnection() {
        //新开一个线程用于发出网络请求
        new Thread(() -> {
            if (month.equals("auto")) {
                //查询当天
                String url_2 = "https://api.yum6.cn/briefing/baidu.php?format=json";
                URL_REQUEST(url_2, "GET", "HttpURLConnection");
            } else if (Integer.parseInt(month) > 0 && Integer.parseInt(month) < 13) {
                //查询自定义日期
                String url_1 = "https://baike.baidu.com/cms/home/eventsOnHistory/" + month + ".json";
                URL_REQUEST(url_1, "GET", "HttpURLConnection");
            } else {
                Looper.prepare();
                Toast.makeText(this, "请按给定格式正确地输入！！！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    private void sendRequestsByOkHttp() {
        //新开一个线程用于发出网络请求
        new Thread(() -> {
            String url = "https://img.xjh.me/random_img.php?return=url";
            URL_REQUEST(url, "GET", "Okhttp");
        }).start();
    }

    //get请求方法
    private void URL_REQUEST(String url_data, String method, String request_mode) {
        //Okhttp的请求方式
        if (request_mode.equals("Okhttp")) {
            Log.d("test", url_data);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url_data).build();
            Call call = client.newCall(request);
            //同步请求
            try {
                Response response = call.execute();
                String response_url = response.body() != null ? response.body().string() : null;
                image_url = "https:" + response_url;
//                ImageView storage_background = new ImageView(SomedayInHistoryActivity.this);
                setBackground(image_url);
            } catch (IOException e) {
                Log.e("error", "code is mistaking");
                e.printStackTrace();
            }
            //异步请求
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    Looper.prepare();
//                    Toast.makeText(SomedayInHistoryActivity.this, "背景图片申请失败！", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    if (response.isSuccessful())
//                        Log.d("test","请求响应成功");
//                    String response_url = response.body() != null ? response.body().string() : null;
//                    image_url = "https:" + response_url;
//                    Log.d("test", "info is" + image_url);
//                }
//            });
            Log.d("test", "url请求执行完成");
        }
        //HttpURLConnection的请求方式
        else if (request_mode.equals("HttpURLConnection")) {
            try {
                URL get_url = new URL(url_data);
                connection = (HttpURLConnection) get_url.openConnection();
                connection.setRequestMethod(method);
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();

                //读取数据
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line_data;
                while ((line_data = reader.readLine()) != null) {
                    response.append(line_data);
                }
                //测试数据是否正常
                Log.d("response", response.toString());
                showResponseView(response.toString());
            } catch (Exception error) {
                error.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

    }

    private void showResponseView(String response_raw_text) {
        //开启UI替换线程
        runOnUiThread(() -> {
            if (month.equals("auto")) {
                List<String> result_data = Parsing_jsonData(response_raw_text);
                String inquire_date = "今天是:\n" + result_data.get(1);
                List<String> result_content_data = result_data;

                Iterator<String> sListIterator = result_content_data.iterator();
                while (sListIterator.hasNext()) {
                    String str = sListIterator.next();
                    //数据中有“0”的全删了
                    if (str.contains("0")) {
                        sListIterator.remove();
                    }
                    /*
                    //数据是“0”和“1”的全删了
                    if(str.equals("0")||str.equals("1")){
                        sListIterator.remove();
                    }
                    */
                }

                todayDate_text.setText(inquire_date);

                ContentDisplayAdapter adapter = new ContentDisplayAdapter(result_content_data);
                content_display.setAdapter(adapter);
//                response_text.setText(thing_content == null ? null : thing_content.toString());
            } else if (Integer.parseInt(month) > 0 && Integer.parseInt(month) < 13) {
                List<String> result_data = GSON_parse(response_raw_text);
                String inquire_date = " ";

                todayDate_text.setText(inquire_date);

                ContentDisplayAdapter adapter = new ContentDisplayAdapter(result_data);
                content_display.setAdapter(adapter);
            } else {
                response_text.setText(response_raw_text);
            }
        });
    }

    // 使用JOSNObject方式解析json数据
    private List<String> Parsing_jsonData(String jsonData) {
        String jsonType = "null";
        List<String> return_data = new ArrayList<>();

        try {
            Object json_str = new JSONTokener(jsonData).nextValue();
            if (json_str instanceof JSONObject) {
                jsonType = "Object";
            } else if (json_str instanceof JSONArray) {
                jsonType = "Array";
            }
        } catch (JSONException e) {
            Log.d("dataType_test", "传入数据不是JSON");
            e.printStackTrace();
        }
        try {
            if (jsonType.equals("Object")) {
                JSONObject jsonObject = new JSONObject(jsonData);
                String status_code = jsonObject.getString("code");
                String today_date = jsonObject.getString("day");

                return_data.add(status_code);
                return_data.add(today_date);

                JSONArray thing_contents = jsonObject.getJSONArray("content");
                // 或者使用
                // JSONArray thing_contents = new JSONArray(jsonObject.getString("content"));
                for (int i = 0; i < thing_contents.length(); i++) {
                    String content_data = (String) thing_contents.get(i);
                    return_data.add(content_data);
                }
            } else if (jsonType.equals("Array")) {
                JSONArray jsonArray = new JSONArray(jsonData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return return_data;
    }

    // 使用GSON方式解析json数据
    private List<String> GSON_parse(String jsonData) {
        List<String> things_title = new ArrayList<>();
        Gson gson = new Gson();

        // 先将json转化Map，再对Map进行操作
        Type DateType = new TypeToken<Map<String, Map<String, List<Map<String, String>>>>>() {
        }.getType();
        Map<String, Map<String, List<Map<String, String>>>> SomeoneMonthDateMap = gson.fromJson(jsonData, DateType);
        for (Map.Entry<String, Map<String, List<Map<String, String>>>> month_obj : SomeoneMonthDateMap.entrySet()) {
            Map<String, List<Map<String, String>>> AMonthDateMap = SomeoneMonthDateMap.get(month_obj.getKey());
            if (AMonthDateMap == null) {
                continue;
            }
            int dayAccount = 0;  // 记录本月有多少天在历史上有故事
            for (Map.Entry<String, List<Map<String, String>>> date_obj : AMonthDateMap.entrySet()) {  // 从本月最后一天开始循环
                List<Map<String, String>> SomedayThingsList = AMonthDateMap.get(date_obj.getKey());
                dayAccount++;
                if (SomedayThingsList == null) {
                    continue;
                }
                // 留一个接口将来给RecycleView做点击事件
                for (int i = 0; i < SomedayThingsList.size(); i++) {
                    Map<String, String> everything = SomedayThingsList.get(i);
                }
            }

            String custom_date = month + day;
            List<Map<String, String>> CustomDayThingList = AMonthDateMap.get(custom_date);
            if (CustomDayThingList != null) {
                for (int i = 0; i < CustomDayThingList.size(); i++) {
                    Map<String, String> everything = CustomDayThingList.get(i);
                    things_title.add((String) everything.get("title"));
                }
            } else {
                Toast.makeText(this, "未请求到这一天的数据！", Toast.LENGTH_SHORT).show();
            }
        }

        /* // 通过建立DataTransmission(util)工具类建立起对json数据的映射，以达到利用的目的
         *  DataTransmission month_key = gson.fromJson(jsonData, DataTransmission.class);
         */

        return things_title;
    }

    private void setBackground(String url) {

        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                bg_linearLayout.setBackground(resource);
//                response_image.setImageDrawable(resource);
                Log.d("test", "背景设置成功");
            }
        };
        runOnUiThread(() -> {
            //Glide需要在主线程中调用
            Glide.with(this).load(url).into(simpleTarget);
        });

    }
}