package com.viewscenes.netsupervisor.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: rpc-provider
 * @description: ${description}
 * @author: shiqizhen
 * @create: 2018-11-30 10:11
 **/
@XStreamAlias("INFOUSER")
@Data
public class InfoUser  implements Serializable {


    private static final long serialVersionUID = 5749665292465659412L;

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

}

