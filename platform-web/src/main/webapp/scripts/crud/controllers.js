'use strict';

/* Controllers */

crudModule.controller('productListCtrl', function($scope, $http,$log) {
	$("#maingrid").ligerGrid({
		height:'100%',
        columns: [
        { display: '产品名', name: 'product_name', align: 'left', width:'40%',minWidth: 100 },
        { display: '生产日期', name: 'product_date', width:'60%',minWidth: 120 }
        ], 
        url:'/product/pageJson.do',
        height:400,
        pageSize:3 ,
        rownumbers:true,
        toolbar: { items: [
        { id:'add',text: '增加', click: itemclick, icon: 'add' },
        { line: true },
        { text: '修改', click: itemclick, icon: 'modify' },
        { line: true },
        { text: '删除', click: itemclick, icon: 'delete'}
        ]
        }
    });
	
	
});
