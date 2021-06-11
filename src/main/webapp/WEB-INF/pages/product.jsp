<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
        Cart : ${cart}
    </p>
    <p>
        product jsp page
            ${product.description}
    </p>
    <p>
        <c:if test="${not empty param.message}">
            <span class="success">
                    ${param.message}
            </span>
        </c:if>
    </p>
    <p>
        <c:if test="${not empty error}">
            <span class="error">
                    There was an error while adding to cart!
            </span>
        </c:if>
    </p>
    <form method="post">
        <table>
            <thead>
            <tr>
                <td>image</td>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>stock</td>
                <td>
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>quantity</td>
                <td class="quantity">
                    <input class="quantity" name="quantity" value="${not empty error ? param.quantity:1}">
                    <c:if test="${not empty error}">
                     <span class="error">
                             ${error}
                     </span>
                    </c:if>
                </td>
            </tr>
            </thead>
        </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>
</tags:master>