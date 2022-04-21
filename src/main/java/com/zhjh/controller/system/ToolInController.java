package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.file.ExcelUtils;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.mapper.ToolInMapper;
import com.zhjh.mapper.ToolMapper;
import com.zhjh.mapper.ToolStockMapper;
import com.zhjh.tool.ToolCommon;
import com.zhjh.tool.ToolExcel;
import com.zhjh.util.Common;
import com.zhjh.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/toolIn/")
public class ToolInController extends BaseController {
	@Inject
	private ToolInMapper toolInMapper;
	@Inject
	private ToolMapper toolMapper;
	@Inject
	private ToolStockMapper toolStockMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@RequestMapping("list")
	public String list(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/toolIn/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public ToolInFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		ToolInFormMap toolInFormMap=new ToolInFormMap();
		if(content==null){
			content="";
		}
		String toolTypeId=getPara("toolTypeId");
		String startTime=getPara("startTime");
		String endTime=getPara("endTime");
		String userId=getPara("userId");
		toolInFormMap.set("content",content);
		toolInFormMap.set("toolTypeId",toolTypeId);
		toolInFormMap.set("startTime",startTime);
		toolInFormMap.set("endTime",endTime);
		toolInFormMap.set("userId",userId);
		if(page.equals("1")){
			total=toolInMapper.findCountByAllLike(toolInFormMap);
		}
		ToolInFormMap toolInFormMapSum=toolInMapper.findAmountAndMoneySumByAllLike(toolInFormMap);
		toolInFormMap=toFormMap(toolInFormMap, page, rows,toolInFormMap.getStr("orderby"));
		List<ToolInFormMap> toolInFormMaps=toolInMapper.findByAllLike(toolInFormMap);
		toolInFormMap.set("rows",toolInFormMaps);
		toolInFormMap.set("total",total);
		toolInFormMap.set("sumEntity",toolInFormMapSum);
		return toolInFormMap;
	}

	@RequestMapping(value = "exportAll")
	@ResponseBody
	@SystemLog(module="工具管理",methods="工具入库-全部导出")
	public void exportAll(HttpServletResponse response, String entity) throws Exception {

		String contentS=ToolCommon.json2Object(entity).getString("content");
		String toolTypeId=ToolCommon.json2Object(entity).getString("toolTypeId");
		String startTime=ToolCommon.json2Object(entity).getString("startTime");
		String endTime=ToolCommon.json2Object(entity).getString("endTime");

		ToolInFormMap toolInFormMap=new ToolInFormMap();
		toolInFormMap.set("content",contentS);
		toolInFormMap.set("toolTypeId",toolTypeId);
		toolInFormMap.set("startTime",startTime);
		toolInFormMap.set("endTime",endTime);

		List<ToolInFormMap> heatTreatFormMaps=toolInMapper.findByAllLike(toolInFormMap);

		//excel标题
		String[] title = {"品名规格",
				"种类",
				"单位",
				"数量",
				"单价",
				"金额",
				"入库时间",
				"备注"};

		//excel文件名
		String fileName ="工具入库统计" + System.currentTimeMillis() + ".xls";

		//sheet名
		String sheetName = "sheet1";

		String [][] content = new String[heatTreatFormMaps.size()][title.length];

		for (int i = 0; i < heatTreatFormMaps.size(); i++) {
			content[i] = new String[title.length];
			ToolInFormMap obj = heatTreatFormMaps.get(i);
			content[i][0] = obj.get("toolName")+"";
			content[i][1] = obj.get("toolTypeName")+"";
			content[i][2] = obj.get("unitName")+"";
			content[i][3] = obj.get("amount")+"";
			content[i][4] = obj.get("price")+"";
			content[i][5] = obj.get("money")+"";
			content[i][6] = obj.get("time")+"";
			content[i][7] = obj.get("remarks")+"";
		}

		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook("工具入库统计",sheetName, title, content, null);

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
	@RequestMapping("upload")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
		String errorMessage="";
		if(file!=null){
			boolean isAdd=false;
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			String deliveryTime="";
			String toolId="";
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				isAdd=true;
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					ToolInFormMap toolInFormMap=new ToolInFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value= ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									j=lastColNum+1;
									i=sheet1.getLastRowNum()+1;
								}else if(value.equals("结束")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 1:
									value=value.replace(" ","");
									ToolFormMap toolFormMap=toolMapper.findByName(value);
									if(toolFormMap==null){
										errorMessage=errorMessage+value+"工具不存在";
										j=lastColNum+1;
										isAdd=false;
									}else{
										toolId=toolFormMap.get("id")+"";
										toolInFormMap.set("toolId",toolId);
									}
									break;
								case 3:
									value=value.replace(" ","");
									toolInFormMap.set("amount",value);
									break;
								case 4:
									value=value.replace(" ","");
									toolInFormMap.set("price",value);;
									break;
								case 5:
									value=value.replace(" ","");
									value=value.replace(",","");
									toolInFormMap.set("money",value);;
									break;
								case 6:
									deliveryTime=value;
									deliveryTime=deliveryTime.replace("//","-");
									deliveryTime=deliveryTime.replace("/","-");
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String f = sdf.format(sdf.parse(deliveryTime));
									toolInFormMap.set("time",f);
									break;
								case 7:
									toolInFormMap.set("remarks",value);;
									break;
							}

						}else{
							i=sheet1.getLastRowNum()+1;
							j=lastColNum+1;
						}
					}
					if(isAdd){
						toolInFormMap.remove("id");
						toolInFormMap.set("userId",userId);
						toolInMapper.addEntity(toolInFormMap);
						addStock(toolInFormMap.get("toolId")+"",toolInFormMap.get("amount")+"");
					}
				}else{
					i=sheet1.getLastRowNum()+1;
				}
			}
		}
		if(errorMessage.equals("")){
			return "success";
		}else{
			return errorMessage;
		}

	}

	public void addStock(String toolId,String amount){
		try {
			float amountF=ToolCommon.StringToFloat(amount);
			ToolStockFormMap toolStockFormMap=toolStockMapper.findbyFrist("toolId",toolId,ToolStockFormMap.class);
			if(toolStockFormMap==null){
				toolStockFormMap=new ToolStockFormMap();
				toolStockFormMap.set("toolId",toolId);
				toolStockFormMap.set("amount",amount);
				toolStockMapper.addEntity(toolStockFormMap);
			}else{
				toolStockMapper.addAmountByToolId(amountF,toolId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="工具管理",methods="入库统计-修改保存")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<ToolInFormMap> list = (List) ToolCommon.json2ObjectList(entity, ToolInFormMap.class);
		for(int i=0;i<list.size();i++){
			ToolInFormMap toolInFormMap=list.get(i);
			String id=toolInFormMap.get("id")+"";
			if(id==null||id.equals("")||id.equals("null")){
				toolInFormMap.remove("id");
				toolInMapper.addEntity(toolInFormMap);
				addStock(toolInFormMap.get("toolId")+"",toolInFormMap.get("amount")+"");
			}else{
				String amount=toolInFormMap.get("amount")+"";
				String oldAmount=toolInFormMap.get("oldAmount")+"";
				toolInMapper.editEntity(toolInFormMap);
				addStock(toolInFormMap.get("toolId")+"",ToolCommon.StringToFloat(amount)-ToolCommon.StringToFloat(oldAmount)+"");
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
				toolInMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}

	}

}