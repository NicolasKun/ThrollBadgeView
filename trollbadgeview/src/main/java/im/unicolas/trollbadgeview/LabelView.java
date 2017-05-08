package im.unicolas.trollbadgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.io.File;

/**
 * Created by qq923 on 2017/4/11.
 */

public class LabelView extends View {

    private static final String TAG = "LabelView";

    /** 角标依附模式  文字 */
    public static final int LABEL_MODE_TEXT = 0x00;

    /** 角标依附模式  图片 */
    public static final int LABEL_MODE_IMG = 0x01;

    private String word = "";  //文字
    private String labelNum;  //角标内字符
    private Paint cPaint;  //角标画笔
    private Paint wPaint;  //文字画笔
    private Paint labelNumPaint;  //角标内文字画笔
    private Rect wRect;   //文字的矩形范围
    private Rect labelNumRect;  //角标内文字的矩形范围

    private boolean labelViewVisible = false;  //角标是否显示
    private int wordSize = sp2px(14);    //文字大小

    private int color_word = 0xffbdbdbd;  //文字颜色
    private int color_label_bg = 0xffef4836;  //角标颜色

    /** 角标依附模式 默认文字 */
    private int labelMode = LABEL_MODE_TEXT;

    private Bitmap bitmap4Icon = null;
    private float density;

    public LabelView(Context context) {
        super(context);
        init(context);
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        wPaint = new Paint();
        wPaint.setAntiAlias(true);
        labelNumPaint = new Paint();
        labelNumPaint.setColor(0xffffffff);
        labelNumPaint.setAntiAlias(true);
        labelNumPaint.setTextSize(sp2px(11));

        wRect = new Rect();
        labelNumRect = new Rect();

        density = dpi();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        cPaint.setColor(color_label_bg);
        wPaint.setColor(color_word);
        wPaint.setTextSize(wordSize);

        //基础数据
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();
        int left = getLeft();
        int radius = 0;  //角标的半径

        float basicValue = 0;
        if (density >= 2 && density < 3) {
            basicValue = 2.5f;
            radius = dp2px(labelNum == null ? 4 : 5.5f);
        }else if (density >= 3) {
            radius = dp2px(labelNum == null ? 4 : 5);
            basicValue = 2f;
        }

        //确定view的中心点
        int centerX = (right - left) / 2;
        int centerY = (bottom - top) / 2;

        //测量文字的宽高
        wPaint.getTextBounds(word, 0, word.length(), wRect);
        int width = wRect.width();  //字符串 矩形宽度
        int height = wRect.height();  //字符串 矩形高度

        //确定主文字的绘图中心
        centerX = centerX - width / 2;  //确定文字的中心  防止偏移
        centerY = centerY + height / 2;

        //偏移量  (通用
        int offset = 0;
        if (labelNum == null) {
            offset = 2;
        } else {
            if (labelNum.length() == 1)
                offset = 2;
            else if (labelNum.length() == 2)
                offset = 3;
            else
                offset = 6;
        }
        //第一个红圆的圆心坐标
        int circleX = centerX + width + ((labelNum != null && labelNum.length() == 3) ? (radius - dp2px(2)) : radius);   //字符串最右的 x轴坐标

        int circleY = centerY - height + dp2px(1);  //字符串最右的 y轴坐标

        if (labelMode == LABEL_MODE_TEXT) {
            canvas.drawText(word, centerX, centerY, wPaint);  //画文字
        } else {
            if (bitmap4Icon == null)
                throw new NullPointerException("bitmap cannot be NULL!!!");
            //图片宽高
            int imgWidth = bitmap4Icon.getWidth();
            int imgHeight = bitmap4Icon.getHeight();
            //图片绘制原点   (覆盖
            centerX = centerX - imgWidth / 2;
            centerY = centerY - imgHeight / 2;
            //若绘制图片则重新计算第一个红圆的圆心
            if (labelNum == null) {
                offset -= 2;
            } else {
                int length = labelNum.length();
                if (length == 1 || length == 2)
                    offset -= -1;
                else
                    offset -= -3;
            }
            circleX = centerX + imgWidth - dp2px(offset);
            circleY = centerY + dp2px(2);
            canvas.drawBitmap(bitmap4Icon, centerX, centerY, wPaint);
        }

        //false则角标不出现
        if (!labelViewVisible) return;

        //画第一个园
        canvas.drawCircle(circleX, circleY, radius, cPaint);
        if (labelNum != null) {
            labelNumPaint.getTextBounds(labelNum, 0, labelNum.length(), labelNumRect);

            //测量角标内字符的宽高
            int labelRWidth = labelNumRect.width();

            //确定中心矩形的右边位置
            int newRectRight = circleX + labelRWidth;

            //测量中心矩形top 以及 bottom
            int rectTop = circleY - radius;
            int rectBottom = circleY + radius;

            //根据字符长度绘制不同图形
            labelNumPaint.setTextSize(sp2px(density >= 3 ? 9 : 8));
            labelNumPaint.setFakeBoldText(true);  //加粗
            if (labelNum.length() == 1) {
                newRectRight -= dp2px(4);  //数字越大  就越短
                canvas.drawRect(circleX, rectTop, newRectRight, rectBottom, cPaint);
                canvas.drawCircle(newRectRight, rectTop + (rectBottom - rectTop) / 2, radius, cPaint);  //画第二个圆

                canvas.drawText(labelNum, circleX - dp2px(basicValue) + (density == 3 ? 1 : 1), rectBottom - dp2px(basicValue), labelNumPaint);
            } else {
                newRectRight -= dp2px(labelNum.length() == 2 ? 4 : 5);
                canvas.drawRect(circleX, rectTop, newRectRight, rectBottom, cPaint);
                canvas.drawCircle(newRectRight, rectTop + (rectBottom - rectTop) / 2, radius, cPaint);  //画第二个圆

                canvas.drawText(labelNum, circleX - dp2px(labelNum.length() == 3 ? basicValue : basicValue), rectBottom - dp2px(basicValue), labelNumPaint);
            }
        }
    }

