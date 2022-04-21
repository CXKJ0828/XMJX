<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/getlist.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true" style="width:auto;"></table>

    <div style="display: none">
        <table id="ttselect" style="width:auto;"></table>
    </div>
    <div id="tb" style="height:auto">
        <input type="text" style="display: none" id="clientId" >
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
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
        <input id="content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;margin-top:5px;width: 200px">
        <input  id="starttime" type="text">
        至
        <input id="endtime" type="text">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <span id="allMoney">合计:</span>
        <span style="font-size: 18px;font-weight: bold"  id="amountSum"></span>
    </div>
</div>
<script>
    var endtime=getNowDate();
    starttime=getNowMonth()+"-01";
    initInputDataInput("starttime");
    initInputDataInput("endtime");
    setDataToDateBox("starttime",starttime);
    setDataToDateBox("endtime",endtime);


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
            var amount=rows[editId].amount;
            var blankSize=content;
            var blankWeight=rows[editId].blankWeight;
            var outside=blankSize.split("*")[0].replace("Φ","");
            var inside=blankSize.split("*")[1];
            if(isContain(inside,"Φ")){
                inside=inside.replace("Φ","");
                length=blankSize.split("*")[2];
                length=length.replace(/[^\d.]/g,'')
            }else{
                if(content.split("*").length==2){
                    length=inside;
                    inside="0";
                }else{
                    length=content.split("*")[2];
                }
            }

            outside=VarToFloat(outside);
            inside=VarToFloat(inside);
            length=VarToFloat(length);
            length=VarToFloat(amount)*length/1000;
            length=floatToVar4(length);
            setContentToDatagridEditorText("tt",editId,'length',length);

            weight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
            weight=floatToVar4(weight);
            setContentToDatagridEditorText("tt",editId,'weight',weight);
        });

        setEditorOnChange("tt",1,function onChangeBack(index,content) {
            var rows = $('#tt').datagrid('getRows');
            var blankSize=rows[editId].blankSize;
            var blankWeight=rows[editId].blankWeight;
            var outside=blankSize.split("*")[0].replace("Φ","");
            var inside=blankSize.split("*")[1];
            if(isContain(inside,"Φ")){
                inside=inside.replace("Φ","");
                length=blankSize.split("*")[2];
                length=length.replace(/[^\d.]/g,'')
            }else{
                if(content.split("*").length==2){
                    length=inside;
                    inside="0";
                }else{
                    length=content.split("*")[2];
                }
            }

            outside=VarToFloat(outside);
            inside=VarToFloat(inside);
            length=VarToFloat(length);
            length=VarToFloat(content)*length/1000;
            length=floatToVar4(length);
            setContentToDatagridEditorText("tt",editId,'length',length);

            weight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
            weight=floatToVar4(weight);
            setContentToDatagridEditorText("tt",editId,'weight',weight);
        });
    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>