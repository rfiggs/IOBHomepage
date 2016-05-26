<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="favicon.ico">

    <title>In Out App</title>

    <!-- Bootstrap core CSS -->
    <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
    <!-- Bootstrap theme -->
    <link href="<c:url value="/resources/css/bootstrap-theme.min.css"/>" rel="stylesheet">
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="<c:url value="/resources/css/ie10-viewport-bug-workaround.css"/>" rel="stylesheet">

    <!-- Custom styles using bootstrap template -->
    <link href="<c:url value="/resources/css/custom-bootstrap.css"/>" rel="stylesheet">
    
    <!-- Custom styles for this template -->
    <link href="<c:url value="/resources/css/template-style.css"/>" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>	
  <!-- mobile nav
  <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>            
          </ul>
        </div>
      </div>
    </nav>
    end mobile nav -->
  <body role="document" ng-app="EmployeeIOB" ng-controller= "ctrl">

  <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
          aria-labelledby="myModalLabel" aria-hidden="true">
         <div class="modal-dialog">
             <div class="modal-content">

                 <form ng-controller ="formctrl" >

                 <sec:csrfInput />
                 <!-- Modal Header -->
                 <div class="modal-header">
                     <button type="button" class="close"
                        data-dismiss="modal">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">Close</span>
                     </button>
                     <h4 class="modal-title" id="modalTitle">
                         Add Absence
                     </h4>
                     <p ng-bind="errorMessage"></p>
                 </div>

                 <!-- Modal Body -->
                 <div class="modal-body">

                        <!-- Username -->
                         <div class="form-group">
                            <label for="username">Enter Username</label>
                            <input class ="form-control" name="username" type="text" id="username" ng-model ="username" required>
                         </div>

                         <!-- Dates -->
                         <div class="form-inline">
                             <div class="form-group">
                                <label for="startTime">Date From</label>
                                <br/>
                                <input class ="form-control" name="startTime" type="date" id="startTime" ng-model ="startTime" required>

                             </div>
                             <div class="form-group">
                                 <label for="endTime">Date Through</label>
                                 <br/>
                                 <input class ="form-control" name="endTime" type="date" id="endTime" ng-model ="endTime" required>
                              </div>
                         </div>

                         <!-- Notes -->
                         <div class="form-group">
                               <label for="notes">Additional Notes:</label>
                               <br/>
                               <textarea class ="form-control" name="notes" rows="4" id="notes" ng-model ="notes" ></textarea>
                          </div>

                 </div>

                 <!-- Modal Footer -->
                  <div class ="modal-footer">
                    <input type="submit" value="Submit" id="submit" class="btn btn-primary" ng-click= "submit()">
                  </div>
                  </form>
             </div>
         </div>
  </div>

  <div class="topbar">
  	<h1>ITS Out Board</h1>
  		<div class="input-group-lg">
  		<input type="text" placeholder="Search Employee" class="form-control" id="search">
  		<a href="#" class="btn btn-search">
  <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
</a></div> 
  </div>
  <div class="container">
  	<div class="topmenu">
  		<a href="#" class="btn btn-topnav active">Day</a>
  		<a href="#" class="btn btn-topnav">Week</a>
  		<a href="#" class="btn btn-topnav">Month</a>
  	</div>
  <div class="titlefeed">4 People are out today</div>
  <div class="sidebar"><button type="button" id="addButton" data-toggle="modal" data-target="#myModal" class="btn btn-addleft"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;Add Absence</a>

  <div class="datecontain">
  	<time datetime="2016-09-20" class="icon">
  		<em>Saturday 2016</em>
  		<strong>March</strong>
  		<span>20</span>
	</time>
    <h3 ><a id="logout" href="logout">Logout</a></h3>
    </div>

    
  </div>
  <div class="content" id="content-outline">
  <table class="table table-day">
              <thead class="thead-day">
                <tr>
                  <th width="25%">Employee</th>
                  <th width="26%">Date</th>
                  <th width="49%"></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td><a href="#">John, Doe</a></td>                  
                  <td>03-20-2016</td>
                  <td><a href="#" class="btn btn-trash">
  <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
</a></td>
                </tr>
                <tr>
                  <td><a href="#">Jane, Doe</a></td>                  
                  <td>03-20-2016</td>
                  <td><a href="#" class="btn btn-trash">
  <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
</a></td>
                </tr>
                <tr>
                  <td><a href="#">John, Smith</a></td>
                  <td>03-20-2016</td>
                  <td><a href="#" class="btn btn-trash">
  <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
</a></td>
                </tr>
                <tr>
                  <td><a href="#">Jane, Smith</a></td>
                  <td>03-20-2016</td>
                  <td><a href="#" class="btn btn-trash">
  <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
</a></td>
                </tr>
              </tbody>
            </table>
  </div>


    
  
  </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="js/vendor/jquery.min.js"><\/script>')</script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
    <script src="<c:url value="resources/js/bootstrap.min.js"/>"></script>
    <script src="<c:url value="resources/js/docs.min.js"/>"></script>
    <script src="<c:url value="resources/js/validate.js"/>"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="js/ie10-viewport-bug-workaround.js"></script>



  </body>
</html>
