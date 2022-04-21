<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/materialbuyorder/list.js"></script>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true" >
<div data-options="region:'west'" style="width:200px;">
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
    </div>
    <ul id="tt" class="easyui-tree" style="margin-top: 10px;margin-left: 10px"></ul>
</div>

<div data-options="region:'center'" style="height:100%;">
    <div style="height: 70%;">
        <table id="goodprocessList"
               class="easyui-datagrid"
               style="width:auto;"
               data-options="rownumbers:true,
                   toolbar:'#tboprate',
                   fit:true,
                   title:'订料明细',
                   view:detailview,
            detailFormatter:detailFormatter,
            onExpandRow:onExpandRow,
            fitColumns:true,
            url:rootPath+'/materialbuyorder/findByMaterialQualityAndMaterialbuyorderId.shtml',
            onBeforeLoad: function (params) {
                clientId=getContentsByCombobox('clientId').toString();
                clientId=clientId.replace('[','');
                 clientId=clientId.replace(']','');
                params.clientId=clientId;
                params.starttime=getDataboxValue('starttime');
                params.endtime=getDataboxValue('endtime');
				params.materialQualityName=materialQualityName;
				params.materialbuyorderId=materialbuyorderId;
				params.isLack=getContentBySelect('isLack')

        },
            onLoadSuccess:function(data){
                $('#goodprocessListExport').datagrid('loadData', data.rows);
                 setContentToDivSpanById('moneyAll', '金额:'+floatToVar2(computeAllMoney('goodprocessList','money'))+'  订料金额:'+floatToVar2(computeMoney('goodprocessList','buyMoney')));
                $('#sumShow').datagrid('loadData', data.rowsSum);
            },
             onDblClickRow:function (rowIndex, rowData) {
                        if(editId!=null){
                            endEditDatagridByIndex('goodprocessList',editId);
                        }else{
                        }
                        editId=rowIndex;
                        beginEditDatagridByIndex('goodprocessList',editId);
                        editAndAddSetContentAndChange();
                    },
            rowStyler: function(index,row){
                        var state=row.state;
                        if(state=='未订料'){

                        }else{
                          var backAmount=VarToFloat(row.backAmount);
                             var buyAmountStr=state.replace('已订料:','');
                            var buyAmount=VarToFloat(buyAmountStr);
                            if (backAmount>buyAmount){
                                return 'color:#048133;font-weight:bold';
                            }else{
                                return 'color:#DC143C;font-weight:bold';//红色欠料
                            }
                        }

                    }">
            <thead>
            <tr>
                <th data-options="field:'id',checkbox:true,width:100,align:'center'">编号</th>
                <th data-options="field:'materialQuality',width:200,align:'center',editor:'text'">材质</th>
                <th data-options="field:'outerCircle',width:200,align:'center',editor:'text'">规格/Φ</th>
                <th data-options="field:'length',width:200,align:'center'">长度/米</th>
                <th data-options="field:'weight',width:200,align:'center'">重量/吨</th>
                <th data-options="field:'lengthAll',hidden:true,width:200,align:'center'">长度/米总</th>
                <th data-options="field:'weightAll',hidden:true,width:200,align:'center'">重量/吨总</th>
                <th data-options="field:'money',width:200,align:'center',editor:'text', formatter: function(value,row,index){
                    taxPrice=row.taxPrice;
                    weight=row.weight;
                    money=VarToFloat(taxPrice)*VarToFloat(weight);
                    money=floatToVar2(money);
                   return money;
                   }
               ">金额</th>
                <th data-options="field:'stockLength',width:200,align:'center',editor:'text'">库存米数</th>
                <th data-options="field:'buyLength',width:150,align:'center',editor:'text'">订料米数</th>
                <th data-options="field:'buyWeight',width:150,align:'center',editor:'text'">订料重量</th>
                <th data-options="field:'taxPrice',width:150,align:'center'">含税单价</th>
                <th data-options="field:'buyMoney',width:150,align:'center',editor:'text', formatter: function(value,row,index){
                    taxPrice=row.taxPrice;
                    buyWeight=row.buyWeight;
                    money=VarToFloat(taxPrice)*VarToFloat(buyWeight);
                    money=floatToVar2(money);
                   return money;
                   }
               ">订料金额</th>
                <th data-options="field:'state',width:200,align:'center'">订料状态</th>
                <th data-options="field:'backstate',width:250,align:'center', formatter: function(value,row,index){
                    lackAmount=row.lackAmount;
                    backAmount=row.backAmount;
                    if(isNull(backAmount)){
                        return '';
                    }else{
                        return '已回料:'+backAmount+',余料:'+lackAmount;
                    }
                   }">回料状态</th>
                <th data-options="field:'buyTime',width:200,align:'center'">订料时间</th>
            </tr>
            </thead>
        </table>
    </div>
    <div style="height: 30%">
        <table id="sumShow"
               class="easyui-datagrid"
               style="width:auto;"
               data-options="rownumbers:true,
                   title:'材质统计',
                   toolbar:'#tbsum',
                   fit:true,
                    onLoadSuccess: function (data) {
                        setContentToDivSpanById('moneysumAll', '金额:'+floatToVar2(computeSumAllMoney('sumShow','money'))+'  订料金额:'+floatToVar2(computeSumBuyMoney('sumShow','buyMoney')));
                    },
            fitColumns:true">
            <thead>
            <tr>
                <th data-options="field:'materialQuality',width:200,align:'center',editor:'text'">材质</th>
                <th data-options="field:'length',width:200,align:'center'">长度/米</th>
                <th data-options="field:'weight',width:200,align:'center'">重量/吨</th>
                <th data-options="field:'lengthAll',hidden:true,width:200,align:'center'">长度/米总</th>
                <th data-options="field:'weightAll',hidden:true,width:200,align:'center'">重量/吨总</th>
                <th data-options="field:'money',width:200,align:'center',editor:'text'">金额</th>
                <th data-options="field:'buyLength',width:200,align:'center',editor:'text'">订料米数</th>
                <th data-options="field:'buyWeight',width:200,align:'center',editor:'text'">订料重量</th>
                <th data-options="field:'buyMoney',width:200,align:'center',editor:'text'">订料金额</th>
                <th data-options="field:'state',width:200,align:'center'">状态</th>
                <th data-options="field:'backstate',width:250,align:'center', formatter: function(value,row,index){
                    lackAmount=row.lackAmount;
                    backAmount=row.backAmount;
                    if(isNull(backAmount)){
                        return '';
                    }else{
                        return '已回料:'+backAmount+',余料:'+lackAmount;
                    }
                   }">回料状态</th>
            </tr>
            </thead>
        </table>
    </div>

            <div  style="display: none">
                <table id="goodprocessListExport"
                       class="easyui-datagrid"
                       data-options="rownumbers:true,
            fitColumns:true">
                    <thead>
                    <tr>
                        <th data-options="field:'materialQuality',width:200,align:'center'">材质</th>
                        <th data-options="field:'outerCircle',width:200,align:'center'">规格/Φ</th>
                        <th data-options="field:'length',width:200,align:'center'">长度/米</th>
                        <th data-options="field:'weight',width:200,align:'center'">重量/吨</th>
                        <th data-options="field:'buyLength',width:200,align:'center',editor:'text'">订料米数</th>
                        <th data-options="field:'buyWeight',width:200,align:'center'">订料重量</th>
                    </tr>
                    </thead>
                </table>
            </div>

            <script>
                function saveEntity() {
                    endEditDatagridByIndex('goodprocessList',editId);
                    var rows= $('#goodprocessList').datagrid('getChanges');
                    var url='${pageContext.request.contextPath}/materialbuyorder/editEntity.shtml';
                    var data={
                        rows:rows,
                    }
                    communatePost(url,ListToJsonString(data),function back(data){
                        reloadDatagridMessage("goodprocessList");
                    })
                }
            </script>
            <div id="tboprate" style="height:auto">
                 <span style="margin-left: 10px">
                   <span style="font-size: 14px;color: red" id="moneyAll"></span>
                 </span>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-sum',plain:true" onclick="selectAll()">全选</a>
                <a href="#" class="easyui-linkbutton" onclick="saveEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>
                <a href="#" class="easyui-linkbutton" onclick="buyEntity()" title="订料" iconCls="icon-ok" plain="true">订料</a>
                <a href="#" class="easyui-linkbutton" onclick="buy2Entity()" title="生成到订料单2" iconCls="icon-ok" plain="true">生成到订料单2</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="cancel()">取消</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="downLoad()">导出</a>
                <a href="#" class="easyui-linkbutton" onclick="deleteEntity()" title="删除" iconCls="icon-cancel" plain="true">删除</a>

                   客户:
                <input id="clientIdValue" style="display: none"  type="text" >
                <input id="clientId"  type="text" >
                交货日期:<input  id="starttime" style="width: 100px" type="text">
                至
                <input id="endtime" style="width: 100px" type="text">
                材质：
                <input  id="materialQuality" class="inputShow">
                状态:
                <select class="easyui-combobox" id='isLack' style="width:80px;">
                    <option value="不限"></option>
                    <option value="欠料">欠料</option>
                    <option value="未欠料">未欠料</option>
                    <option value="不限">不限</option>
                </select>
                <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
            </div>
    <div id="tbsum" style="height:auto">
                 <span style="margin-left: 10px">
                   <span style="font-size: 14px;color: red" id="moneysumAll"></span>
                 </span>
    </div>
            <script>
                var materialQualityName="";
                $('#materialQuality').combobox({
                    url:'${pageContext.request.contextPath}/materialbuyorder/materialQualitySelect.shtml',
                    valueField:'text',
                    textField:'text',
                    width:100,
                    onSelect: function(record){
                        materialQualityName=record.text;
                    }
                });


                function find() {
                    reloadDatagridMessage("goodprocessList");
                }

                initInputDataInput("starttime");
                initInputDataInput("endtime");
                $('#clientId').combobox({
                    url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
                    valueField:'id',
                    textField:'text',
                    multiple:true,
                    width:200,
                    onSelect: function(record){
//                        setContentToInputById('clientIdValue',record.id);
                    }
                });

                function selectAll() {
                    //获取数据列表中的所有数据
                    var rows = $("#goodprocessList").datagrid("getRows");
                    //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
                    for(var i=0;i<rows.length;i++){
                        var buyLength = rows[i].buyLength;
                        var buyLengthF=VarToFloat(buyLength);
                            if(buyLengthF>0){
                                var index = $("#goodprocessList").datagrid("getRowIndex",rows[i])
                                $("#goodprocessList").datagrid("checkRow",index);
                            }
                    }
                }
                function deleteEntity() {
                    var rows=getDatagridSelections('goodprocessList');
                    var ids="";
                    for(i=0;i<rows.length;i++){
                        ids=ids+rows[i].id+",";
                    }
                    if(ids!=""){
                        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
                            if (r){
                                communateGet(rootPath +'/materialbuyorder/deleteDetailsEntity.shtml?ids='+ids+'&password='+r+"&materialBuyOrderId="+materialbuyorderId,function back(data){
                                    if(data=='success'){
                                        reloadDatagridMessage("goodprocessList");
                                    }else{
                                        showErrorAlert("操作",data);
                                    }
                                });
                            }
                        });
                    }else{
                        $.messager.alert('警告','不存在可以删除数据','warning');
                    }
                }
                function buy2Entity() {
                    clientId=getContentsByCombobox('clientId').toString();
                    clientId=clientId.replace('[','');
                    clientId=clientId.replace(']','');
                        communateGet(rootPath +'/materialbuyorder/sureBuy2Entity.shtml?startTime='+getDataboxValue('starttime')+"&endTime="+getDataboxValue('endtime')+"&clientId="+clientId,function back(data){
                            reloadDatagridMessage("goodprocessList");
                        });
                }
                function buyEntity() {
                    var rows=getDatagridSelections('goodprocessList');
                    var ids="";
                    for(i=0;i<rows.length;i++){
                        ids=ids+rows[i].id+",";
                    }
                    if(ids!=""){
                        communateGet(rootPath +'/materialbuyorder/sureBuyEntity.shtml?ids='+ids,function back(data){
                            reloadDatagridMessage("goodprocessList");
                        });
                    }else{
                        $.messager.alert('警告','不存在可以订料数据','warning');
                    }

                }
                
                
                var editId;


                function computeMoney(id) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        row=rows[i];
                        taxPrice=row.taxPrice;
                        buyWeight=row.buyWeight;
                        money=VarToFloat(taxPrice)*VarToFloat(buyWeight);
                        money=floatToVar2(money);
                        total += StrToFloat(money);
                    }
                    total=Math.round(total*100)/100
                    return total;
                }
                function computeAllMoney(id) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        row=rows[i];
                        taxPrice=row.taxPrice;
                        weight=row.weight;
                        money=VarToFloat(taxPrice)*VarToFloat(weight);
                        money=floatToVar2(money);
                        total += StrToFloat(money);
                    }
                    total=Math.round(total*100)/100
                    return total;
                }

                function computeSumAllMoney(id) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        row=rows[i];
                        money=row.money;
                        money=floatToVar2(money);
                        total += StrToFloat(money);
                    }
                    total=Math.round(total*100)/100
                    return total;
                }
                function computeSumBuyMoney(id) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        row=rows[i];
                        money=row.buyMoney;
                        money=floatToVar2(money);
                        total += StrToFloat(money);
                    }
                    total=Math.round(total*100)/100
                    return total;
                }
                function computeByState(id,colName) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                            total += StrToFloat(rows[i][colName]);

                    }
                   if(total==0){

                   }else{
                       total=floatToVar2(total);
                   }
                    return total+"";
                }
                function editAndAddSetContentAndChange() {
                    var buyLength=getDatagridRows("goodprocessList")[editId].buyLength;
                    var taxPrice=getDatagridRows("goodprocessList")[editId].taxPrice;
                    setEditorOnChange("goodprocessList",4,function onChangeBack(index,content) {
                        var outerCircle=getContentByEditor("goodprocessList",1);
                        var outside=outerCircle.split("*")[0].replace("Φ","");
                        var inside=outerCircle.split("*")[1];
                        length=content;
                        if(isContain(inside,"Φ")){
                            inside=inside.replace("Φ","");
                        }else{
                            inside=inside;
                        }
                        outside=VarToFloat(outside);
                        inside=VarToFloat(inside);
                        length=VarToFloat(length);
                        var weight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
                        weight=floatToVar4(weight);
                        setContentToDatagridEditorText("goodprocessList",editId,'buyWeight',weight);
                        var money=weight*VarToFloat(taxPrice);
                        money=floatToVar2(weight*VarToFloat(taxPrice));
                        setContentToDatagridEditorText("goodprocessList",editId,'buyMoney',money);
                    });
                }
            </script>
</div>
</div>
</body>