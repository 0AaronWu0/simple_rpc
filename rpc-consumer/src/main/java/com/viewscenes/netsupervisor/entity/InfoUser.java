package com.viewscenes.netsupervisor.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * @program: rpc-provider
 * @description: ${description}
 * @author: shiqizhen
 * @create: 2018-11-30 10:11
 **/
@XStreamAlias("INFOUSER")
public class InfoUser  implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;


    public InfoUser(){};

    public InfoUser(Long id, String ip, Long times, String userName, String userSex, String passWord) {
        this.id = id;
        this.ip = ip;
        this.times = times;
        this.userName = userName;
        this.userSex = userSex;
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "InfoUser{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", times=" + times +
                ", userName='" + userName + '\'' +
                ", userSex='" + userSex + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }

    /**  */
    private Long id;
    /**  */
    @XStreamAlias("IP")
    private String ip;

    /**  */

    private Long times;

    /**  */
    @XStreamAlias("TIMES")
    private String userName;

    /**  */
    private String userSex;

    /**  */
    private String passWord;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}

