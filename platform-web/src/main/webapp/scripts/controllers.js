'use strict';

/* Controllers */

var app = angular.module('TestApp', [ 'ngGrid' ]);

//app.directive('ngBlur', function () {
//    return function (scope, elem, attrs) {
//        elem.bind('blur', function () {
//            scope.$apply(attrs.ngBlur);
//        });
//    };
//});

app.directive('selfChange', function () {
return function (scope, elem, attrs) {
  elem.bind('change', function () {
      scope.$apply(attrs.selfChange);
  });
};
});

app.run(function($templateCache){
	$templateCache.put("ngGridOperTemplate.html","<div>"+
			"<input type=\"button\" value=\"add\" ng-click=\"addRow()\" />" +
			"<input type=\"button\" value=\"delete\" ng-click=\"deleteRow()\" />" +
			"<input type=\"button\" value=\"save\" ng-click=\"saveAll()\" />" +
			"<div class=\"gridStyle\" ng-grid=\"gridOptions\"></div>" + 
			"</div>");
});

app.directive('ngGridOper',function($log){
	var linkFunc = function(scope,elemenbt,attrs){
		
	};
	
	var controlFunc = function($scope,$element,$attrs){
		//$scope.myData = [];
		$scope.selectedRow = [];
//		var deleteData = [];
		console.log($scope.$eval($attrs.gridOptions));
		var selfGridOptions = $scope.$eval($attrs.gridOptions);
		
		var cellEditableTemplate  = "<input ng-class=\"'colt' + col.index\" ng-input=\"COL_FIELD\" ng-model=\"COL_FIELD\" self-change=\"autoChange(col,row)\" />";
		//editableCellTemplate: cellEditableTemplate
		
		//gridOptions.columnDefs["cellEditableTemplate"] = cellEditableTemplate;
		
		for(var i=0,j = selfGridOptions.columnDefs.length;i<j;i++){
			selfGridOptions.columnDefs[i]["editableCellTemplate"] = cellEditableTemplate;
		}
		$log.info(selfGridOptions);
		$scope.gridOptions = selfGridOptions;
		
		
		$scope.addRow = function(){
			//gridData.push({"rowStatus":"A"});
			$scope.$parent.$eval(selfGridOptions.data+".push({\"rowStatus\":\"A\"})");
			$log.info($scope.gridOptions.ngGrid.data);
			
		};
		//var len = $scope.myData.length;
		var gridData = [];
		var e = $scope.$on('ngGridEventData', function() {
			gridData = $scope.$parent.$eval(selfGridOptions.data);
			var len = gridData.length;
	        $scope.gridOptions.selectItem(len-1, true);
	    });
		$scope.$watch('gridOptions.ngGrid.config.selectedItems', function (newValue, oldValue, scope) {
	        if (newValue != oldValue && newValue.length > 0) {
	            var rowIndex = scope.gridOptions.ngGrid.data.indexOf(newValue[0]);
	            scope.gridOptions.ngGrid.$viewport.scrollTop(Math.max(0, (rowIndex - 6))*scope.gridOptions.ngGrid.config.rowHeight);
	        }
	    }, true);
		
		Array.prototype.remove = function(obj){
			var n = this.indexOf(obj);
			$log.info(n);
			if(n < 0){
				return this;
			} else {
				return this.slice(0,n).concat(this.slice(n+1,this.length));
			}
		}
		
		var deleteData = [];
		$scope.deleteRow = function(){
			$log.info($scope.gridOptions.ngGrid.config.selectedItems[0]);
			//$scope.gridOptions.ngGrid.data.remove($scope.gridOptions.ngGrid.config.selectedItems[0]);
			//$log.info($scope.gridOptions.ngGrid.data);
			//$scope.gridOptions.ngGrid.data = $scope.gridOptions.ngGrid.data.remove($scope.gridOptions.ngGrid.config.selectedItems[0]);
			deleteData.push($scope.gridOptions.ngGrid.config.selectedItems[0]);
			deleteData[deleteData.length-1]["rowStatus"] = "D";
			$scope.$eval("$parent." + selfGridOptions.data + " = $parent." + selfGridOptions.data +".remove(gridOptions.ngGrid.config.selectedItems[0])");
		}
		
		$scope.saveAll = function(){
			var requestText = JSON.stringify(gridData.concat(deleteData));
			$log.info(requestText);
			$scope.$parent.$eval($attrs.gridSave + "(" + requestText + ")");
			
		}
		
		$scope.autoChange = function(col,row){
			var changeRowIndex = gridData.indexOf(row.entity);
			if(gridData[changeRowIndex]["rowStatus"] == undefined || gridData[changeRowIndex]["rowStatus"] == ""){
				gridData[changeRowIndex]["rowStatus"] = "U";
			}
		}
		
	}
	var ngGridOperDirective = {
		compile:function(){
			return{
				pre:linkFunc
			};
		},
		controller:controlFunc,
		restrict:'E',
		templateUrl:'ngGridOperTemplate.html',
		scope:true,
		replace:true
	};
	return ngGridOperDirective;
});

