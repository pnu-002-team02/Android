package com.pnuproject.travellog.Main.MapFragment.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pnuproject.travellog.Main.MapFragment.Model.BlogArticleRetrofitInterface;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataBlog;
import com.pnuproject.travellog.R;
import com.pnuproject.travellog.etc.BlogArticleFilter;
import com.pnuproject.travellog.etc.RetrofitTask;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import retrofit2.Retrofit;

@SuppressLint("ValidFragment")
public class BlogArticleFragment extends Fragment implements AdapterView.OnItemClickListener, RetrofitTask.RetrofitExecutionHandler, View.OnClickListener {
    ListView lvArticle;
    BlogArticleLVAdapter articleLVAdapter;
    private String searchWord;
    final static private int RETROFIT_TASK_ERROR = 0x00;
    final static private int RETROFIT_TASK_GETARTICLE = 0x01;
    final static private int RETROFIT_TASK_GETARTICLE2 = 0x02;
    private RetrofitTask retrofitTask;

    @SuppressLint("ValidFragment")
    public BlogArticleFragment(String searchWord) {
        this.searchWord = searchWord;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_blogarticle, container, false);

        ImageButton btnClose = (ImageButton)view.findViewById(R.id.btnclose_blogarticle);
        retrofitTask = new RetrofitTask(this, "https://dapi.kakao.com");
        articleLVAdapter = new BlogArticleLVAdapter();

        lvArticle = (ListView) view.findViewById(R.id.lvBlogArticle_blogarticle);
        lvArticle.setOnItemClickListener(this);
        btnClose.setOnClickListener(this);
        lvArticle.setAdapter(articleLVAdapter);

        RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_GETARTICLE, searchWord);
        retrofitTask.execute(requestParam);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l_position) {
        if (adapterView.getAdapter() == articleLVAdapter) {
            String url = articleLVAdapter.getURL(position);
            if(url != null && !url.isEmpty()) {

                ArticleViewFragment articleViewFragment = new ArticleViewFragment(url);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.map_main_fragment, articleViewFragment);
                transaction.addToBackStack(null);
                transaction.commit();


                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(browserIntent);
            }
        }
    }

    @Override
    public void onAfterAyncExcute(RetrofitTask.RetrofitResponseParam response3) {
        if (response3 == null || response3.getResponse() == null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.errmsg_retrofit_unknown), Toast.LENGTH_SHORT).show();
                }
            });

            return;
        } else if( response3.getTaskNum() == RETROFIT_TASK_ERROR) {
            final String errMsg = (String)response3.getResponse();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getBaseContext(), errMsg, Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }

        int taskNum = response3.getTaskNum();
        Object responseData = response3.getResponse();

        switch (taskNum) {
            case RETROFIT_TASK_GETARTICLE:
                {
                final ResponseDataBlog res = (ResponseDataBlog) responseData;
                ArrayList<ResponseDataBlog.BlogDocuments> vBlogData = res.getDocuments();
                int blogDataSize = vBlogData.size();
                if(blogDataSize>0) {
                    BlogArticleFilter articleFilter = new BlogArticleFilter();
                    if( articleFilter.applyFilter(vBlogData) > 0 ) {
                        articleLVAdapter.setListItem(vBlogData);
                        articleLVAdapter.notifyDataSetChanged();
                    }

                    if(blogDataSize == 50) {
                        RetrofitTask.RetrofitRequestParam requestParam = new RetrofitTask.RetrofitRequestParam(RETROFIT_TASK_GETARTICLE2, searchWord);
                        retrofitTask.execute(requestParam);
                    }
                }
            }
            break;
            case RETROFIT_TASK_GETARTICLE2:
            {
                final ResponseDataBlog res = (ResponseDataBlog) responseData;
                ArrayList<ResponseDataBlog.BlogDocuments> vBlogData = res.getDocuments();
                if(vBlogData.size()>0) {
                    BlogArticleFilter articleFilter = new BlogArticleFilter();
                    if( articleFilter.applyFilter(vBlogData) > 0 ) {
                        articleLVAdapter.addListItem(vBlogData);
                        articleLVAdapter.notifyDataSetChanged();
                    }
                }
            }
            break;
        }
    }

    @Override
    public Object onBeforeAyncExcute(Retrofit retrofit, RetrofitTask.RetrofitRequestParam paramRequest4) {
        Object response = null;
        int taskNum = paramRequest4.getTaskNum();
        Object requestParam = paramRequest4.getParamRequest();
        BlogArticleRetrofitInterface getBlogArticleRetrofit = retrofit.create(BlogArticleRetrofitInterface.class);

        try {
            switch (taskNum) {
                case RETROFIT_TASK_GETARTICLE:
                    response = getBlogArticleRetrofit.getBlogData((String)requestParam,50,1).execute().body();
                    break;
                case RETROFIT_TASK_GETARTICLE2:
                    response = getBlogArticleRetrofit.getBlogData((String)requestParam,50,2).execute().body();
                    break;
                default:
                    break;
            }
        }
        catch (UnknownHostException ex) {
            paramRequest4.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_ownernetwork));
        }
        catch (ConnectException ex) {
            paramRequest4.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofitbefore_servernetwork));
        }
        catch (Exception ex) {
            paramRequest4.setTaskNum(RETROFIT_TASK_ERROR);
            response = new String(getResources().getString(R.string.errmsg_retrofit_unknown));
        }

        return response;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnclose_blogarticle:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            default:
                break;
        }
    }
}
class BlogArticleLVAdapter extends BaseAdapter {
    ArrayList<ResponseDataBlog.BlogDocuments> m_listItem;
    public BlogArticleLVAdapter() {
        m_listItem = new ArrayList<ResponseDataBlog.BlogDocuments>();
    }
    public BlogArticleLVAdapter(ArrayList<ResponseDataBlog.BlogDocuments> listItem) {
        this.m_listItem = listItem;
    }
    public void addListItem(ArrayList<ResponseDataBlog.BlogDocuments> listItem) {
        this.m_listItem.addAll(listItem);
    }

    public void setListItem(ArrayList<ResponseDataBlog.BlogDocuments> listItem) {
        this.m_listItem = listItem;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lvitem_blogarticle, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle_blogarticle);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvArticle_blogarticle);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage_blogarticle);

        ResponseDataBlog.BlogDocuments blogItem = m_listItem.get(position);
        tvTitle.setText(blogItem.getTitle());
        tvContent.setText(blogItem.getContents());
        if((blogItem.getThumbnail_url() != null && !blogItem.getThumbnail_url().isEmpty())) {
            Glide.with(context).load(blogItem.getThumbnail_url()).into(ivImage);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return m_listItem.size();
    }

    public String getURL(int position){
        return m_listItem.get(position).getUrl();
    }
    @Override
    public Object getItem(int position) {
        return m_listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
