<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/goodproductCategory/list.js"></script>
<body class="easyui-layout">
<div data-options="region:'north'">
    <input style="display: none" id="origin" value="${origin}">
    <form id="form" name="form"  method="post" enctype="multipart/form-data">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-sum',plain:true" onclick="selectAll()">全选</a>
        <a href="#" class="easyui-linkbutton" onclick="clearselectAll()" title="取消全选" iconCls="icon-undo" plain="true">取消全选</a>
        选择Excel：<input name="file" id="file" type="file" >
        <a href="#" class="easyui-linkbutton" onclick="upload()" title="导入" iconCls="page_go" plain="true">导入</a>
        <a href="javascript:void(0)"  onclick="sureComplete()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-redo'">生成完成</a>
        <a href="javascript:void(0)"  onclick="sureBack()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-redo'">生成回单</a>
        客户：
        <input  id="clientId" style="width:100px"  class="inputShow">
        材质：
        <input  id="materialQuality" style="width:100px"  class="inputShow">
        交货截止日期:<input  id="startTimeDelivery"  type="text">
        至
        <input id="endTimeDelivery"  type="text">
        完成日期:<input  id="startTimeComplete"  type="text">
        至
        <input id="endTimeComplete"  type="text">
        回单截止日期:<input  id="startTimeReceipt"  type="text">
        至
        <input id="endTimeReceipt"  type="text">
        回单日期:<input  id="startTimeBack"  type="text">
        至
        <input id="endTimeBack"  type="text">
        负责人:
        <input  id="leader" style="width:100px"  class="inputShow">
        是否完成:
        <select class="easyui-combobox" id='isComplete'  style="width:80px;">
            <option value="不限"></option>
            <option value="否">否</option>
            <option value="是">是</option>
            <option value="不限">不限</option>
        </select>
        是否超时:
        <select class="easyui-combobox" id='isOverTime'  style="width:80px;">
            <option value="不限"></option>
            <option value="否">否</option>
            <option value="是">是</option>
            <option value="不限">不限</option>
        </select>
        是否废品:
        <select class="easyui-combobox" id='isBad'  style="width:80px;">
            <option value="不限"></option>
            <option value="否">否</option>
            <option value="是">是</option>
            <option value="不限">不限</option>
        </select>
        废品责任人:
        <select id="badUserId" style="width:100px" class="easyui-combogrid"  data-options="
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
        超时责任人:
        <select id="overTimeUserId" style="width:100px" class="easyui-combogrid"  data-options="
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
        是否回单:
        <select class="easyui-combobox" id='isBack'  style="width:80px;">
            <option value="不限"></option>
            <option value="否">否</option>
            <option value="是">是</option>
            <option value="不限">不限</option>
        </select>
        原因关键字:<input id="reason" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <div id="sumShow">合计:</div>
    </form>
</div>
<div data-options="region:'west',split:true" style="width:120px;">
    <ul id="tt1"
        data-options="
            method:'get',animate:true"
    ></ul>
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" style="width:auto"></table>
    <div style="display: none;">
        <table id="ttselect" style="width:auto;"></table>
        <table id="ttexport" style="width:auto"></table>
    </div>
