<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order Overview">
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td class="quantity">Quantity</td>
            <td class="price">Price</td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="status">
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
                    <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        ${item.quantity}
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
            </tr>
        </c:forEach>
        <tr>
            <td class="noTableBorderLeftRightBottom"></td>
            <td class="noTableBorderRightBottom"></td>
            <td class="noTableBorderBottom"></td>
            <td>
                <h3 class="quantity">
                    Total quantity:${order.items.size()}
                </h3>
            </td>
        </tr>
        <tr>
            <td class="noTableBorderLeftRightBottom"></td>
            <td class="noTableBorderRightBottom"></td>
            <td class="noTableBorderBottom"></td>
            <td>
                <h3 class="price">
                    Subtotal: <fmt:formatNumber value="${order.subtotal}" type="currency"
                                                currencySymbol="${order.currency}"/>
                </h3>
            </td>
        </tr>
        <tr>
            <td class="noTableBorderLeftRightBottom"></td>
            <td class="noTableBorderRightBottom"></td>
            <td class="noTableBorderBottom"></td>
            <td>
                <h3 class="price">
                    Delivery cost:<fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                                    currencySymbol="${order.currency}"/>
                </h3>
            </td>
        </tr>
        <tr>
            <td class="noTableBorderLeftRightBottom"></td>
            <td class="noTableBorderRightBottom"></td>
            <td class="noTableBorderBottom"></td>
            <td>
                <h3 class="price">
                    Total cost:<fmt:formatNumber value="${order.totalCost}" type="currency"
                                                 currencySymbol="${order.currency}"/>
                </h3>
            </td>
        </tr>
    </table>
    <h2>Order details</h2>
    <table>
        <tags:orderOverviewForm name="firstName" label="First name" order="${order}"></tags:orderOverviewForm>
        <tags:orderOverviewForm name="lastName" label="Last name" order="${order}"></tags:orderOverviewForm>
        <tags:orderOverviewForm name="phone" label="Phone" order="${order}"></tags:orderOverviewForm>
        <tr>
            <td>Delivery date</td>
            <td>${order.deliveryDate}</td>
        </tr>
        <tags:orderOverviewForm name="deliveryAddress" label="Delivery address"
                                order="${order}"></tags:orderOverviewForm>
        <tr>
            <td>Payment method</td>
            <td>${order.paymentMethods}</td>
        </tr>
    </table>
</tags:master>