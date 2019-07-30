package io.insightchain.inbwallet.mvps.model.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

public class NodeVo implements Parcelable {

    /**
     * id : 4
     * host : 192.168.1.106
     * port : 30001
     * country : China
     * city : beijing
     * name : null
     * voteRatio : 1.5
     * type : null
     * lastUpdated : null
     * dateCreated : 10-七月-2019
     * lastUp : null
     * image : www.image.com
     * webSite : 2
     * address : e486016a2a5f701464252f6c9edabc4ef47f5ebe20bc6682c8d91f96300867a827155bea289de308273c6b763dad7bbdef5dd0df32829b049597a37210c2deb9
     * nodeId : e486016a2a5f701464252f6c9edabc4ef47f5ebe20bc6682c8d91f96300867a827155bea289de308273c6b763dad7bbdef5dd0df32829b049597a37210c2deb9
     * email : ghyinsight@insight.io
     * voteNumber : 3
     * voteTotalNumber : 2
     * up : 0
     * longitude : null
     * latitude : null
     * countryCode : null
     */

    private Long id;
    private String host;//节点网址
    private String port;//节点端口
    private String country;
    private String city;
    private String name;//名称
    private double voteRatio;//投票百分比
    private int type;//类型 1.超级节点，2.普通节点
    private String lastUpdated;
    private String dateCreated;
    private Object lastUp;
    private String image;//图标地址
    private String webSite;
    private String address;//节点地址
    private String nodeId;
    private String email;
    private BigInteger voteNumber;//投票数
    private BigInteger voteTotalNumber;
    private int up;
    private Object longitude;
    private Object latitude;
    private Object countryCode;
    private boolean isChecked;
    private String intro;

    public NodeVo() {
        super();
    }

    public NodeVo(long id, String host, String port, String country, String city, String name, double voteRatio, int type, String lastUpdated, String dateCreated, Object lastUp, String image, String webSite, String address, String nodeId, String email, BigInteger voteNumber, BigInteger voteTotalNumber, int up, Object longitude, Object latitude, Object countryCode, boolean isChecked, String intro) {
        super();
        this.id = id;
        this.host = host;
        this.port = port;
        this.country = country;
        this.city = city;
        this.name = name;
        this.voteRatio = voteRatio;
        this.type = type;
        this.lastUpdated = lastUpdated;
        this.dateCreated = dateCreated;
        this.lastUp = lastUp;
        this.image = image;
        this.webSite = webSite;
        this.address = address;
        this.nodeId = nodeId;
        this.email = email;
        this.voteNumber = voteNumber;
        this.voteTotalNumber = voteTotalNumber;
        this.up = up;
        this.longitude = longitude;
        this.latitude = latitude;
        this.countryCode = countryCode;
        this.isChecked = isChecked;
        this.intro = intro;
    }

    protected NodeVo(Parcel in) {
        id = in.readLong();
        host = in.readString();
        port = in.readString();
        country = in.readString();
        city = in.readString();
        name = in.readString();
        voteRatio = in.readDouble();
        type = in.readInt();
        lastUpdated = in.readString();
        dateCreated = in.readString();
        image = in.readString();
        webSite = in.readString();
        address = in.readString();
        nodeId = in.readString();
        email = in.readString();
        up = in.readInt();
        isChecked = in.readByte() != 0;
        intro = in.readString();
    }

    public static final Creator<NodeVo> CREATOR = new Creator<NodeVo>() {
        @Override
        public NodeVo createFromParcel(Parcel in) {
            return new NodeVo(in);
        }

        @Override
        public NodeVo[] newArray(int size) {
            return new NodeVo[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public double getVoteRatio() {
        return voteRatio;
    }

    public void setVoteRatio(double voteRatio) {
        this.voteRatio = voteRatio;
    }

    public Object getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Object getLastUp() {
        return lastUp;
    }

    public void setLastUp(Object lastUp) {
        this.lastUp = lastUp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getVoteNumber() {
        return voteNumber;
    }

    public void setVoteNumber(BigInteger voteNumber) {
        this.voteNumber = voteNumber;
    }

    public BigInteger getVoteTotalNumber() {
        return voteTotalNumber;
    }

    public void setVoteTotalNumber(BigInteger voteTotalNumber) {
        this.voteTotalNumber = voteTotalNumber;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Object countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "NodeVo{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", name='" + name + '\'' +
                ", voteRatio=" + voteRatio +
                ", type=" + type +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastUp=" + lastUp +
                ", image='" + image + '\'' +
                ", webSite='" + webSite + '\'' +
                ", address='" + address + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", email='" + email + '\'' +
                ", voteNumber=" + voteNumber +
                ", voteTotalNumber=" + voteTotalNumber +
                ", up=" + up +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", countryCode=" + countryCode +
                ", isChecked=" + isChecked +
                ", intro='" + intro + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeVo nodeVo = (NodeVo) o;
        return this.id == nodeVo.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(host);
        dest.writeString(port);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(name);
        dest.writeDouble(voteRatio);
        dest.writeInt(type);
        dest.writeString(lastUpdated);
        dest.writeString(dateCreated);
        dest.writeString(image);
        dest.writeString(webSite);
        dest.writeString(address);
        dest.writeString(nodeId);
        dest.writeString(email);
        dest.writeInt(up);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(intro);
    }
}
