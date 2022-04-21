<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/good/goodprocessContentlist.js"></script>
<body class="easyui-layout">

<div data-options="region:'west'" style="width:350px;">
    <table id="tt" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
            <input name="filesImg" id="filesImg" type="file" multiple="multiple">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'page_go',plain:true" onclick="uploadImg()">上传图纸</a>
            <input id="clientId" style="display: none" name="clientId" type="text" >
            <%--<input   id="clientName" style="width: 250px" type="text" >--%>
            <input name="files" id="files" type="file" multiple="multiple">
            <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'page_go',plain:true" onclick="upload()">导入工艺</a>
        </form>
    </div>
</div>
<script>
    <%--$('#clientName').combogrid({--%>
        <%--panelWidth:400,--%>
        <%--idField:'fullName',--%>
        <%--textField:'fullName',--%>
        <%--mode: 'remote',--%>
        <%--url: '${pageContext.request.contextPath}/client/clientSelect.shtml',--%>
        <%--columns:[[--%>
            <%--{field:'id',hidden:true,title:'客户编码',width:100},--%>
            <%--{field:'fullName',title:'客户全称',width:350},--%>
        <%--]],--%>
        <%--onSelect:selectClient,--%>
        <%--onShowPanel: function () {--%>

        <%--}--%>
    <%--});--%>
    <%--function selectClient(rowIndex, rowData) {--%>
        <%--setContentToInputById("clientId",rowData.id);--%>
        <%--setContentToInputById("clientName",rowData.fullName);--%>
    <%--}--%>
    var opts1;
