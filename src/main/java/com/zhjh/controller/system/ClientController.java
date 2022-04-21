package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.ComboboxEntity;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.ClientFormMap;
import com.zhjh.entity.GoodFormMap;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.entity.UserFormMap;
import com.zhjh.mapper.ClientMapper;
import com.zhjh.mapper.GoodMapper;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/client/")
public class ClientController extends BaseController {
	@Inject
	private ClientMapper clientMapper;

	@Inject
	private GoodMapper goodMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;
	private int listSize;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		ClientFormMap clientFormMap = getFormMap(ClientFormMap.class);
		return Common.BACKGROUND_PATH + "/system/client/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public ClientFormMap findByPage(String content) throws Exception {
		ClientFormMap clientFormMap = getFormMap(ClientFormMap.class);
		if(content==null){
			content="";
		}
		clientFormMap.set("content",content);
       List<ClientFormMap> clientFormMapList=clientMapper.findByAllLike(clientFormMap);
		clientFormMap.set("rows",clientFormMapList);
		return clientFormMap;
	}

	@ResponseBody
	@RequestMapping("clientSelectTwoContent")
	public List<ComboboxEntity>  clientSelectTwoContent(String q) throws Exception {
		if(q==null){
			q="";
		}
		List<ClientFormMap> clientFormMaps=clientMapper.findByFullNameLike(q);
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<clientFormMaps.size();i++){
			ClientFormMap roleFormMap1=clientFormMaps.get(i);
			if(roleFormMap1!=null){
				ComboboxEntity entity=new ComboboxEntity();
				entity.id=roleFormMap1.get("id")+"";
				entity.text=roleFormMap1.getStr("fullName");
				comboboxEntityList.add(entity);
			}

		}
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="不限";
		entity.text="不限";
		comboboxEntityList.add(entity);
		return comboboxEntityList;
	}

	@ResponseBody
	@RequestMapping("clientSimpleNameSelectTwoContent")
	public List<ComboboxEntity>  clientSimpleNameSelectTwoContent(String q) throws Exception {
		if(q==null){
			q="";
		}
		List<ComboboxEntity> clientFormMaps=clientMapper.findSimpleNameText(q);
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="不限";
		entity.text="不限";
		clientFormMaps.add(entity);
		return clientFormMaps;
	}

	@ResponseBody
	@RequestMapping("clientSelect")
	public ClientFormMap  clientSelect(String p) throws Exception {
		ClientFormMap  clientFormMap  = getFormMap(ClientFormMap .class);
		String q=getPara("q");
		if(q==null){
			q="";
		}
		clientFormMap.set("content",q);
		List<ClientFormMap > departmentFormMapList=clientMapper.findByFullNameLike(q );
		clientFormMap.set("rows",departmentFormMapList);
		return clientFormMap ;
	}

	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/client/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统基础管理",methods="客户管理-新增修改")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String entity) throws Exception {
		List<ClientFormMap> list = (List) ToolCommon.json2ObjectList(entity, ClientFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			ClientFormMap clientFormMap=list.get(i);
			String id=clientFormMap.get("id")+"";
			clientFormMap.set("modifyTime",nowtime);
			clientFormMap.set("userId",userId);
				if(id.equals("")){
					clientFormMap.remove("id");
					clientMapper.addEntity(clientFormMap);
				}else{
					String taxRateOld=clientFormMap.getStr("taxRateOld");
					String taxRate=clientFormMap.getStr("taxRate");
					clientMapper.editEntity(clientFormMap);
					String taxFreeRatioOld=clientFormMap.get("taxFreeRatioOld")+"";
					String taxFreeRatio=clientFormMap.get("taxFreeRatio")+"";
					List<GoodFormMap> goodFormMapList=goodMapper.findByClientId(id);
					updateGoodPriceByChange(goodFormMapList,taxRate,taxRateOld,taxFreeRatioOld,taxFreeRatio);
				}
		}
		return "success";
	}

	public void updateGoodPriceByChange(List<GoodFormMap> goodFormMapList,
										String taxRate,
										String taxRateOld,
										String taxFreeRatioOld,
										String taxFreeRatio){
		try {
			for(int j=0;j<goodFormMapList.size();j++){
				boolean isUpdateGood=false;
				GoodFormMap goodFormMap=goodFormMapList.get(j);
				String nottaxPrice=goodFormMap.getStr("nottaxPrice");
				float taxPrice=0;
				if(ToolCommon.StringToFloat(taxFreeRatioOld)==ToolCommon.StringToFloat(taxFreeRatio)){

				}else{
					nottaxPrice=ToolCommon.FloatToMoney(ToolCommon.StringToFloat(nottaxPrice)*ToolCommon.StringToFloat(taxFreeRatio))+"";
					goodFormMap.set("nottaxPrice",nottaxPrice);
					if(ToolCommon.isNull(taxRate)){
						SystemconfigFormMap systemconfigFormMapResult=systemconfigMapper.findByName("taxRate");
						taxRate=systemconfigFormMapResult.get("content")+"";
					}
					taxPrice=ToolCommon.StringToFloat(nottaxPrice)*(1+ToolCommon.StringToFloat(taxRate));
					goodFormMap.set("taxPrice",taxPrice);
					isUpdateGood=true;
				}
				if(ToolCommon.StringToFloat(taxRateOld)==ToolCommon.StringToFloat(taxRate)){

				}else{
					taxPrice=ToolCommon.StringToFloat(nottaxPrice)*(1+ToolCommon.StringToFloat(taxRate));
					goodFormMap.set("taxPrice",taxPrice);
					isUpdateGood=true;
				}
				if(isUpdateGood){
					goodMapper.editEntity(goodFormMap);
				}
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
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
			clientMapper.deleteById(id);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("client", clientMapper.findbyFrist("id", id, ClientFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/client/edit";
	}

}