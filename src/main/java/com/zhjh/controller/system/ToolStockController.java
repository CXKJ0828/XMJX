package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.entity.ToolStockFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.file.ExcelUtils;
import com.zhjh.mapper.SystemconfigMapper;
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
@RequestMapping("/toolStock/")
public class ToolStockController extends BaseController {
	@Inject
	private ToolStockMapper toolStockMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@RequestMapping("list")
	public String list(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/toolStock/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public ToolStockFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		ToolStockFormMap toolStockFormMap=new ToolStockFormMap();
		if(content==null){
			content="";
		}
		String toolTypeId=getPara("toolTypeId");
		toolStockFormMap.set("content",content);
		toolStockFormMap.set("toolTypeId",toolTypeId);
		if(page.equals("1")){
			total=toolStockMapper.findCountByAllLike(toolStockFormMap);
		}
		int sumAmount=toolStockMapper.findSumAmountByAllLike(toolStockFormMap);
		toolStockFormMap=toFormMap(toolStockFormMap, page, rows,toolStockFormMap.getStr("orderby"));
		List<ToolStockFormMap> toolStockFormMaps=toolStockMapper.findByAllLike(toolStockFormMap);
		toolStockFormMap.set("rows",toolStockFormMaps);
		toolStockFormMap.set("total",total);
		toolStockFormMap.set("sumAmount",sumAmount);
		return toolStockFormMap;
	}

	@RequestMapping(value = "exportAll")
	@ResponseBody
	@SystemLog(module="工具管理",methods="工具库存-全部导出")
	public void exportAll(HttpServletResponse response, String entity) throws Exception {

		String contentS=ToolCommon.json2Object(entity).getString("content");
		String toolTypeId=ToolCommon.json2Object(entity).getString("toolTypeId");
		ToolStockFormMap toolStockFormMap=new ToolStockFormMap();
		toolStockFormMap.set("toolTypeId",toolTypeId);
		toolStockFormMap.set("content",contentS);

		List<ToolStockFormMap> heatTreatFormMaps=toolStockMapper.findByAllLike(toolStockFormMap);

		//excel标题
		String[] title = {"品名规格",
				"种类",
				"单位",
				"数量",
				"备注"};

		//excel文件名
		String fileName ="工具库存" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[heatTreatFormMaps.size()][title.length];

		for (int i = 0; i < heatTreatFormMaps.size(); i++) {
			content[i] = new String[title.length];
			ToolStockFormMap obj = heatTreatFormMaps.get(i);
			content[i][0] = obj.get("name")+"";
			content[i][1] = obj.get("typeName")+"";
			content[i][2] = obj.get("unitName")+"";
			content[i][3] = obj.get("amount")+"";
			content[i][4] = obj.get("remarks")+"";
		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("工具库存",sheetName, title, content, null);

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

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="工具管理",methods="工具库存-修改保存")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		String errorMessage="";
		List<ToolStockFormMap> list = (List) ToolCommon.json2ObjectList(entity, ToolStockFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			ToolStockFormMap toolStockFormMap=list.get(i);
			String id=toolStockFormMap.get("id")+"";
			toolStockFormMap.set("modifytime",nowtime);
			toolStockFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				String toolId=toolStockFormMap.get("toolId")+"";
				String name=toolStockFormMap.get("name")+"";
				ToolStockFormMap toolFormMap1=toolStockMapper.findByToolId(toolId);
				if(toolFormMap1==null){
					toolStockFormMap.remove("id");
					toolStockMapper.addEntity(toolStockFormMap);
				}else{
					errorMessage=errorMessage+"["+name+"]品名已存在库存;";
				}

			}else{
				toolStockMapper.editEntity(toolStockFormMap);
			}
		}
		if(ToolCommon.isNull(errorMessage)){
			return "success";
		}else{
			return errorMessage;
		}
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
				toolStockMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

}