app.controller('TestCtrl', function($scope, $http,$log) {
	$scope.myData = [];
	$scope.selectedRow = [];
//	var deleteData = [];
	var cellEditableTemplate  = "<input ng-class=\"'colt' + col.index\" ng-input=\"COL_FIELD\" ng-model=\"COL_FIELD\" self-change=\"autoChange(col,row)\" />";
	$http.get('/angular').success(function(data) {
		$scope.myData = data;
	});
	
//
	$scope.gridOptions = {
		data : 'myData',
		i18n : 'zh-cn',
		enableColumnResize : true,
		enablePinning : true,
		enableCellSelection: true,
        enableRowSelection: true,
        enableCellEdit: true,
        multiSelect: false,
		columnDefs : [ {
			field : 'product_name',
			displayName : '产品名'
		}, {
			field : 'product_price',
			displayName : '产品价格'
		} ]
	};
//
////	 beforeSelectionChange:function(a1){
////		 if(a1.rowIndex===$scope.myData.length-1){
////		 $scope.myData.push({name:"new",age:"0"});
////		 }
//	$scope.autoChange = function(col,row){
//		$log.info("change...."+col.field);
//		//$log.info(eval("row.entity." + col.field));
//		$log.info(JSON.stringify(row.entity));
//		var changeRowIndex = $scope.myData.indexOf(row.entity);
//		$log.info(changeRowIndex);
//		$log.info($scope.myData[changeRowIndex].product_name);
//		if($scope.myData[changeRowIndex]["rowStatus"] == undefined || $scope.myData[changeRowIndex]["rowStatus"] == ""){
//			$scope.myData[changeRowIndex]["rowStatus"] = "U";
//		}
//		
//	}
	
//	var len = $scope.myData.length;
//	var e = $scope.$on('ngGridEventData', function() {
//		len = $scope.myData.length;
//        $scope.gridOptions.selectItem(len-1, true); 
//    });
//	
//	$scope.$watch('gridOptions.ngGrid.config.selectedItems', function (newValue, oldValue, scope) {
//        if (newValue != oldValue && newValue.length > 0) {
//            var rowIndex = scope.gridOptions.ngGrid.data.indexOf(newValue[0]);
//            scope.gridOptions.ngGrid.$viewport.scrollTop(Math.max(0, (rowIndex - 6))*scope.gridOptions.ngGrid.config.rowHeight);
//        }
//    }, true);
//	
	$scope.save = function(requestText){
		var a = JSON.stringify(requestText);
		$log.info(a);
		
		$http.post("/angular/save","",
		{params:{json:a}}).success(function(response){
		    $log.info(response);
		    alert(response.result);
			$scope.myData = response.resultData;
		}).error(function(error){
			$log.info(error);
		    //$scope.error = error;
		});
	}
//	$scope.save = function(){
////		$http({
////		    url: "/angular/save",
////		    dataType: "json",
////		    method: "POST",
////		    data: JSON.stringify({"foo":"bar"}),
////		    headers: {
////		        "Content-Type": "application/json; charset=utf-8"
////		    }
////		}).success(function(response){
////		    $log.info("success");
////			//$scope.response = response;
////		}).error(function(error){
////			$log.info("error");
////		    //$scope.error = error;
////		});
//		
//		var a = JSON.stringify($scope.myData.concat(deleteData));
//		$log.info(a);
//		
////		$http.post("/angular/save","",
////		{params:{json:a}}).success(function(response){
////		    $log.info(response);
////		    alert(response.result);
////			$scope.myData = response.resultData;
////		}).error(function(error){
////			$log.info(error);
////		    //$scope.error = error;
////		});
//	}
//	
//	Array.prototype.remove = function(obj){
//		var n = this.indexOf(obj);
//		if(n < 0){
//			return this;
//		} else {
//			return this.slice(0,n).concat(this.slice(n+1,this.length));
//		}
//	}
//	$scope.deleteRowqqq = function(){
//		//alert($scope.myData.indexOf($scope.selectedRow[0]));
//		//var i = $scope.myData.indexOf($scope.selectedRow[0]);
//		//$scope.myData = $scope.myData.slice(0,i).concat($scope.myData.slice(i+1));
//		deleteData.push($scope.selectedRow[0]);
//		deleteData[deleteData.length-1]["rowStatus"] = "D";
//		$log.info(deleteData[deleteData.length-1]);
//		$scope.myData = $scope.myData.remove($scope.selectedRow[0]);
//	}

});
