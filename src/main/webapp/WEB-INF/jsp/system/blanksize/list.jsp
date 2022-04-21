<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blanksize/list.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data">
        <input type="text" style="display: none" id="clientId" >
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
            客户：
            <input  id="clientIdS" class="inputShow">
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <input name="file" id="file" type="file">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'page_go',plain:true" onclick="upload()">导入</a>
        </form>
    </div>
</div>
<script>
    var clientId="";
    $('#clientIdS').combobox({
        url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
        valueField:'id',
        textField:'text',
        width:130,
        onSelect: function(record){
            clientId=record.id;
        }
    });

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
            setContentToDatagridEditorText('tt',editId,"materialQuality",rowData.materialQualityName);
        }
    }
    function editAndAddSetContentAndChange() {
        setEditorOnChange("tt",6,function onChangeBack(index,content) {
            var outside=content.split("*")[0].replace("Φ","");
            var inside=content.split("*")[1];
            if(isContain(inside,"Φ")){
                inside=inside.replace("Φ","");
                length=content.split("*")[2];
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
            blankWeight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
            blankWeight=floatToVar4(blankWeight);
            setContentToDatagridEditorText("tt",editId,'blankWeight',blankWeight);
        });
    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>