</script>
<div data-options="region:'center'" >
    <div class="easyui-tabs" >
        <div title="工序信息" style="padding:5px">
            <table id="goodprocessList"
                   class="easyui-datagrid"
                   style="width:auto;height:auto"
                   data-options="
                   toolbar:'#tboprate',
                    nowrap:false,
            fitColumns:true,
            singleSelect:true">
                <thead>
                <tr>
                    <th data-options="field:'id',hidden:true,width:100,align:'center',editor:'text'">编码</th>
                    <th data-options="field:'goodId',hidden:true,width:100,align:'center',editor:'text'">产品编号</th>
                    <th data-options="field:'number',width:20,align:'center',editor:'text'">工序号</th>
                    <th data-options="field:'processId',hidden:true,width:100,align:'center',editor:'text'">工序编码</th>
                    <th class="easyui-combogrid" data-options="field:'processName',width:100,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 200,
			idField: 'name',
			textField: 'name',
			url: '${pageContext.request.contextPath}/process/processSelect.shtml',
			mode: 'remote',
			columns: [[
				{field:'id',hidden:true,title:'编码',width:150,align:'center'},
               {field:'name',title:'名称',width:300,align:'center'},
                {field:'artificial',hidden:true,title:'人工',width:150,align:'center'},
                {field:'remark',hidden:true,title:'备注',width:150,align:'center'},
                {field:'isMust',hidden:true,title:'是否必须',width:150,align:'center'},
                {field:'multiple',hidden:true,title:'倍数',width:150,align:'center'},
			]],
			fitColumns: true,
			onSelect:selectProcess
			}
		}">工序</th>
                    <th data-options="field:'content',width:200,align:'center',editor:'text'">加工内容</th>
                </tr>
                </thead>
            </table>
            <div id="rightMenuGoodCode" class="easyui-menu" style="width: 50px; display: none;">    
                <div  data-options="iconCls:'icon-add'" onclick="addContentProcess()">新增行</div>
                <div  data-options="iconCls:'icon-edit'" onclick="editProcess()">修改</div>
                <div  data-options="iconCls:'icon-remove'" onclick="delProcess()">删除</div>         
            </div>
            <div id="rightMenuHeaderGoodCode" class="easyui-menu" style="width: 50px; display: none;">    
                <div  data-options="iconCls:'icon-add'" onclick="addContentProcess()">新增行</div>
            </div>
            <script>
                initComboGridEditor();
                function selectProcess(rowIndex, rowData) {
                    if(checkIsSelectEd(editId)){
                        setContentToDatagridEditorText('goodprocessList',goodprocesseditId,"processId",rowData.id);
                        setContentToDatagridEditorText('goodprocessList',goodprocesseditId,"processName",rowData.name);
                    }
                }
                var goodprocesseditId;
                $("#goodprocessList").datagrid({
                    onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
                        e.preventDefault(); //阻止浏览器捕获右键事件
                        if(goodprocesseditId!=null){
                            endEditDatagridByIndex('goodprocessList',goodprocesseditId)
                        }
                        clearSelectAndSelectRowDatagrid('goodprocessList',rowIndex);
                        goodprocesseditId=rowIndex;
                        showMenu("rightMenuGoodCode",e.pageX,e.pageY);
                        e.preventDefault();  //阻止浏览器自带的右键菜单弹出
                    },
                    onHeaderContextMenu: function (e, field){ //右键时触发事件                      
                        e.preventDefault(); //阻止浏览器捕获右键事件
                        if(goodprocesseditId!=null){
                            endEditDatagridByIndex('goodprocessList',goodprocesseditId)
                        }
                        showMenu("rightMenuHeaderGoodCode",e.pageX,e.pageY);
                        e.preventDefault();  //阻止浏览器自带的右键菜单弹出
                    }
                });
                function addContentProcess() {
                    if(goodprocesseditId!=null||goodprocesseditId!=""){
                        endEditDatagridByIndex("goodprocessList",goodprocesseditId);
                    }
                    var rows=getAllRowsContent("goodprocessList");
                    $('#goodprocessList').datagrid('appendRow',
                        {
                            "goodId":goodId,
                            "number":rows.length+1
                        }
                    );
                    var rows = getAllRowsContent("goodprocessList");
                    goodprocesseditId=rows.length-1;
                    beginEditDatagridByIndex('goodprocessList',goodprocesseditId);
                }
                function editProcess() {
                    beginEditDatagridByIndex('goodprocessList',goodprocesseditId);
                }
                function delProcess() {
                    var rowData=getDatagridJsonByIndexAndId("goodprocessList",goodprocesseditId);
                    var url='${pageContext.request.contextPath}/good/deleteGoodProcessEntity.shtml?id='+rowData.id;
                    if(goodprocesseditId!=null){
                        communateGet(url,function back(data){
                            reloadDatagridMessage('goodprocessList');
                        });
                    }else{
                        $.messager.alert('警告','不存在可以删除数据','warning');
                    }
                }

            </script>
        </div>
    </div>

</div>
<div id="tboprate" style="padding:5px;height:auto">
    <div style="margin-bottom:5px">
        <a href="#" class="easyui-linkbutton" onclick="saveEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>
        <a href="#" class="easyui-linkbutton" onclick="cancelGoodProcess()" title="取消"  iconCls="icon-undo" plain="true">取消</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="showImg()">查看图纸</a>
    </div>
</div>
<script>
    function showImg() {
        if(img!=undefined){
            window.open(rootPath + '/good/showImgUI.shtml?goodId='+goodId);
        }else {
            showErrorAlert("提示","暂无图纸");
        }

    }

    function cancelGoodProcess() {
        var url='${pageContext.request.contextPath}/good/getGoodProcessByGoodId.shtml?id='+goodId;
        $('#goodprocessList').datagrid('options').url=url;
        $("#goodprocessList").datagrid('reload');
    }
    function saveEntity() {
        endEditDatagridByIndex('goodprocessList',goodprocesseditId);
        var goodprocessList= $('#goodprocessList').datagrid('getChanges');
        var url='${pageContext.request.contextPath}/good/editGoodProcessEntity.shtml';
        var data={
            goodprocessList:goodprocessList,
        }
        communatePost(url,ListToJsonString(data),function back(data){
            var url='${pageContext.request.contextPath}/good/getGoodProcessByGoodId.shtml?id='+goodId;
            $('#goodprocessList').datagrid('options').url=url;
            $("#goodprocessList").datagrid('reload');
        })
    }
</script>

</body>