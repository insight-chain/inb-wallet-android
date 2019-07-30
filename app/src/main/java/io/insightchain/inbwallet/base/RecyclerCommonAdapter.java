package io.insightchain.inbwallet.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.utils.InterfaceUtils;

/**
 * Created by lijilong on 04/18.
 * RecyclerView通用适配器
 */

public abstract class RecyclerCommonAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private String TAG = "CommonAdapter";
    protected Context context;
    protected int layoutId;
    protected int loadmoreId = R.layout.layout_load_more;
    protected int HeadId = 0;
    protected List<T> listDatas;
    protected LayoutInflater inflater;
    protected int type;
    protected int status;
    protected final int VIEW_ITEM = 1;
    protected final int VIEW_LOAD_MORE = 0;
    protected final int VIEW_FIST_VIEW = 2;
    protected boolean isRank = false;//社区评级排行
    protected boolean isTotalRank = false;//是否为总排行
    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private InterfaceUtils.OnLoadMoreListener onLoadMoreListener;
    protected boolean hasMoreData = true;


    public RecyclerCommonAdapter(Context context, int layoutId, List<T> datas, int type, int status) {
        this.context = context;
        this.layoutId = layoutId;
        this.listDatas = datas;
        this.inflater = LayoutInflater.from(context);
        this.type = type;
        this.status = status;
    }

    public RecyclerCommonAdapter(Context context, int layoutId, final List<T> datas, RecyclerView recyclerView, int status) {
        this(context, layoutId, datas, recyclerView);
        this.status = status;
    }

    public RecyclerCommonAdapter(Context context, int layoutId, final List<T> datas, RecyclerView recyclerView) {
        this.context = context;
        this.layoutId = layoutId;
        this.listDatas = datas;
        this.inflater = LayoutInflater.from(context);
        Log.e(TAG, "RecyclerCommonAdapter(Context context, int layoutId, final List<T> datas, RecyclerView recyclerView)");
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (listDatas == null || listDatas.size() == 0 || dy == 0) {
                        Log.e(TAG, "listSize " + (listDatas == null ? "null" : listDatas.size()));
                        return;
                    }

//                    Log.e(TAG, "dx=" + dx + ",dy=" + dy);
//                    Log.e(TAG, "recyclerView.getChildCount()=" + recyclerView.getChildCount());
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
//                    Log.e(TAG, "loading=" + loading + ",totalItemCount=" + totalItemCount + ",lastVisibleItem=" + lastVisibleItem + ",visibleThreshold=" + visibleThreshold);
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder = null;
        if (viewType == VIEW_ITEM) {
            viewHolder = RecyclerViewHolder.get(context, parent, layoutId);
        } else if (viewType == VIEW_LOAD_MORE) {
            viewHolder = RecyclerViewHolder.get(context, parent, loadmoreId);
        } else if (viewType == VIEW_FIST_VIEW) {
            viewHolder = RecyclerViewHolder.get(context, parent, HeadId);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        if (HeadId != 0) {
            if(listDatas!=null && listDatas.size()!=0) {
                if (position == 0) {
                    convert(holder, listDatas.get(0));
                    convert(holder, listDatas.get(0), position);
                } else {
                    convert(holder, listDatas.get(position - 1));
                    convert(holder, listDatas.get(position - 1), position);
                }
            }
        } else {
            convert(holder, listDatas.get(position));
            convert(holder, listDatas.get(position), position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (HeadId != 0) {
            if (position == 0) {
                return VIEW_FIST_VIEW;
            } else {
                return listDatas.get(position - 1) != null ? VIEW_ITEM : VIEW_LOAD_MORE;
            }
        } else {
            return listDatas.get(position) != null ? VIEW_ITEM : VIEW_LOAD_MORE;
        }
    }

    public void setHeadView(int layout) {
        this.HeadId = layout;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (HeadId != 0) {
            count++;
        }
        return listDatas == null ? 0 : listDatas.size() + count;
    }

    public abstract void convert(RecyclerViewHolder holder, T t);

    public void convert(RecyclerViewHolder holder, T t, int position) {
    }

    ;

    //增加元素
    public void addData(T vo) {
        if (listDatas != null) {
            listDatas.add(vo);
            notifyDataSetChanged();
        }
    }

    public void addEmptyData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
        if (listDatas.size() == 0 || listDatas.get(listDatas.size() - 1) == null)
            return;
        listDatas.add(null);
        notifyItemInserted(listDatas.size() - 1);
    }

    public void removeEmptyData() {
        if (listDatas == null)
            return;
        int lastSize = listDatas.size();
        if (lastSize != 0 && listDatas.get(lastSize - 1) == null) {
            listDatas.remove(lastSize - 1);
            notifyItemRemoved(lastSize - 1);
        }
    }

    public void addDatas(List<T> vos) {
        if (listDatas == null)
            return;
        if (vos != null && vos.size() > 0) {
            listDatas.addAll(vos);
            notifyDataSetChanged();
        }
    }

    public void addData(T vo, int index) {
        if (listDatas != null) {
            listDatas.add(index, vo);
            notifyDataSetChanged();
        }
    }


    //重新设置元素列表
    public void setData(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        listDatas = list;
        notifyDataSetChanged();

    }

    public List<T> getData() {
        if (listDatas == null) {
            listDatas = new ArrayList<>();
        }
        return listDatas;
    }

    public T getItem(int position) {
        if (listDatas != null && listDatas.size() > 0) {
            return listDatas.get(position);
        }
        return null;
    }

    public void needRanking(boolean isRank) {
        this.isRank = isRank;
    }

    public void isTotallist(boolean isTotalRank) {
        this.isTotalRank = isTotalRank;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public interface OnMutilClickListener {
        void onClick(int position, int flag);
    }





    public void setLoaded() {
        loading = false;
        removeEmptyData();
    }


    public List<T> getListDatas() {
        return listDatas;
    }

    public void setListDatas(List<T> listDatas) {
        this.listDatas = listDatas;
    }

    public void addTag(String msg) {
        TAG = TAG + "->" + msg;
    }

    public void setOnLoadMoreListener(InterfaceUtils.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
}
