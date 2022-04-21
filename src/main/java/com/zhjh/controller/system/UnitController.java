package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.UnitFormMap;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.UnitMapper;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
@RequestMapping("/unit/")
public class UnitController extends BaseController {
	@Inject
	private UnitMapper unitMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@RequestMapping("list")
	public String list(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/unit/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public UnitFormMap findByPage(String content) throws Exception {
		if(content==null){
			content="";
		}
		UnitFormMap unitFormMap=new UnitFormMap();
		List<UnitFormMap> unitFormMaps=unitMapper.findByAllLike(content);
		unitFormMap.set("rows",unitFormMaps);
		return unitFormMap;
	}

	@ResponseBody
	@RequestMapping("unitSelect")
	public List<UnitFormMap> unitSelect(String q) throws Exception {
		q=getPara("q");
		String content=getPara("content");
		if(q==null){
			q="";
		}
		List<UnitFormMap> unitFormMaps=unitMapper.findByAllLike(q);
		return unitFormMaps;
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<UnitFormMap> list = (List) ToolCommon.json2ObjectList(entity, UnitFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			UnitFormMap unitFormMap=list.get(i);
			String id=unitFormMap.get("id")+"";
			unitFormMap.set("modifytime",nowtime);
			unitFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				unitFormMap.remove("id");
				unitMapper.addEntity(unitFormMap);
			}else{
				unitMapper.editEntity(unitFormMap);
			}
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			String[] idsStr=ids.split(",");
			for (String id : idsStr) {
				unitMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

}