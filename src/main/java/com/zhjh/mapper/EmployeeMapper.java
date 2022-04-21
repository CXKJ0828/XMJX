package com.zhjh.mapper;

import com.zhjh.entity.EmployeeFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface EmployeeMapper extends BaseMapper{
    public int findCountByAllLike(EmployeeFormMap departmentFormMap);
    public List<EmployeeFormMap> selectNotInUserEntity();
    public List<EmployeeFormMap> findByAllLike(EmployeeFormMap departmentFormMap);
    public void deleteById(String id);
}
