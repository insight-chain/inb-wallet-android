package io.insightchain.inbwallet.mvps.model.vo;

import java.util.List;

public class UrlVo {

    private List<String> api;
    private List<String> explorer;
    private List<String> chain;

    public List<String> getApi() {
        return api;
    }

    public void setApi(List<String> api) {
        this.api = api;
    }

    public List<String> getExplorer() {
        return explorer;
    }

    public void setExplorer(List<String> explorer) {
        this.explorer = explorer;
    }

    public List<String> getChain() {
        return chain;
    }

    public void setChain(List<String> chain) {
        this.chain = chain;
    }

    @Override
    public String toString() {
        return "UrlVo{" +
                "api=" + api +
                ", explorer=" + explorer +
                ", chain=" + chain +
                '}';
    }
}
