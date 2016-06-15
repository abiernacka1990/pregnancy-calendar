package biernacka.pregnancycalendar.ui.decorators;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import biernacka.pregnancycalendar.R;

/**
 * Created by agnieszka on 23.03.16.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DividerItemDecoration(Resources resources) {
        mDivider = ResourcesCompat.getDrawable(resources, R.drawable.divider, null);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parentView, RecyclerView.State state) {
        super.onDrawOver(canvas, parentView, state);
        int paddingLeft = parentView.getPaddingLeft();
        int paddingRight = parentView.getWidth() - parentView.getPaddingRight();
        int childCount = parentView.getChildCount();
        int lastItemWithDivider = childCount - 1;

        for (int i = 0; i < lastItemWithDivider; i++) {
            drawDivider(canvas, parentView.getChildAt(i), paddingLeft, paddingRight);
        }
    }

    private void drawDivider(Canvas canvas, View child, int paddingLeft, int paddingRight) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int top = child.getBottom() + params.bottomMargin;
        int bottom = top + mDivider.getIntrinsicHeight();

        mDivider.setBounds(paddingLeft, top, paddingRight, bottom);
        mDivider.draw(canvas);
    }
}