</div>
<script>
    $('#tt1').tree({
        url:'${pageContext.request.contextPath}/goodproductCategory/findClientByPage.shtml',
        onBeforeLoad: function (node,params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            var origin=getValueById('origin');
            params.origin=origin;
            params.reason=getValueById('reason');
            params.content=getValueById('content');
            params.materialQuality=getContentBySelect("materialQuality");
            params.startTimeDelivery=getDataboxValue('startTimeDelivery');
            params.endTimeDelivery=getDataboxValue('endTimeDelivery');
            params.startTimeComplete=getDataboxValue('startTimeComplete');
            params.endTimeComplete=getDataboxValue('endTimeComplete');
            params.startTimeBack=getDataboxValue('startTimeBack');
            params.endTimeBack=getDataboxValue('endTimeBack');
            params.startTimeReceipt=getDataboxValue('startTimeReceipt');
            params.endTimeReceipt=getDataboxValue('endTimeReceipt');
            params.leader=getContentBySelect("leader");
            params.clientId=getContentBySelect("clientId");
            params.isComplete=getContentBySelect("isComplete");
            params.isBack=getContentBySelect("isBack");
            params.isOverTime=getContentBySelect("isOverTime");
            params.isBad=getContentBySelect("isBad");
            params.overTimeUserId=getContentBySelect("overTimeUserId");
            params.badUserId=getContentBySelect("badUserId");
        },onBeforeSelect:function (node) {
            var clientId=node.id;
            setContentToCombobox("clientId",clientId);
            reloadDatagridMessage("tt");
        },
        onSelect:function(node){
            var childrenNodes = treeObjext.tree('getChildren',node.target);
            var lengthChild=childrenNodes.length;
            if(lengthChild>0){
                $(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
                node.state = node.state === 'closed' ? 'open' : 'closed';
            }
        },
    });

    function clearselectAll() {
        clearDatagridSelections('tt');
    }

    function selectAll() {

        //获取数据列表中的所有数据
        var rows = $("#tt").datagrid("getRows");
        //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
        for(var i=0;i<rows.length;i++){
            var index = $("#tt").datagrid("getRowIndex",rows[i])
                $("#tt").datagrid("checkRow",index);
        }
    }

    $('#leader').combobox({
        url:'${pageContext.request.contextPath}/goodproductCategory/leaderSelect.shtml',
        valueField:'id',
        textField:'text',
        width:130
    });

    function sureComplete() {
        isOprate=true;
        var rows=getDatagridSelections('tt');
        var ids="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
            var state=rows[i].isComplete;
            if(state=='是'){
                showErrorAlert("提示","选中数据已生成完成，请重新选择");
                isOprate=false;
                i=rows.length;
            }
        }
        if(ids!=""){
            if(isOprate){
                communateGet(rootPath +'/goodproductCategory/sureCompleteEntity.shtml?ids='+ids,function back(data){
                    if(data=='success'){
                        reloadDatagridMessage("tt");
                        showMessagerCenter("提示","操作成功");
                    }else{
                        showErrorAlert("操作",data);
                    }
                });
            }

        }else{
            $.messager.alert('警告','不存在可以操作数据','warning');
        }
    }
    function sureBack() {
        isOprate=true;
        var rows=getDatagridSelections('tt');
        var ids="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
            var state=rows[i].isBack;
            if(state=='是'){
                showErrorAlert("提示","选中数据已生成回单，请重新选择");
                isOprate=false;
                i=rows.length;
            }
        }
        if(ids!=""){
            if(isOprate){
                communateGet(rootPath +'/goodproductCategory/sureBackEntity.shtml?ids='+ids,function back(data){
                    if(data=='success'){
                        reloadDatagridMessage("tt");
                        showMessagerCenter("提示","操作成功");
                    }else{
                        showErrorAlert("操作",data);
                    }
                });
            }

        }else{
            $.messager.alert('警告','不存在可以操作数据','warning');
        }
    }
    $('#materialQuality').combobox({
        url:'${pageContext.request.contextPath}/materialQualityType/materialQualityTypeSelect.shtml',
        valueField:'id',
        textField:'name',
        width:100
    });

    $('#clientId').combobox({
        url:'${pageContext.request.contextPath}/client/clientSimpleNameSelectTwoContent.shtml',
        valueField:'id',
        textField:'text',
        width:130
    });

    initInputDataInput('startTimeDelivery');
    initInputDataInput('endTimeDelivery');
    initInputDataInput('startTimeComplete');
    initInputDataInput('endTimeComplete');
    initInputDataInput('startTimeBack');
    initInputDataInput('endTimeBack');
    initInputDataInput('startTimeReceipt');
    initInputDataInput('endTimeReceipt');

    function selectClient(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"clientId",rowData.id);
            setContentToInputById("clientId",rowData.id);
            setContentToDatagridEditorText('tt',editId,"clientName",rowData.simpleName);
        }
    }
    function selectGood(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"goodId",rowData.id);
            setContentToDatagridEditorText('tt',editId,"mapNumber",rowData.mapNumber);
            setContentToDatagridEditorText('tt',editId,"goodName",rowData.name);
            setContentToDatagridEditorText('tt',editId,"goodSize",rowData.goodSize);
            setContentToDatagridEditorText('tt',editId,"materialQualityName",rowData.materialQualityName);
        }
    }

</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>