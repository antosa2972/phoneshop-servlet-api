<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>${pageTitle}</title>
    <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body class="product-list">
<header>
    <a href="${pageContext.servletContext.contextPath}">
        <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
        PhoneShop
    </a>
    <jsp:include page="/cart/minicart"></jsp:include>
</header>
<main>
    <jsp:doBody/>
</main>
<footer>
    <c:if test="${not empty recentlyViewedProducts}">
        <h3>
            Recently viewed
        </h3>
        <table class="recently-viewed-table">
            <c:forEach var="recentlyViewedProduct" items="${recentlyViewedProducts}">
                <td width="100">
                    <img class="product-tile" src="${recentlyViewedProduct.imageUrl}">
                    <br>
                    <a href="${pageContext.servletContext.contextPath}/products/${recentlyViewedProduct.id}">
                            ${recentlyViewedProduct.description}
                    </a>
                    <br>
                    <fmt:formatNumber value="${recentlyViewedProduct.price}" type="currency"
                                      currencySymbol="${recentlyViewedProduct.currency.symbol}"/>
                </td>
            </c:forEach>
        </table>
    </c:if>
    <p>
        (c) Expert soft
    </p>
</footer>
</body>
</html>