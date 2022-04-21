<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/toolStock/list.js"></script>
<style>
    .tdShow{
        width:20%;
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
<div data-options="region:'center'" style="height:200px;">
    <table id="tt" style="width: 4000px"></table>
    <table id="ttselect" style="display: none"></table>
    <div id="tb" style="height:auto">

        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="out()">出库</a>
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        种类：
        <input  id="toolTypeId" class="inputShow">
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <div id="sumAmount">数量合计:</div>
    </div>
</div>
<div id="outWindow" title="出库">
</div>
<script>
    var toolTypeId="";
    $('#toolTypeId').combobox({
        url:'${pageContext.request.contextPath}/toolType/toolTypeSelect.shtml',
        valueField:'id',
        textField:'name',
        width:100,
        onSelect: function(record){
            toolTypeId=record.id;
        }
    });

    function out() {
        var rows=getDatagridSelections('tt');
        var length=rows.length;
        if(length==0){
            showErrorAlert("提示","请选择出库工具");
        }else if(length>1){
            showErrorAlert("提示","请选择一个工具出库");
        }else{
            $("#outWindow").window({
                width: 500,
                modal: true,
                height: 300,
                top:100,
                href: rootPath + '/toolOut/addUI.shtml?toolId='+rows[0].toolId,
                onClose:function () {
                    $('#tt').datagrid("reload");
                }
            });
        }
    }
</script>
</body>