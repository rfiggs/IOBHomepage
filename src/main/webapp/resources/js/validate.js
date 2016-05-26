
var app = angular.module('EmployeeIOB',[]);

app.controller('formctrl', function($scope) {
    $scope.submit = function() {
        $scope.errorMessage = "submited";
    };

});
app.controller('ctrl', function($scope) {


});
function valid(scope){
    console.log(scope.username);
    return (scope.username.$valid && scope.startTime.$valid && scope.endTime.$valid);
}

function add(username,start,end,notes){
    console.log(username);
    console.log(start);
    console.log(end);
    console.log(notes);

}