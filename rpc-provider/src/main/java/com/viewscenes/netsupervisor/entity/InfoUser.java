package com.viewscenes.netsupervisor.entity;

/**
 * visitor
 *
 * @author bianj
 * @version 1.0.0 2019-06-19
 */
public class InfoUser implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 3034387654419766725L;

    public InfoUser(Long id, String ip, Long times, String userName, String userSex, String passWord) {
        this.id = id;
        this.ip = ip;
        this.times = times;
        this.userName = userName;
        this.userSex = userSex;
        this.passWord = passWord;
    }

    /**  */

    private Long id;

    /**  */
    private String ip;

    /**  */
    private Long times;

    /**  */
    private String userName;

    /**  */
    private String userSex;

    /**  */
    private String passWord;

    /**
     * 获取
     *
     * @return
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 设置
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取
     *
     * @return
     */
    public String getIp() {
        return this.ip;
    }

    /**
     * 设置
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取
     *
     * @return
     */
    public Long getTimes() {
        return this.times;
    }

    /**
     * 设置
     *
     * @param times
     */
    public void setTimes(Long times) {
        this.times = times;
    }

    /**
     * 获取
     *
     * @return
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * 设置
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取
     *
     * @return
     */
    public String getUserSex() {
        return this.userSex;
    }

    /**
     * 设置
     *
     * @param userSex
     */
    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    /**
     * 获取
     *
     * @return
     */
    public String getPassWord() {
        return this.passWord;
    }

    /**
     * 设置
     *
     * @param passWord
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}