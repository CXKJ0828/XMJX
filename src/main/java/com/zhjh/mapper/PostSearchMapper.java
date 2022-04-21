package com.zhjh.mapper;

import com.zhjh.entity.PostSearchFormMap;
import com.zhjh.mapper.base.BaseMapper;

import java.util.List;

public interface PostSearchMapper extends BaseMapper{
	public List<PostSearchFormMap> findAll();
}
