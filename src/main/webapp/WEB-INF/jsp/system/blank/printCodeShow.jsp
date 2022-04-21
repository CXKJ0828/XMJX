<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/printCodeShow.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <div style="height: 70%">
        <table id="tt" data-options="nowrap:false,fit:true"  style="width:auto;height:300px"></table>
    </div>
   <div style="height: 30%">
       <table id="ttSelect" data-options="nowrap:false,fit:true"   style="width:auto"></table>
   </div>
    <script>
        $("#tt").datagrid({
            onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
                e.preventDefault(); //阻止浏览器捕获右键事件
                if(editId!=null){
                    endEditDatagridByIndex('tt',editId)
                }
                clearSelectAndSelectRowDatagrid('tt',rowIndex);
                editId=rowIndex;
                showMenu("rightMenu",e.pageX,e.pageY);
                e.preventDefault();  //阻止浏览器自带的右键菜单弹出
            }
        })
        function edit() {
            beginEditDatagridByIndex('tt',editId);
        }
        function saveEntity() {
            endEditDatagridByIndex('tt',editId);
            var rows= $('#tt').datagrid('getChanges');
            var url='${pageContext.request.contextPath}/blank/editTechnologyEntity.shtml';
            var data={
                rows:rows,
                goodId:rows[0].goodId
            }
            communatePost(url,ListToJsonString(data),function back(data){
                if(isContain(data,'error')){
                    showErrorAlert("警告",data);
                }
                var url= rootPath+'/blank/findTechnologyByClientOrderGoodProcess.shtml?clientId='+clientId
                    +'&contractNumber='+getValueById("contractNumber")
                    +'&mapNumber='+getValueById("mapNumber")
                    +'&deliveryTime='+getDataboxValue("deliveryTime")
                    +'&state=未完成'
                    +'&processId='+processId;
                $('#tt').datagrid('options').url=url;
                $("#tt").datagrid('reload');
            })
        }
    </script>
    <div id="rightMenu" class="easyui-menu" style="width: 50px; display: none;">    
        <div  data-options="iconCls:'icon-edit'" onclick="edit()">修改</div>
    </div>

    <div id="tb" style="height:auto">
        <a href="#" class="easyui-linkbutton" onclick="saveEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>
        客户：
        <input  id="clientId" class="inputShow">
        交货日期:
        <input  id="starttime" type="text" style="width: 90px">
        至
        <input id="endtime" type="text" style="width: 90px">
        <script>
            initInputDataInput("starttime");
            initInputDataInput("endtime");
        </script>
        <input id="contractNumber" placeholder="请输入订单号" style="line-height:20px;width:150px;border:1px solid #ccc;margin-left: 10px">
        <input id="mapNumber" placeholder="请输入图号" style="line-height:20px;width:150px;border:1px solid #ccc;margin-left: 10px">
        工序:
        <input  id="processId" class="inputShow">
        编号：
        <input  id="code" class="inputShow">
        <script>
            var code="";
            var userId="0";
            var userName="";
            $('#code').combobox({
                url:'${pageContext.request.contextPath}/blank/codeSelect.shtml',
                valueField:'id',
                textField:'text',
                width:100,
                onSelect: function(record){
                    code=record.text;
                }
            });
        </script>
        <input id="blankSize" placeholder="请输入下料尺寸" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
        材质：
        <input  id="materialQuality" class="inputShow">
        产品名称:
        <select class="easyui-combobox" id='goodName' name="state" style="width:80px;">
            <option value="不限"></option>
            <option value="销轴">销轴</option>
            <option value="钢套">钢套</option>
            <option value="垫">垫</option>
            <option value="不限">不限</option>
        </select>
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
    <div id="tbSelect" style="height:auto">
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
                            userId=rowData.id;
                            userName=rowData.userName;
                        }"></select>
        <a href="#" class="easyui-linkbutton" onclick="distributionEntity()" title="分配" iconCls="icon-redo" plain="true">分配</a>
        <a href="#" class="easyui-linkbutton" onclick="printEntity()" title="打印派工单" iconCls="icon-print" plain="true">打印派工单</a>
        <a href="#" class="easyui-linkbutton" onclick="clearEntity()" title="清空" iconCls="icon-remove" plain="true">清空</a>
    </div>
    <script>
        var materialQuality="";
        $('#materialQuality').combobox({
            url:'${pageContext.request.contextPath}/blank/materialQualitySelect.shtml',
            valueField:'id',
            textField:'text',
            width:100,
            onSelect: function(record){
                materialQuality=record.text;
            }
        });

        $('#clientId').combobox({
            url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
            valueField:'id',
            textField:'text',
            width:130,
            onSelect: function(record){
                clientId=record.id;
            }
        });
        $('#processId').combobox({
            url:'${pageContext.request.contextPath}/process/processSelectTwoContent.shtml',
            valueField:'id',
            textField:'text',
            width:130,
            onSelect: function(record){
                processId=record.id;
            }
        });
    </script>
</div>
<div id="codeShow">

</div>
<script>
    function clearEntity() {
        $('#ttSelect').datagrid('loadData', { total: 0, rows: [] });
    }
    function printEntity() {
                endEditDatagridByIndex('ttSelect',editId);
                var rows=getDatagridRows('ttSelect');
        var ids="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
        }
        $("#printOrder").window({
            width:1000,
            title:'派工单',
            modal: true,
            top:10,
            href: rootPath + '/blank/distributionOrderPrintUI.shtml?ids='+ids+'&submiterId='+userId,
            onClose:function () {

            }
        });
    }


    function distributionEntity() {
        if(userId=='0'){
            showErrorAlert("提示",'员工为空');
        }else{
            showconfirmDialog("提示","是否确认分配给【"+userName+"】？",function (back) {
                if(back){
                    endEditDatagridByIndex('ttSelect',editId);
                    var isOprate=true;
                    var rows=getDatagridRows('ttSelect');
                    var ids="";
                    var codes="";
                    for(i=0;i<rows.length;i++){
                        var nowreceiveAmount=rows[i].nowreceiveAmount;
                        var nowreceiveAmount=VarToInt(nowreceiveAmount);
                        var unreceiveAmount=rows[i].unreceiveAmount;
                        var unreceiveAmount=VarToInt(unreceiveAmount);
                        if(nowreceiveAmount<=0||nowreceiveAmount>unreceiveAmount){
                            isOprate=false;
                            i=rows.length;
                        }else {
                            codes = codes + rows[i].startQRCode + "amount" + rows[i].nowreceiveAmount + ",";
                        }
                    }
                    if(isOprate) {
                        if(userId=='0'){
                            showErrorAlert("提示",'员工为空');
                        }else{

                            // var rows= $('#ttSelect').datagrid('getChanges');
                            var rows=getDatagridRows('ttSelect');
                            if(rows.length>0){
                                communatePost(rootPath +'/blank/inputReceiveEntity.shtml?userId='+userId,ListToJsonString(rows),function back(data){
                                    reloadDatagridMessage('tt');
                                });
                            }else{
                                $.messager.alert('警告','不存在可以保存数据','warning');
                            }
                        }

                    }else{
                        showErrorAlert("提示",'无法分配<br>当前接收数量为空或者当前接收数量大于未接收数量');
                    }
                }
            })
        }

    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
<div id="printOrder">

</div>
</body>