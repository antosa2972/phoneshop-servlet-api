<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <p>
        <c:if test="${not empty error}">
            <span class="error">
                    There was an error while adding to cart!
            </span>
        </c:if>
    </p>
    <p>
        <span class="success">
            <c:if test="${empty error}">
                ${param.message}
        </span>
        </c:if>
    </p>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc"/>
                <tags:sortLink sort="description" order="desc"/>
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"/>
                <tags:sortLink sort="price" order="desc"/>
            </td>
            <td class="quantity">
                Add to cart
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td>
                    <a href="javascript:showPopUp(${product.id})">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                        <script>
                            function showPopUp(prodId) {
                                var id = prodId;
                                var link = "${pageContext.servletContext.contextPath}/price_history/" + id.toString();
                                window.open(link, "_blank", "toolbar=yes,scrollbars=yes,resizable=yes," +
                                    "top=500,left=500,width=450,height=400");
                            }
                        </script>
                    </a>
                </td>
                <td class="quantity">
                    <form method="post">
                        <input class="quantity" type="text" name="quantity" value="${not empty error && product.id == errorId ? param.quantity:1}">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button>Add to cart</button>
                    </form>
                    <p>
                        <c:if test="${not empty error && product.id == errorId}">
                            <span class="error">
                                ${error}
                            </span>
                        </c:if>
                    </p>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>