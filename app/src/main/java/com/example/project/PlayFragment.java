package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_play, container, false);
        WebView newsView = root.findViewById(R.id.webview);
        newsView.getSettings().setJavaScriptEnabled(true);
        newsView.getSettings().setUseWideViewPort(true);
        newsView.getSettings().setDomStorageEnabled(true);
        newsView.getSettings().setAppCacheEnabled(true);
        newsView.getSettings().setLoadsImagesAutomatically(true);
        newsView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        newsView.loadUrl("https://isc.ro/");
        return root;
    }
}
