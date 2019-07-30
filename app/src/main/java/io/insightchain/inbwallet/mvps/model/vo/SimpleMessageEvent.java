package io.insightchain.inbwallet.mvps.model.vo;

public class SimpleMessageEvent {

    private String target;
    private Object obj;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
