//控制层
app.controller('userController', function ($scope, $controller, userService) {
    $scope.entity=[];

    $scope.userRegister = function () {
        if ($scope.entity.username == null||$scope.entity.username==""){
            alert("用户名不能为空");
            return;
        }

        if ($scope.entity.password == null||$scope.entity.password==""){
            alert("密码不能为空");
            return;
        }

        if ($scope.entity.password != $scope.password) {
            alert("两次密码不一致，请重新输入");
            return;
        }



        userService.add($scope.entity,$scope.smsCode).success(
            function (response) {
                // if (response.success){
                //     location.href = "http://localhost:9100/cas/login";
                // } else {
                //     alert(response.message);
                // }
                alert(response.message);
            })

    };

    $scope.sendSms = function () {
        if ($scope.entity.phone == null||$scope.entity.phone==""){
            alert("手机号不能为空");
            return;
        }
        userService.sendSms($scope.entity.phone).success(
            function (response) {
                alert(response.message);
            }
        )
    };

});	
