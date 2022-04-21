<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/good/list.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true" style="width:auto"></table>
    <table id="ttExport" >
    </table>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data">
        <input style="display: none"   type="text" id="taxRate" value="${taxRate}" />
        <input  style="display: none"  type="text" id="defaulttaxRate" value="${taxRate}" />
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="#" class="easyui-linkbutton" onclick="upload()" title="导入" iconCls="page_go" plain="true">导入</a>
        模板：<input name="file" id="file" type="file">
            客户：
            <input  id="clientId" class="inputShow">
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <a href="javascript:void(0)" style="display:none;" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="calculationWeight()">计算重量</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="downLoad()">导出</a>
        </form>
    </div>
</div>
<script>
    var clientId="";
    $('#clientId').combobox({
        url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
        valueField:'id',
        textField:'text',
        width:130,
        onSelect: function(record){
            clientId=record.id;
        }
    });


    function selectMaterialQuality(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"materialQuality",rowData.id);
            setContentToDatagridEditorText('tt',editId,"materialQualityName",rowData.name);
        }
    }

    function upload() {
        $.messager.progress({
            title:'Please waiting',
            msg:'Loading data...'
        });
        $('#form').form({
            url:rootPath + '/good/uploadGood.shtml',
            onSubmit: function(){
                return $(this).form('validate');
            },
            success:function(data){
                $.messager.progress('close');
                var jsonObj = eval( '(' + data + ')' ); // eval();方法
                if(jsonObj=="success"){
                    reloadDatagridMessage('tt');
                    showMessagerCenter("提示","导入成功");
                }else{
                    showErrorAlert("警告",jsonObj);
                }
            }
        });
// submit the form
        $('#form').submit();
    }

    function calculationWeight() {
        var rows=getDatagridSelections('tt');
        var ids="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
        }
        communateGet(rootPath +'/good/calculationEntity.shtml?ids='+ids,function back(data){
            reloadDatagridMessage('tt');
        });
    }

    var taxRate=getValueById("taxRate");
    taxRate=VarToFloat(taxRate);
    initComboGridEditor();
    function selectClient(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            var taxRate=rowData.taxRate;
            if(taxRate!=null&&taxRate!=""){
                setContentToInputById("taxRate",taxRate);
            }else{
                taxRate=getValueById("defaulttaxRate");
                setContentToInputById("taxRate",taxRate);
            }
            setContentToDatagridEditorText('tt',editId,"clientId",rowData.id);
            setContentToDatagridEditorText('tt',editId,"clientFullName",rowData.fullName);
        }
    }

    function editAndAddSetContentAndChange() {
        setEditorOnChange("tt",9,function onChangeBack(index,content) {
            taxRate=VarToFloat(getValueById('taxRate'));
            taxPrice=VarToFloat(content)*(1+taxRate);
            taxPrice=floatToVar2(taxPrice);
            setContentToDatagridEditorText("tt",editId,'taxPrice',taxPrice);
        });
        setEditorOnChange("tt",3,function onChangeBack(index,content) {
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
            roughcastWeight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
            roughcastWeight=floatToVar4(roughcastWeight);
            setContentToDatagridEditorText("tt",editId,'roughcastWeight',roughcastWeight);
        });
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
            goodWeight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
            goodWeight=floatToVar4(goodWeight);
            setContentToDatagridEditorText("tt",editId,'goodWeight',goodWeight);
        });
    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>