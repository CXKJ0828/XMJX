<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/stock/list.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" style="width:auto"></table>
    <div style="display: none;">
        <table id="ttselect" style="width:auto;"></table>
        <table id="ttexport" style="width:auto"></table>
    </div>
    <div id="tb" style="height:auto">
        <input type="text" style="display: none" id="clientId" >
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
</div>
<script>

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
            setContentToDatagridEditorText('tt',editId,"mapNumber",rowData.mapNumber);
            setContentToDatagridEditorText('tt',editId,"goodName",rowData.name);
            setContentToDatagridEditorText('tt',editId,"goodSize",rowData.goodSize);
            setContentToDatagridEditorText('tt',editId,"materialQuality",rowData.materialQuality);
            setContentToDatagridEditorText('tt',editId,"price",rowData.taxPrice);
        }
    }
    function editAndAddSetContentAndChange() {
        var price=getDatagridEditorObj("tt",editId,"price");
        setEditorOnChange("tt",5,function onChangeBack(index,content) {
            var rows = $('#tt').datagrid('getRows');
            var price=getContentByEditor("tt",6);
            var money=floatToVar2(VarToFloat(content)*VarToFloat(price));
            setContentToDatagridEditorText("tt",editId,'money',money);
        });
        setEditorOnChange("tt",6,function onChangeBack(index,content) {
            var rows = $('#tt').datagrid('getRows');
            var amount=getContentByEditor("tt",5);;
            var money=floatToVar2(VarToFloat(content)*VarToFloat(amount));
            setContentToDatagridEditorText("tt",editId,'money',money);
        });
    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>