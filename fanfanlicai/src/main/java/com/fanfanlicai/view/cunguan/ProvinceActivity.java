package com.fanfanlicai.view.cunguan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fanfanlicai.activity.BaseActivity;
import com.fanfanlicai.fanfanlicai.R;
import com.fanfanlicai.sign.SignUtil;
import com.fanfanlicai.sign.SortRequestData;
import com.fanfanlicai.utils.CacheUtils;
import com.fanfanlicai.utils.ConstantUtils;
import com.fanfanlicai.utils.LogUtils;
import com.fanfanlicai.utils.ToastUtils;
import com.fanfanlicai.utils.volley.HttpBackBaseListener;
import com.fanfanlicai.utils.volley.VolleyUtil;
import com.fanfanlicai.view.dialog.CustomProgressDialog;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 7/27 0027.
 */
public class ProvinceActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private BaseRecyclerAdapter<Province> mAdapter;

    private TextView tvLetterHeader;
    private TextView tvCenter;

    private List<Province> mDatas = new ArrayList<Province>();

    private List<String> mLetterDatas = new ArrayList<String>();

    private boolean move;
    private int selectPosition = 0;

    private LetterNavigationView mNavigationView;

    public static final int HEADER_FIRST_VIEW = 0x00000111;
    public static final int HEADER_VISIBLE_VIEW = 0x00000222;
    public static final int HEADER_NONE_VIEW = 0x00000333;
    private CustomProgressDialog progressdialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_province);

        progressdialog = new CustomProgressDialog(this, "正在获取数据...");

        TextView get_back = (TextView) findViewById(R.id.get_back);
        get_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCenter = (TextView) findViewById(R.id.tv_letter_hide);
        mNavigationView = (LetterNavigationView) findViewById(R.id.navigation);
        tvLetterHeader = (TextView) findViewById(R.id.tv_letter_header);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        //获取数据
        getProvinces();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View transView = recyclerView.findChildViewUnder(
                        tvLetterHeader.getMeasuredWidth(), tvLetterHeader.getMeasuredHeight() - 1);

                if (transView != null) {
                    TextView tvLetter = (TextView) transView.findViewById(R.id.tv_letter_header);
                    if (tvLetter != null) {
                        String tvLetterStr = tvLetter.getText().toString().trim();
                        String tvHeaderStr = tvLetterHeader.getText().toString().trim();
                        if (!tvHeaderStr.equals(tvLetterStr)) {
                            for (int i = 0; i < mLetterDatas.size(); i++) {
                                if (tvLetterStr.equals(mLetterDatas.get(i))) {
                                    mNavigationView.setSelectorPosition(i);
                                    break;
                                }
                            }
                        }
                        tvLetterHeader.setText(tvLetterStr);
                        if (transView.getTag() != null) {
                            int headerMoveY = transView.getTop() - tvLetterHeader.getMeasuredHeight();
                            int tag = (int) transView.getTag();
                            if (tag == HEADER_VISIBLE_VIEW) {
                                if (transView.getTop() > 0) {
                                    tvLetterHeader.setTranslationY(headerMoveY);
                                } else {
                                    tvLetterHeader.setTranslationY(0);
                                }
                            } else {
                                tvLetterHeader.setTranslationY(0);
                            }
                        }
                    }
                }

                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = selectPosition - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < mRecyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = mRecyclerView.getChildAt(n).getTop();
                        //最后的移动
                        mRecyclerView.scrollBy(0, top);
                    }
                }


            }
        });

        mNavigationView.setOnTouchListener(new LetterNavigationView.OnTouchListener() {
            @Override
            public void onTouchListener(String str, boolean hideEnable) {
                tvCenter.setText(str);
                if (hideEnable) {
                    tvCenter.setVisibility(View.GONE);
                } else {
                    tvCenter.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).firstPinYin.equals(str)) {
                        selectPosition = i;
                        break;
                    }
                }
                moveToPosition(selectPosition);
            }

        });

    }


    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }


    private void getProvinces() {
        Map<String, String> map = SortRequestData.getmap();
        String token = CacheUtils.getString(this, "token", null);
        progressdialog.show();
        map.put("token", token);
        String requestData = SortRequestData.sortString(map);
        String signData = SignUtil.sign(requestData);
        map.put("sign", signData);
        VolleyUtil.sendJsonRequestByPost(ConstantUtils.LISTPROVINCE_URL, null, map,
                new HttpBackBaseListener() {

                    @Override
                    public void onSuccess(String string) {
                        LogUtils.i("省份列表="+string);
                        progressdialog.dismiss();
                        JSONObject json = JSON.parseObject(string);
                        String code = json.getString("code");

                        if ("0".equals(code)) {
                            String datastr = json.getString("data");
                            if (com.fanfanlicai.utils.StringUtils.isBlank(datastr)) {
                                // datastr为空不验签
                            } else {
                                String sign = json.getString("sign");
                                boolean isSuccess = SignUtil.verify(sign,datastr);
                                if (isSuccess) {// 验签成功
                                    JSONObject data = JSON.parseObject(datastr);
                                    JSONObject datas = data.getJSONObject("datas");
                                    JSONArray letters = data.getJSONArray("letters");
                                    for (Object letter : letters) {
                                        JSONArray jsonArray = datas.getJSONArray(letter.toString());
                                        mLetterDatas.add(letter.toString());
                                        for (Object o : jsonArray) {
                                            JSONObject obj = (JSONObject) o;
                                            Province province = new Province();
                                            province.firstPinYin = obj.getString("provinceFirstLetter");
                                            province.id = obj.getString("provinceId");
                                            province.provincePinYin = obj.getString("provinceLetters");
                                            province.provinceName = obj.getString("provinceName");
                                            province.hideEnable = false;
                                            mDatas.add(province);
                                        }
                                    }

                                    LogUtils.i("mLetterDatas=="+mLetterDatas);
                                    mNavigationView.setData(mLetterDatas);

                                    mRecyclerView.setHasFixedSize(true);
                                    mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(ProvinceActivity.this));

                                    mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Province>(ProvinceActivity.this, mDatas, R.layout.rv_item_city) {
                                        @Override
                                        protected void convert(BaseViewHolder helper, final Province item) {

                                            if (helper.getAdapterPosition() == 0) {
                                                helper.setVisible(R.id.tv_letter_header, true);
                                                helper.itemView.setTag(HEADER_FIRST_VIEW);
                                            } else {
                                                if (item.firstPinYin.equals(mDatas.get(helper.getAdapterPosition() - 1).firstPinYin)) {
                                                    helper.setVisible(R.id.tv_letter_header, false);
                                                    helper.itemView.setTag(HEADER_NONE_VIEW);
                                                } else {
                                                    helper.setVisible(R.id.tv_letter_header, true);
                                                    helper.itemView.setTag(HEADER_VISIBLE_VIEW);
                                                }
                                            }

                                            if (item.hideEnable) {
                                                helper.setVisible(R.id.tv_city, false);
                                            } else {
                                                helper.setVisible(R.id.tv_city, true);
                                            }

                                            helper.setText(R.id.tv_letter_header, item.firstPinYin);
                                            helper.setText(R.id.tv_city, item.provinceName);

                                            helper.setOnClickListener(R.id.tv_city, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int flags = getIntent().getFlags();
                                                    Intent intent = new Intent(ProvinceActivity.this,CityActivity.class);
                                                    intent.putExtra("province",item.provinceName);
                                                    intent.putExtra("proId",item.id);
                                                    intent.putExtra("flags",flags);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });

                                            helper.setOnClickListener(R.id.tv_letter_header, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    for (Province province : mDatas) {
                                                        if (province.firstPinYin.equals(item.firstPinYin)) {
                                                            province.hideEnable = !province.hideEnable;
                                                        }
                                                    }
                                                    notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    ToastUtils.toastshort("地区列表获取异常");
                                }
                            }
                        } else {
                            String msg = json.getString("msg");
                            ToastUtils.toastshort(msg);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressdialog.dismiss();
                        ToastUtils.toastshort("网络请求失败");
                    }
                });
    }

}
