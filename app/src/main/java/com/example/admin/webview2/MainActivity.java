package com.example.admin.webview2;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    private String webUrl="https://www.amazon.in/";
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    Button check;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       webView=(WebView)findViewById(R.id.webview_id);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);

        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout_error);
        check=(Button)findViewById(R.id.button_check_internet);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.refresh_bar);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(checkConnection())
                {
                    webView.reload();
                }
                else
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        checkConnection();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait....");




        //Page Redirect On Correct Application
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               view.loadUrl(url);
               return true;
            }
        });

        checkConnection();

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                setTitle("Loading...");
                progressDialog.show();

                if (newProgress==100)
                {
                    progressBar.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                    progressDialog.dismiss();
                }
                super.onProgressChanged(view,newProgress);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });
    }

    //Back button Go Back
    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
        {
            webView.goBack();
        }
        else
        super.onBackPressed();
    }

    public  boolean  checkConnection()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobiledata=connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);


        if(networkInfo.isConnected())
        {
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            return true;
        }
        else if(mobiledata.isConnected())
        {
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            return true;
        }
        else
        {
            webView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            return false;
        }

    }
}
