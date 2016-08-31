package com.itcast.cn.swipedelete;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        listview.setAdapter(new MyAdapter());
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                //要是触摸的话就关闭
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    //关闭当前打开的
                    SwipeDeleteManager.create().closeLayout();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View conterView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (conterView == null) {

                conterView = View.inflate(MainActivity.this, R.layout.adapter_list, null);
                holder = new ViewHolder(conterView);
                conterView.setTag(holder);

            }else {

                holder = (ViewHolder) conterView.getTag();
            }
            //数据的绑定
            holder.tvName.setText("数据"+position);
            holder.swipeLayout.SetOnSwipeLayoutListener(new SwipeLayout.onSwipeLayoutClickListener() {
                @Override
                public void click() {
                    Toast.makeText(MainActivity.this,""+position,Toast.LENGTH_SHORT).show();
                }
            });

            return conterView;
        }


    }
    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_delete)
        TextView tvDelete;
        @Bind(R.id.swipeLayout)
        SwipeLayout swipeLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
内容区的布局
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="80dp"
    android:paddingLeft="15dp"
    android:background="#33666666"
    android:gravity="center_vertical"
    android:orientation="horizontal" >
    
    <ImageView android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@mipmap/head_1"/>
    
    <TextView android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="#99000000"
        android:id="@+id/tv_name"
        android:layout_marginLeft="10dp"
        android:textSize="20sp"
        android:text="名称"/>

</LinearLayout>
删除区的布局
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="80dp"
    android:orientation="horizontal" >

    <TextView android:layout_width="100dp"
        android:layout_height="match_parent"
        android:textSize="18sp"
        android:textColor="#ffffff"
        android:gravity="center"
        android:background="#aa000000"
        android:text="Call"/>
    
    <TextView android:layout_width="100dp"
        android:layout_height="match_parent"
        android:textSize="18sp"
        android:textColor="#ffffff"
        android:id="@+id/tv_delete"
        android:gravity="center"
        android:background="#eeff0000"
        android:text="Delete"/>

</LinearLayout>
填充器的view的页面的布局
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">


    <com.wugaungdog.cn.swipedelete.SwipeLayout
        android:id="@+id/swipeLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <!--contentView的布局-->
        <include layout="@layout/layout_content"/>
        <!--deleteView的布局-->
        <include layout="@layout/layout_delete"/>

    </com.wugaungdong.cn.swipedelete.SwipeLayout>

</LinearLayout>
主页面的布局
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.itcast.cn.swipedelete.MainActivity">

    <ListView
        android:id="@+id/listview"
        android:splitMotionEvents="false"
        android:dividerHeight="1dp"
        android:divider="@color/colorPrimary"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</RelativeLayout>
