package com.yechaoa.multipleitempage.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yechaoa.multipleitempage.R;
import com.yechaoa.multipleitempage.adapter.MultipleItemQuickAdapter;
import com.yechaoa.multipleitempage.bean.MultipleItem;
import com.yechaoa.multipleitempage.widget.CircleImageView;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.YUtils;

import java.util.ArrayList;
import java.util.List;

public class Fragment4 extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private MultipleItem multipleItem = null;

    private List<MultipleItem> itemDataList;

    private MultipleItemQuickAdapter multipleItemQuickAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment4, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mSwipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = getActivity().findViewById(R.id.recyclerView);

        initSwipeRefreshLayout();

        initItemData();

        initRecyclerView();

        initListener();

    }


    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        multipleItemQuickAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        YUtils.showToast("刷新完成");
                    }
                }, 2000);
            }
        });
    }


    private void initItemData() {
        itemDataList = new ArrayList<>();

        multipleItem = new MultipleItem(MultipleItem.TYPE_COUNT, 5);
        multipleItem.mString1 = "收藏";
        multipleItem.mString2 = "关注";
        itemDataList.add(multipleItem);

        multipleItem = new MultipleItem(MultipleItem.TYPE_ORDER_HEADER, 5);
        multipleItem.mString2 = "type2";
        itemDataList.add(multipleItem);

        for (int i = 0; i < 5; i++) {
            multipleItem = new MultipleItem(MultipleItem.TYPE_ORDER, 1);
            multipleItem.mString1 = "待付款";
            if (i % 2 == 0) {
                multipleItem.isShow = true;
                multipleItem.count = 6;
            } else {
                multipleItem.isShow = false;
                multipleItem.count = 0;
            }
            itemDataList.add(multipleItem);
        }

        multipleItem = new MultipleItem(MultipleItem.TYPE_BALANCE, 5);
        multipleItem.mString1 = "￥9999.00";
        itemDataList.add(multipleItem);

        multipleItem = new MultipleItem(MultipleItem.TYPE_TOOLS_HEADER, 5);
        multipleItem.mString1 = "type5";
        itemDataList.add(multipleItem);

        for (int i = 0; i < 5; i++) {
            multipleItem = new MultipleItem(MultipleItem.TYPE_TOOLS, 1);
            multipleItem.mString1 = "使用帮助";
            if (i % 2 == 0) {
                multipleItem.isShow = true;
                multipleItem.count = 100;
            } else {
                multipleItem.isShow = false;
                multipleItem.count = 0;
            }
            itemDataList.add(multipleItem);
        }
    }


    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        multipleItemQuickAdapter = new MultipleItemQuickAdapter(itemDataList);

        View headerView = getHeaderView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.my_header_image:
                        YUtils.showToast("你点击了头像");
                        break;
                    case R.id.my_header_settings:
                        YUtils.showToast("你点击了设置");
                        break;
                }
            }
        });

        multipleItemQuickAdapter.addHeaderView(headerView);

        mRecyclerView.setAdapter(multipleItemQuickAdapter);
    }


    private View getHeaderView(View.OnClickListener listener) {
        View headerView = getLayoutInflater().inflate(R.layout.layout_my_header, (ViewGroup) mRecyclerView.getParent(), false);

        CircleImageView myHeaderImage = headerView.findViewById(R.id.my_header_image);
        myHeaderImage.setImageResource(R.drawable.header_image);
        myHeaderImage.setOnClickListener(listener);

        TextView myHeaderName = headerView.findViewById(R.id.my_header_name);
        myHeaderName.setText("名字");

        TextView myHeaderMobile = headerView.findViewById(R.id.my_header_mobile);
        myHeaderMobile.setText("手机号");

        ImageView myHeaderSettings = headerView.findViewById(R.id.my_header_settings);
        myHeaderSettings.setOnClickListener(listener);

        return headerView;
    }


    private void initListener() {
        multipleItemQuickAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return itemDataList.get(position).getSpanSize();
            }
        });

        multipleItemQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                YUtils.showToast("第  " + position);

                //可以再加一层 类型 的判断，一般来说订单不是点了就消失的
                if (itemDataList.get(position).getItemType() == MultipleItem.TYPE_TOOLS) {
                    if (itemDataList.get(position).isShow) {
                        itemDataList.get(position).isShow = false;
                        LogUtil.i("count  =  " + itemDataList.get(position).count);
                        multipleItemQuickAdapter.notifyItemChanged(position + 1);
                    } else
                        itemDataList.get(position).isShow = false;
                }

            }
        });

        multipleItemQuickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.my_favorites:
                        YUtils.showToast("收藏");
                        break;
                    case R.id.my_bands:
                        YUtils.showToast("关注");
                        break;
                    case R.id.ll_my_order:
                        YUtils.showToast("全部订单");
                        break;
                    case R.id.my_balance_btn:
                        YUtils.showToast("立即充值");
                        break;
                }
            }
        });

    }

}
