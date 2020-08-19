<%@page import="java.util.Map"%>
<%@page import="com.biddingapp.pojo.BidHistory"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.biddingapp.pojo.Product"%>
<%@page import="java.util.List"%>
<%@page import="com.biddingapp.util.Constants"%>
<%@page import="com.biddingapp.pojo.User"%>
<%
	User user = (User) session.getAttribute("user");
	if (user==null) {
		response.sendRedirect("login.jsp?msg=Session Expired. Please login again");
	} else {
%>
<html>
<head>
    <title> Bidding App</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- font Awesome -->
    <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Ionicons -->
    <link href="css/ionicons.min.css" rel="stylesheet" type="text/css" />
    <!-- Morris chart -->
    <link href="css/morris/morris.css" rel="stylesheet" type="text/css" />
    <!-- jvectormap -->
    <link href="css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
    <!-- Date Picker -->
    <link href="css/datepicker/datepicker3.css" rel="stylesheet" type="text/css" />
    <!-- fullCalendar -->
    <!-- <link href="css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" /> -->
    <!-- Daterange picker -->
    <link href="css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
    <!-- iCheck for checkboxes and radio inputs -->
    <link href="css/iCheck/all.css" rel="stylesheet" type="text/css" />
    <!-- bootstrap wysihtml5 - text editor -->
    <!-- <link href="css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" /> -->
    <link href='http://fonts.googleapis.com/css?family=Lato' rel='stylesheet' type='text/css'>
    <!-- Theme style -->
    <link href="css/style.css" rel="stylesheet" type="text/css" />



    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
          <![endif]-->

          <style type="text/css">

          </style>
      </head>
      <body class="skin-black">
        <!-- header logo: style can be found in header.less -->
        <header class="header">
            <a href="index.html" class="logo">
                Bidding Application
            </a>
            <!-- Header Navbar: style can be found in header.less -->
            <nav class="navbar navbar-static-top" role="navigation">
                <!-- Sidebar toggle button-->
                <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                
               		
                <div class="navbar-right">
                    <ul class="nav navbar-nav">       
                                <li>
                                    <a href="discussions?type=get" target="_blank">
                                        <strong><i class="fa fa-wechat"></i> <span>Discussion Forum</span></strong>
                                    </a>
                                </li>
                        
                        <!-- User Account: style can be found in dropdown.less -->
                        <li class="dropdown user user-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="fa fa-user"></i>
                                <span><%=user.getFname() %> <%=user.getLname() %> <i class="caret"></i></span>
                            </a>
                            <ul class="dropdown-menu dropdown-custom dropdown-menu-right">
                                <li class="dropdown-header text-center">Account</li>



									<% if (!user.getRole().equals(Constants.ROLE_ADMIN)) { %>
                                    <li>
                                        <a href="updateprofile.jsp">
                                        <i class="fa fa-user fa-fw pull-right"></i>
                                            Edit Profile
                                        </a>
                                        <a href="changepassword.jsp">
                                        <i class="fa fa-cog fa-fw pull-right"></i>
                                            Change Password
                                        </a>
                                        <a href="user?request_type=deleteprofile">
                                        <i class="fa fa-ban fa-fw pull-right"></i>
                                            Delete Account
                                        </a>
                                        </li>

                                        <li class="divider"></li>
									<% } %>
                                        <li>
                                            <a href="user?request_type=logout"><i class="fa fa-arrow-circle-right fa-fw pull-right"></i> Logout</a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </header>
                <div class="wrapper row-offcanvas row-offcanvas-left">
                    <!-- Left side column. contains the logo and sidebar -->
                    <aside class="left-side sidebar-offcanvas">
                        <!-- sidebar: style can be found in sidebar.less -->
                        <section class="sidebar">
                            <!-- Sidebar user panel -->
                            <div class="user-panel">
                                <div class="pull-left image">
                                    <img src="img/user.jpg" class="img-circle" alt="User Image" />
                                </div>
                                <div class="pull-left info">
                                    <p>Hello, <%=user.getFname() %></p>

                                    <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
                                    <br/>
                                    <br/>
                                    <%=user.getRole() %>
                                </div>
                            </div>
                            <!-- sidebar menu: : style can be found in sidebar.less -->
                            
                            <ul class="sidebar-menu">
                                <li>
                                    <a href="welcome.jsp">
                                        <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                                    </a>
                                </li>
                                <li >
                                    <a href="products?type=buyer_products_get">
                                        <i class="fa fa-product-hunt"></i> <span>Products</span>
                                    </a>
                                </li>
                                <li >
                                    <a href="buyer_bid.jsp">
                                        <i class="fa fa-inr"></i> <span>Bidding</span>
                                    </a>
                                </li>
                                <li >
                                    <a href="bid?type=buyer_bid_history">
                                        <i class="fa fa-table"></i> <span>My Orders</span>
                                    </a>
                                </li>
                                
                                <li class='active'>
                                    <a href="feedback?type=buyer_get_my_feedback">
                                        <i class="fa fa-star"></i> <span>My Feedbacks</span>
                                    </a>
                                </li>
                            </ul>                           
                           
                        </section>
                        <!-- /.sidebar -->
                    </aside>

                    <aside class="right-side">    
   
                <!-- Main content -->
                <section class="content">

                    <div class="row" style="margin:5px; min-height:700px;">
							<section class="panel">
                              <header class="panel-heading">
                                  <strong>FEEDBACK</strong>
                              </header>
                              <div class='panel-body'>
                    
 											<%
												String msg = request.getParameter("msg");
												if (msg!=null) {
											%>	
												<div class="alert alert-warning" role="alert">
													<strong><%=msg%></strong>
													<button type="button" class="close" data-dismiss="alert" aria-label="Close">
													  <span aria-hidden="true">&times;</span>
													</button>
												</div>
											<% } %>
											
										
										<%
										Product product = (Product) request.getAttribute("product");
										SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
										if (product!=null) {
											%>
												<div class="panel panel-default">
												  <div class="panel-heading">
												    <table class='table'>
												    	<tr>
												    		<th> 
												    			<% if (product.getPimageurl().trim().length()>0) { %>
												    				<img src='<%=product.getPimageurl() %>' width=400 />
												    			<% } else { %>
												    				<img src='img/product.png' width=400 />
												    			<% } %>
												    		</th>   
												    		<td width=70%> 
												    			<table class='table'>
												    				<tr>
												    					<td> <strong><%=product.getPname() %></strong></td>
												    				</tr>
												    				<tr>
												    					<td> <%=product.getPdesc() %></td>
												    				</tr>
												    				<tr>
												    					<td> <b>Seller ID :</b> <%=product.getSeller_id() %></td>
												    				</tr>
												    				<tr>
												    					<td> <b>Base Price :</b> Rs. <%=product.getPbaseprice() %> /-</td>
												    				</tr>
												    				<tr>
												    					<td> <b>Bidding window open until :</b> <%=sdf.format(product.getDeadline()) %></td>
												    				</tr>
												    			</table>
												    		</td>
												    	</tr>
												    </table>
												  </div>
												  <div class="panel-body">
												    <h4><b>Write Feedback</b></h4>
												    <form action='feedback' method=post>
												    	<input type=hidden name='type' value='buyer_feedback_write' />
												    	<input type=hidden name='pid' value='<%=product.getPid() %>' />
												    	<textarea class='form-control' name='feedback' placeholder='Write your feedback here'></textarea>
												    	<br/>
												    	<input type=submit value='Submit Feedback' class='btn btn-primary' />
												    </form>
										   		  </div>
										</div>
									<% } %>	    
																					
								</div>
								
										
								
							</section>
						
                       
                    </div>  

              <!-- row end -->
                </section><!-- /.content -->
                <div class="footer-main">
                    Copyright &copy Bidding Application, 2017
                </div>
            </aside><!-- /.right-side -->

        </div><!-- ./wrapper -->


        <!-- jQuery 2.0.2 -->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>

        <!-- jQuery UI 1.10.3 -->
        <script src="js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>

        <script src="js/plugins/chart.js" type="text/javascript"></script>

        <!-- datepicker
        <script src="js/plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>-->
        <!-- Bootstrap WYSIHTML5
        <script src="js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>-->
        <!-- iCheck -->
        <script src="js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>
        <!-- calendar -->
        <script src="js/plugins/fullcalendar/fullcalendar.js" type="text/javascript"></script>

        <!-- Director App -->
        <script src="js/Director/app.js" type="text/javascript"></script>

        <!-- Director dashboard demo (This is only for demo purposes) -->
        <script src="js/Director/dashboard.js" type="text/javascript"></script>

        <!-- Director for demo purposes -->
        <script type="text/javascript">
            $('input').on('ifChecked', function(event) {
                // var element = $(this).parent().find('input:checkbox:first');
                // element.parent().parent().parent().addClass('highlight');
                $(this).parents('li').addClass("task-done");
                console.log('ok');
            });
            $('input').on('ifUnchecked', function(event) {
                // var element = $(this).parent().find('input:checkbox:first');
                // element.parent().parent().parent().removeClass('highlight');
                $(this).parents('li').removeClass("task-done");
                console.log('not');
            });

        </script>
        <script>
            $('#noti-box').slimScroll({
                height: '400px',
                size: '5px',
                BorderRadius: '5px'
            });

            $('input[type="checkbox"].flat-grey, input[type="radio"].flat-grey').iCheck({
                checkboxClass: 'icheckbox_flat-grey',
                radioClass: 'iradio_flat-grey'
            });
</script>
<script type="text/javascript">
    $(function() {
                "use strict";
                //BAR CHART
                var data = {
                    labels: ["January", "February", "March", "April", "May", "June", "July"],
                    datasets: [
                        {
                            label: "My First dataset",
                            fillColor: "rgba(220,220,220,0.2)",
                            strokeColor: "rgba(220,220,220,1)",
                            pointColor: "rgba(220,220,220,1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(220,220,220,1)",
                            data: [65, 59, 80, 81, 56, 55, 40]
                        },
                        {
                            label: "My Second dataset",
                            fillColor: "rgba(151,187,205,0.2)",
                            strokeColor: "rgba(151,187,205,1)",
                            pointColor: "rgba(151,187,205,1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(151,187,205,1)",
                            data: [28, 48, 40, 19, 86, 27, 90]
                        }
                    ]
                };
            new Chart(document.getElementById("linechart").getContext("2d")).Line(data,{
                responsive : true,
                maintainAspectRatio: false,
            });

            });
            // Chart.defaults.global.responsive = true;
</script>
</body>
</html>

<% } %>