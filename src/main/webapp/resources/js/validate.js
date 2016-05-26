
var app = angular.module('EmployeeIOB',[]);

app.controller('formctrl', function($scope) {
    $scope.submit = function() {
    if(($scope.username != null) && ($scope.startTime!=null) && ($scope.endTime != null)){
       console.log($scope.username);
       console.log($scope.startTime);
       console.log($scope.endTime);
       console.log($scope.notes);
       }
    };

});
app.controller('ctrl', function($scope) {


});

function add(username,start,end,notes){
    console.log(username);
    console.log(start);
    console.log(end);
    console.log(notes);

}