<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/list.js"></script>
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
    function blank() {
        isOprate=true;
        var ids="";
        var rows=getDatagridSelections("tt");
        for(i=0;i<rows.length;i++){
            var state=rows[i].state;
            if(state=='已生成下料单'){
                showErrorAlert("提示","选中订单存在已生成下料订单，请重新选择");
                isOprate=false;
            }
            ids=ids+rows[i].id+",";
        }
        if(isOprate){
            $.messager.progress({
                title:'Please waiting',
                msg:'Loading data...'
            });
            communateGet(rootPath +'/order/blankEntity.shtml?id='+ids,function back(data){
                $.messager.progress('close');
                if(data=='success'){
                    showMessagerCenter("提示","下料成功")
                    setContentToInputById("state",'已生成下料单');
                }else{
                    showErrorAlert("操作",data);
                }
            });
        }
    }

    function saveEntity() {
        endEditDatagridByIndex('orderdetails',editId);
        var id=getValueById("id");
        var contractNumber=getValueById("contractNumber");
        var makeTime=getDataboxValue("makeTime");
        var remarks=getValueById("remarks");
        var clientId=getValueById("clientId");
        var state=getValueById("state");
        var rows= $('#orderdetails').datagrid('getChanges');
        var orderEntity=new Object();
        orderEntity.id=id;
        orderEntity.contractNumber=contractNumber;
        orderEntity.makeTime=makeTime;
        orderEntity.remarks=remarks;
        orderEntity.remarks=remarks;
        orderEntity.clientId=clientId;
        orderEntity.state=state;
        var url='${pageContext.request.contextPath}/order/editEntity.shtml';
        var data={
            order:orderEntity,
            orderdetails:rows,
        }
        communatePost(url,ListToJsonString(data),function back(data){
            id=data.split(":")[1];
            $('#add').window('refresh', rootPath + '/order/detailsUI.shtml?orderId='+id);
        })
    }
    var editId;

    function selectAll() {
        //获取数据列表中的所有数据
        var rows = $("#tt").datagrid("getRows");
        //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
        for(var i=0;i<rows.length;i++){
            var state = rows[i].state;
            if(state==''){
                var index = $("#tt").datagrid("getRowIndex",rows[i])
                $("#tt").datagrid("checkRow",index);
            }
        }
    }
</script>
<div data-options="region:'center'">
    <table id="tt" data-options="fit:true" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data">
            <%--<a href="#" class="easyui-linkbutton" onclick="selectAll()" title="全选" iconCls="icon-sum" plain="true">全选</a>--%>
            <%--<a href="#" class="easyui-linkbutton" onclick="blank()" title="下料" iconCls="icon-redo" plain="true">下料</a>--%>
            <c:forEach items="${res}" var="key">
                ${key.description}
            </c:forEach>
                <a href="#" class="easyui-linkbutton" onclick="upload()" title="导入1" iconCls="page_go" plain="true">导入1</a>
            模板1：<input name="file" id="file" type="file" >
                <a href="#" class="easyui-linkbutton" onclick="upload2()" title="导入2" iconCls="page_go" plain="true">导入2</a>
            模板2：<input name="file2" id="file2" type="file" >
                客户：
                <input  id="clientIdS" class="inputShow" >
                来单日期:
                <input  id="starttime" type="text" style="width: 90px">
                至
                <input id="endtime" type="text" style="width: 90px">
            <input id="content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;width: 100px">
            <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
             <br>合计金额：<span id="allMoney"></span>
        </form>
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
            initInputDataInput("starttime");
            initInputDataInput("endtime");
        </script>
    </div>
</div>
</body>