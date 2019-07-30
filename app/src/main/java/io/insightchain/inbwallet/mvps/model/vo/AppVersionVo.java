package io.insightchain.inbwallet.mvps.model.vo;

public class AppVersionVo {

    private String versionName;
    private int versionCode;
    private String downloadUrl;
    private String releaseNote;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    @Override
    public String toString() {
        return "AppVersionVo{" +
                "versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", releaseNote='" + releaseNote + '\'' +
                '}';
    }
}
