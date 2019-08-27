package cn.yumben.pojo;


import java.util.List;
import java.util.Map;
/**
 * @author zzg
 */
public class BugReport {

    /**
     * 漏洞名称
     */
    private String loopholeName;
    /**
     * CNNVD编号
     */
    private String cnnvdId;
    /**
     * 危害等级
     */
    private String importantLevel;
    /**
     * CVE编号
     */
    private String cveId;
    /**
     * 漏洞类型
     */
    private String loopholeClass;
    /**
     * 发布时间
     */
    private String publishTime;
    /**
     * 威胁类型
     */
    private String threatenClass;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 厂商
     */
    private String manufacturers;
    /**
     * 漏洞来源
     */
    private String source;
    /**
     * 漏洞简介
     */
    private String loopholeSynopsis;

    /**
     * 受影响的实体
     */
    private List<String> affectedEntities;
    /**
     * 补丁
     */
    private Map<String, String> patch;

    public BugReport() {
        super();
    }

    @Override
    public String toString() {
        return "BugReport{" +
                "loopholeName='" + loopholeName + '\'' +
                ", cnnvdId='" + cnnvdId + '\'' +
                ", importantLevel='" + importantLevel + '\'' +
                ", cveId='" + cveId + '\'' +
                ", loopholeClass='" + loopholeClass + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", threatenClass='" + threatenClass + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", manufacturers='" + manufacturers + '\'' +
                ", source='" + source + '\'' +
                ", loopholeSynopsis='" + loopholeSynopsis + '\'' +
                ", affectedEntities=" + affectedEntities +
                ", patch=" + patch +
                '}';
    }

    public String getLoopholeName() {
        return loopholeName;
    }

    public void setLoopholeName(String loopholeName) {
        this.loopholeName = loopholeName;
    }

    public String getCnnvdId() {
        return cnnvdId;
    }

    public void setCnnvdId(String cnnvdId) {
        this.cnnvdId = cnnvdId;
    }

    public String getImportantLevel() {
        return importantLevel;
    }

    public void setImportantLevel(String importantLevel) {
        this.importantLevel = importantLevel;
    }

    public String getCveId() {
        return cveId;
    }

    public void setCveId(String cveId) {
        this.cveId = cveId;
    }

    public String getLoopholeClass() {
        return loopholeClass;
    }

    public void setLoopholeClass(String loopholeClass) {
        this.loopholeClass = loopholeClass;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getThreatenClass() {
        return threatenClass;
    }

    public void setThreatenClass(String threatenClass) {
        this.threatenClass = threatenClass;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(String manufacturers) {
        this.manufacturers = manufacturers;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLoopholeSynopsis() {
        return loopholeSynopsis;
    }

    public void setLoopholeSynopsis(String loopholeSynopsis) {
        this.loopholeSynopsis = loopholeSynopsis;
    }

    public List<String> getAffectedEntities() {
        return affectedEntities;
    }

    public void setAffectedEntities(List<String> affectedEntities) {
        this.affectedEntities = affectedEntities;
    }

    public Map<String, String> getPatch() {
        return patch;
    }

    public void setPatch(Map<String, String> patch) {
        this.patch = patch;
    }

    public BugReport(String loopholeName, String cnnvdId, String importantLevel, String cveId, String loopholeClass, String publishTime, String threatenClass, String updateTime, String manufacturers, String source, String loopholeSynopsis, List<String> affectedEntities, Map<String, String> patch) {
        this.loopholeName = loopholeName;
        this.cnnvdId = cnnvdId;
        this.importantLevel = importantLevel;
        this.cveId = cveId;
        this.loopholeClass = loopholeClass;
        this.publishTime = publishTime;
        this.threatenClass = threatenClass;
        this.updateTime = updateTime;
        this.manufacturers = manufacturers;
        this.source = source;
        this.loopholeSynopsis = loopholeSynopsis;
        this.affectedEntities = affectedEntities;
        this.patch = patch;
    }
}
