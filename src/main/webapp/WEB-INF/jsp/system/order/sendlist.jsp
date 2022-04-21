<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/sendlist.js"></script>
<body class="easyui-layout">
<div data-options="region:'north'" style="height: 35px">
    <input id="startTime" style="display: none" type="text" value="${startTime}">
    <input id="endTime" style="display: none" type="text" value="${endTime}">
    <div style="margin-left: 5px;margin-top: 10px">
        <input type="text" id="fullName" style="display: none;">
        <input  id="contractNumber" class="inputShow" placeholder="请输入订单编号">
        <input  id="mapNumber" class="inputShow" placeholder="请输入图号">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
</div>
<div data-options="region:'west'" style="width:200px;">
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
    </div>
    <ul id="tt" class="easyui-tree" style="margin-top: 10px;margin-left: 10px"></ul>
</div>

<div data-options="region:'center'" >
    <div class="easyui-tabs" >
        <div title="发货明细" style="padding:5px">
            <table id="goodprocessList"
                   class="easyui-datagrid"
                   style="width:auto;height:auto"
                   data-options="rownumbers:true,
                   toolbar:'#tboprate',
            fitColumns:true">
                <thead>
                <tr>
                    <th data-options="field:'id',hidden:true,width:100,align:'center'">编号</th>
                    <th data-options="field:'contractNumber',width:200,align:'center'">订单编号</th>
                    <th data-options="field:'mapNumber',width:250,align:'center'">图号</th>
                    <th data-options="field:'goodName',width:200,align:'center'">产品名称</th>
                    <th data-options="field:'materialCode',width:200,align:'center'">物料编码</th>
                    <th data-options="field:'makeTime',width:200,align:'center'">来单日期</th>
                    <th data-options="field:'deliveryTime',width:200,align:'center'">交货日期</th>
                    <th data-options="field:'amount',width:200,align:'center'">订单数量</th>
                    <th data-options="field:'sendAmount',width:150,align:'center',editor:'text'">发货数量</th>
                    <th data-options="field:'boxId',width:200,align:'center',editor:'text'">箱号</th>
                    <th data-options="field:'sendRemarks',width:200,align:'center',editor:'text'">发货备注</th>
                    <th data-options="field:'alreadysendAmount',width:150,align:'center',editor:'text'">已发货数量</th>
                    <th data-options="field:'unsendAmount',width:150,align:'center',editor:'text'">未发货数量</th>
                    <th data-options="field:'stockAmount',width:150,align:'center',editor:'text'">库存数量</th>
                    <th data-options="field:'sendTime',width:200,align:'center'">录入时间</th>
                    <th data-options="field:'state',width:200,align:'center'">状态</th>
                    <th data-options="field:'goodId',hidden:true,width:200,align:'center'">产品id</th>
                </tr>
                </thead>
            </table>
            <script>
                function sendEntity() {
                    var isOprate=true;
                    var rows=getDatagridSelections('goodprocessList');
                    var message="";

                    if(rows.length>0){
                        for(i=0;i<rows.length;i++){
                            var sendAmount=rows[i].sendAmount;
                            if(sendAmount==null||sendAmount=='0'||sendAmount==''){
                                isOprate=false;
                                message=message+"【"+rows[i].mapNumber+"】,";
                            }
                        }
                        if(isOprate){
                            var url='${pageContext.request.contextPath}/order/sureSendEntity.shtml';
                            var data={
                                rows:rows,
                            }
                            communatePost(url,ListToJsonString(data),function back(data){
                                showMessagerCenter("提示","发货成功");
                                reloadDatagridMessage("goodprocessList");
                            })
                        }else{
                            showErrorAlert("警告","所选择图号"+message+"无发货数量");
                            clearDatagridSelections('goodprocessList');
                        }

                    }else{
                        $.messager.alert('警告','不存在可以发货数据','warning');
                    }
                }
            </script>
            <div id="tboprate" style="height:auto">
                <a href="#" class="easyui-linkbutton" onclick="selectAll()" title="全选" iconCls="icon-sum" plain="true">全选</a>
                <a href="#" class="easyui-linkbutton" onclick="saveEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>
                <a href="#" class="easyui-linkbutton" onclick="print('price')" title="打印(含单价)" iconCls="icon-print" plain="true">打印(含单价)</a>
                <a href="#" class="easyui-linkbutton" onclick="print('noprice')" title="打印(不含单价)" iconCls="icon-print" plain="true">打印(不含单价)</a>
                <a href="#" class="easyui-linkbutton" onclick="sendEntity()" title="发货" iconCls="icon-redo" plain="true">发货</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="cancel()">取消</a>
                <a href="#" class="easyui-linkbutton" style="display: none" onclick="completeEntity()" title="全部完成" iconCls="icon-ok" plain="true">全部完成</a>
                <a href="#" class="easyui-linkbutton" onclick="deleteEntity()" title="删除" iconCls="icon-cancel" plain="true">删除</a>
            </div>
            <div id="printOrder">

            </div>
            <script>
                function selectAll() {
                    //获取数据列表中的所有数据
                    var rows = $("#goodprocessList").datagrid("getRows");
                    //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
                    for(var i=0;i<rows.length;i++){
                        var sendAmount = rows[i].sendAmount;
                        if(sendAmount!=''&&sendAmount!='undefined'&&sendAmount!=undefined){
                            var index = $("#goodprocessList").datagrid("getRowIndex",rows[i])
                            $("#goodprocessList").datagrid("checkRow",index);
                        }
                    }
                }
                function print(origin) {
                    var rows=getDatagridSelections('goodprocessList');
                    var ids="";
                    for(i=0;i<rows.length;i++){
                        ids=ids+rows[i].id+",";
                    }
                    $("#printOrder").window({
                        width:1000,
                        title:'发货单',
                        modal: true,
                        top:10,
                        href: rootPath + '/order/sendorderPrintUI.shtml?ids='+ids+"&fullName="+getValueById("fullName")+"&origin="+origin,
                        onClose:function () {

                        }
                    });
                }

                function saveEntity() {
                    endEditDatagridByIndex("goodprocessList",editId);
                    var rows=getChangesContent('goodprocessList');
                    var url='${pageContext.request.contextPath}/order/saveSendEntity.shtml';
                    var data={
                        rows:rows,
                    }
                    communatePost(url,ListToJsonString(data),function back(data){
                       reloadDatagridMessage("goodprocessList");
                    })
                }

                function completeEntity() {
                        communateGet(rootPath +'/order/completeSendEntity.shtml?orderId='+orderId,function back(data){
                            reloadDatagridMessage("goodprocessList");
                        });
                }
                
                
                var editId;

                $("#goodprocessList").datagrid({
                    url:'${pageContext.request.contextPath}/order/findSendByClientId.shtml',
                    onBeforeLoad: function (params) {
                        params.pageNumber = params.page;
                        params.sortName = params.sort;
                        params.clientId=clientId;
                        params.contractNumber=getValueById('contractNumber');
                        params.mapNumber=getValueById('mapNumber');
                    },
                    onDblClickRow:function (rowIndex, rowData) {
                        if(editId!=null){
                            endEditDatagridByIndex('goodprocessList',editId);
                        }else{
                        }
                        editId=rowIndex;
                        beginEditDatagridByIndex('goodprocessList',editId);
                        editAndAddSetContentAndChange();
                    },
                    rowStyler: function(index,row){
                        var deliveryTime=row.deliveryTime;
                        var startTime=getValueById("startTime");
                        var endTime=getValueById("endTime");
                        if (deliveryTime>=startTime&&deliveryTime<endTime){
                            return 'color:#ff0030;';//红色
                        }else{
                            return 'color:#000;';
                        }
                    },
                    onLoadSuccess: function (data) {
                        detailsStock = new Array(data.rows.length);
                        for(var i=0;i<data.rows.length;i++){
                            detailsStock[i]=data.rows[i].stockAmount;
                        }
                    }
                });
                function computeByState(id,colName) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        if(rows[i].state!='已订料'){
                            total += StrToFloat(rows[i][colName]);
                        }

                    }
                    total=Math.round(total*100)/100
                    return total;
                }
                function editAndAddSetContentAndChange() {
//                    setEditorOnChange("goodprocessList",0,function onChangeBack(index,content) {
//                        var stockWeight=detailsStock[editId];
//                        if(stockWeight=='null'){
//                            stockWeight="0";
//                        }
//                        var unsendAmount=getContentByEditor("goodprocessList",4);
//                        var alreadysendAmount=getContentByEditor("goodprocessList",3);
//                        if(alreadysendAmount==''){
//                            alreadysendAmount="0";
//                        }
////                        setContentToDatagridEditorText("goodprocessList",editId,'stockAmount',VarToFloat(stockWeight)-VarToFloat(content));
////                        setContentToDatagridEditorText("goodprocessList",editId,'unsendAmount',VarToFloat(unsendAmount)-VarToFloat(content));
////                        setContentToDatagridEditorText("goodprocessList",editId,'alreadysendAmount',VarToFloat(alreadysendAmount)+VarToFloat(content));
//                    });
                }
            </script>
        </div>
    </div>
</div>
</body>