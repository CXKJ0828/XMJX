<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div>
    <form id="form" name="form"  method="post" enctype="multipart/form-data"
    >
    <div class="easyui-panel" title="android软件版本更新" style="width:400px">
        <div style="padding:10px 0 10px 60px">
                <table>
                    <tr>
                        <td>当前版本号:</td>
                        <td>${appConfigFormMap.code}</td>
                    </tr>
                    <tr>
                        <td>更新版本号:</td>
                        <td><input class="easyui-validatebox" id="code" type="number" data-options="required:true"></input></td>
                    </tr>
                    <tr>
                        <td>apk:</td>
                        <td><input class="easyui-validatebox" id="file" name="file" type="file"></input></td>
                    </tr>
                </table>
        </div>
        <div style="text-align:center;padding:5px">
            <a href="#" class="easyui-linkbutton" onclick="submit()">确认上传</a>
        </div>
    </div>
    </form>
        <script>
            function  submit() {
                var code=getValueById("code");
                $.messager.progress({
                    title:'Please waiting',
                    msg:'Loading data...'
                });
                $('#form').form({
                    url:rootPath + '/appconfig/uploadAndoirdAPKM.shtml?code='+code,
                    onSubmit: function(){
                        return $(this).form('validate');
                    },
                    success:function(data){
                        $.messager.progress('close');
                        var jsonObj = eval( '(' + data + ')' ); // eval();方法
                        if(jsonObj=="success"){
                            reloadDatagridMessage('tt');
                        }else{
                            alert("系统异常，请联系管理员");
                        }
                    }
                });
// submit the form
                $('#form').submit();

            }
        </script>
    </div>
</div>
