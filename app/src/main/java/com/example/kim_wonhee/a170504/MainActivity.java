package com.example.kim_wonhee.a170504;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    ListView listView;
    EditText editText;
    Button button;
    ProgressDialog dialog;
    Animation anim;
    LinearLayout linearLayout;
    ArrayList<Site> sites = new ArrayList<Site>();
    SiteAdapter adapter;
    Handler handler = new Handler();

    int add = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Web");


        webView = (WebView) findViewById(R.id.webView);
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        //--- Go 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(editText.getText().toString());
            }
        });

        //--- Loading... 대화상자 보이기
        dialog = new ProgressDialog(this);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.setMessage("Loading...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) dialog.dismiss();
            }
        });

        //--- 애니메이션
        anim = AnimationUtils.loadAnimation(this, R.anim.top);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //--- listView

        adapter = new SiteAdapter(sites, this);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);

                dlg.setTitle("즐겨찾기 삭제");
                dlg.setIcon(R.drawable.bookmark);
                dlg.setMessage("삭제하겠습니까? ");
                dlg.setNegativeButton("취소", null);
                dlg.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sites.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "선택한 사이트가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                dlg.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                webView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                webView.loadUrl(sites.get(position).getUrl());
            }
        });

        //-- urladd.html 과의 통신

        webView.addJavascriptInterface(new JavaScriptMethods(), "myApp");



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "즐겨찾기 추가");
        menu.add(111, 2, 0, "즐겨찾기 목록");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==  1) {
            linearLayout.setAnimation(anim);
            anim.start();
            webView.loadUrl("file:///android_asset/www/urladd.html");
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            linearLayout.setAnimation(anim);
            anim.start();
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);

    }

    class JavaScriptMethods {

        @JavascriptInterface
        public void showUrl() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        @JavascriptInterface
        public void addBookMark(final String name, final String url) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < sites.size(); i++) {
                        String one = sites.get(i).getUrl().toString();
                        if (one.contains(url.toString())) {
                            add = 0;
                            break;
                        } else {
                            add = 1;
                        }
                    }
                    if (add == 0) {
                        webView.loadUrl("javascript:displayMsg()");
                        add = 0;
                    } else if (add == 1) {
                        webView.loadUrl("javascript:setMsg('즐겨찾기에 추가되었습니다')");
                        sites.add(new Site(name, url));
                        adapter.notifyDataSetChanged();
                        add = 0;
                    }
                }
            });
        }


    }
}
