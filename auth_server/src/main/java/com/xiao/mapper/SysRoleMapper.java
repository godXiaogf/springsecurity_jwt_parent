package com.xiao.mapper;

import com.xiao.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper {
    @Select("select sr.id, sr.role_name roleName, sr.role_desc roleDesc " +
            "from sys_user_role sur, sys_role sr " +
            "where sur.rid=sr.id and sur.uid = #{uid}")
    List<SysRole> findRolesByUid(Integer uid);
}