    //设置角标是否显示
    public void setLabelViewVisiable(boolean visiable) {
        labelViewVisible = visiable;
        invalidate();
    }

    //设置文字显示
    public void setWordShow(String word) {
        this.word = word;
        invalidate();
    }

    //设置角标字符
    public void setLabelNum(String num) {
        this.labelNum = num;
        invalidate();
    }

    //设置文字大小
    public void setWordSize(int spSize) {
        this.wordSize = spSize;
        invalidate();
    }

    //设置文字颜色
    public void setWordColor(int color) {
        this.color_word = color;
        invalidate();
    }

    //设置角标背景
    public void setLabelBg(int color) {
        this.color_label_bg = color;
        invalidate();
    }

    /**
     * 角标依附模式
     * {@link LabelView#LABEL_MODE_TEXT}  文字
     * {@link LabelView#LABEL_MODE_IMG}   图片
     *
     * * 默认文字
     * @param mode  模式设置
     */
    public void setLabelMode(int mode) {
        this.labelMode = mode;
    }

    public void setBitmap4Icon(Bitmap bitmap) {
        this.bitmap4Icon = bitmap;
        invalidate();
    }

    public void setBitmap4Icon(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        setBitmap4Icon(bitmap);
    }

    public void setBitmap4Icon(String imgPath) {
        if (!new File(imgPath).exists()) {
            throw new IllegalArgumentException("cannot found this file at " + imgPath);
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        setBitmap4Icon(bitmap);
    }

    //返回角标是否显示
    public boolean isLabelViewVisiable() {
        return this.labelViewVisible;
    }

    //返回文字
    public String getWord() {
        return this.word;
    }

    //返回角标内字符
    public String getLabelNum() {
        return this.labelNum;
    }

    int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, getResources().getDisplayMetrics());
    }

    int sp2px(float sp) {
        return (int) (sp * (getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    float dpi() {
        return getResources().getDisplayMetrics().density;
    }

}
