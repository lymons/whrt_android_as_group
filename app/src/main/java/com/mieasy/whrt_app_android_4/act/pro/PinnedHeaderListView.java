package com.mieasy.whrt_app_android_4.act.pro;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by alan on 16-11-22.
 * 二级列表的listview
 */
public class PinnedHeaderListView extends ListView implements AbsListView.OnScrollListener {

    private OnScrollListener mOnScrollListener;

    public static interface PinnedSectionedHeaderAdapter {
        public boolean isSectionHeader(int position);//是否是组的头部

        public int getSectionForPosition(int position);//根据位置判断对应的组号

        public View getSectionHeaderView(int section, View convertView, ViewGroup parent);

        public int getSectionHeaderViewType(int section);

        public int getCount();

    }

    private PinnedSectionedHeaderAdapter mAdapter;
    private View mCurrentHeader;
    private int mCurrentHeaderViewType = 0;
    private float mHeaderOffset;
    private boolean mShouldPin = true;
    private int mCurrentSection = 0;
    private int mWidthMode;
    private int mHeightMode;

    public PinnedHeaderListView(Context context) {
        super(context);
        super.setOnScrollListener(this);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnScrollListener(this);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOnScrollListener(this);
    }

    public void setPinHeaders(boolean shouldPin) {
        mShouldPin = shouldPin;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        mCurrentHeader = null;
        mAdapter = (PinnedSectionedHeaderAdapter) adapter;
        super.setAdapter(adapter);
    }

    //头部什么时候显示和什么时候不显示
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (mAdapter == null || mAdapter.getCount() == 0 || !mShouldPin || (firstVisibleItem < getHeaderViewsCount())) {
            mCurrentHeader = null;
            mHeaderOffset = 0.0f;
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                View header = getChildAt(i);
                if (header != null) {
                    header.setVisibility(VISIBLE);
                }
            }
            return;
        }

        firstVisibleItem -= getHeaderViewsCount();

        int section = mAdapter.getSectionForPosition(firstVisibleItem);
        int viewType = mAdapter.getSectionHeaderViewType(section);
        mCurrentHeader = getSectionHeaderView(section, mCurrentHeaderViewType != viewType ? null : mCurrentHeader);
        ensurePinnedHeaderLayout(mCurrentHeader);
        mCurrentHeaderViewType = viewType;

        mHeaderOffset = 0.0f;

        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            if (mAdapter.isSectionHeader(i)) {
                View header = getChildAt(i - firstVisibleItem);
                float headerTop = header.getTop();
                float pinnedHeaderHeight = mCurrentHeader.getMeasuredHeight();
                header.setVisibility(VISIBLE);
                if (pinnedHeaderHeight >= headerTop && headerTop > 0) {   // 下一个组的头部快滑动到顶部，距离顶部的距离小于现在在顶部悬浮的head的高度了
                    mHeaderOffset = headerTop - header.getHeight();
                } else if (headerTop <= 0) {  //下一个组的头部滑动到了顶部了
                    header.setVisibility(INVISIBLE);
                }
            }
        }

        invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    private View getSectionHeaderView(int section, View oldView) {
        boolean shouldLayout = section != mCurrentSection || oldView == null;

        View view = mAdapter.getSectionHeaderView(section, oldView, this);
        if (shouldLayout) {
            ensurePinnedHeaderLayout(view);
            mCurrentSection = section;
        }
        return view;
    }

    private void ensurePinnedHeaderLayout(View header) {
        if (header.isLayoutRequested()) {
            int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), mWidthMode);

            int heightSpec;
            ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
            if (layoutParams != null && layoutParams.height > 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
            } else {
                heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }
            header.measure(widthSpec, heightSpec);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
        }
    }

    //通过得到当前应该悬浮的头部的view,调用listview的dispatchDraw方法，在Listview头部绘制悬浮头部
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mAdapter == null || !mShouldPin || mCurrentHeader == null)
            return;
        int saveCount = canvas.save();
        canvas.translate(0, mHeaderOffset);
        canvas.clipRect(0, 0, getWidth(), mCurrentHeader.getMeasuredHeight()); // needed
        mCurrentHeader.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
    }

    public void setOnItemClickListener(PinnedHeaderListView.OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public static abstract class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int rawPosition, long id) {
            SectionedBaseAdapter adapter;
            if (adapterView.getAdapter().getClass().equals(HeaderViewListAdapter.class)) {
                HeaderViewListAdapter wrapperAdapter = (HeaderViewListAdapter) adapterView.getAdapter();
                adapter = (SectionedBaseAdapter) wrapperAdapter.getWrappedAdapter();
            } else {
                adapter = (SectionedBaseAdapter) adapterView.getAdapter();
            }
            int section = adapter.getSectionForPosition(rawPosition);
            int position = adapter.getPositionInSectionForPosition(rawPosition);

            if (position == -1) {
                onSectionClick(adapterView, view, section, id);
            } else {
                onItemClick(adapterView, view, section, position, id);
            }
        }

        public abstract void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id);

        public abstract void onSectionClick(AdapterView<?> adapterView, View view, int section, long id);

    }
}
