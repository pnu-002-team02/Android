package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import com.pnuproject.travellog.R;

@SuppressLint("ValidFragment")
public class ArticleViewFragment extends Fragment {
    private String url;

    @SuppressLint("ValidFragment")
    public ArticleViewFragment(String url)
    {
        this.url = url;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_blogarticleview, container, false);

        ImageButton btnClose = (ImageButton)view.findViewById(R.id.btnclose_blogarticleview);
        WebView webView = (WebView)view.findViewById(R.id.webview_blogarticleview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
