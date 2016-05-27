
var app = angular.module('EmployeeIOB',[]);


app.controller('ctrl', ['$scope', '$http', '$location', function($scope,$http,$location) {
    $scope.submit = function() {
        var token = $("meta[name='_csrf']").attr("content")
        $http({
            method: 'POST',
            url: "add",
            data: $.param({'username' : $scope.username,
             'startTime':$scope.startTime,
              'endTime': $scope.endTime,
               'notes' : $scope.notes }),

            headers: { 'X-CSRF-Token' : token,
                       'Content-Type': 'application/x-www-form-urlencoded'
                       }
        }).success(function(data,status,headers,config){
            if(data.search("SUCCESS")!=-1){
                $('#myModal').modal('hide');
            }
            else{
                $scope.errorMessage = data;
            }
        }).error(function(data,status,headers,config){
            console.log(status);
        });
    }

    $('.modal').on('hidden.bs.modal', function(){
        $scope.errorMessage = null;
        $scope.username = null;
        $scope.startTime= null;
        $scope.endTime = null;
        $scope.notes = null;
        $scope.$apply();
    });

}]);


