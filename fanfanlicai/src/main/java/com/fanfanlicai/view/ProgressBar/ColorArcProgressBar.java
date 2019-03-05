package com.fanfanlicai.view.ProgressBar;

import com.fanfanlicai.fanfanlicai.R;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * colorful arc progress bar
 * Created by shinelw on 12/4/15.
 */
public class ColorArcProgressBar extends View{

    private int mWidth;
    private int mHeight;
    private int diameter = 500;  //直径
    private float centerX;  //圆心X坐标
    private float centerY;  //圆心Y坐标

    private Paint bgPaint;//自己加的背景
    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;
    private Paint trianglePaint;//自己加的画三角形的Paint
    private Paint triangleTextPaint;//自己加的三角形上的利率文字的Paint
    private Matrix triangleMatrix;

    private RectF bgRect;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = 180;
    private float sweepAngle = 180;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(30);//日期文字大小
    private float hintSize = dipToPx(10);//5个特殊投资日期的文字大小
    private float curSpeedSize = dipToPx(25);//标题文字大小
    private int aniSpeed = 1000;//进度条动画时间
    private float longdegree = dipToPx(13);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);

    private String hintColor = "#FBAB77";
    private String longDegreeColor = "#BB4A00";
    private String shortDegreeColor = "#BB4A00";
    private String bgArcColor = "#FBAB77";//进度条背景颜色
    private String titleString;
    private String hintString;

    private boolean isShowCurrentSpeed = true;
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    
    
    
    private String value="";//值

    // sweepAngle / maxValues 的值
    private float k;
    
    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ColorArcProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
        int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2, color1);
        int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3, color1);
        colors = new int[]{color1, color2, color3, color3};

        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10));
        isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false);
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE );
        int height= (int) (longdegree + 3*progressWidth/4 + diameter/2 + DEGREE_PROGRESS_DISTANCE );
//        int height= (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {

//        diameter = 3 * getScreenWidth() / 5;
        diameter = 7 * getScreenWidth() / 10;
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //三角形triangle  #FE802C
        trianglePaint = new Paint();
        trianglePaint.setColor(Color.parseColor("#FE812F"));
        triangleMatrix = new Matrix();
        
        //三角形上的利率文字
        triangleTextPaint = new Paint();
        triangleTextPaint.setTextSize(hintSize);
        triangleTextPaint.setFakeBoldText(true); //true为粗体，false为非粗体
        triangleTextPaint.setColor(Color.parseColor("#FE812F"));
        triangleTextPaint.setTextAlign(Paint.Align.CENTER);
        
        //整个背景
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeWidth(bgArcWidth);
        bgPaint.setColor(Color.parseColor("#ffffff"));
        bgPaint.setStrokeCap(Paint.Cap.BUTT);
//        allArcPaint.setStrokeCap(Paint.Cap.ROUND);
        
        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.BUTT);
