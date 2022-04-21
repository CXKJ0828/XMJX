package com.zhjh.controller.system;

import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.AreaMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/area/")
public class AreaController extends BaseController {
	@Inject
	private AreaMapper areaMapper;
	@ResponseBody
	@RequestMapping("areaSelect")
	public List<AreaFormMap> areaSelect(String q,String parentId) throws Exception {
		AreaFormMap areaFormMap = getFormMap(AreaFormMap.class);
		q=getPara("q");
		if(q==null){
			q="";
		}
		if(parentId==null){
			parentId="1";
		}
		areaFormMap.set("parentId",parentId);
		areaFormMap.set("content",q);
		List<AreaFormMap> departmentFormMapList=areaMapper.findByParentId(areaFormMap);
		return departmentFormMapList;
	}

}