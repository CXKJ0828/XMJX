package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.RoleFormMap;
import com.zhjh.entity.SupplierFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.entity.UserRoleFormMap;
import com.zhjh.mapper.RoleMapper;
import com.zhjh.mapper.SupplierMapper;
import com.zhjh.mapper.UserMapper;
import com.zhjh.mapper.UserRoleMapper;
import com.zhjh.plugin.PageView;
import com.zhjh.tool.ToolCommon;
import com.zhjh.tool.ToolExcel;
import com.zhjh.util.Common;
import com.zhjh.util.ExcelUtil;
import com.zhjh.util.PasswordHelper;
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
import java.util.List;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/supplier/")
public class SupplierController extends BaseController {
	@Inject
	private SupplierMapper supplierMapper;

	@Inject
	private UserMapper userMapper;

	@Inject
	private UserRoleMapper userRoleMapper;

	@Inject
	private RoleMapper roleMapper;

	int total=0;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/supplier/list";
	}

	@ResponseBody
	@RequestMapping("upload")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
						 HttpServletRequest request) throws Exception {
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		SupplierFormMap supplierFormMap=new SupplierFormMap();
		boolean isAdd=false;
		if(file!=null){
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet= ExcelUtil.readRowsAndColums(fileBean.getPath());
			int lastColNum=0;
			for(int i=1;i<=sheet.getLastRowNum();i++){
				Row row = null;
				row = sheet.getRow(i);
				if(row!=null){
					lastColNum = row.getLastCellNum();
					SupplierFormMap supplierFormMap1=new SupplierFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									isAdd=false;
									j=lastColNum;
								}else{
									isAdd=true;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 0:
									supplierFormMap1.set("code",value);
									break;
								case 1:supplierFormMap1.set("name",value);break;
								case 2:supplierFormMap1.set("simpleName",value);break;
								case 3:supplierFormMap1.set("address",value);break;
								case 4:supplierFormMap1.set("remark",value);break;
								case 5:supplierFormMap1.set("isUse",value);break;
								case 6:supplierFormMap1.set("organization",value);break;
								case 7:

									break;
							}
						}

					}
					if(isAdd){
						String code=supplierFormMap1.get("code")+"";
						if(code!=null&&!code.equals("")&&!code.equals("null")){
							supplierFormMap1.set("modifyTime",nowtime);
							supplierFormMap1.set("userId",userId);
							supplierMapper.addEntity(supplierFormMap1);
						}else{
							i=sheet.getLastRowNum();
						}

					}
				}
			}
		}
		return "success";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	public SupplierFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");

		System.out.print(rows);
		SupplierFormMap supplierFormMap =new SupplierFormMap();

		if(content==null){
			content="";
		}
		supplierFormMap.set("content",content);
		if(page.equals("1")){
			total=supplierMapper.findCountByAllLike(supplierFormMap);
		}
		supplierFormMap=toFormMap(supplierFormMap, page, rows,supplierFormMap.getStr("orderby"));
		supplierFormMap.set("total",total);
		List<SupplierFormMap> departmentFormMapList=supplierMapper.findByAllLike(supplierFormMap);
		supplierFormMap.set("rows",departmentFormMapList);
		return supplierFormMap;
	}

	@ResponseBody
	@RequestMapping("supplierSelect")
	public SupplierFormMap  workingConterSelect(String p) throws Exception {
		SupplierFormMap  supplierFormMap  = getFormMap(SupplierFormMap .class);
		String q=getPara("q");
		if(q==null){
			q="";
		}
		supplierFormMap.set("content",q);
		List<SupplierFormMap > departmentFormMapList=supplierMapper.findByAllLike(supplierFormMap );
		supplierFormMap.set("rows",departmentFormMapList);
		return supplierFormMap ;
	}

	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/supplier/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String entity) throws Exception {
		boolean isSuccess=true;
		List<SupplierFormMap> list = (List) ToolCommon.json2ObjectList(entity, SupplierFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			SupplierFormMap departmentFormMap=list.get(i);
			String id=departmentFormMap.get("id")+"";
				departmentFormMap.set("modifyTime",nowtime);
				departmentFormMap.set("userId",userId);
				if(id.equals("")){
					departmentFormMap.remove("id");
					supplierMapper.addEntity(departmentFormMap);
				}else{
					supplierMapper.editEntity(departmentFormMap);
				}
		}
			return "success";
	}
	/**
	 * 验证资源是否存在
	 *
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
	 * @param name
	 * @return
	 */
	@RequestMapping("supplierId_isExist")
	@ResponseBody
	public boolean supplierIdIsExist(String name) {

		SupplierFormMap supplierFormMap = new SupplierFormMap();
		supplierFormMap.put("supplierId", name);
		List<SupplierFormMap> list = baseMapper.findByNames(supplierFormMap);
		if (list == null || list.size()<=0) {
			return true;
		} else {
			return false;
		}
	}
	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		for (String id : idsStr) {
			supplierMapper.deleteById(id);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("supplier", supplierMapper.findbyFrist("id", id, SupplierFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/supplier/edit";
	}
	@ResponseBody
	@RequestMapping("getsupplierById")
	public SupplierFormMap getsupplierById(@RequestParam(required=true) String id) throws Exception {
		SupplierFormMap supplierFormMap=new SupplierFormMap();
		SupplierFormMap supplierFormMap1=supplierMapper.findbyFrist("id",id,SupplierFormMap.class);
		if(supplierFormMap1!=null){
			supplierFormMap.set("id","1");
			supplierFormMap.set("supplierId",supplierFormMap1.getStr("supplierId"));
		}else{
			supplierFormMap.set("id","0");
			supplierFormMap.set("result","所选员工不存在");
		}
		return supplierFormMap;
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-修改组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity() throws Exception {
		SupplierFormMap supplierFormMap = getFormMap(SupplierFormMap.class);
		supplierMapper.editEntity(supplierFormMap);
		return "success";
	}
	

}