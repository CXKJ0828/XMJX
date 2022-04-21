<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/materialbuyexcel/list.js"></script>
<body class="easyui-layout">
<script>
    initComboGridEditor();
    function selectMaterialQuality(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('goodprocessList',editId,"materialQuality",rowData.name);
        }
    }

</script>
<div data-options="region:'north'" style="height: 30px">
    <div style="width:30%;float: left;">
        <a href="#" class="easyui-linkbutton" onclick="deleteAllEntity()" title="清空" iconCls="icon-cancel" plain="true">清空</a>
    </div>
</div>
<div data-options="region:'west'" style="width:380px;">
    <table id="tt"
           class="easyui-datagrid"
           style="width:auto;"
           data-options="
                   title:'交货时间段',
                    url:rootPath + '/materialbuyexcel/findlist.shtml',
                   toolbar:'#tb',
                   nowrap:false,
                   fit:true,
            fitColumns:true,
            onBeforeLoad: function (params) {
                params.pageNumber = params.page;
                params.sortName = params.sort;
                params.materialQuality=materialQualityName;
            },">
        <thead>
        <tr>
            <th data-options="field:'time',width:180,align:'center'">时间段</th>
            <th data-options="field:'client',width:200,align:'center'">客户</th>
        </tr>
        </thead>
    </table>
</div>

