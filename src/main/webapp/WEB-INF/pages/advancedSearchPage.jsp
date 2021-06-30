<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced Search">
    <p>
        Advanced search page
    </p>
    <form>
        <label for="description">Description:</label>
        <input id="description" name="description" value="${param.description}">
        <select name="typeOfSearch">
            <c:forEach var="typeOfSearch" items="${typeOfSearches}">
                <option <c:if test="${typeOfSearch eq param.searchType}">
                    selected</c:if>>
                        ${typeOfSearch}
                </option>
            </c:forEach>
        </select>
        <br>
        <label for="minimalPrice">Min Price: </label>
        <input id="minimalPrice" name="minimalPrice" value="${param.minPrice}">
        <c:if test="${not empty errors['Error Minimal price']}">
            <div class="error">
                    ${errors['Error Minimal price']}
            </div>
        </c:if>
        <br>
        <label for="maximalPrice">Max Price: </label>
        <input id="maximalPrice" name="maximalPrice" value="${param.maxPrice}">
        <c:if test="${not empty errors['Error Maximal price']}">
            <div class="error">
                    ${errors['Error Maximal price']}
            </div>
        </c:if>
        <button>Search</button>
    </form>
    <br>
    <c:if test="${not empty success}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
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
                            <input class="quantity" type="text" name="quantity"
                                   value="${not empty error && product.id == errorId ? param.quantity:1}">
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
    </c:if>
</tags:master>