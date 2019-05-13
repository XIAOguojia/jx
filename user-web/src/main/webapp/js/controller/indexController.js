//控制层
app.controller('indexController', function ($scope, $controller,$window,$location, payService,loginService, myOrderService) {
    //获取当前登录的账号
    $scope.showName = function () {
        loginService.showName().success(
            function (response) {
                $scope.username = response.username;
            }
        )
    };

    // $scope.myOrder = {"orderItemMap": {}, "orderList": []};
    //查找我的订单
    $scope.findAllByUserId = function () {
        myOrderService.findAllByUserId().success(
            function (response) {
                $scope.myOrder = response;
            }
        )
    };

    //根据状态选择
    $scope.orderStatus = ['','立即付款', '确认收货','已收货'];
    $scope.orderOperation = ['','取消订单', '取消订单','删除订单'];

    $scope.confirmOrder = function (orderId,status) {
        if (status == 1){
            // $scope.orderId = orderId;
            // alert($scope.orderId);
            //立即付款
            location.href = "http://localhost:9106/pay.html#?orderId="+orderId;
        } else {

            //确认收货
            myOrderService.confirmOrder(orderId).success(
                function (response) {
                    //刷新页面
                    $window.location.reload();
                }
            )
        }
    };

    //本地生成二维码
    $scope.createNative = function () {
        $scope.orderId = $location.search()['orderId'];
        payService.createNative($scope.orderId).success(
            function (response) {
                //金额从分变回元
                $scope.totalFee = (response.total_fee / 100).toFixed(2);
                //订单号
                $scope.outTradeNo = response.out_trade_no;
                //二维码
                var qr = new QRious({
                    element: document.getElementById('qrious'),
                    size: 250,
                    level: 'H',
                    value: response.code_url
                });
                // //查询支付状态
                // queryPayStatus(response.out_trade_no);
            }
        )
    };
});