//        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.BUTT);
//        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.BLACK);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
    	
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);
        
        //画白色背景
    	canvas.drawArc(bgRect, startAngle, sweepAngle, true, bgPaint);

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);
        
        //自己画的三角形
        Path path = new Path();  
        path.moveTo(dipToPx(19), centerY);// 此点为多边形的起点  
        path.lineTo(dipToPx(15), centerY-dipToPx(2));  
        path.lineTo(dipToPx(15), centerY+dipToPx(2));  
        path.close(); // 使这些点构成封闭的多边形  
        triangleMatrix.setRotate(currentAngle, centerX, centerY);
        path.transform(triangleMatrix);
        canvas.drawPath(path, trianglePaint);
        
        //自己画的利率文字
        Path path2 = new Path(); 
        if (currentAngle<5) {
        	path2.moveTo(dipToPx(10), centerY+dipToPx(3));  
        	path2.lineTo(dipToPx(10), centerY-dipToPx(30));  
        	triangleMatrix.setRotate(0, centerX, centerY);
		}else if (currentAngle>172) {
			path2.moveTo(dipToPx(10), centerY+dipToPx(30));  
        	path2.lineTo(dipToPx(10), centerY-dipToPx(30));
        	triangleMatrix.setRotate(172, centerX, centerY);
		}else{
			path2.moveTo(dipToPx(10), centerY+dipToPx(30));  
			path2.lineTo(dipToPx(10), centerY-dipToPx(30));
			triangleMatrix.setRotate(currentAngle, centerX, centerY);
		}
        path2.transform(triangleMatrix);
        canvas.drawTextOnPath(value, path2, 5, 5, triangleTextPaint);
        
	    //设置渐变色
	    rotateMatrix.setRotate(130, centerX, centerY);
	    sweepGradient.setLocalMatrix(rotateMatrix);
	    progressPaint.setShader(sweepGradient);
        
        //当前进度
        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);

        if (isNeedContent) {
            canvas.drawText(String.format("%.0f", curValues), centerX, centerY + textSize / 3, vTextPaint);
        }
        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + 2 * textSize / 3, hintPaint);
        }
        if (isNeedTitle) {//饭碗名称
            canvas.drawText(titleString, centerX, centerY - textSize , curSpeedPaint);
//            canvas.drawText(titleString, centerX, centerY - 2 * textSize / 3, curSpeedPaint);
        }
        
        
        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 20; i++) {
                if (i > 5 && i < 15) {
                    canvas.rotate(18f, centerX, centerY);
                    continue;
                }
                //刻度的线宽
                if (i == 15||i == 17||i == 19||i == 1||i == 3||i == 5) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                  //刻度的线长
                    canvas.drawLine(centerX, centerY - diameter / 2 +progressWidth/2-1,
                    		centerX, centerY - diameter / 2 - progressWidth / 2, degreePaint);
                    //5个临界的投资日
                    if (i==15) {
                        canvas.drawText("30天", centerX+dipToPx(11), centerY - diameter / 2 +2*progressWidth+10 , hintPaint);
					}
                    if (i==17) {
                        canvas.drawText("60天", centerX, centerY - diameter / 2 +2*progressWidth+10 , hintPaint);
                    }
                    if (i==19) {
                        canvas.drawText("90天", centerX, centerY - diameter / 2 +2*progressWidth+10 , hintPaint);
                    }
                    if (i==1) {
                        canvas.drawText("180天", centerX, centerY - diameter / 2 +2*progressWidth+10 , hintPaint);
                    }
                    if (i==3) {
                        canvas.drawText("270天", centerX, centerY - diameter / 2 +2*progressWidth+10 , hintPaint);
                    }
                    if (i==5) {
                        canvas.drawText("360天", centerX-dipToPx(14), centerY - diameter / 2 +2*progressWidth+10 , hintPaint);
                    }
                } else {
                    degreePaint.setStrokeWidth(dipToPx(0.5f));
                  //刻度的线长
                    canvas.drawLine(centerX, centerY - diameter / 2 +progressWidth/2-1,
                    		centerX, centerY - diameter / 2 - progressWidth / 2, degreePaint);
                }
                //刻度的颜色
                degreePaint.setColor(Color.parseColor(shortDegreeColor));
                canvas.rotate(18f, centerX, centerY);
            }
        }

        
        invalidate();

    }

    /**
     * 设置最大值
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle/maxValues;
    }

    /**
     * 设置当前值
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }

        if (currentValues < 0) {
        	currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        if(currentValues==30){
            setAnimation(lastAngle, 0, aniSpeed);
        }
        if(currentValues==60){
            setAnimation(lastAngle, 36, aniSpeed);
        }
        if(currentValues==90){
            setAnimation(lastAngle, 72, aniSpeed);
        }
        if(currentValues==180){
            setAnimation(lastAngle, 108, aniSpeed);
        }
        if(currentValues==270){
            setAnimation(lastAngle, 144, aniSpeed);
        }
        if(currentValues==360){
            setAnimation(lastAngle, 180, aniSpeed);
        }
    }

    /**
     * 设置整个圆弧宽度
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置直径大小
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 设置标题
     * @param title
     */
    private void setTitle(String title){
        this.titleString = title;
    }

    /**
     * 设置是否显示标题
     * @param isNeedTitle
     */
    private void setIsNeedTitle(boolean isNeedTitle) {
        this.isNeedTitle = isNeedTitle;
    }

    /**
     * 设置是否显示单位文字
     * @param isNeedUnit
     */
    private void setIsNeedUnit(boolean isNeedUnit) {
        this.isNeedUnit = isNeedUnit;
    }

    /**
     * 设置是否显示外部刻度盘
     * @param isNeedDial
     */
    private void setIsNeedDial(boolean isNeedDial) {
        this.isNeedDial = isNeedDial;
    }

    /**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
     * 为进度设置动画
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle= (Float) animation.getAnimatedValue();
                curValues = currentAngle/k;
            }
        });
        progressAnimator.start();
        
    }

    /**
     * dip 转换成px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
