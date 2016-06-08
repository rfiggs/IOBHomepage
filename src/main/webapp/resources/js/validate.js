var app = angular.module('EmployeeIOB',['ui.bootstrap']);



app.controller('ctrl', ['$scope', '$http', '$location', '$filter', function($scope,$http,$location,$filter) {
    $scope.calendar=[[]];
    $scope.dates =[];
    $scope.absences = [];
    $scope.options = { 'showWeeks': false, 'datePickerMode':'day' };
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
    today = new Date()
    dayString = today.toDateString();
    $scope.getAbsences(dayString,dayString);

    }
    $scope.week = function(){
    $scope.template ='resources/templates/week.html';
    start = new Date();
    end = new Date();
    start.setDate(start.getDate()-3)
    end.setDate(end.getDate()+3)
    startString = start.toDateString();
    endString = end.toDateString();
    $scope.getAbsences(startString,endString)



    }
    $scope.month = function(){
    $scope.template ='resources/templates/month.html';
    start = new Date();
    start.setDate(1);
    end = new Date()
    end.setMonth(end.getMonth()+1)
    end.setDate(0)
    console.log(start.toDateString())
    console.log(end.toDateString())
    $scope.getAbsences(start,end)

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
    $scope.getAbsences = function(start,end){

        var token = $("meta[name='_csrf']").attr("content")
        $http({
            method: 'POST',
            url: "lookup",
            data: $.param({'start' : start , 'end':end}),
            headers: { 'X-CSRF-Token' : token,
                       'Content-Type': 'application/x-www-form-urlencoded'
             }
            }).success(function(data,status,headers,config){
                temp = [];
                console.log(data)
                for(x in data){
                    temp.push({ 'key' : x, 'date': new Date(x)})
                }
                $scope.dates = $filter('orderBy')(temp,'date')
                $scope.buildMonth(new Date());
                $scope.absences = data;
                console.log($scope.dates)
            }).error(function(data,status,headers,config){
                console.log(status);
        });
    }

    $scope.buildMonth = function(day){
            start = new Date(day.getTime());
            start.setDate(1);
            start.setDate(start.getDate()-start.getDay())
            console.log(start)
            end = new Date(day.getTime())
            end.setMonth(end.getMonth()+1)
            end.setDate(0)
            end.setDate(end.getDate()+(6-end.getDay()))
            console.log(end)
            tempMonth = [[]];
            week = 0;
            for(i = new Date(start.getTime()); i.getTime()<=end.getTime(); i.setDate(i.getDate()+1)){

                tempMonth[week][i.getDay()] = {'key':i.toDateString(),'val':i.getDate()};
                if(i.getDay()==6){
                week += 1;
                tempMonth[week]=[]
                }
            }
            $scope.calendar = tempMonth;
         }
    //loads default view as day page
    $scope.day()
}]);