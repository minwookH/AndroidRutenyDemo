package address.teruten.com.terutenaddress.vo.realm;

import com.google.gson.annotations.SerializedName;


/**
 * Created by teruten on 2017-07-13.
 */

public class Member1 {

    //@JsonProperty("ID")
    @SerializedName("ID")
    public String id;

    //@JsonProperty("NAME")
    @SerializedName("NAME")
    public String name;

    //@JsonProperty("DUTY_NAME")
    @SerializedName("DUTY_NAME")
    public String DUTY_NAME;

    //@JsonProperty("P_NAME")
    @SerializedName("P_NAME")
    public String pName;

    //@JsonProperty("DEPARTMENT_NAME")
    @SerializedName("DEPARTMENT_NAME")
    public String department;

    //@JsonProperty("DEPARTMENT_PID")
    @SerializedName("DEPARTMENT_PID")
    public String departmentId;

    //@JsonProperty("POSITION_NAME")
    @SerializedName("POSITION_NAME")
    public String position;

    //@JsonProperty("DEPARTMENT_HEAD")
    @SerializedName("DEPARTMENT_HEAD")
    public String departmentHead;

    //@JsonProperty("DEPARTMENT_UID")
    @SerializedName("DEPARTMENT_UID")
    public String departmentUid;

    //@JsonProperty("TEL1")
    @SerializedName("TEL1")
    public String tel1;

    //@JsonProperty("TEL2")
    @SerializedName("TEL2")
    public String tel2;

    //@JsonProperty("TEL3")
    @SerializedName("TEL3")
    public String tel3;

    //@JsonProperty("INNERTEL")
    @SerializedName("INNERTEL")
    public String innerTel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDUTY_NAME() {
        return DUTY_NAME;
    }

    public void setDUTY_NAME(String DUTY_NAME) {
        this.DUTY_NAME = DUTY_NAME;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(String departmentHead) {
        this.departmentHead = departmentHead;
    }

    public String getDepartmentUid() {
        return departmentUid;
    }

    public void setDepartmentUid(String departmentUid) {
        this.departmentUid = departmentUid;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getTel3() {
        return tel3;
    }

    public void setTel3(String tel3) {
        this.tel3 = tel3;
    }

    public String getInnerTel() {
        return innerTel;
    }

    public void setInnerTel(String innerTel) {
        this.innerTel = innerTel;
    }
}
