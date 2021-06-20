<%@ page import="com.es.phoneshop.model.product.exception.ProductNotFoundException" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="Product not found">
    <h1>
        Order not found: ${pageContext.exception.message}
    </h1>
    <p>
        An unexpected error
    </p>
</tags:master>