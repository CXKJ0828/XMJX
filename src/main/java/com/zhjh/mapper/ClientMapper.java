package com.zhjh.mapper;

import com.zhjh.bean.ComboboxEntity;
import com.zhjh.entity.ClientFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface ClientMapper extends BaseMapper{
    public List<ComboboxEntity> findSimpleNameText(String simpleName);
    public List<ClientFormMap> findBySimpleNameLike(String simpleName);
    public String findSimpleNameByIds(String ids);
    public void deleteById(String id);
    public List<ClientFormMap> findUnCompleteOrderClientGroupByClientId(ClientFormMap clientFormMap);
    public List<ClientFormMap> findUnSendOrderClientGroupByClientId();
    public List<ClientFormMap> findOrderClientGroupByClientId();
    public List<ClientFormMap> findByFullNameLike(String fullName);
    public List<ClientFormMap> findByFullName(String fullName);
    public List<ClientFormMap> findBySimpleName(String simpleName);
    public List<ClientFormMap> findByAllLike(ClientFormMap clientFormMap);
}
