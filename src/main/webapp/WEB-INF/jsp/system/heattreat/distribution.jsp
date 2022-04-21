<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script>
    var money=0;
</script>
<div style="padding:10px 0 10px 10px">
    <form id="ff"  method="post">
        <input style="display: none" id="source" name="heatTreatFormMap.source" value="${source}">
        <input style="display: none" id="heattreatIds" name="heatTreatFormMap.heattreatIds" value="${heattreatIds}">
        <table>
            <tr>
                <td>员工:</td>
                <td>  <select name="heatTreatFormMap.submiterId" id="submiterId" style="width:200px" class="easyui-combogrid"  data-options="
                        panelWidth: 200,
                        idField: 'id',
                        textField: 'userName',
                         required:true,
                        mode: 'remote',
                        url: '${pageContext.request.contextPath}/user/userSelectHeatTreat.shtml?origin='+'${origin}'+'&remarks='+'${remarks}',
                        method: 'get',
                        columns: [[
                            {field:'id',hidden:true,title:'编号',width:80},
                            {field:'accountName',title:'账号',width:120},
                            {field:'userName',title:'姓名',width:120},

                        ]],
                         onSelect: function (item) {
                            var submiterId=$('#submiterId').combobox('getValue');
                             communateGet(rootPath +'/heattreat/getLatelyEstimateCompleteTimeBySubmiterId.shtml?submiterId='+submiterId,function back(data){
                                        setContentToDivSpanById('estimateCompleteTime',data);
                             });
                    },
                    onUnselect: function (item) {
                            setContentToDivSpanById('estimateCompleteTime','');
                    },
                        fitColumns: true"></select></td>
            </tr>
            <tr>
                <td>最迟应完成时间:</td>
                <td>
                    <span id="estimateCompleteTime"></span>
                </td>
            </tr>
            <tr>
                <td>工序:</td>
                <td>
                    <input  name="heatTreatFormMap.processId[]" id="processId" style="width:200px" class="easyui-combobox" data-options="
                    valueField: 'value',
                    textField: 'text',
                    required:true,
                    multiple:true,
                    onSelect: function (item) {
                    var processNames=$('#processId').combobox('getText');
                     communateGet(rootPath +'/heattreat/calculationMoney.shtml?processNames='+processNames+'&heattreatIds='+getValueById('heattreatIds'),function back(data){
                                 var overTimeLimit=parseInt(VarToFloat(data)/200*8)+1;
                                setContentToDivSpanById('money',data);
                                setContentToInputById('overTimeLimit',overTimeLimit);
                     });
                    },
                    onUnselect: function (item) {
                    var processNames=$('#processId').combobox('getText');
                     communateGet(rootPath +'/heattreat/calculationMoney.shtml?processNames='+processNames+'&heattreatIds='+getValueById('heattreatIds'),function back(data){
                                 var overTimeLimit=parseInt(VarToFloat(data)/200*24)+1+2;
                                setContentToDivSpanById('money',data);
                                setContentToInputById('overTimeLimit',overTimeLimit);

                     });
                    },
                    url: '${pageContext.request.contextPath}${processUrl}'">
                </td>
            </tr>
            <tr>
                <td>所需金额(元):</td>
                <td>
                    <span id="money">0</span>
                </td>
            </tr>
            <tr>
                <td>超时设定(h):</td>
                <td>
                    <input id="overTimeLimit"  name="heatTreatFormMap.overTimeLimit"  style="width:200px" type="text">
                </td>
            </tr>
        </table>
    </form>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">确定</a>
</div>
<script>

    function submitForm(){
            submitFormNow('ff','/heattreat/distributionEntity.shtml',function (data) {
                var jsonObj = eval( '(' + data + ')' ); // eval();方法
                if(jsonObj=="success"){
                    $('#distributionWindow').window('close');
                    reloadDatagridMessage('tt');
                    if(getValueById("source")=='小调度'){
                        clearDatagridSelections('tt');
                    }

                    }else{
                    var result=jsonObj.replace("success:","");
                   showErrorAlert("提示",result);
                }
            })
    }
</script>