
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
    //Clear modal form after closing
    $('.modal').on('hidden.bs.modal', function(){
        $scope.errorMessage = null;
        $scope.username = null;
        $scope.startTime= null;
        $scope.endTime = null;
        $scope.notes = null;
        $scope.$apply();
    });

    $scope.day = function(){
    $http({
      method: 'GET',
      url: 'day'
    }).then(function successCallback(response) {
        $scope.content = response.data;
      }, function errorCallback(response) {

      });

    }
    $scope.week = function(){
    $http({
          method: 'GET',
          url: 'week'
        }).then(function successCallback(response) {
            $scope.content = response.data;
          }, function errorCallback(response) {

          });
    }
    $scope.month = function(){
    $http({
          method: 'GET',
          url: 'month'
        }).then(function successCallback(response) {
            $scope.content = response.data;
          }, function errorCallback(response) {

          });
    }
    //loads default view as day page
    $scope.day()
}]);




