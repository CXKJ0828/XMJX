<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/productionlist.js"></script>
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
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div id="add" title="详细" data-options="minimizable:false,tools:'#details'">
</div>
<div id="details">
</div>
<script>
    function saveEntity() {
        endEditDatagridByIndex('orderdetails',editId);
        var detailsArrayLength=datagridDetailsArray.length;
        var datailsContent=new Array();
        var offersContent=new Array();
        var offerContent=getAllRowsContent('orderdetails');
        for(var num=0;num<offerContent.length;num++){
            var childDatagridId='ddv-'+num;
            endEditDatagrid(childDatagridId);
            var offerJson=offerContent[num];
            offerEntity=new Object();
            offerEntity.parentEntity=offerJson;
            var childs=getChangesContent(childDatagridId);
            offerEntity.childs=childs;
            offersContent[num]=offerEntity;
        }
        var id=getValueById("id");
        var orderId=getValueById("orderId");
        var time=getDataboxValue("time");
        var remarks=getValueById("remarks");
        var name=getValueById("name");
        var amount=getValueById("amount");
        var modifyTime=getValueById("modifyTime");
        var modifyUserId=getValueById("modifyUserId");
        var state=getValueById("state");
        if(state==''){
            state="未完成";
        }

        var rows= $('#orderdetails').datagrid('getChanges');
        var orderEntity=new Object();
        orderEntity.id=id;
        orderEntity.orderId=orderId;
        orderEntity.time=time;
        orderEntity.remarks=remarks;
        orderEntity.name=name;
        orderEntity.amount=amount;
        orderEntity.modifyTime=modifyTime;
        orderEntity.modifyUserId=modifyUserId;
        orderEntity.state=state;
        var url='${pageContext.request.contextPath}/order/editEntity.shtml';
        var data={
            order:orderEntity,
            orderdetails:offersContent,
        }
        communatePost(url,ListToJsonString(data),function back(data){
            id=data.split(":")[1];
            $('#add').window('refresh', rootPath + '/order/detailsUI.shtml?orderId='+id);
        })
    }
    var editId;
</script>
<div data-options="region:'center'">
    <table id="tt" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data">
            <c:forEach items="${res}" var="key">
                ${key.description}
            </c:forEach>
            <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
            <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        </form>
    </div>
</div>
</body>