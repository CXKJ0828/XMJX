package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.MaterialFormMap;
import com.zhjh.entity.MaterialweightFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.MaterialMapper;
import com.zhjh.mapper.MaterialweightMapper;
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
@RequestMapping("/materialweight/")
public class MaterialweightController extends BaseController {

	@Inject
	private MaterialweightMapper materialweightMapper;

	@Inject
	private MaterialMapper materialMapper;
	@RequestMapping("list")
	@SystemLog(module="物料管理",methods="物料统计-进入界面")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/materialweight/list";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	@SystemLog(module="物料管理",methods="物料统计-获取列表内容")
	public MaterialweightFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		MaterialweightFormMap materialweightFormMap = getFormMap(MaterialweightFormMap.class);
		if(content==null){
			content="";
		}
		materialweightFormMap.set("content",content);
		if(page.equals("1")){
			total=materialweightMapper.findCountByAllLike(materialweightFormMap);
		}
		materialweightFormMap=toFormMap(materialweightFormMap, page, rows,materialweightFormMap.getStr("orderby"));
		List<MaterialweightFormMap> departmentFormMapList=materialweightMapper.findByAllLike(materialweightFormMap);
		materialweightFormMap.set("rows",departmentFormMapList);
		materialweightFormMap.set("total",total);
		return materialweightFormMap;
	}


	@ResponseBody
	@RequestMapping("findAllByContent")
	public MaterialweightFormMap findAllByContent(String content) throws Exception {
		MaterialweightFormMap materialweightFormMap =new MaterialweightFormMap();
		if(content==null){
			content="";
		}
		materialweightFormMap.set("content",content);
		List<MaterialweightFormMap> departmentFormMapList=materialweightMapper.findByAllLike(materialweightFormMap);
		materialweightFormMap.set("rows",departmentFormMapList);
		return materialweightFormMap;
	}



	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="物料管理",methods="物料统计-编辑保存")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<MaterialweightFormMap> list = (List) ToolCommon.json2ObjectList(entity, MaterialweightFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			MaterialweightFormMap materialweightFormMap=list.get(i);
			String materialQuality=materialweightFormMap.get("materialQuality")+"";
			String outerCircle=materialweightFormMap.get("outerCircle")+"";
			String taxPrice=materialweightFormMap.get("taxPrice")+"";
			String id=materialweightFormMap.get("id")+"";
			materialweightFormMap.set("modifytime",nowtime);
			materialweightFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				MaterialFormMap materialFormMap=new MaterialFormMap();
				materialFormMap.set("materialQuality",materialQuality);
				materialFormMap.set("outerCircle",outerCircle);
				String materialId="";
				MaterialFormMap materialFormMap1=materialMapper.findByMaterialQualityAndOuterCircle(materialFormMap);
				if(materialFormMap1==null){
					materialFormMap.set("taxPrice",taxPrice);
					materialMapper.addEntity(materialFormMap);
					materialId=materialFormMap.get("id")+"";
				}else{
					materialId=materialFormMap1.get("id")+"";
					taxPrice=materialFormMap1.get("taxPrice")+"";
				}
				materialweightFormMap.set("materialId",materialId);
				materialweightFormMap.remove("id");
				float taxPriceF=ToolCommon.StringToFloat(taxPrice);
				float weight=ToolCommon.StringToFloat(materialweightFormMap.get("weight")+"");
				float money=taxPriceF*weight;
				materialweightFormMap.set("money",ToolCommon.FloatToMoney(money));
				materialweightMapper.addEntity(materialweightFormMap);
			}else{
				materialweightMapper.editEntity(materialweightFormMap);
				String materialId=materialweightFormMap.get("materialId")+"";
				MaterialFormMap materialFormMap=materialMapper.findbyFrist("id",materialId,MaterialFormMap.class);
				materialFormMap.set("taxPrice",taxPrice);
				materialMapper.editEntity(materialFormMap);

			}
		}
		return "success";
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
			materialweightMapper.deleteById(id);
		}
		return "success";
	}

}