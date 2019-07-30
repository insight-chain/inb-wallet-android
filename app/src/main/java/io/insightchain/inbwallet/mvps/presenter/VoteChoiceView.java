package io.insightchain.inbwallet.mvps.presenter;

import java.util.ArrayList;
import java.util.List;

import io.insightchain.inbwallet.base.mvp.BaseMvpView;
import io.insightchain.inbwallet.mvps.model.vo.NodeVo;
import io.insightchain.inbwallet.mvps.view.widget.CustomPopupWindow;

public interface VoteChoiceView extends BaseMvpView {

    void showList(List<NodeVo> nodeVos);
    void showPopwindow(CustomPopupWindow customPopupWindow);
    void updateSelectedList(ArrayList<NodeVo> newSelectedList);
}
