package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.entity.ToolOutFormMap;
import com.zhjh.entity.ToolStockFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.file.ExcelUtils;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.mapper.ToolOutMapper;
import com.zhjh.mapper.ToolStockMapper;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/toolOut/")
public class ToolOutController extends BaseController {
	@Inject
	private ToolOutMapper toolOutMapper;
	@Inject
	private ToolStockMapper toolStockMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@RequestMapping("list")
	public String list(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/toolOut/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public ToolOutFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		ToolOutFormMap toolOutFormMap=new ToolOutFormMap();
		if(content==null){
			content="";
		}
		String toolTypeId=getPara("toolTypeId");
		String startTime=getPara("startTime");
		String endTime=getPara("endTime");
		String userId=getPara("userId");
		toolOutFormMap.set("content",content);
		toolOutFormMap.set("toolTypeId",toolTypeId);
		toolOutFormMap.set("startTime",startTime);
		toolOutFormMap.set("endTime",endTime);
		toolOutFormMap.set("userId",userId);
		if(page.equals("1")){
			total=toolOutMapper.findCountByAllLike(toolOutFormMap);
		}
		ToolOutFormMap sumToolOut=toolOutMapper.findAmoutAndMoneySumByAllLike(toolOutFormMap);
		toolOutFormMap=toFormMap(toolOutFormMap, page, rows,toolOutFormMap.getStr("orderby"));
		List<ToolOutFormMap> toolOutFormMaps=toolOutMapper.findByAllLike(toolOutFormMap);
		toolOutFormMap.set("rows",toolOutFormMaps);
		toolOutFormMap.set("total",total);
		toolOutFormMap.set("sumEntity",sumToolOut);
		return toolOutFormMap;
	}

	@RequestMapping(value = "exportAll")
	@ResponseBody
	@SystemLog(module="工具管理",methods="工具库存-全部导出")
	public void exportAll(HttpServletResponse response, String entity) throws Exception {

		String contentS=ToolCommon.json2Object(entity).getString("content");
		String toolTypeId=ToolCommon.json2Object(entity).getString("toolTypeId");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");
		String userId=ToolCommon.json2Object(entity).getString("userId");

		ToolOutFormMap toolOutFormMap=new ToolOutFormMap();
		toolOutFormMap.set("content",contentS);
		toolOutFormMap.set("toolTypeId",toolTypeId);
		toolOutFormMap.set("startTime",startTime);
		toolOutFormMap.set("endTime",endTime);
		toolOutFormMap.set("userId",userId);

		List<ToolOutFormMap> heatTreatFormMaps=toolOutMapper.findByAllLike(toolOutFormMap);

		//excel标题
		String[] title = {"品名规格",
				"种类",
				"领料人",
				"领料时间",
				"领料数量",
				"领料金额",
				"备注"};

		//excel文件名
		String fileName ="工具出库统计" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[heatTreatFormMaps.size()][title.length];

		for (int i = 0; i < heatTreatFormMaps.size(); i++) {
			content[i] = new String[title.length];
			ToolOutFormMap obj = heatTreatFormMaps.get(i);
			content[i][0] = obj.get("toolName")+"";
			content[i][1] = obj.get("toolTypeName")+"";
			content[i][2] = obj.get("userShow")+"";
			content[i][3] = obj.get("time")+"";
			content[i][4] = obj.get("amount")+"";
			content[i][5] = obj.get("money")+"";
			content[i][6] = obj.get("remarks")+"";
		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("工具出库统计",sheetName, title, content, null);

		//响应到客户端
		try {
			ExcelUtils.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("addUI")
	public String addUI(Model model) {
		String toolId=getPara("toolId");
		model.addAttribute("toolId",toolId);
		return Common.BACKGROUND_PATH + "/system/toolOut/add";
	}
	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<ToolOutFormMap> list = (List) ToolCommon.json2ObjectList(entity, ToolOutFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			ToolOutFormMap toolOutFormMap=list.get(i);
			String id=toolOutFormMap.get("id")+"";
			toolOutFormMap.set("modifytime",nowtime);
			toolOutFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				toolOutFormMap.remove("id");
				toolOutMapper.addEntity(toolOutFormMap);
			}else{
				toolOutMapper.editEntity(toolOutFormMap);
			}
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="工具管理",methods="工具库存-出库")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String entity) throws Exception {
		ToolOutFormMap toolOutFormMap=getFormMap(ToolOutFormMap.class);
		toolOutMapper.addEntity(toolOutFormMap);
		String amount=toolOutFormMap.get("amount")+"";
		float amountF=ToolCommon.StringToFloat(amount);
		String toolId=toolOutFormMap.get("toolId")+"";
		toolStockMapper.reduceAmountByToolId(amountF,toolId);
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
				toolOutMapper.deleteByIds(ids);
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

}