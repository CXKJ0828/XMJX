var pageii = null;
var grid = null;
$(function() {
	grid = lyGrid({
		id : 'paging',
		l_column : [ {
			colkey : "id",
			name : "id",
			width : "50px",
			hide : true
		}, {
			colkey : "employeeId",
			name : "员工编号"
		}, {
			colkey : "name",
			name : "姓名",
			width : "50px",
		}, {
			colkey : "sex",
			name : "性别"
		}, {
			colkey : "phone",
			name : "电话",
		}
            , {
                colkey : "entrytime",
                name : "入职时间",
                renderData : function(rowindex,data, rowdata, column) {
                    return new Date(data).format("yyyy-MM-dd hh:mm:ss");
                }
            }
            , {
                colkey : "department",
                name : "所在部门",
            }
            , {
                colkey : "position",
                name : "职称",
            }, {
                colkey : "remark",
                name : "备注",
            }],
		jsonUrl : rootPath + '/employee/employeeSelect.shtml',
		dymCol:true,
        usePage:false,
		checkbox : true
	});
    $("#submit").click("click", function() {// 绑定查询按扭
        var cbox = grid.getSelectedCheckbox();
        if (cbox.length > 1 || cbox == "") {
            layer.msg("只能选中一个");
            return;
        }else{
            var url=rootPath + '/employee/getEmployeeById.shtml?id='+cbox;
            $.ajax({
                type: "get",
                url: url,
                dataType : 'json',
                success: function(json){
                    parent.show(json.employeeId);
                    parent.layer.close(parent.pageii);
                    return false;
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(XMLHttpRequest.statusText);
                    alert(XMLHttpRequest.status);
                    alert(XMLHttpRequest.readyState);
                }
            });
		}
    });
});
