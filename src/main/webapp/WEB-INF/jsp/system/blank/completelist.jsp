<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/completelist.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true"  style="width:auto"></table>

    <div style="display: none">
        <table id="ttselect" style="width:auto;"></table>
    </div>
    <div id="tb" style="height:auto">
        <input type="text" style="display: none" id="clientId" >
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="javascript:void(0)" onclick="makeHeattreat('调质')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-redo'">生成调质</a>
        <a href="javascript:void(0)" onclick="makeHeattreat('正火')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-redo'">生成正火</a>
        员工:
        <select id="userId" style="width:100px" class="easyui-combogrid"  data-options="
                        panelWidth: 200,
                        idField: 'id',
                        textField: 'userName',
                        mode: 'remote',
                        url: '${pageContext.request.contextPath}/user/userSelect.shtml',
                        method: 'get',
                        columns: [[
                            {field:'id',hidden:true,title:'编号',width:80},
                            {field:'accountName',title:'账号',width:120},
                            {field:'userName',title:'姓名',width:120},

                        ]],
                        fitColumns: true,
                        onSelect:function(rowIndex, rowData){
                           setContentToInputById('user',rowData.userName);
                        }"></select>
        <input id="user" placeholder="请输入员工姓名" style="display: none">
        <input id="content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;margin-top:5px;width: 100px">
        <input  id="starttime" type="text">
        至
        <input id="endtime" type="text">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <a href="#" class="easyui-linkbutton" onclick="print()" title="打印工资条" iconCls="icon-print" plain="true">打印工资条</a>
        <br>
        <input style="display: none" id="roleId" type="text" value="${user.roleId}" />
        <input style="display: none"  id="roleName" type="text" value="${user.roleName}" />
            <span id="allMoney">合计:</span>
        <span style="font-size: 18px;font-weight: bold"  id="amountSum"></span>
        <script>
            function print() {
                var user=getValueById("user");
                var startTime=getDataboxValue("starttime");
                var endTime=getDataboxValue("endtime");
                $("#printOrder").window({
                    width:500,
                    title:'工资条',
                    modal: true,
                    top:50,
                    href: rootPath + '/blank/wagesPrintUI.shtml?user='+user+"&startTime="+startTime+"&endTime="+endTime,
                    onClose:function () {

                    }
                });
            }

            var roleId=getValueById("roleId");
            var roleName=getValueById("roleName");
            if(roleId=='16'||isContain(roleName,'班组')){
                getObjectById("allMoney").style.display="none";
            }

            function makeHeattreat(origin) {
                var rows=getDatagridSelections('tt');
                var ids="";
                for(i=0;i<rows.length;i++){
                    ids=ids+rows[i].id+",";
                }
                if(ids!=""){
                            communateGet(rootPath +'/blank/makeHeattreatEntity.shtml?ids='+ids+'&origin='+origin,function back(data){
                                if(data=='success'){
                                   showMessagerCenter("提示","操作成功");
                                   reloadDatagridMessage("tt");
                                }else{
                                    showErrorAlert("操作",data);
                                }
                            });
                }else{
                    $.messager.alert('警告','不存在可以删除数据','warning');
                }
            }
        </script>
    </div>
</div>
<script>
    var endtime=getNowDate();
    starttime=getNowMonth()+"-01";
    initInputDataInput("starttime");
    initInputDataInput("endtime");
    setDataToDateBox("starttime",starttime);
    setDataToDateBox("endtime",endtime);

    var taxRate=getValueById("taxRate");
    taxRate=VarToFloat(taxRate);
    function selectClient(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"clientId",rowData.id);
            setContentToInputById("clientId",rowData.id);
            setContentToDatagridEditorText('tt',editId,"clientFullName",rowData.fullName);
            var ed=getDatagridEditorObj('tt',editId,'goodMapNumber');
            if(ed!=null){
                ed.combogrid('grid').datagrid('options').url =  '${pageContext.request.contextPath}/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId');
                ed.combogrid('grid').datagrid('reload');
            }
        }
    }
    function selectGood(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"goodId",rowData.id);
            setContentToDatagridEditorText('tt',editId,"goodMapNumber",rowData.mapNumber);
            setContentToDatagridEditorText('tt',editId,"goodName",rowData.name);
            setContentToDatagridEditorText('tt',editId,"goodSize",rowData.goodSize);
            setContentToDatagridEditorText('tt',editId,"materialQuality",rowData.materialQuality);
        }
    }
    function editAndAddSetContentAndChange() {
        setEditorOnChange("tt",0,function onChangeBack(index,content) {
            var rows = $('#tt').datagrid('getRows');
            var badAmount=rows[editId].badAmount;
            var taxPrice=rows[editId].taxPrice;
            var deductRate=getComboboxContentByEditor('tt','deductRate');
            var deductWages=VarToFloat(taxPrice)*VarToFloat(badAmount)*VarToFloat(deductRate);
            setContentToDatagridEditorText("tt",editId,'deductWages',deductWages);
            var trueWages=VarToFloat(content)-VarToFloat(deductWages);
            trueWages=floatToVar2(trueWages);
            setContentToDatagridEditorText("tt",editId,'trueWages',trueWages);
        });

    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
<div id="printOrder">

</div>
</body>