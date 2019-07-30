package io.insightchain.inbwallet.mvps.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.base.mvp.CreatePresenter;
import io.insightchain.inbwallet.base.mvp.PresenterVariable;
import io.insightchain.inbwallet.common.GlideCircleTransform;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;
import io.insightchain.inbwallet.mvps.presenter.VoteChoicePresenter;
import io.insightchain.inbwallet.mvps.presenter.VoteChoiceView;
import io.insightchain.inbwallet.mvps.view.adapter.VoteChoiceAdapter;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;
import io.insightchain.inbwallet.utils.ScreenUtils;
import io.insightchain.inbwallet.utils.ToastUtils;

import static io.insightchain.inbwallet.common.Constants.VOTE_MAX_NUMBER;

@CreatePresenter(presenter = VoteChoicePresenter.class)
public class VoteChoiceFragment extends BaseMvpFragment implements VoteChoiceView {

    @BindView(R.id.recycler_view_fragment_vote_choice)
    RecyclerView recyclerView;
    @BindView(R.id.ptr_frame_fragment_vote_choice)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.tv_chosen_nodes)
    TextView chosenNodesText;
    @BindView(R.id.ll_selected_icon_layout)
    LinearLayout selectedIconLayout;
    @BindView(R.id.rl_bottom_layout)
    View bottomLayout;

    @PresenterVariable
    VoteChoicePresenter voteChoicePresenter;

    private List<NodeVo> nodeVoList;
    private ArrayList<NodeVo> selectedList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vote_choice;
    }

    private VoteChoiceAdapter adapter;

    @Override
    protected void init() {
        voteChoicePresenter.getNodeList();
        initRecyclerView(recyclerView);
        selectedList = new ArrayList<>();
        nodeVoList = new ArrayList<>();
        adapter = new VoteChoiceAdapter(mContext,nodeVoList,recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnCheckChangedListner((buttonView, isChecked, position) -> {
            if(selectedList.size() >= VOTE_MAX_NUMBER && !selectedList.contains(nodeVoList.get(position))){
                ToastUtils.showToast(mContext,"选择节点数达到上限");
                nodeVoList.get(position).setChecked(false);
                adapter.notifyItemChanged(position);
                return;
            }
//                nodeVoList.get(position).setChecked(isChecked);
            if(isChecked){
                selectedList.add(nodeVoList.get(position));
            }else{
                selectedList.remove(nodeVoList.get(position));
            }
            updateSelectedShow();

        });

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                voteChoicePresenter.getNodeList();
                frame.refreshComplete();
            }
        });

    }

    public static VoteChoiceFragment newInstanse(){
        VoteChoiceFragment voteChoiceFragment = new VoteChoiceFragment();
        return voteChoiceFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.tv_confirm_vote, R.id.rl_show_selected})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_confirm_vote:

                if(selectedList.size() == 0){
                    ToastUtils.showToast(mContext, "未选择任何节点，请在选择后投票！");
                    return;
                }
//                ArrayList<String> addressList = new ArrayList<>();
//                for (NodeVo nodeVo:selectedList){
//                    addressList.add(nodeVo.getAddress());
//                }
                voteChoicePresenter.jumpToVoteConfirm(selectedList);
                break;
            case R.id.rl_show_selected:
                //TODO 弹窗
                Log.e("VoteChoiceFragment","rl_show_selected is clicked" );
                voteChoicePresenter.showSelectedPop(selectedList);
                break;
        }
    }

    public void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    private List<NodeVo> getTestNodeList(){
        List<NodeVo> testList = new ArrayList<>();
        for(int i=0;i<60;i++) {
            if(i%2 == 0) {
                NodeVo nodeVo = new NodeVo();
                nodeVo.setId(i);
                nodeVo.setAddress("0xf502cD443f3111AB0e374Be0A6e7E4BBbE9D33e9");
                nodeVo.setCity("北京");
                nodeVo.setImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562835098717&di=4980c41e32b1c83f7c982699f1d00f14&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D319190465%2C3717476812%26fm%3D214%26gp%3D0.jpg");
                nodeVo.setName("测试" + i);
                nodeVo.setVoteRatio(0.75d);
                nodeVo.setIntro("坐落于北京海淀区上地九街数码科技广场的世外桃源。");
                testList.add(nodeVo);
            }else {
                NodeVo nodeVo2 = new NodeVo();
                nodeVo2.setId(i);
                nodeVo2.setAddress("0xaa18a055AB2017a0Cd3fB7D70f269C9B80092206");
                nodeVo2.setCity("上海");
                nodeVo2.setImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562826542081&di=88c5df5f9d19d1773b65fe1c9529c527&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201410%2F16%2F20141016220822_uc5hc.thumb.700_0.jpeg");
                nodeVo2.setName("测试" + i);
                nodeVo2.setVoteRatio(0.2d);
                nodeVo2.setIntro("那里水清沙白，椰林树影，是游客们向往的天堂。");
                testList.add(nodeVo2);
            }
        }
        return testList;
    }

    @Override
    public void showList(List<NodeVo> nodeVos) {
        if(nodeVos==null){
            return;
        }
        nodeVoList.clear();
        nodeVoList.addAll(nodeVos);
//        nodeVoList.addAll(getTestNodeList());
        for(NodeVo nodeVo:selectedList){
            for(NodeVo nodeVo1:nodeVoList){
                if(nodeVo.getId() == nodeVo1.getId()){
                    nodeVo1.setChecked(true);
                }else{
                    nodeVo1.setChecked(false);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showPopwindow(CustomPopupWindow customPopupWindow) {
        customPopupWindow.showAtLocation(ptrFrameLayout, Gravity.CENTER, 0, 0);
    }

    @Override
    public void updateSelectedList(ArrayList<NodeVo> newSelectedList) {
//        selectedList.clear();
//        selectedList.addAll(newSelectedList);
        if(newSelectedList == null){
            selectedList.clear();
            for (NodeVo nodeVo1 : nodeVoList) {
                nodeVo1.setChecked(false);
            }
        }else {
            for (NodeVo nodeVo : selectedList) {
                for (NodeVo nodeVo1 : nodeVoList) {
                    if (nodeVo.getId() == nodeVo1.getId()) {
                        nodeVo1.setChecked(true);
                    } else {
                        nodeVo1.setChecked(false);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
        updateSelectedShow();
    }

    private void updateSelectedShow(){
        //TODO 将头像加入selectedIconLayout
        selectedIconLayout.removeAllViews();
        for(int i = 0; i<(4>selectedList.size()?selectedList.size():4); i++){
            NodeVo nodeVo = selectedList.get(i);
            ImageView imageView = new ImageView(mContext);
            Glide.with(mContext).load(nodeVo.getImage())
                    .error(R.mipmap.icon_default_head_img_circle_gray)
                    .transform(new GlideCircleTransform(mContext))
                    .into(imageView);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ScreenUtils.dp2px(mContext,30),ScreenUtils.dp2px(mContext,30));
            if(selectedList.indexOf(nodeVo) != 0){
                lp.leftMargin = -ScreenUtils.dp2px(mContext,8);

            }
            lp.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(lp);
            selectedIconLayout.addView(imageView);
        }
        chosenNodesText.setText(selectedList.size()+"/"+VOTE_MAX_NUMBER);

    }


}
