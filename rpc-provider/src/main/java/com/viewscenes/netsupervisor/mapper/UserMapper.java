package com.viewscenes.netsupervisor.mapper;

import com.viewscenes.netsupervisor.entity.InfoUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    void insertInfoUser(InfoUser infoUser);

	InfoUser getInfoUserById(String id);

	void deleteInfoUserById(String id);

	String getNameById(String id);

	Map<String,InfoUser> getAllUser();

}