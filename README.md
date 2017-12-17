# MultipleItemPage
RecyclerView多布局，“我的”、“个人中心” 页面经典写法

#### 效果：

![](https://github.com/yechaoa/MultipleItemPage/raw/master/gif/multiple_item_page.gif)
![](https://github.com/yechaoa/MultipleItemPage/raw/master/gif/multiple_item_page.png)


<br>

> 多布局的使用场景还是蛮多的，比如“首页”、“我的”等页面，早期的时候大家一般都是拼起来的，后来开始自定义ListView（支付宝现在的首页还是ListView），再到后来的RecyclerView。
> 
> 其实多布局都是一个套路，根据类型去引入layout，本文以RecyclerView为例，以BaseRecyclerViewAdapterHelper为辅演示多布局的写法。

<br>

### 1.添加依赖

> compile 'com.github.yechaoa:YUtils:2.0.6'
    compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.34'

YUtils是一个快速开发工具集合，感兴趣的可以戳 [YUtils](https://github.com/yechaoa/YUtils)

<br>

### 2.主页面

#### 主页面采用的是 ViewPager + BottomNavigationView

> 稍微过一下吧。。不想看的直接跳到第3步

<br>

#### 布局：

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:orientation="vertical"
    android:layout_height="match_parent"
    tools:context="com.yechaoa.multipleitempage.MainActivity">

    <android.support.v4.view.ViewPager
        android:id="@+id/viewPager"
        android:layout_weight="1"
        android:layout_width="match_parent"
        android:layout_height="0dp" />

    <android.support.design.widget.BottomNavigationView
        android:id="@+id/navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:background="?android:attr/windowBackground"
        app:menu="@menu/navigation" />

</LinearLayout>
```
<br>

#### 添加Listener让二者关联起来

```
mViewPager.addOnPageChangeListener(mOnPageChangeListener); 
mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
```

```
private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mNavigation.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_category:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_cart:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_my:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
```

> ViewPager选中的时候让BottomNavigationView的item也选中，BottomNavigationView的item选中的时候让ViewPager切换page

<br>

### 3.Fragment（“我的”页面）

#### 布局，SwipeRefreshLayout（下拉刷新）嵌套RecyclerView：

```
<android.support.v4.widget.SwipeRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/swipeRefreshLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</android.support.v4.widget.SwipeRefreshLayout>

```
<br>

#### 初始化Data

```
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
```
<br>

#### 获取id

```
mSwipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
mRecyclerView = getActivity().findViewById(R.id.recyclerView);
```
<br>

#### 初始化SwipeRefreshLayout

```
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
```
<br>

#### 初始化RecyclerView

```
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
```
<br>

#### 添加Header

```
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
```
<br>

#### 初始化Listener

```
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
```

> 如果考虑到在GridLayoutManager复用item问题可以配置setSpanSizeLookup
> 
> setOnItemClickListener，item点击事件
> 
> setOnItemChildClickListener，item里面的子view点击事件
> 
> item点击的时候，角标（徽章）消失，然后局部刷新

<br>

### 4.Bean

> 实体类必须实现MultiItemEntity，在设置数据的时候，需要给每一个数据设置itemType

```
public class MultipleItem implements MultiItemEntity {

    public static final int TYPE_COUNT = 1;
    public static final int TYPE_ORDER_HEADER = 2;
    public static final int TYPE_ORDER = 3;
    public static final int TYPE_BALANCE = 4;
    public static final int TYPE_TOOLS_HEADER = 5;
    public static final int TYPE_TOOLS = 6;
    private int itemType;
    private int spanSize;

    public MultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }


    public String mString1;

    public String mString2;

    public boolean isShow;

    public int count;

}
```
<br>

### 5.Adapter

> 多布局关键写法就在Adapter里面，根据类型返回不同的layout，然后填充数据、处理事件等等。
> 
> 在构造里面addItemType绑定type和layout的关系

```
public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.TYPE_COUNT, R.layout.layout_my_count);
        addItemType(MultipleItem.TYPE_ORDER_HEADER, R.layout.layout_my_order_header);
        addItemType(MultipleItem.TYPE_ORDER, R.layout.layout_my_order);
        addItemType(MultipleItem.TYPE_BALANCE, R.layout.layout_my_balance);
        addItemType(MultipleItem.TYPE_TOOLS_HEADER, R.layout.layout_my_tools_header);
        addItemType(MultipleItem.TYPE_TOOLS, R.layout.layout_my_tools);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TYPE_COUNT:
                helper.setText(R.id.my_favorites, item.mString1).addOnClickListener(R.id.my_favorites);
                helper.setText(R.id.my_bands, item.mString2).addOnClickListener(R.id.my_bands);
                break;
            case MultipleItem.TYPE_ORDER_HEADER:
                helper.addOnClickListener(R.id.ll_my_order);
                break;
            case MultipleItem.TYPE_ORDER:
                helper.setImageDrawable(R.id.my_order_image, ContextCompat.getDrawable(mContext, R.drawable.ic_launcher));
                helper.setText(R.id.my_order_name, item.mString1);
                if (item.isShow) {
                    helper.getView(R.id.my_order_count).setVisibility(View.VISIBLE);
                    if (item.count > 0) {
                        if (item.count < 99) {
                            helper.setText(R.id.my_order_count, String.valueOf(item.count));
                        } else {
                            helper.setText(R.id.my_order_count, String.valueOf("99+"));
                        }
                    } else {
                        helper.getView(R.id.my_order_count).setVisibility(View.GONE);
                    }
                } else {
                    helper.getView(R.id.my_order_count).setVisibility(View.GONE);
                }
                break;
            case MultipleItem.TYPE_BALANCE:
                helper.setText(R.id.my_balance_text, item.mString1);
                helper.addOnClickListener(R.id.my_balance_btn);
                break;
            case MultipleItem.TYPE_TOOLS_HEADER:
                //helper.setText(R.id.tv_item_name, item.mString1);
                break;
            case MultipleItem.TYPE_TOOLS:
                helper.setImageDrawable(R.id.my_tools_image, ContextCompat.getDrawable(mContext, R.drawable.ic_launcher));
                helper.setText(R.id.my_tools_text, item.mString1);
                if (item.isShow) {
                    helper.getView(R.id.my_tools_count).setVisibility(View.VISIBLE);
                    if (item.count > 0) {
                        if (item.count < 99) {
                            helper.setText(R.id.my_tools_count, String.valueOf(item.count));
                        } else {
                            helper.setText(R.id.my_tools_count, String.valueOf("99+"));
                        }
                    } else {
                        helper.getView(R.id.my_tools_count).setVisibility(View.GONE);
                    }
                } else {
                    helper.getView(R.id.my_tools_count).setVisibility(View.GONE);
                }
                break;
        }
    }
```

<br>

> item的layout就不用贴了吧，就一般的布局。。
> 
> 角标（徽章）的话没用BadgeView，只直接用ImageView写的。
> 
> Demo是从项目里抽出来的，好像也没什么要注意的了吧。。忘了。。

<br>


```
   Copyright 2017 yechaoa

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
```
