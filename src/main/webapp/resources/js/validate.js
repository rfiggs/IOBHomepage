
var app = angular.module('EmployeeIOB',[]);

app.controller('ctrl', ['$scope', '$http', '$location', function($scope,$http,$location) {
    $scope.dayList = [];
    $scope.weekList = [];
    $scope.monthList=[];
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
            if(data =="SUCCESS"){
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
    $scope.template ='resources/templates/day.html';
    $http({
      method: 'GET',
      url: 'day'
    }).then(function successCallback(response) {
        $scope.dayList = angular.fromJson(response.data);


      }, function errorCallback(response) {

      });

    }
    $scope.week = function(){
    $scope.template ='resources/templates/week.html';
    $http({
          method: 'GET',
          url: 'week'
        }).then(function successCallback(response) {

          }, function errorCallback(response) {

          });
    }
    $scope.month = function(){
    $scope.template ='resources/templates/month.html';
    $http({
          method: 'GET',
          url: 'month'
        }).then(function successCallback(response) {

          }, function errorCallback(response) {

          });
    }
    $scope.remove = function (absid){
        var token = $("meta[name='_csrf']").attr("content")
                $http({
                    method: 'POST',
                    url: "remove",
                    data: $.param({'absid' : absid}),
                    headers: { 'X-CSRF-Token' : token,
                               'Content-Type': 'application/x-www-form-urlencoded'
                               }
                }).success(function(data,status,headers,config){
                    console.log("removed")
                }).error(function(data,status,headers,config){
                    console.log(status);
                });
    }
    //loads default view as day page
    $scope.day()
}]);




