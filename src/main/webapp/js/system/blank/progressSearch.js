var editId;
var pageii = null;
var distributionAmountSum=0;
var origin="全部";
$(function() {
    id='tt';
    url='/blank/findSearchProgressByPage.shtml';
    var
    columns=[[
        {field:'progressSearchId',hidden:true,title:'progressSearchId',width:80,align:'center',editor:'text'},
        {field:'clientName',title:'客户',width:150,align:'center'},
        {field:'contractNumber',title:'订单号',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:100,align:'center'},
        {field:'goodName',title:'产品名称',width:100,align:'center'},
        {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
        {field:'blankSize',title:'下料尺寸',width:80,align:'center'},
        {field:'materialQuality',title:'材质',width:100,align:'center'},
        {field:'deliveryTime',title:'交货日期',width:80,align:'center'},
        {field:'waitDistributionAmount',title:'数量',width:80,align:'center'},
        {field:'nowDistributionAmount',title:'当前分配数量',width:80,align:'center',editor:'text'},
        {field:'progress',title:'进度',width:20000,align:'left'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'progressSearchId',
        pagination:true,
        onLoadSuccess:function(data){
            clearDatagridSelections("tt");
            distributionAmountSum=0;
            setContentToDivSpanById("distributionAmountSum",distributionAmountSum);
        },
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.origin=origin;
            params.goodName=getContentBySelect("goodName");
            params.content=getValueById('content');
            params.clientId=getContentBySelect("clientId");
            params.startTime=getDataboxValue("starttime");
            params.endTime=getDataboxValue("endtime");
        },
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            var progress=rowData.progress;
            document.getElementById("progressContent").innerHTML=progress;
            $('#progressWindow').window('open');
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        },
        onCheck:function (rowIndex,rowData) {
            distributionAmountSum=distributionAmountSum+VarToInt(rowData.nowDistributionAmount);
            setContentToDivSpanById("distributionAmountSum",distributionAmountSum);
        },
        onUncheck:function (rowIndex,rowData) {
            if(distributionAmountSum>0){
                distributionAmountSum=distributionAmountSum-VarToInt(rowData.nowDistributionAmount);
                setContentToDivSpanById("distributionAmountSum",distributionAmountSum);
            }else{
                setContentToDivSpanById("distributionAmountSum",0);
            }

        },
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
        },
        //是否显示行号
        rownumbers:true,
        columns:columns
    });

    id='ttExport';
    var
        columns=[[
            {field:'clientName',title:'客户',width:150,align:'center',editor:'text'},
            {field:'contractNumber',title:'订单号',width:100,align:'center',editor:'text'},
            {field:'mapNumber',title:'图号',width:100,align:'center',editor:'text'},
            {field:'goodName',title:'产品名称',width:100,align:'center',editor:'text'},
            {field:'goodSize',title:'产品尺寸',width:80,align:'center',editor:'text'},
            {field:'blankSize',title:'下料尺寸',width:80,align:'center',editor:'text'},
            {field:'materialQuality',title:'材质',width:100,align:'center',editor:'text'},
            {field:'deliveryTime',title:'交货日期',width:80,align:'center',editor:'text'},
            {field:'progress',title:'进度',width:20000,align:'left',editor:'text'},
        ]];
    $('#'+id).datagrid({
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        columns:columns
    });
});

function  del() {
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].progressSearchId+",";
    }
    if(ids!=""){
        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
            if (r){
                communateGet(rootPath +'/blank/deleteProgressSearchEntity.shtml?ids='+ids+'&password='+r,function back(data){
                    reloadDatagridMessage('tt');
                });
            }
        });

    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}

function  downLoad() {
    var rows = $('#ttExport').datagrid('getRows');
    for (var i=rows.length;i>0;i--){
        $('#ttExport').datagrid('deleteRow',i-1);
    }

    var rows=getDatagridSelections("tt");
    for(i=0;i<rows.length;i++){
        $('#ttExport').datagrid('appendRow',
            rows[i]
        );
    }
    var fileName='进度查询导出.xls';
    $('#ttExport').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "origin":origin,
        "goodName":getContentBySelect('goodName'),
        "clientId":getContentBySelect('clientId'),
        "content":getValueById('content'),
        "startTime":getDataboxValue('starttime'),
        "endTime":getDataboxValue('endtime'),

    }
    downfileByUrl(rootPath +'/blank/progressSearchDownload.shtml?entity='+ListToJsonString(data));

}


function find(form) {
    origin=form;
    if(origin=='全部'){
        clearComboBoxSelect('clientId');
        clearComboBoxSelect('goodName');
        clearDataBox('starttime');
        clearDataBox('endtime');
        setContentToInputById("content","");
    }
    reloadDatagridMessage("tt");
}

function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/blank/editNowDistributionAmountByIds.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }
}

function  cancel() {
    $('#tt').datagrid('rejectChanges');
    clearDatagridSelections("tt");
    distributionAmountSum=0;
    setContentToDivSpanById("distributionAmountSum",distributionAmountSum);
}