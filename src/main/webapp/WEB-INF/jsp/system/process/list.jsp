<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/process/list.js"></script>
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
<div data-options="region:'center'" style="height:auto;">
    <table id="tt" style="width: auto"></table>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data"
    >
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </form>
    </div>
</div>
<div id="add" title="详细" data-options="minimizable:false,tools:'#details'">
</div>
<div id="details">
    <a href="javascript:void(0)" class="icon-save" onclick="saveEntity()"></a>
</div>
<script>
    function saveEntity() {
        endEditDatagridByIndex('childs',editId)
        var id=getValueById("id");
        var code=getValueById("code");
        var name=getValueById("name");
        var oneTime=getValueById("oneTime");
        var oneCost=getValueById("oneCost");
        var oneTimeUnit=getValueById("oneTimeUnit");
        var simpleName=getValueById("simpleName");
        var address=getValueById("address");
        var remark=getValueById("remark");
        var isUse=getRadioValueByName("isUse");
        if(isUse){
            isUse="可用";
        }else{
            isUse="不可用";
        }
        var organization=getValueById("organization");
        var rows= $('#childs').datagrid('getChanges');
        var processEntity=new Object();
        processEntity.id=id;
        processEntity.code=code;
        processEntity.name=name;
        processEntity.oneTime=oneTime;
        processEntity.oneCost=oneCost;
        processEntity.oneTimeUnit=oneTimeUnit;
        processEntity.simpleName=simpleName;
        processEntity.address=address;
        processEntity.remark=remark;
        processEntity.isUse=isUse;
        processEntity.organization=organization;
        var url='${pageContext.request.contextPath}/process/editEntity.shtml';
        console.log(url);
        var data={
            process:processEntity,
            processChild:rows
        }
        communatePost(url,ListToJsonString(data),function back(data){
            var id=data.split(":")[1];
            $('#add').window('refresh', rootPath + '/process/detailsUI.shtml?id='+id);
            clearDatagridSelections('tt');
            reloadDatagridMessage('tt');
        })
    }
</script>
</body>