package edu.hrbeu.myweather;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.view.GestureDetector;


public class SlideMenu extends ViewGroup {

        private Scroller scroller;//用于模拟数据
        private final int MENU = 0;//侧边栏
        private final int MAIN = 1;//主界面
        private int currentScreen = MENU;//当前界面，默认为主界面
        private int startx;//手指按下时，第一个点距离屏幕左边缘的距离
        //带两个参数的构造函数，可以布局文件中使用
        public SlideMenu(Context context, AttributeSet attrs) {
                super(context, attrs);
                scroller = new Scroller(context);//初始化
        }

        //因为这个自定义控件是继承的ViewGroup  在布局文件中又包含了两个子布局
        //因此在显示的时候，需要先测量，然后在布局，然后才能显示出来
        //该方法就是测量子布局的方法
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                // TODO Auto-generated method stub
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                
                View menu = getChildAt(0);//找到侧边栏
                menu.measure(menu.getLayoutParams().width, heightMeasureSpec);//测量，参数一是宽度，在这里是获得子布局文件中写死的那个宽度
                View main = getChildAt(1);//获得主界面
                main.measure(widthMeasureSpec, heightMeasureSpec);//主界面宽高设为与父布局宽高一致
        }
        
        //将两个子布局进行布局
        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
                // TODO Auto-generated method stub
                View menu = getChildAt(0);//得到侧边栏
                menu.layout(-menu.getLayoutParams().width, t, 0, b);//设置控件的四边，侧边栏默认设置为显示在屏幕的左边缘外
                View main = getChildAt(1);//得到主界面
                main.layout(l, t, r, b);//主界面默认填充屏幕
        }

        //触摸事件
        @Override
        public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                        startx = (int) event.getX();//记录手指按下时，点到屏幕左边的距离
                        break;
                case MotionEvent.ACTION_MOVE:
                        int movex = (int) event.getX();//移动后，手指点到屏幕左边的距离
                        int diffx = startx - movex;//屏幕左边的偏移量
                        int newscrollx =getScrollX()+diffx;//偏移后
                        if(newscrollx>0){
                                scrollTo(0, 0);//如果屏幕左边超过了主界面左边，那么让屏幕左边与主界面重合
                        }else if(newscrollx<-getChildAt(0).getWidth()){
                                scrollTo(-getChildAt(0).getWidth(), 0);//如果屏幕左边超过了侧边栏左边，那么让屏幕左边与侧边栏左边重合
                        }
                        scrollBy(diffx, 0);//持续偏移
                        startx = movex;
                        break;
                case MotionEvent.ACTION_UP:
                        int scrollx = getScrollX();//屏幕左边距离主界面左边的距离，屏幕左边在主界面左边的左边，为负值
                        if(scrollx>-getChildAt(0).getWidth()/2){
                                currentScreen = MAIN;//拖动屏幕不到侧边栏的一半时，放手，显示主界面
                                switchScreen();
                        }else if(scrollx<-getChildAt(0).getWidth()/2){
                                currentScreen = MENU;//拖动屏幕超过了侧边栏的一般，放手，显示侧边栏
                                switchScreen();
                        }
                        break;

                default:
                        break;
                }
                return true;
        }
        
        //切换显示侧边栏和主界面
        private void switchScreen() {
                int dx = 0 ;
                int startX = getScrollX();//获得屏幕左边距离主界面左边的距离
                if(currentScreen == MAIN){//切换到主界面
                        dx = 0 - getScrollX();//目标是将屏幕左边与主界面左边重合
                }else if(currentScreen == MENU){
                        dx = -getChildAt(0).getWidth()-getScrollX();//目标是将屏幕左边与侧边栏的左边重合
                }
                //模拟数据，该方法不会真正的去执行，只是模拟
                scroller.startScroll(startX, 0, dx, 0, Math.abs(dx)*5);
                invalidate();//调用computeScroll()
        }
        
        //invalidate()的最终的调用方法就是computeScroll()   因此需要重写该方法
        @Override
        public void computeScroll() {
                if(scroller.computeScrollOffset()){//如果还在进行数据模拟
                        scrollTo(scroller.getCurrX(), 0);//getCurrX()方法作用是，获得模拟数据时的移动路径的点
                        invalidate();//只要在进行数据模拟，那么就继续调用computeScroll()方法，类似于递归
                }
        }
        //判断当前显示的是不是侧边栏
        public boolean isMenuShow() {
                // TODO Auto-generated method stub
                return currentScreen == MENU;
        }

        //隐藏侧边栏
        public void hideMenu() {
                // TODO Auto-generated method stub
                currentScreen = MAIN;
                switchScreen();
        }

        //显示侧边栏
        public void showMenu() {
                // TODO Auto-generated method stub
                currentScreen = MENU;
                switchScreen();
        }

        
}

