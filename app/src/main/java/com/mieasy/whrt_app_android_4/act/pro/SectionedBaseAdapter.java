package com.mieasy.whrt_app_android_4.act.pro;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mieasy.whrt_app_android_4.act.pro.PinnedHeaderListView.PinnedSectionedHeaderAdapter;

/**
 * Created by alan on 16-11-22.
 * 主要是对PinnedHeaderLIstView的接口的实现
 *
 *
 */
public abstract class SectionedBaseAdapter extends BaseAdapter implements PinnedSectionedHeaderAdapter {

    private static int HEADER_VIEW_TYPE = 0;
    private static int ITEM_VIEW_TYPE = 0;

    private SparseArray<Integer> mSectionPositionCache;

    private SparseArray<Integer> mSectionCache;

    private SparseArray<Integer> mSectionCountCache;

    /**
     * 子item的数目
     */
    private int mCount;
    /**
     * 标题section的数量
     */
    private int mSectionCount;

    public SectionedBaseAdapter() {
        super();
        mSectionCache = new SparseArray<Integer>();
        mSectionPositionCache = new SparseArray<Integer>();
        mSectionCountCache = new SparseArray<Integer>();
        mCount = -1;
        mSectionCount = -1;
    }

    @Override
    public void notifyDataSetChanged() {
        mSectionCache.clear();
        mSectionPositionCache.clear();
        mSectionCountCache.clear();
        mCount = -1;
        mSectionCount = -1;
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        mSectionCache.clear();
        mSectionPositionCache.clear();
        mSectionCountCache.clear();
        mCount = -1;
        mSectionCount = -1;
        super.notifyDataSetInvalidated();
    }

    @Override
    public final int getCount() {
        if (mCount >= 0) {
            return mCount;
        }
        int count = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            count += internalGetCountForSection(i);//添加组内元素的个数
            count++;//添加组头部位
        }
        mCount = count;
        return count;
    }

    @Override
    public final Object getItem(int position) {
        return getItem(getSectionForPosition(position), getPositionInSectionForPosition(position));
    }

    @Override
    public final long getItemId(int position) {
        return getItemId(getSectionForPosition(position), getPositionInSectionForPosition(position));
    }

    /**
     * 根据position是不是sectionHeader,来判断是调用返回getSectionHeaderView，还是调用返回getItemView
     */
    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (isSectionHeader(position)) {
            return getSectionHeaderView(getSectionForPosition(position), convertView, parent);
        }
        return getItemView(getSectionForPosition(position), getPositionInSectionForPosition(position), convertView, parent);
    }

    @Override
    public final int getItemViewType(int position) {
        if (isSectionHeader(position)) {
            return getItemViewTypeCount() + getSectionHeaderViewType(getSectionForPosition(position));
        }
        return getItemViewType(getSectionForPosition(position), getPositionInSectionForPosition(position));
    }

    @Override
    public final int getViewTypeCount() {
        return getItemViewTypeCount() + getSectionHeaderViewTypeCount();
    }

    public final int getSectionForPosition(int position) {
        Integer cachedSection = mSectionCache.get(position);
        if (cachedSection != null) {
            return cachedSection;
        }
        int sectionStart = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            int sectionCount = internalGetCountForSection(i);
            int sectionEnd = sectionStart + sectionCount + 1;
            if (position >= sectionStart && position < sectionEnd) {
                mSectionCache.put(position, i);
                return i;
            }
            sectionStart = sectionEnd;
        }
        return 0;
    }

    //根据位置得到对应的组号
    public int getPositionInSectionForPosition(int position) {
        Integer cachedPosition = mSectionPositionCache.get(position);
        if (cachedPosition != null) {
            return cachedPosition;
        }
        int sectionStart = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            int sectionCount = internalGetCountForSection(i);
            int sectionEnd = sectionStart + sectionCount + 1;
            if (position >= sectionStart && position < sectionEnd) {
                int positionInSection = position - sectionStart - 1;
                mSectionPositionCache.put(position, positionInSection);
                return positionInSection;
            }
            sectionStart = sectionEnd;
        }
        return 0;
    }

    //是否是组的头部position
    public final boolean isSectionHeader(int position) {
        int sectionStart = 0;
        for (int i = 0; i < internalGetSectionCount(); i++) {
            if (position == sectionStart) {
                return true;
            } else if (position < sectionStart) {
                return false;
            }
            sectionStart += internalGetCountForSection(i) + 1;
        }
        return false;
    }

    public int getItemViewType(int section, int position) {
        return ITEM_VIEW_TYPE;
    }

    public int getItemViewTypeCount() {
        return 1;
    }

    public int getSectionHeaderViewType(int section) {
        return HEADER_VIEW_TYPE;
    }

    public int getSectionHeaderViewTypeCount() {
        return 1;
    }

    public abstract Object getItem(int section, int position);

    public abstract long getItemId(int section, int position);

    public abstract int getSectionCount();

    public abstract int getCountForSection(int section);

    public abstract View getItemView(int section, int position, View convertView, ViewGroup parent);

    public abstract View getSectionHeaderView(int section, View convertView, ViewGroup parent);

    private int internalGetCountForSection(int section) {
        Integer cachedSectionCount = mSectionCountCache.get(section);
        if (cachedSectionCount != null) {
            return cachedSectionCount;
        }
        int sectionCount = getCountForSection(section);
        mSectionCountCache.put(section, sectionCount);
        return sectionCount;
    }

    private int internalGetSectionCount() {
        if (mSectionCount >= 0) {
            return mSectionCount;
        }
        mSectionCount = getSectionCount();
        return mSectionCount;
    }

}
