<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p>
        Cart : ${cart}
    </p>
    <p>
        <span class="success">
                ${param.message}
        </span>
    </p>
    <p>
    <c:if test="${not empty errors || not empty error}">
        <div class="error">
            There were some errors updating a cart.
                ${errors[0]}
        </div>
    </c:if>
    </p>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td class="quantity">Quantity</td>
                <td class="price">Price</td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                ${item.product.description}
                        </a>
                    </td>
                    <td class="quantity">
                        <c:set var="error" value="${errors[item.product.id]}"/>
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <input name="quantity"
                               value="${not empty error ? paramValues['quantity'][status.index]: item.quantity}"
                               class="quantity"/>
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${errors[item.product.id]}
                            </div>
                        </c:if>
                        <input type="hidden" name="productId" value="${item.product.id}"/>
                    </td>
                    <td>
                        <a href="javascript:showPopUp(${item.product.id})">
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                                              currencySymbol="${item.product.currency.symbol}"/>
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
                    <td>
                        <button form="deleteCartItem"
                                formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p>
            Total quantity:${cart.totalQuantity}
        </p>
        <p>
            Total cost:${cart.totalCost}
        </p>
        <p>
            <button>Update</button>
        </p>
    </form>
    <form action="${pageContext.servletContext.contextPath}/checkout" method="get">
        <button>Checkout</button>
    </form>
    <form id="deleteCartItem" method="post"></form>
</tags:master>