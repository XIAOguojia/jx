// 定义模块:
var app = angular.module("jx",[]);

//定义过滤器，信任html
app.filter('trustHtml',["$sce",function ($sce) {
    return function (data) {
        return $sce.trustAsHtml(data);
    }
}]);