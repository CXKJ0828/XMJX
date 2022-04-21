package com.zhjh.entity;

import com.zhjh.annotation.TableSeg;
import com.zhjh.util.FormMap;

import java.io.Serializable;


/**
 * 下料单表
 */
@TableSeg(tableName = "ly_blank", id="id")
public class BlankFormMap extends FormMap<String,Object> implements Serializable, Comparable<BlankFormMap> {


	/**
	 *@descript
	 *@author lanyuan
	 *@date 2015年3月29日
	 *@version 1.0
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int compareTo(BlankFormMap o) {
		return 0;
	}
}
