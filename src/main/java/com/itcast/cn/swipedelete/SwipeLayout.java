package com.itcast.cn.swipedelete;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Created by 吴广东 on 2016/8/31.
 */
public class SwipeLayout extends FrameLayout {

    private View contentView;
    private View deleteView;
    private ViewDragHelper viewDrawHelper;
    private int touchslop;
    private float moveX;
    private float moveY;
    private long downTime;
    private float downX;
    private float downY;

    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    /**
     * 初始化控件
     */
    private void init() {
        viewDrawHelper = ViewDragHelper.create(this,callback);
        touchslop = ViewConfiguration.get(getContext()).getScaledEdgeSlop();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);

    }

    /**
     * 控件的摆放的位置
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);

        contentView.layout(0,0,contentView.getMeasuredWidth(),contentView.getMeasuredHeight());
        deleteView.layout(contentView.getMeasuredWidth(),0,contentView.getMeasuredWidth()+deleteView.getMeasuredWidth(),deleteView.getMeasuredHeight());

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //优先看子View是否拦截，如果不拦截，那就拦截，否则不拦截，传递给子View
        boolean result = viewDrawHelper.shouldInterceptTouchEvent(ev);

        //判断是否可以滑动
       if(!SwipeDeleteManager.create().isCanSwipe(SwipeLayout.this)){
           //先关闭已经打开的
           SwipeDeleteManager.create().closeLayout();
           result = true;
       }
        return result;


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //判断当前是否可以滑动
        if(!SwipeDeleteManager.create().isCanSwipe(SwipeLayout.this)){
            //再次请求listview不要拦截
            requestDisallowInterceptTouchEvent(true);
            return  true;

        }
        switch (event.getAction()){

            case  MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downTime = System.currentTimeMillis();
                break;
            case  MotionEvent.ACTION_HOVER_MOVE:
                //获取手指的移动的距离
                moveX = event.getX();
                moveY = event.getY();
                float dx = moveX- downX;
                float dy = moveY - downY;
                //判断dx和dy谁大
                if(Math.abs(dx)>Math.abs(dy)){
                    requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:
                float upDistancex = event.getX() - downX;
                float upDistancey = event.getY() - downY;
                float distance = (float) Math.sqrt(Math.pow(upDistancex,2)+Math.pow(upDistancey,2));
                long duration = System.currentTimeMillis() - downTime;
                //判断是否满足点击的条件
                if(duration <400 && distance <touchslop){
                //满足条件的时候
                    if(listener !=null){
                        listener.click();
                    }
            }
                break;

        }
        viewDrawHelper.processTouchEvent(event);




        return true;
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
      @Override
      public boolean tryCaptureView(View child, int pointerId) {



          return child == contentView ||child == deleteView;
      }

      @Override
      public int getViewHorizontalDragRange(View child) {
          return 1;
      }

      /**
       * 水平位置的修正
       * @param child
       * @param left
       * @param dx
       * @return
       */
      @Override
      public int clampViewPositionHorizontal(View child, int left, int dx) {
          //限制
          if(child == contentView){
              if(left > 0){
                  left = 0;
              }else if (left <-deleteView.getMeasuredWidth()){
                  left = -deleteView.getMeasuredWidth();
              }
          }else if(child == deleteView){
              //限制deleteView的操作
              if(left > contentView.getMeasuredWidth()){
                  left = contentView.getMeasuredWidth();
              }else  if (left <(contentView.getMeasuredWidth()-deleteView.getMeasuredWidth())){
                  left = (contentView.getMeasuredWidth()-deleteView.getMeasuredWidth());
              }

          }

          return left;
      }

      /**
       * 位置的改变的时候调用
       * @param changedView
       * @param left
       * @param top
       * @param dx
       * @param dy
       */
      @Override
      public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
          super.onViewPositionChanged(changedView, left, top, dx, dy);
          if(changedView == contentView){
              int newLeft = deleteView.getLeft() +dx;
              deleteView.layout(newLeft,0,newLeft+deleteView.getMeasuredWidth(),deleteView.getBottom());
          }else if(changedView == deleteView){
              int newLeft = contentView.getLeft() +dx;
              contentView.layout(newLeft,0,newLeft+contentView.getMeasuredWidth(),contentView.getBottom());
          }

          //判断是否是打开
          if(contentView.getLeft()<0){
              //说明打开了。需要记录一下
              SwipeDeleteManager.create().setSwipeLayout(SwipeLayout.this);
          }else if (changedView.getLeft() == 0){
              //说明是关闭了，需要清除一下
              SwipeDeleteManager.create().clearSwipeLayout();
          }
      }

      @Override
      public void onViewReleased(View releasedChild, float xvel, float yvel) {
          super.onViewReleased(releasedChild, xvel, yvel);
          if(contentView.getLeft() <-deleteView.getMeasuredWidth()/2){
              //打开
              open();
          }else{
              //关闭
              close();
          }
      }
  };

    /**
     * 打开的操作
     */
    public void open() {
        viewDrawHelper.smoothSlideViewTo(contentView,-deleteView.getMeasuredWidth(),0);
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);

    }

    /**
     * 关闭的操作
     */
    public void close() {
        viewDrawHelper.smoothSlideViewTo(contentView,0,0);
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDrawHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
    }
    private onSwipeLayoutClickListener listener;
    public void SetOnSwipeLayoutListener(onSwipeLayoutClickListener listener){
        this.listener = listener;
    }
    public interface  onSwipeLayoutClickListener {
        void click();
    }

    /**
     * 当VIew移除的时候执行
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //清除
        SwipeDeleteManager.create().clearSwipeLayout();
    }
}
