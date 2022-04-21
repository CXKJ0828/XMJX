<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<style>
    .tdShow{
        width:20%;
        height: 40px;
        text-align: left;
    }
    .spanShow{
        width: 30%!important;
    }
    .inputShow{
        width: 70%;
    }
</style>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',split:true" >
        <a href="#" class="easyui-linkbutton" onclick="saveEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>
        <table border="1"  cellspacing="5" style="border-color:gainsboro">
            <tbody>
            <tr>
                <td class="tdShow"><span class="spanShow">客户：</span>
                    <input id="clientId" style="display: none" type="text" class="inputShow easyui-validatebox"  value="${order.clientId}">
                    <input id="clientName" style="width: 250px" type="text"   value="${order.clientName}"> </td>
                <td class="tdShow"><span class="spanShow">订单编号：</span>
                    <input id="id" style="display: none" type="text" class="inputShow easyui-validatebox"  value="${order.id}">
                    <input id="contractNumber" type="text" class="inputShow easyui-validatebox"  value="${order.contractNumber}"> </td>
                <td class="tdShow"><span class="spanShow">来单日期：</span>
                    <input id="makeTime" type="text" value="${order.makeTime}"></td>

                <input style="display: none;" id="state" type="text" class="inputShow easyui-validatebox"  value="${order.state}">
            </tr>
            <script>
                initInputDataInput("makeTime");
                $('#clientName').combogrid({
                    panelWidth:400,
                    idField:'fullName',
                    textField:'fullName',
                    url: '${pageContext.request.contextPath}/client/clientSelect.shtml',
                    columns:[[
                        {field:'id',hidden:true,title:'客户编码',width:100},
                        {field:'fullName',title:'客户全称',width:350},
                    ]],
                    onSelect:selectClient,
                    onShowPanel: function () {

                    }
                });
                function selectClient(rowIndex, rowData) {
                    setContentToInputById("clientId",rowData.id);
                    setContentToInputById("clientName",rowData.fullName);
                    var ed=getDatagridEditorObj('orderdetails',editId,'mapNumber');
                    if(ed!=null){
                        ed.combogrid('grid').datagrid('options').url =  '${pageContext.request.contextPath}/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId')
                        ed.combogrid('grid').datagrid('reload');
                    }
                }
            </script>
            <tr style="display: none;">
                <td class="tdShow"><span class="spanShow">制表时间：</span><input id="modifytime" type="text" class="inputShow easyui-validatebox"  value="${order.modifytime}"> </td>
                <td class="tdShow"><span class="spanShow">制表用户：</span><input id="username" type="text" class="inputShow easyui-validatebox"  value="${order.userName}"> </td>
                <td class="tdShow" colspan="2"><span class="spanShow">备注：</span><input id="remarks" type="text" class="inputShow easyui-validatebox"  value="${order.remarks}"> </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="tbdetails">
       <span style="margin-left: 20px">
            金额合计:<span style="font-size: 14px;color: red" id="moneyAll"></span>
       </span>
    </div>
    <div data-options="region:'center'" style="height:auto;">
        <table id="orderdetails"
               title="订单明细"
               class="easyui-datagrid"
               style="width:auto;height:auto"
               data-options="rownumbers:true,
            fitColumns:true,
             toolbar: '#tbdetails',
            fit:true,
            singleSelect:true,
           url:'${pageContext.request.contextPath}/order/getOrderDetailsByOrderId.shtml?id='+${order.id}">
            <thead>
            <tr>
                <th data-options="field:'id',hidden:true,width:200,align:'center',editor:'text'">编号</th>
                <th data-options="field:'goodId',hidden:true,width:200,align:'center',editor:'text'">产品id</th>
                <th class="easyui-combogrid" data-options="field:'mapNumber',width:200,align:'center',editor:{type:'combogrid',
            options: {panelWidth:400,
			idField: 'mapNumber',
			textField: 'mapNumber',
			url: '${pageContext.request.contextPath}/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId'),
			mode: 'remote',
			pagePosition:top,
			 pagination:true,
			columns: [[
				{field:'id',hidden:true,title:'编码',width:150,align:'center'},
				 {field:'mapNumber',title:'图号',width:150,align:'center'},
            {field:'name',hidden:true,title:'产品名称',width:150,align:'center'},
            {field:'roughcastSize',hidden:true,title:'毛坯尺寸',width:150,align:'center'},
            {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
            {field:'goodSize',hidden:true,title:'成品尺寸',width:100,align:'center'},
            {field:'roughcastWeight',hidden:true,title:'毛坯重量',width:150,align:'center'},
            {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
            {field:'taxPrice',hidden:true,title:'含税单价',width:150,align:'center'},
            {field:'nottaxPrice',hidden:true,title:'不含税单价',width:150,align:'center'},
			]],
			fitColumns: true,
			onSelect:selectGood
			}
		}">产品图号</th>
                <th data-options="field:'goodName',width:200,align:'center',editor:'text'">产品名称</th>
                <th data-options="field:'goodSize',width:200,align:'center',editor:'text'">规格型号</th>
                <th data-options="field:'amount',width:200,align:'center',editor:'text'">数量</th>
                <th data-options="field:'ordertaxPrice',width:200,align:'center',editor:'text'">含税单价</th>
                <th data-options="field:'ordernottaxPrice',width:200,align:'center',editor:'text'">不含税单价</th>
                <th data-options="field:'money',width:200,align:'center',editor:'text'">金额</th>
                <th data-options="field:'deliveryTime',width:200,align:'center',editor:'text'">交货日期</th>
                <th data-options="field:'remakrs',width:200,align:'center',editor:'text'">备注</th>
                <th data-options="field:'taxPrice',width:200,align:'center',editor:'text'">公司含税价</th>
                <th data-options="field:'nottaxPrice',width:200,align:'center',editor:'text'">公司不含税价</th>
                <th data-options="field:'modifytime',hidden:true,width:200,align:'center',editor:'text'">修改时间</th>
                <th data-options="field:'userName',hidden:true,width:200,align:'center',editor:'text'">修改人</th>
                <th data-options="field:'state',width:80,align:'center',editor:'text'">状态</th>
                <th data-options="field:'orderId',hidden:true,width:200,align:'center',editor:'text'">订单id</th>
                <th data-options="field:'materialQuality',hidden:true,width:200,align:'center',editor:'text'">材质</th>
            </tr>
            </thead>
        </table>
    </div>
    <script>
        initComboGridEditor();
        function selectGood(rowIndex, rowData) {
            if(checkIsSelectEd(editId)){
                setContentToDatagridEditorText('orderdetails',editId,"materialQuality",rowData.materialQuality);
                setContentToDatagridEditorText('orderdetails',editId,"goodId",rowData.id);
                setContentToDatagridEditorText('orderdetails',editId,"goodName",rowData.name);
                setContentToDatagridEditorText('orderdetails',editId,"roughcastSize",rowData.roughcastSize);
                setContentToDatagridEditorText('orderdetails',editId,"taxPrice",rowData.taxPrice);
                setContentToDatagridEditorText('orderdetails',editId,"nottaxPrice",rowData.nottaxPrice);
            }
        }

        $("#orderdetails").datagrid({
            onLoadSuccess: function () {
                setContentToDivSpanById("moneyAll", floatToVar2(compute('orderdetails',"money")));
            },

            onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
                e.preventDefault(); //阻止浏览器捕获右键事件
                if(editId!=null){
                    endEditDatagridByIndex('orderdetails',editId)
                }
                clearSelectAndSelectRowDatagrid('orderdetails',rowIndex);
                editId=rowIndex;
                showMenu("rightMenuGoodCode",e.pageX,e.pageY);
                e.preventDefault();  //阻止浏览器自带的右键菜单弹出
            },
            onHeaderContextMenu: function (e, field){ //右键时触发事件                      
                e.preventDefault(); //阻止浏览器捕获右键事件
                if(editId!=null){
                    endEditDatagridByIndex('orderdetails',editId)
                }
                showMenu("rightMenuHeaderGoodCode",e.pageX,e.pageY);
                e.preventDefault();  //阻止浏览器自带的右键菜单弹出
            }
        })
        function addContentOrderDetails() {
            if(editId!=null||editId!=""){
                endEditDatagridByIndex("orderdetails",editId);
            }
            $('#orderdetails').datagrid('appendRow',
                {
                    "orderId":getValueById("id")
                }
            );
            var rows = getAllRowsContent("orderdetails");
            editId=rows.length-1;
            beginEditDatagridByIndex('orderdetails',editId);

            var ed=getDatagridEditorObj('orderdetails',editId,'mapNumber');
            if(ed!=null){
                ed.combogrid('grid').datagrid('options').url =  '${pageContext.request.contextPath}/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId')
                ed.combogrid('grid').datagrid('reload');
            }
            editAndAddSetContentAndChange();
        }
        function editOrderDetails() {
            beginEditDatagridByIndex('orderdetails',editId);
            var ed=getDatagridEditorObj('orderdetails',editId,'mapNumber');
            if(ed!=null){
                ed.combogrid('grid').datagrid('options').url =  '${pageContext.request.contextPath}/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId')
                ed.combogrid('grid').datagrid('reload');
            }
            editAndAddSetContentAndChange();
        }
        function delOrderDetails() {
            var rowData=getDatagridJsonByIndexAndId("orderdetails",editId);
            var url='${pageContext.request.contextPath}/order/deleteDetailsEntity.shtml?id='+rowData.id;
            if(editId!=null){
                communateGet(url,function back(data){
                    reloadDatagridMessage('orderdetails');
                });
            }else{
                $.messager.alert('警告','不存在可以删除数据','warning');
            }
        }


        function editAndAddSetContentAndChange() {
            setEditorOnChange("orderdetails",5,function onChangeBack(index,content) {
                ordertaxPrice=getContentByEditor("orderdetails",6);
                setContentToDatagridEditorText("orderdetails",editId,'money',VarToFloat(content)*VarToFloat(ordertaxPrice));
            });

            setEditorOnChange("orderdetails",6,function onChangeBack(index,content) {
                taxPrice=getContentByEditor("orderdetails",11);
                if(VarToFloat(content)!=VarToFloat(taxPrice)){
                    showErrorAlert("警告","含税单价与公司含税单价不一致，请重新输入");
                    setContentToDatagridEditorText("orderdetails",editId,'ordertaxPrice',"");
                }else{
                    amount=getContentByEditor("orderdetails",5);
                    setContentToDatagridEditorText("orderdetails",editId,'money',floatToVar2(VarToFloat(content)*VarToFloat(amount)));
                }
            });
            setEditorOnChange("orderdetails",7,function onChangeBack(index,content) {
                nottaxPrice=getContentByEditor("orderdetails",12);
                if(VarToFloat(content)>0&&VarToFloat(content)!=VarToFloat(nottaxPrice)){
                    showErrorAlert("警告","不含税单价与公司不含税单价不一致，请重新输入");
                    setContentToDatagridEditorText("orderdetails",editId,'ordernottaxPrice',"");
                }
            });

        }

    </script>
</div>
</div>
<%@include file="/rightOpration.jsp"%>

<div id="rightMenuGoodCode" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContentOrderDetails()">新增行</div>
    <div  data-options="iconCls:'icon-edit'" onclick="editOrderDetails()">修改</div>
    <div  data-options="iconCls:'icon-remove'" onclick="delOrderDetails()">删除</div>         
</div>
<div id="rightMenuHeaderGoodCode" class="easyui-menu" style="width: 50px; display: none;">    
    <div  data-options="iconCls:'icon-add'" onclick="addContentOrderDetails()">新增行</div>
</div>
</body>