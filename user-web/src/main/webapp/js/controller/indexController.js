//控制层
app.controller('indexController', function ($scope, $controller, loginService) {
    //获取当前登录的账号
    $scope.showName = function () {
        loginService.showName().success(
            function (response) {
                $scope.username = response.username;
            }
        )
    }
});