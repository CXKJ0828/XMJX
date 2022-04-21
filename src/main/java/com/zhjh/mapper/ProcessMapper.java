package com.zhjh.mapper;

import com.zhjh.entity.ProcessFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;
import java.util.Map;

public interface ProcessMapper extends BaseMapper{
    public String findNameById(String id);
    public String findIdsByNames(String[] names);
    public List<ProcessFormMap> findByName(String name);
    public List<ProcessFormMap> findAllProcessByRoleId(ProcessFormMap processFormMap);
    public int findCountByAllLike(ProcessFormMap processFormMap);
    public int findCountByNameLike(ProcessFormMap processFormMap);
    public void deleteById(String id);
    public List<ProcessFormMap> findByAllLike(ProcessFormMap processFormMap);
    public List<ProcessFormMap> findByNameLike(ProcessFormMap processFormMap);
}
