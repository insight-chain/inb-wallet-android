package io.insightchain.inbwallet.mvps.presenter;

import java.util.List;

import io.insightchain.inbwallet.base.mvp.BaseMvpView;
import io.insightchain.inbwallet.mvps.model.vo.TransactionResultVo;

public interface TransactionRecordView extends BaseMvpView {
    void getList(List<TransactionResultVo> results);
    void showLastLine();
}
