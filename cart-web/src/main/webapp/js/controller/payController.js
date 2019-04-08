app.controller('payController', function ($scope,$location, payService) {
    //本地生成二维码
    $scope.createNative = function () {
        payService.createNative().success(
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
                //查询支付状态
                queryPayStatus(response.out_trade_no);
            }
        )
    };

    //查询支付状态
    queryPayStatus = function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(
            function (response) {
                if (response.success) {
                    location.href = "paysuccess.html#?money="+$scope.totalFee;
                } else {
                    if (response.message == "二维码超时") {
                        $scope.createNative();
                    } else {
                        location.href = "payfail.html";
                    }
                }
            }
        );
    };

    //获取金额
    $scope.getMoney=function(){
        return $location.search()['money'];
    };


});