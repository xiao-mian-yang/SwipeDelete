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
