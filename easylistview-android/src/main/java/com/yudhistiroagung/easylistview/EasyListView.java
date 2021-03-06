package com.yudhistiroagung.easylistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * Created by yudhistiroagung on 12/24/17.
 */

public class EasyListView extends FrameLayout {

    private ViewType mType;
    private RecyclerView mListView;
    private EasyListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnScrollEndListener mOnScrolledListener;

    public EasyListView(@NonNull Context context) {
        super(context);
        init();
    }

    public EasyListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initType(context, attrs);
        init();
    }

    public EasyListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initType(context, attrs);
        init();
    }

    private void initType(Context mContext, AttributeSet attrs){
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.EasyListView, 0, 0);
        try {
            int type = ta.getInt(R.styleable.EasyListView_easyListViewType, 0);
            this.mType = ViewType.values()[type];
        } finally {
            ta.recycle();
        }
    }

    private void init(){
        View v = LayoutInflater.from(getContext()).inflate(R.layout.template_easy_list_view, this);
        mListView = v.findViewById(R.id.main_recycler_view);
        initRecyclerView();
    }

    private void initRecyclerView(){
        switch (this.mType){
            case GRID:
                mLayoutManager = new GridLayoutManager(getContext(), 2);
                mListView.addItemDecoration( new SpacesItemDecoration(16, 2) );
                break;
            case STAGGERED_GRID:
                mLayoutManager = new StaggeredGridLayoutManager(2, VERTICAL);
                mListView.addItemDecoration( new SpacesItemDecoration(16, 2) );
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getContext());
        }

        if (mAdapter == null)
            mAdapter = new EasyListAdapter(this.mType, this.mListView);

        mLayoutManager.setAutoMeasureEnabled(true);
        mListView.setLayoutManager(mLayoutManager);
        mListView.setAdapter(mAdapter);
        mListView.addOnScrollListener(mScrollListener);
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(dy > 0){
                int visibleCount = mListView.getChildCount();
                int totalItem = mLayoutManager.getItemCount();

                int firstVisibleCount = 0;
                if(mLayoutManager instanceof LinearLayoutManager){
                    firstVisibleCount = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                }else if (mLayoutManager instanceof GridLayoutManager){
                    firstVisibleCount = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                }else {
                    int[] firstVisibleItems = null;
                    firstVisibleItems = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0)
                        firstVisibleCount = firstVisibleItems[0];
                }

                if( (visibleCount + firstVisibleCount) >= totalItem){
                    if (mOnScrolledListener != null){
                        mOnScrolledListener.onScrollEnd();
                    }
                }
            }

        }
    };

    /**
     * replace list with new dataset
     * @param items
     */
    public void setListItems(List<? extends ListItem> items){
        mAdapter.setListItems(items);
    }

    /**
     * append list with new dataset
     * @param items a list of object that implements {@link ListItem}
     */
    public void addListItems(List<? extends ListItem> items){
        mAdapter.addListItems(items);
    }

    /**
     * add list with one new data
     * @param item an object that implements {@link ListItem}
     */
    public void addListItem(ListItem item){
        mAdapter.addListItem(item);
    }

    /**
     * add list with one new data, to specific position
     * @param position : position of the object should be placed
     * @param item an object that implements {@link ListItem}
     */
    public void addListItem(int position, ListItem item){
        mAdapter.addListItem(position, item);
    }

    /**
     * set click listener
     * @param listener {@link OnItemClickListener}
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mAdapter.setOnItemClickListener(listener);
    }

    /**
     * set scroll listener
     * @param listener
     */
    public void setOnScrollEndListener(OnScrollEndListener listener){
        this.mOnScrolledListener = listener;
    }

}
