<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Tour Agency</title>
    <link rel="stylesheet" href="/resources/bower_components/bootstrap/dist/css/bootstrap.css"/>
    <link rel="stylesheet" href="/resources/css/master.css"/>
    <link rel="stylesheet" href="/resources/css/purchase.css"/>
    <link rel="stylesheet" href="/resources/css/tour.css"/>
</head>
<body>

<c:import url="/resources/components/navbar.html"/>

    <div class="container ovf-hidden">
        <div class="col-md-3 stretch sidebar">
            <c:import url="/resources/components/sidebar.html"/>
        </div>

        <div class="col-md-9 stretch content">
            <h1>Мої замовлення</h1>
            <hr/>
            
            <c:forEach var="purchase" items="${purchases}">
                <c:set var="purchase" value="${webApplication.purchaseDao.deepen(purchase)}"/>   
                <c:set var="tour" value="${purchase.tour}" />
                     
	            <div class="col-md-12">
	                <div class="tour clearfix">
	                    <div class="heading">
	                        <c:out value="${tour.title}"/>
	                    </div>
	                    
	                    <div class="body">
	                        <c:out value="${tour.description}"/>
	                    </div>
	                    
	                    <div class="footer">
	                        <div class="pull-left badge badge-success" style="line-height: 20px">
	                            <c:out value="${purchase.price}"/>
	                        </div>
	                       	                     
	                        <div class="pull-right btn-tour-info fix-footer-overlay">
	                           <i><c:out value="${purchase.date}"/></i>
	                        </div>
	                    </div>
	                    
	                </div>
	            </div>  
		    </c:forEach>   
		           
        </div>
    </div>

</body>
</html>