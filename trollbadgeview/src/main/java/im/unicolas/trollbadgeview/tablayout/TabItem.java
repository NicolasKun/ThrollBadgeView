package im.unicolas.trollbadgeview.tablayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

import im.unicolas.trollbadgeview.R;

/**
 * Created by qq923 on 2017/4/10.
 */

public class TabItem extends View {
    final CharSequence mText;
    final Drawable mIcon;
    final int mCustomLayout;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.TabItem);
        mText = a.getText(R.styleable.TabItem_troll_text);
        mIcon = a.getDrawable(R.styleable.TabItem_troll_icon);
        mCustomLayout = a.getResourceId(R.styleable.TabItem_troll_layout, 0);
        a.recycle();
    }
}
