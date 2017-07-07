package com.ke.library;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;


public class StickyDecoration extends RecyclerView.ItemDecoration {
    private GroupCallback mGroupCallback;
    private TextPaint mTextPaint;
    private Paint mPaint;
    private int mGroupHeight;
    private int mLeftMargin;


    private StickyDecoration(GroupCallback groupCallback, int groupHeight, int leftMargin, int textColor, int groupBackgroundColor, float textSize) {
        mGroupCallback = groupCallback;
        mGroupHeight = groupHeight;
        mLeftMargin = leftMargin;

        mPaint = new Paint();
//        mPaint.setAlpha(0);
        mPaint.setColor(groupBackgroundColor);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);

        if (isFirstInGroup(position)) {
            //为分割线留出空间
            outRect.top = mGroupHeight;
        }
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getLeft() + parent.getPaddingLeft();
        int right = parent.getRight() - parent.getPaddingRight();
        String preGroupName;
        String currentGroupName = null;

        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);
            preGroupName = currentGroupName;
            currentGroupName = mGroupCallback.getGroupName(position);
            if (TextUtils.equals(currentGroupName, preGroupName))
                continue;
//            int viewBottom = childView.getBottom();
            float bottom = Math.max(mGroupHeight, childView.getTop());//top 决定当前顶部第一个悬浮Group的位置
            if (position + 1 < itemCount) {

                //获取下个GroupName
                String nextGroupName = mGroupCallback.getGroupName(position + 1);
                //下一组的第一个View接近头部
                int viewBottom = childView.getBottom();
                if (!currentGroupName.equals(nextGroupName) && viewBottom < bottom) {
                    bottom = viewBottom;
                }
            }

            //根据top绘制group
            c.drawRect(left, bottom - mGroupHeight, right, bottom, mPaint);
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            //文字竖直居中显示
            float baseLine = bottom - (mGroupHeight - (fm.bottom - fm.top)) / 2 - fm.bottom;
            c.drawText(currentGroupName, left + mLeftMargin, baseLine, mTextPaint);
        }

    }

    /**
     * 判断当前位置是不是分组的第一个
     */
    private boolean isFirstInGroup(int position) {
        if (position == 0) {
            return true;
        }
        //如果当前位置和上一个位置 分组名称不一样 就不是一个分组，当前位置就是分组的第一个
        String preGroupName = mGroupCallback.getGroupName(position - 1);
        String groupName = mGroupCallback.getGroupName(position);

        return !TextUtils.equals(preGroupName, groupName);
    }


    public static class Builder {
        private GroupCallback mGroupCallback;
        private int mTextColor;
        private int mGroupBackgroundColor;
        private int mGroupHeight;
        private float mTextSize;
        private int mTextMargin;

        public Builder(@NonNull GroupCallback groupCallback) {
            mGroupCallback = groupCallback;
        }

        public Builder setTextColor(@ColorInt int textColor) {
            mTextColor = textColor;
            return this;
        }

        public Builder setGroupBackgroundColor(@ColorInt int groupBackgroundColor) {
            mGroupBackgroundColor = groupBackgroundColor;
            return this;
        }

        public Builder setGroupHeight(@Size(min = 0) int groupHeight) {
            mGroupHeight = groupHeight;
            return this;
        }

        public Builder setTextMargin(@Size(min = 0) int textMargin) {
            mTextMargin = textMargin;
            return this;
        }

        public Builder setTextSize(@Size(min = 0) float textSize) {
            mTextSize = textSize;
            return this;
        }

        public StickyDecoration build() {
            return new StickyDecoration(mGroupCallback, mGroupHeight, mTextMargin, mTextColor, mGroupBackgroundColor, mTextSize);
        }
    }

    public interface GroupCallback {
        @NonNull
        String getGroupName(int position);

    }
}