<div data-options="region:'center'" style="height:100%;" >
    <div style="height: 70%;">
        <table id="goodprocessList"
               class="easyui-datagrid"
               style="width:auto;"
               data-options="rownumbers:true,
                   toolbar:'#tboprate',
                   fit:true,
                   title:'订料明细',
            fitColumns:true">
            <thead>
            <tr>
                <th data-options="field:'id',hidden:true,width:200,align:'center',editor:'text'">编号</th>
                <th data-options="field:'materialQuality',width:200,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 200,
                idField: 'name',
                textField: 'name',
                url: rootPath +'/materialQualityType/materialQualityTypeSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                    {field:'name',title:'名称',width:100,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectMaterialQuality
            }
        }">材质</th>
                <th data-options="field:'size',width:200,align:'center',editor:'text'">规格/Φ</th>
                <th data-options="field:'length',width:200,align:'center',editor:'text'">订料米数</th>
                <th data-options="field:'weight',width:200,align:'center',editor:'text'">订料重量</th>
                <th data-options="field:'price',width:200,align:'center'">含税单价</th>
                <th data-options="field:'money',width:200,align:'center'">订料金额</th>
            </tr>
            </thead>
        </table>
    </div>
    <div style="height: 30%">
        <table id="sumShow"
               class="easyui-datagrid"
               style="width:auto;"
               data-options="rownumbers:true,
                   title:'材质统计',
                   toolbar:'#tbsum',
                   fit:true,
                    onLoadSuccess: function (data) {
                        setContentToDivSpanById('moneysumAll', '金额:'+floatToVar2(computeAllMoney('sumShow')));
                    },
            fitColumns:true">
            <thead>
            <tr>
                <th data-options="field:'materialQuality',width:200,align:'center',editor:'text'">材质</th>
                <th data-options="field:'length',width:200,align:'center'">订料米数</th>
                <th data-options="field:'weight',width:200,align:'center'">订料重量</th>
                <th data-options="field:'money',width:200,align:'center',editor:'text'">订料金额</th>
            </tr>
            </thead>
        </table>
    </div>

    <div id="tboprate" style="height:auto">
                 <span style="margin-left: 10px">
                   <span style="font-size: 14px;color: red" id="moneyAll"></span>
                 </span>
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="#" class="easyui-linkbutton" onclick="deleteEntity()" title="删除" iconCls="icon-cancel" plain="true">删除</a>
        <a href="#" class="easyui-linkbutton" onclick="back2Entity()" title="生成到回料单2" iconCls="icon-ok" plain="true">生成到回料单2</a>
        材质：
        <input  id="materialQuality" class="inputShow">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
    <div id="tbsum" style="height:auto">
                 <span style="margin-left: 10px">
                   <span style="font-size: 14px;color: red" id="moneysumAll"></span>
                 </span>
    </div>
    <script>
        function back2Entity() {
           showInputDialog("提示","请输入备注",function (content) {
               if(!isNull(content)){
                   communateGet(rootPath +"/materialbuyexcel/back2Entity.shtml?materialQuality="+getContentBySelect("materialQuality")+"&remarks="+content,function buy(data){
                       if(data=='success'){
                           reloadDatagridMessage('goodprocessList');
                           reloadDatagridMessage('tt');
                       }else{
                           showErrorAlert("操作",data);
                       }
                   });
               }
           })
        }

        var materialQualityName="";
        $('#materialQuality').combobox({
            url:rootPath +'/materialQualityType/materialQualityTypeSelect.shtml',
            valueField:'id',
            textField:'name',
            width:100,
            onSelect: function(record){
                materialQualityName=record.name;
            }
        });
        function  downLoad() {
            var fileName='订料单2导出.xls';
            var rows=getDatagridRows('goodprocessList');
            $('#goodprocessList').datagrid('toExcel', {
                filename: fileName,
                rows: rows,
                worksheet: 'Worksheet'
            });
        }
        function add() {
            var rows = $('#goodprocessList').datagrid('getRows');
            $('#goodprocessList').datagrid('appendRow',
                {
                }
            );
        }
        function  cancel() {
            $('#goodprocessList').datagrid('rejectChanges');
        }
        function  save() {
            endEditDatagridByIndex('goodprocessList',editId);
            var rows= $('#goodprocessList').datagrid('getChanges');
            if(rows.length>0){
                communatePost(rootPath +'/materialbuyexcel/editEntity.shtml',ListToJsonString(rows),function back(data){
                    reloadDatagridMessage('goodprocessList');
                });
            }else{
                $.messager.alert('警告','不存在可以保存数据','warning');
            }
        }
        function find() {
            reloadDatagridMessage("goodprocessList");
            reloadDatagridMessage("tt");
        }

        function deleteAllEntity() {
                $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
                    if (r){
                        communateGet(rootPath +'/materialbuyexcel/deleteAllEntity.shtml?password='+r,function buy(data){
                            if(data=='success'){
                                reloadDatagridMessage('goodprocessList');
                                reloadDatagridMessage('tt');
                            }else{
                                showErrorAlert("操作",data);
                            }
                        });
                    }
                });
        }
        function deleteEntity() {
            var rows=getDatagridSelections('goodprocessList');
            var ids="";
            for(i=0;i<rows.length;i++){
                ids=ids+rows[i].id+",";
            }
            if(ids!=""){
                $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
                    if (r){
                        communateGet(rootPath +'/materialbuyexcel/deleteEntity.shtml?ids='+ids+'&password='+r,function buy(data){
                            if(data=='success'){
                                reloadDatagridMessage('goodprocessList');
                                reloadDatagridMessage('tt');
                            }else{
                                showErrorAlert("操作",data);
                            }
                        });
                    }
                });
            }else{
                $.messager.alert('警告','不存在可以删除数据','warning');
            }
        }
        $("#goodprocessList").datagrid({
            url:rootPath + '/materialbuyexcel/findByMaterialQuality.shtml',
            onDblClickRow:function (rowIndex, rowData) {
                if(editId!=null){
                    endEditDatagridByIndex('goodprocessList',editId);
                    editId=rowIndex;
                    beginEditDatagridByIndex('goodprocessList',editId);
                }else{
                    editId=rowIndex;
                    beginEditDatagridByIndex('goodprocessList',editId);
                }
                var id=getContentByEditor('goodprocessList',0);
                if(isNull(id)){
                    setContentToDatagridEditorText('goodprocessList',editId,"weight",0);
                    setContentToDatagridEditorText('goodprocessList',editId,"length",0);
                }

            },
            onLoadSuccess:function(data){
                setContentToDivSpanById('moneyAll', '金额:'+floatToVar2(computeAllMoney('goodprocessList')));
                $('#sumShow').datagrid('loadData', data.rowsSum);
            },
            onBeforeLoad: function (params) {
                params.pageNumber = params.page;
                params.sortName = params.sort;
                params.materialQuality=materialQualityName;
            },
            rowStyler: function(index,row){
                var lackLength=VarToFloat(row.lackLength);
                if (lackLength>=0){
                    return 'color:#048133;font-weight:bold';
                }else{
                    return 'color:#DC143C;font-weight:bold';//红色
                }
            }
        });
        function computeAllMoney(id) {
            var rows = $('#'+id).datagrid('getRows');
            var total = 0;
            for (var i = 0; i < rows.length; i++) {
                row=rows[i];
                money=floatToVar2(row.money);
                total += StrToFloat(money);
            }
            total=Math.round(total*100)/100
            return total;
        }
    </script>
</div>
</body>