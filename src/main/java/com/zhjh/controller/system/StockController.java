package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.StockFormMap;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.StockMapper;
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
@RequestMapping("/stock/")
public class StockController extends BaseController {
	@Inject
	private StockMapper stockMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/stock/list";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	@SystemLog(module="产成品管理",methods="产成品统计-获取列表内容")
	public StockFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		StockFormMap stockFormMap = getFormMap(StockFormMap.class);
		if(content==null){
			content="";
		}
		stockFormMap.set("content",content);
		if(page.equals("1")){
			total=stockMapper.findCountByAllLike(stockFormMap);
		}
		stockFormMap=toFormMap(stockFormMap, page, rows,stockFormMap.getStr("orderby"));
		List<StockFormMap> departmentFormMapList=stockMapper.findByAllLike(stockFormMap);
		stockFormMap.set("rows",departmentFormMapList);
		stockFormMap.set("total",total);
		return stockFormMap;
	}

	@ResponseBody
	@RequestMapping("findAll")
	public StockFormMap findAll(String content) throws Exception {
		StockFormMap stockFormMap = getFormMap(StockFormMap.class);
		if(content==null){
			content="";
		}
		stockFormMap.set("content",content);
		List<StockFormMap> departmentFormMapList=stockMapper.findByAllLike(stockFormMap);
		stockFormMap.set("rows",departmentFormMapList);
		return stockFormMap;
	}




	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<StockFormMap> list = (List) ToolCommon.json2ObjectList(entity, StockFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			StockFormMap stockFormMap=list.get(i);
			String id=stockFormMap.get("id")+"";
			stockFormMap.set("modifytime",nowtime);
			stockFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				stockFormMap.remove("id");
				stockMapper.addEntity(stockFormMap);
			}else{
				stockMapper.editEntity(stockFormMap);
			}
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap = new SystemconfigFormMap();
		systemconfigFormMap.set("name", "deletepassword");
		String content = getPara("password");
		systemconfigFormMap.set("content", content);
		List<SystemconfigFormMap> systemconfigFormMaps = systemconfigMapper.findByNames(systemconfigFormMap);
		if (systemconfigFormMaps.size() > 0) {
			String ids = getPara("ids");
			ids = ids.substring(0, ids.length() - 1);
			String[] idsStr = ids.split(",");
			for (String id : idsStr) {
				stockMapper.deleteById(id);
			}
			return "success";
		} else {
			return "删除密码错误";
		}
	}
}