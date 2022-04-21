<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/sendStatistics.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data">
            <a href="#" class="easyui-linkbutton" onclick="upload()" title="导入" iconCls="page_go" plain="true">导入</a>
            模板：<input name="file" id="file" type="file">
            <c:forEach items="${res}" var="key">
                ${key.description}
            </c:forEach>
            客户：
            <input  id="clientId" class="inputShow">
            <input id="contractNumber" placeholder="请输入订单编号" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
            <input id="mapNumber" placeholder="请输入图号" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
            产品名称:
            <select class="easyui-combobox" id='goodName' style="width:80px;">
                <option value="不限"></option>
                <option value="轴">轴</option>
                <option value="套">套</option>
                <option value="垫/片">垫/片</option>
                <option value="其他">其他</option>
                <option value="不限">不限</option>
            </select>
            发货日期:<input  id="starttime" style="width: 100px" type="text" value="${starttime}">
            至
            <input id="endtime" style="width: 100px" type="text" value="${endtime}">
            <input id="modifyTime"  style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
            <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
            <span id="all">合计：</span>
        </form>
    </div>
</div>
<script>
    function upload() {
        $.messager.progress({
            title:'Please waiting',
            msg:'Loading data...'
        });
        $('#form').form({
            url:rootPath + '/order/uploadSendStock.shtml',
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
                    reloadDatagridMessage('tt');
                    showErrorAlert("警告",jsonObj);
                }
            }
        });
// submit the form
        $('#form').submit();
    }

    initInputDataInput("starttime");
    initInputDataInput("endtime");
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
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>