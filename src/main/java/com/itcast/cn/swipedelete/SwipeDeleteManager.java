package com.itcast.cn.swipedelete;

import android.os.Bundle;

/**
 * Created by 吴广东 on 2016/8/31.
 */
public class SwipeDeleteManager {
    private static SwipeDeleteManager mInstance = new SwipeDeleteManager();
    private SwipeLayout openedLayout;

    private SwipeDeleteManager(){

    }
    public static SwipeDeleteManager create(){
        return mInstance;
    }

    /**
     * 记录打开的条目
     * @param layout
     */
    public  void  setSwipeLayout(SwipeLayout layout){
        this.openedLayout = layout;
    }

    public void  clearSwipeLayout(){
        openedLayout = null;
    }

    public boolean isCanSwipe(SwipeLayout touchLayout){
        if(openedLayout == null){
            return  true;
        }else{
            return  openedLayout == touchLayout;
        }


    }
    public  void closeLayout(){

        if(openedLayout!= null){
            openedLayout.close();
        }
    }
}
