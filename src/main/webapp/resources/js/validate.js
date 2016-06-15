var app = angular.module('EmployeeIOB',['ui.bootstrap']);



app.controller('ctrl', ['$scope', '$http', '$location', '$filter', function($scope,$http,$location,$filter) {
    $scope.template ='resources/templates/day.html';
    $scope.todayDate = new Date(new Date().toDateString());
    $scope.selectedDate = $scope.todayDate;
    $scope.calendar=[[]];
    $scope.dates =[];
    $scope.absences = [];
    $scope.monthLabel = function() { return document.getElementsByClassName("uib-title")[0].getElementsByTagName("strong")[0]};
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
        $scope.getAbsences();
        $scope.$apply();
    });

    $('.modal').on('shown.bs.modal', function () {
        console.log("modal open")
      $scope.startTime = $scope.selectedDate;
      $scope.endTime = $scope.selectedDate;
      $scope.$apply();
    })

   /* $scope.$watch( function(scope){

                thing = "" + $scope.monthLabel().innerHTML + $scope.selectedDate.toDateString() + $scope.template;
                console.log("thing: " + thing)
               return thing;
           },
           function(){
                $scope.getAbsences();
           },true);*/
     $scope.$watch( function(scope){
        if($scope.template =='resources/templates/month.html' ){
            thing = $scope.monthLabel().innerHTML;
        }
        else {
            thing = $filter('date')($scope.selectedDate,"MMMM");
        }
        console.log("thing: " + thing)
       return thing;
       },
       function(){
            $scope.getAbsences();
       });




    $scope.day = function(){
        $scope.template ='resources/templates/day.html';

    }
    $scope.week = function(){
        $scope.template ='resources/templates/week.html';

    }
    $scope.month = function(){
        $scope.template ='resources/templates/month.html';

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
                    $scope.getAbsences()
                }).error(function(data,status,headers,config){
                    console.log(status);
                });
    }
    $scope.getAbsences = function(){

        if($scope.template == 'resources/templates/month.html'){
            console.log("month")
            start = new Date("01"+$scope.monthLabel().innerHTML);
            start.setDate(1);
            end = new Date("01"+$scope.monthLabel().innerHTML);
            end.setMonth(end.getMonth()+1)
            end.setDate(0)
        }
        else{
            console.log("notmonth")
            start = new Date($scope.selectedDate.getTime());
            start.setDate(1);
            end = new Date($scope.selectedDate.getTime());
            end.setMonth(end.getMonth()+1)
            end.setDate(0)
        }
        console.log(start)
        console.log(end)
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
                for(x in data){
                    temp.push({ 'key' : x, 'date': new Date(x)})
                }
                $scope.dates = $filter('orderBy')(temp,'date')
                $scope.buildMonth(start);
                $scope.absences = data;
            }).error(function(data,status,headers,config){
                console.log(status);
        });
    }

    $scope.weekInMonth = function(day){
        tempDate = new Date(day.getTime());
        weekInYear = $filter('date')(tempDate,"w");
        tempDate.setDate(1);
        firstWeekInMonth = $filter('date')(tempDate,"w")
        result = (weekInYear - firstWeekInMonth);

        return result;
    }

    $scope.dayOfWeek = function(day){
        formatted = $filter('date')(day,"EEE MMM dd");
        return $filter('date')(formatter,"EEE MMM dd");

    }

    $scope.buildMonth = function(day){
            start = new Date(day.getTime());
            start.setDate(1);
            start.setDate(start.getDate()-start.getDay())
            end = new Date(day.getTime())
            end.setMonth(end.getMonth()+1)
            end.setDate(0)
            end.setDate(end.getDate()+(6-end.getDay()))
            tempMonth = [[]];
            week = 0;
            for(i = new Date(start.getTime()); i.getTime()<=end.getTime(); i.setDate(i.getDate()+1)){

                tempMonth[week][i.getDay()] = {'key':new Date(i.getTime()),'val':i.getDate()};
                if(i.getDay()==6){
                week += 1;
                tempMonth[week]=[]
                }
            }
            console.log(tempMonth);
            $scope.calendar = tempMonth;
         }

}]);

