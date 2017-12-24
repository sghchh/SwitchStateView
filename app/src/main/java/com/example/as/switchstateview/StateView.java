package com.example.as.switchstateview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by as on 2017/12/24.
 */

public class StateView extends View {

    private StateView stateView;
    //默认状态下的颜色
    private static final int STATE_COLOR= Color.parseColor("#ff008800");
    //当前状态位CLOSE时小圆的颜色
    private static final int CLOSE_COLOR=Color.parseColor("#ff233234");

    private final float CX_OPEN=136;
    private final float CX_CLOSE=40;
    private final int defWidth=176;   //默认宽度176px
    private final int defHeight=80;   //默认的高度为80px
    private final float defRadius=30;    //默认的小圆的半径为30px
    private State mState;    //当前的状态
    //设置的颜色
    private int stateColor;
    //画小圆的画笔
    private Paint circlePaint;
    //画外围的画笔
    private Paint mPaint;

    //点击事件监听器
    private OnClickListener listener;
    private ObjectAnimator animator;     //平移的动画
    private float cx;    //内部那个小圆的圆心的横坐标

    public StateView(Context context) {
        this(context,null,0);
        stateView=this;
    }

    public StateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        stateView=this;
    }

    public StateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);

        stateView=this;
        initParams(context,attrs);
        initPaints();
    }

    /**
     * 初始化XML属性
     * @param context 上下文对象
     * @param attributeSet 集合
     */
    private void initParams(Context context,AttributeSet attributeSet)
    {
        TypedArray typedArray=context.obtainStyledAttributes(attributeSet,R.styleable.StateView);
        stateColor=typedArray.getColor(R.styleable.StateView_state_color,STATE_COLOR);
        int sta=typedArray.getInt(R.styleable.StateView_state,1);
        switch (sta)
        {
            case 0:
                mState= State.OPEN;
                break;
            case 1:
                mState= State.CLOSE;
                break;
        }
        cx=typedArray.getDimension(R.styleable.StateView_cx,CX_CLOSE);
    }

    /**
     * 初始化画笔
     */
    private void initPaints()
    {
        circlePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(stateColor);

        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }


    public void initListener()
    {
        listener=new OnClickListener() {
            @Override
            public void onClick(View v) {
                float start=getCx();
                if(mState== State.CLOSE)
                    animator=ObjectAnimator.ofFloat(stateView,"cx",CX_OPEN);
                else
                    animator=ObjectAnimator.ofFloat(stateView,"cx",CX_CLOSE);
                animator.start();
                switchState();
            }
        };
        this.setOnClickListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(defWidth,defHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect=canvas.getClipBounds();
        canvas.drawRoundRect(0,0,defWidth,defHeight,defHeight/2,defHeight/2,mPaint);
        if(mState== State.CLOSE)
        {
            circlePaint.setColor(CLOSE_COLOR);
            canvas.drawCircle(cx,defHeight/2,defRadius,circlePaint);
        }
        else
        {
            circlePaint.setColor(stateColor);
            canvas.drawCircle(cx,defHeight/2,defRadius,circlePaint);
        }
    }

    public void setCx(float cx)
    {
        this.cx=cx;
        invalidate();
    }

    public float getCx()
    {
        return this.cx;
    }

    /**
     * 切换当前的状态
     */
    private void switchState()
    {
        mState=mState== State.OPEN? State.CLOSE: State.OPEN;
        if(mState== State.OPEN)
            cx=CX_OPEN;
        else
            cx=CX_CLOSE;
    }
    /**
     * 内部枚举类
     * 代表了两种状态
     */
    private enum State{
        OPEN,CLOSE;
    }
}
