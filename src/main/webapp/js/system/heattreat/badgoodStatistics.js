var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/heattreat/findBadgoodStatisticsByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'contractNumber',title:'订单编号',width:230,align:'center',editor:'text'},
        {field:'deliveryTime',title:'交货日期',width:230,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:230,align:'center',editor:'text'},
        {field:'goodName',title:'产品名称',width:230,align:'center',editor:'text'},
        {field:'processName',title:'工序',width:150,align:'center',editor:'text'},
        {field:'badAmount',title:'废品数',width:150,align:'center',editor:'text'},
        {field:'submiterShow',title:'完成人',width:230,align:'center',editor:'text'},
        {field:'completeTime',title:'完成时间',width:300,align:'center',editor:'text'},
        {field:'deductWages',title:'应扣工资',width:200,align:'center',editor:'text'},
        {field:'state',title:'状态',width:230,align:'center',editor:'text',formatter: function(value,row,index){
                var state=row.state;
                if(isNull(state)){
                    completeAmount=row.completeAmount;
                    return '已完成:'+completeAmount;
                }else{
                    return state;
                }


    }},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        columns:columns,
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.state=getContentBycombogrid("state");
            params.userId=getContentBySelect("userId");
            params.content=getValueById('content');
            params.starttime=getDataboxValue('starttime');
            params.endtime=getDataboxValue('endtime');
        },
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            editId=rowIndex;
            showMenu("rightMenu",e.pageX,e.pageY);
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        },
        onLoadSuccess:function(data){
            setContentToDivSpanById("all","废品数合计:"+data.sumAmount);
        },
    });
});
function find() {
   reloadDatagridMessage('tt');
}
function add() {
    var rows = $('#tt').datagrid('getRows');
    $('#tt').datagrid('appendRow',
        {
        }
    );
    editAndAddSetContentAndChange();

}

function  cancel() {
    $('#tt').datagrid('rejectChanges');
}
function  del() {
    endEditDatagridByIndex('tt',editId);
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        communateGet(rootPath +'/stock/deleteEntity.shtml?ids='+ids,function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/stock/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
